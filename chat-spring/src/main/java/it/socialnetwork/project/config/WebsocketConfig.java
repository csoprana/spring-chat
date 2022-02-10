package it.socialnetwork.project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ExecutorChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.sun.istack.Nullable;

import it.socialnetwork.project.repository.MessageRepository;

import org.springframework.messaging.Message;

@Configuration
@EnableWebSocketMessageBroker
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {

	@Autowired
	private MessageRepository messageRepository;

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {

		config.enableSimpleBroker("/topic", "/queue", "/user");
		config.setApplicationDestinationPrefixes("/app");
		config.setUserDestinationPrefix("/user");

	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {

		registry.addEndpoint("/chat");
		// .withSockJS()

	}
	

	public void configureClientOutboundChannel(ChannelRegistration registration) {
		registration.interceptors(new ExecutorChannelInterceptor() {
			@Override
			public void afterMessageHandled(Message<?> message, MessageChannel channel, MessageHandler handler,
					@Nullable Exception ex) {
				
				
				outChannel = channel;
			}
		});
		
		
	}

	private MessageChannel outChannel;

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors(new ExecutorChannelInterceptor() {

		

			@Override
			public void afterMessageHandled(Message<?> inMessage, MessageChannel inChannel, MessageHandler handler,
					Exception ex) {

				StompHeaderAccessor inAccessor = StompHeaderAccessor.wrap(inMessage);
				
				// quando il server riceve il messaggio lo comunica al client
				if (StompCommand.SEND.equals(inAccessor.getCommand())) {
					
					if (outChannel != null) {
						StompHeaderAccessor outAccessor = StompHeaderAccessor.create(StompCommand.RECEIPT);
						outAccessor.setSessionId(inAccessor.getSessionId());
						outAccessor.setLeaveMutable(true);
						outAccessor.setMessageId(inAccessor.getMessageId());
						outAccessor.addNativeHeader("receipt_id", inAccessor.getNativeHeader("receipt_id").toString());
						outAccessor.addNativeHeader("uuid", inAccessor.getNativeHeader("uuid").get(0).toString());
						Message<byte[]> outMessage = MessageBuilder.createMessage(new byte[0],outAccessor.getMessageHeaders());
						
						outChannel.send(outMessage);
					}
				}	
			}
		});
		registration.interceptors(new SubscribeInterceptor(messageRepository));

	}

}
