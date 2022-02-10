package it.socialnetwork.project.controller;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import it.socialnetwork.project.model.Chat;
import it.socialnetwork.project.model.UserMessage;
import it.socialnetwork.project.repository.ChatRepository;
import it.socialnetwork.project.repository.MessageRepository;


@Controller
public class ChatController {
	
	@Autowired
	private MessageRepository messageRepository;
	
	@Autowired
	private ChatRepository chatRepository;
	
	@Autowired
    private SimpMessagingTemplate messageSender;
	
	// impedisco di tornare alla schermata login nel caso l'utente sia già loggato
	@GetMapping("/login")
	public String login(Principal principal,@RequestParam(required = false) String error, Model model) {
		if(error != null) {
			if(error.equals("true") && principal == null) {
				model.addAttribute("show", true);
				return "login";
			}
		}
		if(principal==null) {
			model.addAttribute("show", false);
			return "login";
		}else {
			return "redirect:/";
		}
	
	}
	
	
	
	// ritorna la pagina della chat
	@GetMapping("/")
	public String home(Model model, Principal principal) {
		//model.addAttribute("sessionId", principal.getName());
		if(principal != null) {
			model.addAttribute("sessionId", principal.getName());
		}else {
			model.addAttribute("sessionId", "Non autenticato");
		}
		return "chat";
	}
	
	@MessageMapping("/sendmessage")
	public void sendMessage(Message<?> message, UserMessage msg, Principal principal) {
		
		
		StompHeaderAccessor inAccessor = StompHeaderAccessor.wrap(message);
		UUID uuid = UUID.fromString(inAccessor.getNativeHeader("uuid").get(0));
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Long chatId = chatRepository.findChatId(principal.getName(), msg.getRecipient());
		if(chatId == null) {
			chatRepository.save(new Chat(principal.getName(), msg.getRecipient()));
		}
		chatId = chatRepository.findChatId(principal.getName(), msg.getRecipient());
		messageRepository.save(new UserMessage(uuid, msg.getRecipient(), principal.getName(), msg.getBody(),timestamp, chatId));
		messageSender.convertAndSendToUser(msg.getRecipient(), "/queue/reply", new UserMessage(uuid, msg.getRecipient(), principal.getName(), msg.getBody(),timestamp, chatId), Map.of("message-id", uuid.toString()));
	}
    
	
	
	@GetMapping("/data")
	@ResponseBody
	public List<UserMessage> getData(@RequestParam String user1, @RequestParam String user2, Principal principal){
		Long id = chatRepository.findChatId(user1, user2);
		//otteniamo gli ultimi messaggi della chat
		List<UserMessage> list = messageRepository.findLastMessages((long)1);
		//tutti i messaggi inviati dal client1 ora sono considerati "letti" dal client2
		messageRepository.setAllHasReceivedToTrue((long)1, principal.getName());
		//s.forEach(z -> System.out.println(z.getBody()));
	    return list;
	}
}



