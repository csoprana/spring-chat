package it.socialnetwork.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import it.socialnetwork.project.model.User;
import it.socialnetwork.project.repository.UserRepository;



@SpringBootApplication
public class WebChatApplication implements CommandLineRunner{

	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	public static void main(String[] args) {
		SpringApplication.run(WebChatApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//userRepository.save(new User("cris", "cris", "cris@gmail.com", passwordEncoder.encode("123"), new String[] {"USER"}));
		//userRepository.save(new User("james", "james", "james@gmail.com", passwordEncoder.encode("123"), new String[] {"USER"}));		
	}

	
	
	
	
}
