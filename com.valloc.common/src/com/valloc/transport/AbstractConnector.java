/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.transport;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;

import com.valloc.CategoryType;
import com.valloc.Constants;
import com.valloc.MessageSummary;
import com.valloc.MessageSummaryStatus;
import com.valloc.framework.CallType;
import com.valloc.framework.FrameworkManager;
import com.valloc.framework.FrameworkRequest;
import com.valloc.framework.FrameworkResponse;
import com.valloc.interrupt.InterruptHandler;
import com.valloc.interrupt.InterruptHandlerAdapter;
import com.valloc.interrupt.InterruptTracker;
import com.valloc.interrupt.InterruptType;
import com.valloc.log.LogManager;
import com.valloc.log.Logger;
import com.valloc.session.Session;
import com.valloc.util.Result;
import com.valloc.util.UniqueId;

/**
 *
 *
 * @author wstevens
 */
abstract class AbstractConnector implements Connector
{
	private static final Logger logger = LogManager.manager().getLogger(AbstractConnector.class, CategoryType.TRANSPORT_CONNECTOR);

	private final static Map<UniqueId, RequestTracker> activeClientPerspectiveRequests = new HashMap<UniqueId, RequestTracker>();
	private final static Map<UniqueId, RequestTracker> activeServicePerspectiveRequests = new HashMap<UniqueId, RequestTracker>();
	private Session session;
	private Channel channel;
	private FrameworkManager frameworkManager;

	/*
	 * (non-Javadoc)
	 *
	 * @see com.valloc.transport.Connector#disconnect()
	 */
	@Override
	public MessageSummary disconnect() {
		MessageSummary messageSummary = null;

		// close this first to stop accepting new connections
		// NOTE: calling close here also unbinds the server from the port
		ChannelFuture clientCloseFuture = getChannel().close();

		// now back to server closing, if not done then wait..
		if (!clientCloseFuture.isDone()) {
			try { // reassign on await
				clientCloseFuture = clientCloseFuture.await();
			} catch (final InterruptedException e) {
				logger.error("Transport server was interrupted while attempting to shutdown.", e);
			}
		}
		if (!clientCloseFuture.isSuccess()) {
			final Throwable thrown = clientCloseFuture.getCause();
			if (thrown != null) {
				logger.error("Transport server was not shutdown cleanly due to this throwable: " + thrown.toString(), thrown);
			} else {
				logger.warn("Transport server was not shutdown cleanly due to this throwable for unknown reason.");
			}
		}

		if (clientCloseFuture.isSuccess()) {
			messageSummary = new MessageSummary(MessageSummaryStatus.SUCCESS);
		} else if (clientCloseFuture.isCancelled()) {
			messageSummary = new MessageSummary(MessageSummaryStatus.CANCEL);
		} else if (clientCloseFuture.getCause() != null) {
			messageSummary = new MessageSummary(MessageSummaryStatus.ERROR);
		}

		if (logger.isFine()) {
			final boolean isDone = clientCloseFuture.isDone();
			final String msg = "Transport connector with done flag as %b was left in state of: %s.";
			if (clientCloseFuture.isSuccess()) {
				logger.fine(String.format(msg, isDone, "successful shutdown"));
			} else if (clientCloseFuture.isCancelled()) {
				logger.fine(String.format(msg, isDone, "cancelled shutdown"));
			} else if (clientCloseFuture.getCause() != null) {
				logger.fine(String.format(msg, isDone, "an errored shutdown"));
			}
		}

		return messageSummary;
	}

	private final class RequestTracker {
		CallType callType;
		Lock lock;
		Condition condition;
		FrameworkRequest frameworkRequest;
		FrameworkResponse frameworkResponse;
		InterruptHandler interruptHandler;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.valloc.transport.Connector#submitSync(com.valloc.framework.FrameworkRequest)
	 */
	@Override
	public void submitSync(final FrameworkRequest frameworkRequest, final FrameworkResponse frameworkResponse) {
		final TransportLayerMessage transportMsg = new TransportLayerMessage();
		transportMsg.messageType = MessageType.REQUEST;
		transportMsg.remoteFrameworkRequest = frameworkManager.toRemoteFrameworkRequest(frameworkRequest);
		transportMsg.frameworkResponse = frameworkResponse;

		final Lock blockLock = new ReentrantLock();
		final Condition blockCondition = blockLock.newCondition();

		final ChannelFuture writeCompleteFuture = getChannel().write(transportMsg);
		writeCompleteFuture.addListener(new ChannelFutureListener() {
			@Override public void operationComplete(final ChannelFuture future) throws Exception {
				try {
					if (future.isCancelled()) {
						// TODO: remove interrupt handler, set result object/status; break out of method, etc.
					}
					blockLock.lock();
					blockCondition.signal();
				} finally {
					blockLock.unlock();
				}
			}
		});

		// while waiting for write to complete, setup and register tracker object
		final InterruptTracker interruptTracker = frameworkRequest.getInterruptTracker();
		final RequestTracker requestTracker = new RequestTracker();
		final UniqueId requestId = frameworkRequest.id();
		requestTracker.frameworkRequest = frameworkRequest;
		requestTracker.frameworkResponse = frameworkResponse;
		requestTracker.callType = frameworkRequest.getCallType();
		requestTracker.lock = blockLock;
		requestTracker.condition = blockCondition;
		requestTracker.interruptHandler = new InterruptHandlerAdapter(interruptTracker) {
			@Override public void handleInterrupt(final InterruptType interruptType, final Result result) {
				interruptTracker.unsubscribeInterrputHandler(requestTracker.interruptHandler);
				activeClientPerspectiveRequests.remove(frameworkRequest.id());
				if (logger.isFine()) {
					logger.fine("Blocking submitSync call was interrupted for transport connector under session %s.", session.toString());
				}
			}
		};
		activeClientPerspectiveRequests.put(requestId, requestTracker);
		interruptTracker.subscribeInterrputHandler(requestTracker.interruptHandler);

		try { // block until write complete
			blockLock.lock();
			while (!writeCompleteFuture.isDone()) {
				blockCondition.await();
			}
		} catch (final InterruptedException e) {
			logger.error("Interruption during wait on attempt to write, while submitting a synchronous request.", e);
			frameworkResponse.setMessageSummary(MessageSummary.ERROR);
		} finally {
			blockLock.unlock();
		}

		blockForResponse(requestTracker);

		interruptTracker.unsubscribeInterrputHandler(requestTracker.interruptHandler);
		activeClientPerspectiveRequests.remove(requestId);
	}

	private void blockForResponse(final RequestTracker requestTracker) {
		final Lock blockLock = requestTracker.lock;
		final Condition blockCondition = requestTracker.condition;

		try { // block until write complete
			blockLock.lock();
			blockCondition.await();
		} catch (final InterruptedException e) {
			logger.error("Interruption during wait on attempt to write, while submitting a synchronous request.", e);
		} finally {
			blockLock.unlock();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.valloc.transport.Connector#submitAsync(com.valloc.framework.FrameworkRequest)
	 */
	@Override
	public void submitAsync(final FrameworkRequest frameworkRequest, final FrameworkResponse frameworkResponse) {
		final TransportLayerMessage transportMsg = new TransportLayerMessage();
		transportMsg.messageType = MessageType.REQUEST;
		transportMsg.remoteFrameworkRequest = frameworkManager.toRemoteFrameworkRequest(frameworkRequest);
		transportMsg.frameworkResponse = frameworkResponse;

		final Lock blockLock = new ReentrantLock();
		final Condition blockCondition = blockLock.newCondition();

		final ChannelFuture writeCompleteFuture = getChannel().write(transportMsg);
		writeCompleteFuture.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(final ChannelFuture future) throws Exception {
				try {
					blockLock.lock();
					blockCondition.signal();
				} finally {
					blockLock.unlock();
				}
			}
		});

		// while waiting for write to complete, setup and register tracker object
		final InterruptTracker interruptTracker = frameworkRequest.getInterruptTracker();
		final RequestTracker requestTracker = new RequestTracker();
		final UniqueId requestId = frameworkRequest.id();
		requestTracker.frameworkRequest = frameworkRequest;
		requestTracker.frameworkResponse = frameworkResponse;
		requestTracker.callType = frameworkRequest.getCallType();
		requestTracker.lock = blockLock;
		requestTracker.condition = blockCondition;
		requestTracker.interruptHandler = new InterruptHandlerAdapter(interruptTracker) {
			@Override public void handleInterrupt(final InterruptType interruptType, final Result result) {
				interruptTracker.unsubscribeInterrputHandler(requestTracker.interruptHandler);
				activeClientPerspectiveRequests.remove(frameworkRequest.id());
				if (logger.isFine()) {
					logger.fine("Blocking submitSync call was interrupted for transport connector under session %s.", session.toString());
				}
			}
		};
		activeClientPerspectiveRequests.put(requestId, requestTracker);

		try { // block until write complete
			blockLock.lock();
			while (!writeCompleteFuture.isDone()) {
				blockCondition.await();
			}
		} catch (final InterruptedException e) {
			logger.error("Interruption during wait on attempt to write, while submitting an asynchronous request.", e);
		} finally {
			blockLock.unlock();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.valloc.transport.Connector#requestReceived(com.valloc.framework.FrameworkRequest)
	 */
	@Override
	public void requestReceived(final FrameworkRequest frameworkRequest, final FrameworkResponse frameworkResponse) {
		final RequestTracker requestTracker = new RequestTracker();
		final UniqueId requestId = frameworkRequest.id();
		// now is appropriate time to build a new interrupt tracker for a new request on this remote node
		final InterruptTracker interruptTracker = frameworkManager.createAndRegisterInterruptTracker(requestId);
		frameworkRequest.setInterruptTracker(interruptTracker);
		// now set common associations
		requestTracker.callType = frameworkRequest.getCallType();
		activeServicePerspectiveRequests.put(requestId, requestTracker);
		frameworkRequest.setSession(session);
		// fire upwards
		session.requestReceived(frameworkRequest, frameworkResponse);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.valloc.transport.Connector#responseReceived(com.valloc.framework.FrameworkResponse)
	 */
	@Override
	public void responseReceived(final FrameworkResponse frameworkResponse) {
		final RequestTracker requestTracker = activeClientPerspectiveRequests.get(frameworkResponse.id());
		if (requestTracker.callType == CallType.REMOTE_SYNCHRONOUS) {
			try { // signal blocking condition to resume; response then pulled from tracker object
				final FrameworkResponse existingResponse = requestTracker.frameworkResponse;
				// reset inner source Response object to maintain outter origal FrameworkResponse reference
				existingResponse.setResponse(frameworkResponse.getResponse());
				requestTracker.lock.lock();
				requestTracker.condition.signal();
			} finally {
				requestTracker.lock.unlock();
			}
		} else if (requestTracker.callType == CallType.REMOTE_ASYNCHRONOUS) {
			// first unregister interrupt handler
			final InterruptTracker interruptTracker = requestTracker.frameworkRequest.getInterruptTracker();
			interruptTracker.unsubscribeInterrputHandler(requestTracker.interruptHandler);
			// then unregister overall request
			activeClientPerspectiveRequests.remove(requestTracker);
			// finally pass response up to framework for session to rejoin with controller
			session.asyncResponseReceived(frameworkResponse);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.valloc.transport.Connector#returnResponse(com.valloc.framework.FrameworkResponse)
	 */
	@Override
	public void returnResponse(final FrameworkResponse frameworkResponse) {
		final TransportLayerMessage transportMsg = new TransportLayerMessage();
		transportMsg.messageType = MessageType.RESPONSE;
		transportMsg.frameworkResponse = frameworkResponse;

		final Lock blockLock = new ReentrantLock();
		final Condition blockCondition = blockLock.newCondition();

		final ChannelFuture writeCompleteFuture = getChannel().write(transportMsg);
		writeCompleteFuture.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(final ChannelFuture future) throws Exception {
				try {
					blockLock.lock();
					blockCondition.signal();
				} finally {
					blockLock.unlock();
				}
			}
		});

		try { // block until write complete
			blockLock.lock();
			if (!writeCompleteFuture.isDone()) {
				blockCondition.await();
			}
		} catch (final InterruptedException e) {
			logger.error("Interruption during wait on attempt to write, while submitting an asynchronous request.", e);
		} finally {
			blockLock.unlock();
		}

		activeServicePerspectiveRequests.remove(frameworkResponse.id());
	}

	@Override
	public <S extends Session> S getSession() {
		@SuppressWarnings(Constants.UNCHECKED)
		final S sessionRetval = (S) session;
		return sessionRetval;
	}

	@Override
	public <S extends Session> void setSession(final S session) {
		this.session = session;
	}

	@Override
	public int getTransportId() {
		return channel.getId();
	}

	@Override
	public void setChannel(final Channel channel) {
		this.channel = channel;
	}

	@Override
	public Channel getChannel() {
		return channel;
	}

	@Override
	public void setFrameworkManager(final FrameworkManager frameworkManager) {
		this.frameworkManager = frameworkManager;
	}
}
