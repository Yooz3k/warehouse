package pg.ium.warehouse.security;

import lombok.extern.jbosslog.JBossLog;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@JBossLog
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
						 AuthenticationException authException) throws IOException {
		log.debug("JWT authentication failed: " + authException);

		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT authentication failed");
	}

}