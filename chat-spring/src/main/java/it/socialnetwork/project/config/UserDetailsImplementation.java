package it.socialnetwork.project.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import it.socialnetwork.project.model.User;

public class UserDetailsImplementation implements UserDetails {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private User user;
	
	
	public UserDetailsImplementation(User user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		//SimpleGrantedAuthority authority = new SimpleGrantedAuthority("");
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		
		for(String role : user.getRoles()) {
			authorities.add(new SimpleGrantedAuthority(role));
		}
		
		return authorities;
		
		
		
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getUsername();
	}

	

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}
