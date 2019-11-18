package pg.ium.warehouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pg.ium.warehouse.repository.UserRepository;
import pg.ium.warehouse.security.AuthenticationRequest;
import pg.ium.warehouse.security.JwtTokenProvider;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	JwtTokenProvider jwtTokenProvider;
	@Autowired
	UserRepository users;

	@PostMapping("/login")
	public ResponseEntity login(@RequestBody AuthenticationRequest request) {
		try {
			String username = request.getUsername();
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, request.getPassword()));
			String token = jwtTokenProvider.createToken(username, users.findByUsername(username)
					.orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found"))
					.getRoles());
			Map<Object, Object> model = new HashMap<>();
			model.put("username", username);
			model.put("token", token);
			return ok(model);
		} catch (AuthenticationException e) {
			throw new BadCredentialsException("Received invalid credentials!");
		}
	}
}