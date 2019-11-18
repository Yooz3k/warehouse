package pg.ium.warehouse.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import lombok.extern.jbosslog.JBossLog;
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
import pg.ium.warehouse.security.JwtTokenProvider;
import pg.ium.warehouse.security.dao.AuthenticationRequest;
import pg.ium.warehouse.security.dao.OAuthTokenVerificationRequest;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;
import static pg.ium.warehouse.OAuthTokens.WEB_CLIENT_TOKEN;

@JBossLog
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

	@PostMapping("/google/login")
	public ResponseEntity googleLogin(@RequestBody OAuthTokenVerificationRequest request) {
		try {
			GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
					.setAudience(Collections.singletonList(WEB_CLIENT_TOKEN))
					.build();

			GoogleIdToken idToken = verifier.verify(request.getIdToken());
			if (idToken != null) {
				GoogleIdToken.Payload payload = idToken.getPayload();

				String userId = payload.getSubject();
				log.info("User ID: " + payload.getSubject());

				String token = jwtTokenProvider.createToken(userId, users.findByUsername(userId)
						.orElseThrow(() -> new UsernameNotFoundException("User with username (ID) " + userId + " not found"))
						.getRoles());
				Map<Object, Object> model = new HashMap<>();
				model.put("username", userId);
				model.put("token", token);
				return ok(model);
			} else {
				throw new GeneralSecurityException();
			}
		} catch (GeneralSecurityException | IOException e) {
			throw new BadCredentialsException("Received invalid OAuth token!");
		} catch (UsernameNotFoundException e) {
			throw new BadCredentialsException("User not found!");
		}
	}
}