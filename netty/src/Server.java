import static org.jboss.netty.channel.Channels.*;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import javax.net.ssl.SSLEngine;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.WriteCompletionEvent;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.example.securechat.SecureChatSslContextFactory;
import org.jboss.netty.handler.codec.compression.ZlibDecoder;
import org.jboss.netty.handler.codec.compression.ZlibEncoder;
import org.jboss.netty.handler.codec.compression.ZlibWrapper;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.jboss.netty.handler.logging.LoggingHandler;
import org.jboss.netty.handler.ssl.SslHandler;
import org.jboss.netty.logging.InternalLogLevel;


public class Server
{

	public static void main(final String[] args)
	{

		final ServerBootstrap bootstrap = new ServerBootstrap(
                new NioServerSocketChannelFactory(
                        Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool()));

        
        bootstrap.setPipelineFactory(new ServerPipelineFactory());

        // Bind and start to accept incoming connections.
        bootstrap.bind(new InetSocketAddress(8080));
	}

	
	public static class ServerPipelineFactory implements ChannelPipelineFactory {

		public ChannelPipeline getPipeline() throws Exception {
		    final ChannelPipeline pipeline = pipeline();

		    final SSLEngine engine = SecureChatSslContextFactory.getServerContext().createSSLEngine();
		    engine.setUseClientMode(false);		    
		    
		    pipeline.addLast("tracker", new TrackerHandler()); 
		    pipeline.addLast("ssl", new SslHandler(engine));
		    pipeline.addLast("gzipdeflater", new ZlibEncoder(ZlibWrapper.GZIP));
		    pipeline.addLast("gzipinflater", new ZlibDecoder(ZlibWrapper.GZIP));
		    pipeline.addLast("stringencoder", new StringEncoder());
		    pipeline.addLast("stringdecoder", new StringDecoder());
		    pipeline.addLast("hihandler", new ServerHandler());
		    pipeline.addLast("logger", new LoggingHandler(InternalLogLevel.INFO));		    
		    return pipeline;
		}
	}

	static public class ServerHandler extends SimpleChannelHandler
	{

		@Override
		public void handleUpstream(final ChannelHandlerContext ctx, final ChannelEvent e) throws Exception
		{
			System.out.println("ServerHandler Upstream event: " + e);
			if (e instanceof MessageEvent) {
				final Object o = ((MessageEvent)e).getMessage();
				System.out.println("ServerHandler Upstream event: " + o);
			}
			
			ctx.sendUpstream(e);
		}

		@Override
		public void handleDownstream(final ChannelHandlerContext ctx, final ChannelEvent e) throws Exception
		{
			System.out.println("ServerHandler Downstream event: " + e);
			if (e instanceof MessageEvent) {
				final Object o = ((MessageEvent)e).getMessage();
				System.out.println("ServerHandler Downstream event: " + o);
			}
			ctx.sendDownstream(e);
		}

	    @Override
	    public void channelConnected(final ChannelHandlerContext ctx, final ChannelStateEvent e) throws Exception {

	        // Get the SslHandler in the current pipeline.
	        // We added it in SecureChatPipelineFactory.
	        final SslHandler sslHandler = ctx.getPipeline().get(SslHandler.class);

	        // Get notified when SSL handshake is done.
//	        final ChannelFuture handshakeFuture = sslHandler.handshake();
	        sslHandler.handshake();
//	        handshakeFuture.addListener(new Greeter(sslHandler));
	    }

		/* (non-Javadoc)
		 * @see org.jboss.netty.channel.SimpleChannelHandler#channelClosed(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.ChannelStateEvent)
		 */
		@Override
		public void channelClosed(final ChannelHandlerContext ctx, final ChannelStateEvent e)
				throws Exception
		{
			super.channelClosed(ctx, e);
		}

		/* (non-Javadoc)
		 * @see org.jboss.netty.channel.SimpleChannelHandler#channelBound(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.ChannelStateEvent)
		 */
		@Override
		public void channelBound(final ChannelHandlerContext ctx, final ChannelStateEvent e)
				throws Exception
		{
			super.channelBound(ctx, e);
		}

		/* (non-Javadoc)
		 * @see org.jboss.netty.channel.SimpleChannelHandler#channelDisconnected(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.ChannelStateEvent)
		 */
		@Override
		public void channelDisconnected(final ChannelHandlerContext ctx,
				final ChannelStateEvent e) throws Exception
		{
			super.channelDisconnected(ctx, e);
		}

		/* (non-Javadoc)
		 * @see org.jboss.netty.channel.SimpleChannelHandler#channelOpen(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.ChannelStateEvent)
		 */
		@Override
		public void channelOpen(final ChannelHandlerContext ctx, final ChannelStateEvent e)
				throws Exception
		{
			super.channelOpen(ctx, e);
		}

		/* (non-Javadoc)
		 * @see org.jboss.netty.channel.SimpleChannelHandler#channelUnbound(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.ChannelStateEvent)
		 */
		@Override
		public void channelUnbound(final ChannelHandlerContext ctx,
				final ChannelStateEvent e) throws Exception
		{
			super.channelUnbound(ctx, e);
		}

		/* (non-Javadoc)
		 * @see org.jboss.netty.channel.SimpleChannelHandler#messageReceived(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.MessageEvent)
		 */
		@Override
		public void messageReceived(final ChannelHandlerContext ctx, final MessageEvent e)
				throws Exception
		{
			super.messageReceived(ctx, e);
		}

		/* (non-Javadoc)
		 * @see org.jboss.netty.channel.SimpleChannelHandler#unbindRequested(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.ChannelStateEvent)
		 */
		@Override
		public void unbindRequested(final ChannelHandlerContext ctx,
				final ChannelStateEvent e) throws Exception
		{
			super.unbindRequested(ctx, e);
		}

		/* (non-Javadoc)
		 * @see org.jboss.netty.channel.SimpleChannelHandler#writeComplete(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.WriteCompletionEvent)
		 */
		@Override
		public void writeComplete(final ChannelHandlerContext ctx,
				final WriteCompletionEvent e) throws Exception
		{
			super.writeComplete(ctx, e);
		}

		/* (non-Javadoc)
		 * @see org.jboss.netty.channel.SimpleChannelHandler#writeRequested(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.MessageEvent)
		 */
		@Override
		public void writeRequested(final ChannelHandlerContext ctx, final MessageEvent e)
				throws Exception
		{
			super.writeRequested(ctx, e);
		}
	}
	
	
	

	static public class TrackerHandler extends SimpleChannelHandler
	{

		@Override
		public void handleUpstream(final ChannelHandlerContext ctx, final ChannelEvent e) throws Exception
		{
//			ctx.sendUpstream(e);
			super.handleUpstream(ctx, e);
		}

		@Override
		public void handleDownstream(final ChannelHandlerContext ctx, final ChannelEvent e) throws Exception
		{
//			ctx.sendDownstream(e);
			super.handleDownstream(ctx, e);
		}

	    @Override
	    public void channelConnected(final ChannelHandlerContext ctx, final ChannelStateEvent e) throws Exception 
	    {
	        super.channelConnected(ctx, e);
	    }

		/* (non-Javadoc)
		 * @see org.jboss.netty.channel.SimpleChannelHandler#channelClosed(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.ChannelStateEvent)
		 */
		@Override
		public void channelClosed(final ChannelHandlerContext ctx, final ChannelStateEvent e) throws Exception
		{
			super.channelClosed(ctx, e);
		}

		/* (non-Javadoc)
		 * @see org.jboss.netty.channel.SimpleChannelHandler#channelBound(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.ChannelStateEvent)
		 */
		@Override
		public void channelBound(final ChannelHandlerContext ctx, final ChannelStateEvent e) throws Exception
		{
			super.channelBound(ctx, e);
		}

		/* (non-Javadoc)
		 * @see org.jboss.netty.channel.SimpleChannelHandler#channelDisconnected(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.ChannelStateEvent)
		 */
		@Override
		public void channelDisconnected(final ChannelHandlerContext ctx,
				final ChannelStateEvent e) throws Exception
		{
			super.channelDisconnected(ctx, e);
		}

		/* (non-Javadoc)
		 * @see org.jboss.netty.channel.SimpleChannelHandler#channelOpen(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.ChannelStateEvent)
		 */
		@Override
		public void channelOpen(final ChannelHandlerContext ctx, final ChannelStateEvent e)
				throws Exception
		{
			super.channelOpen(ctx, e);
		}

		/* (non-Javadoc)
		 * @see org.jboss.netty.channel.SimpleChannelHandler#channelUnbound(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.ChannelStateEvent)
		 */
		@Override
		public void channelUnbound(final ChannelHandlerContext ctx,
				final ChannelStateEvent e) throws Exception
		{
			super.channelUnbound(ctx, e);
		}

		/* (non-Javadoc)
		 * @see org.jboss.netty.channel.SimpleChannelHandler#messageReceived(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.MessageEvent)
		 */
		@Override
		public void messageReceived(final ChannelHandlerContext ctx, final MessageEvent e)
				throws Exception
		{
			super.messageReceived(ctx, e);
		}

		/* (non-Javadoc)
		 * @see org.jboss.netty.channel.SimpleChannelHandler#unbindRequested(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.ChannelStateEvent)
		 */
		@Override
		public void unbindRequested(final ChannelHandlerContext ctx,
				final ChannelStateEvent e) throws Exception
		{
			super.unbindRequested(ctx, e);
		}

		/* (non-Javadoc)
		 * @see org.jboss.netty.channel.SimpleChannelHandler#writeComplete(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.WriteCompletionEvent)
		 */
		@Override
		public void writeComplete(final ChannelHandlerContext ctx,
				final WriteCompletionEvent e) throws Exception
		{
			super.writeComplete(ctx, e);
		}

		/* (non-Javadoc)
		 * @see org.jboss.netty.channel.SimpleChannelHandler#writeRequested(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.MessageEvent)
		 */
		@Override
		public void writeRequested(final ChannelHandlerContext ctx, final MessageEvent e)
				throws Exception
		{
			super.writeRequested(ctx, e);
		}
	}
}
