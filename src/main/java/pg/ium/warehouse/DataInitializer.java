package pg.ium.warehouse;

import lombok.extern.jbosslog.JBossLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pg.ium.warehouse.domain.Tyre;
import pg.ium.warehouse.domain.User;
import pg.ium.warehouse.repository.TyreRepository;
import pg.ium.warehouse.repository.UserRepository;
import pg.ium.warehouse.security.config.UserRoles;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

@JBossLog
@Component
public class DataInitializer implements CommandLineRunner {

	@Autowired
	UserRepository users;

	@Autowired
	TyreRepository tyres;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) throws Exception {
		tyres.save(Tyre.builder()
				.producer("Goodyear")
				.name("Ultra Grp 9+")
				.rimSize(16)
				.price(new BigDecimal(320.00))
				.quantity(8)
				.build());
		tyres.save(Tyre.builder()
				.producer("Bridgestone")
				.name("Blizzak")
				.rimSize(17)
				.price(new BigDecimal(514.30))
				.quantity(5)
				.build());
		tyres.save(Tyre.builder()
				.producer("Michelin")
				.name("Alpine 4")
				.rimSize(19)
				.price(new BigDecimal(771.80))
				.quantity(3)
				.build());

		users.save(User.builder()
				.username("manager")
				.password(passwordEncoder.encode("password"))
				.roles(Arrays.asList(UserRoles.MANAGER.getName(), UserRoles.EMPLOYEE.getName()))
				.build());

		users.save(User.builder()
				.username("employee")
				.password(passwordEncoder.encode("password"))
				.roles(Collections.singletonList(UserRoles.EMPLOYEE.getName()))
				.build());

		//OAuth user
		users.save(User.builder()
				.username("106536279007322987683")
				//Theoretically it's possible to log in with this password!
				.password(passwordEncoder.encode("bCD2t4Q54yzXEtLGRcHX"))
				.roles(Arrays.asList(UserRoles.MANAGER.getName(), UserRoles.EMPLOYEE.getName()))
				.build());
	}
}
