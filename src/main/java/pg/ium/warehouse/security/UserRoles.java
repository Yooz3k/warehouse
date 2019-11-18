package pg.ium.warehouse.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRoles {

	MANAGER("role_manager"),
	EMPLOYEE("role_employee");

	private final String name;
}
