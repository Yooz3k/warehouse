package pg.ium.warehouse.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRoles {

	MANAGER("manager"),
	EMPLOYEE("employee");

	private final String name;
}
