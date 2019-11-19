package pg.ium.warehouse.security.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRoles {

	MANAGER("ROLE_MANAGER"),
	EMPLOYEE("ROLE_EMPLOYEE");

	private final String name;
}
