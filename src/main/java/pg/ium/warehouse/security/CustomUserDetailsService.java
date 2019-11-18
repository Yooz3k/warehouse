package pg.ium.warehouse.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import pg.ium.warehouse.repository.UserRepository;

@Component
public class CustomUserDetailsService implements UserDetailsService {

	private UserRepository users;

	public CustomUserDetailsService(UserRepository users) {
		this.users = users;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return users.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " was not found!"));
	}
}