package it.socialnetwork.project.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import it.socialnetwork.project.model.User;
import it.socialnetwork.project.repository.UserRepository;

@Service
public class UserDetailsServiceImplementation  implements UserDetailsService {

	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Optional<User> user = userRepository.findByUsername(username);
		if(user.isPresent()) {
			return new UserDetailsImplementation(user.get());
		}
		throw new UsernameNotFoundException("Utente non presente");
		
		
	}

}
