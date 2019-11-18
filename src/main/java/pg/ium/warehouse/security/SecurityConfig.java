package pg.ium.warehouse.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import static pg.ium.warehouse.security.UserRoles.EMPLOYEE;
import static pg.ium.warehouse.security.UserRoles.MANAGER;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	JwtTokenProvider jwtTokenProvider;

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.httpBasic().disable()
				.csrf().disable()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.authorizeRequests()
				.antMatchers("/auth/login").permitAll()
				.antMatchers("/me").authenticated()
				.antMatchers(HttpMethod.DELETE, "/tyres**").hasRole(MANAGER.getName())
				.antMatchers(HttpMethod.GET, "/tyres**").hasAnyRole(MANAGER.getName(), EMPLOYEE.getName())
				.antMatchers(HttpMethod.POST, "/tyres**").hasAnyRole(MANAGER.getName(), EMPLOYEE.getName())
				.antMatchers(HttpMethod.PUT, "/tyres**").hasAnyRole(MANAGER.getName(), EMPLOYEE.getName())
				.antMatchers(HttpMethod.PATCH, "/tyres**").hasAnyRole(MANAGER.getName(), EMPLOYEE.getName())
				.anyRequest().authenticated()
				.and()
				.apply(new JwtConfigurer(jwtTokenProvider));
	}
}