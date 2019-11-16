package pg.ium.warehouse.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static pg.ium.warehouse.security.UserRoles.EMPLOYEE;
import static pg.ium.warehouse.security.UserRoles.MANAGER;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
//        http.cors().and().csrf().disable();
		http
				.csrf().disable();
//				.authorizeRequests()
//				.antMatchers(HttpMethod.GET, "/tyres/**").hasAnyRole(MANAGER.getName(), EMPLOYEE.getName())
//				.antMatchers(HttpMethod.POST, "/tyres/**").hasAnyRole(MANAGER.getName(), EMPLOYEE.getName())
//				.antMatchers(HttpMethod.PUT, "/tyres/**").hasAnyRole(MANAGER.getName(), EMPLOYEE.getName())
//				.antMatchers(HttpMethod.PATCH, "/tyres/**").hasAnyRole(MANAGER.getName(), EMPLOYEE.getName())
//				.antMatchers(HttpMethod.DELETE, "/tyres/**").hasRole(MANAGER.getName());
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
				.withUser("manager").password("password").roles(MANAGER.getName()).and()
				.withUser("employee").password("password").roles(EMPLOYEE.getName());
	}

    /*@Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }*/
}