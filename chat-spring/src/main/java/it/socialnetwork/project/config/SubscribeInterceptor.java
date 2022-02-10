package it.socialnetwork.project.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import it.socialnetwork.project.model.UserMessage;
import it.socialnetwork.project.repository.MessageRepository;


public class SubscribeInterceptor implements ChannelInterceptor {

	MessageRepository messageRepository;
	
	
	
	
	public SubscribeInterceptor(MessageRepository messageRepository) {
		super();
		this.messageRepository = messageRepository;
		
		
	}
	

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		// TODO Auto-generated method stub
		//System.out.println(message.getHeaders());
		StompHeaderAccessor headerAccessor= StompHeaderAccessor.wrap(message);
		SimpMessageHeaderAccessor nativeAccessor = SimpMessageHeaderAccessor.wrap(message);
		Authentication authentication = (Authentication)message.getHeaders().get("simpUser");
		
		if(StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())) {
			
			//controlliamo che il percorso sia corretto
			if(nativeAccessor.getHeader("simpDestination").equals("/user/queue/reply")) {
				nativeAccessor.setHeader("simpDestination", "/user/" + authentication.getName() +  "/queue/reply");
				Message<?> newMessage = MessageBuilder.createMessage(new byte[0], nativeAccessor.getMessageHeaders());
				return newMessage;
			}
			throw new RuntimeException("Endpoint modificato in modo malevolo");	
		}
		
		// generiamo l'UUID qui in modo che sia accoppiato all'id generato dal client
		if(StompCommand.SEND.equals(headerAccessor.getCommand())) {
			StompHeaderAccessor inAccessor = StompHeaderAccessor.wrap(message);
			StompHeaderAccessor outAccessor = StompHeaderAccessor.create(StompCommand.SEND);
			UUID uuid = UUID.randomUUID();
			outAccessor = inAccessor;
			outAccessor.addNativeHeader("uuid", uuid.toString());
			Message<?> outMessage = MessageBuilder.createMessage(message.getPayload(), outAccessor.getMessageHeaders());
			return outMessage;	
		}
		
		
		// l'avvenuta ricezione del messaggio viene salvata nel database
		if(StompCommand.ACK.equals(headerAccessor.getCommand())) {
			messageRepository.setHasReceivedToTrue(UUID.fromString(nativeAccessor.getNativeHeader("message-id").get(0)));
		}
		return message;	
	}

	
}
