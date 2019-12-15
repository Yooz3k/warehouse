package pg.ium.warehouse.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import pg.ium.warehouse.security.config.UserRoles;

public class RoleChecker {

	public boolean currentUserInRole(UserRoles role) {
		SecurityContext context = SecurityContextHolder.getContext();
		if (context == null)
			return false;

		Authentication authentication = context.getAuthentication();
		if (authentication == null)
			return false;

		for (GrantedAuthority auth : authentication.getAuthorities()) {
			if (role.getName().equals(auth.getAuthority()))
				return true;
		}

		return false;
	}
}
