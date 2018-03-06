/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.transport;

import javax.net.ssl.SSLEngine;

import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.compression.ZlibDecoder;
import org.jboss.netty.handler.codec.compression.ZlibEncoder;
import org.jboss.netty.handler.codec.compression.ZlibWrapper;
import org.jboss.netty.handler.ssl.SslHandler;

import com.valloc.CategoryType;
import com.valloc.Constants;
import com.valloc.domain.Serializer;
import com.valloc.log.LogManager;
import com.valloc.log.Logger;
import com.valloc.security.SecuritySupportService;

/**
 * 
 * 
 * @author wstevens
 */
public class NettyPipelineFactory implements ChannelPipelineFactory
{
	private final Logger pipelineFactoryLogger = LogManager.manager().getLogger(NettyPipelineFactory.class, CategoryType.TRANSPORT);

	private final static String HANDLER_PREFIX = Constants.VALLOC.toLowerCase() + Constants.DOT;
	private final static String PIPELINE_HANDLER_1_SSL = HANDLER_PREFIX + "security";
	private final static String PIPELINE_HANDLER_2_COMPRESSION_PREFIX = HANDLER_PREFIX + "compression" + Constants.DOT;
	private final static String PIPELINE_HANDLER_2_COMPRESSION_ENCODER = PIPELINE_HANDLER_2_COMPRESSION_PREFIX + "encoder";
	private final static String PIPELINE_HANDLER_2_COMPRESSION_DECODER = PIPELINE_HANDLER_2_COMPRESSION_PREFIX + "decoder";
	private final static String PIPELINE_HANDLER_3_SERIALIZER_PREFIX = HANDLER_PREFIX + "serialization" + Constants.DOT;
	private final static String PIPELINE_HANDLER_3_SERIALIZER_ENCODER = PIPELINE_HANDLER_3_SERIALIZER_PREFIX + "encoder";
	private final static String PIPELINE_HANDLER_3_SERIALIZER_DECODER = PIPELINE_HANDLER_3_SERIALIZER_PREFIX + "decoder";
	private final static String PIPELINE_HANDLER_4_WIRE_MSG_CONVERTER = HANDLER_PREFIX + "wire-msg-converter";
	private final static String PIPELINE_HANDLER_5_FRAMEWORK = HANDLER_PREFIX + "framework";

	private ChannelHandler channelHandler;
	private SecuritySupportService securityService;
	private boolean useSslClientMode;
	private Serializer serializer;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.netty.channel.ChannelPipelineFactory#getPipeline()
	 */
	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline newChannelPipeline = null;
		try {
			newChannelPipeline = Channels.pipeline();

			final SSLEngine engine = securityService.newSSLContext().createSSLEngine();
			engine.setUseClientMode(useSslClientMode);

			newChannelPipeline.addLast("interaction-monitor-handler", new NettyInteractionMonitorHandler(useSslClientMode));

			// newChannelPipeline.addLast("DUMMY", new DummyHandler(useSslClientMode ? "client" : "server"));

			newChannelPipeline.addLast(PIPELINE_HANDLER_1_SSL, new SslHandler(engine));
			newChannelPipeline.addLast(PIPELINE_HANDLER_2_COMPRESSION_ENCODER, new ZlibEncoder(ZlibWrapper.GZIP));
			newChannelPipeline.addLast(PIPELINE_HANDLER_2_COMPRESSION_DECODER, new ZlibDecoder(ZlibWrapper.GZIP));
			newChannelPipeline.addLast(PIPELINE_HANDLER_3_SERIALIZER_ENCODER, new SerializerEncoder(serializer));
			newChannelPipeline.addLast(PIPELINE_HANDLER_3_SERIALIZER_DECODER, new SerializerDecoder(serializer));
			newChannelPipeline.addLast(PIPELINE_HANDLER_4_WIRE_MSG_CONVERTER, new MessageConverterChannelHandler());
			newChannelPipeline.addLast(PIPELINE_HANDLER_5_FRAMEWORK, channelHandler);
		} catch (final Exception e) {
			pipelineFactoryLogger.error("Problem while trying to create pipeline: %s.", e, e.toString());
		}

		return newChannelPipeline;
	}

	void setComponentApplicationChannelHandler(final ChannelHandler channelHandler) {
		this.channelHandler = channelHandler;
	}

	void setSecurityService(final SecuritySupportService securityService) {
		this.securityService = securityService;
	}

	void setUseSslClientMode(final boolean useSslClientMode) {
		this.useSslClientMode = useSslClientMode;
	}

	void setSerializer(final Serializer serializer) {
		this.serializer = serializer;
	}
}
