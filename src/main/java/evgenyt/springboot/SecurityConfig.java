package evgenyt.springboot;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Security configurator
 * @author EUTyrin
 *
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
    PasswordEncoder passwordEncoder;
	
	@Autowired
	DataSource dataSource;	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// Add some users to memory storage
		auth.inMemoryAuthentication()
			.withUser("user1")
			.password(passwordEncoder.encode("pass1"))
			.authorities("role1")
			.and()
			.withUser("user2")
			.password(passwordEncoder.encode("pass2"))
			.authorities("role2");		
		
		// Some users from standard user database described in in sql files
		/*
		auth
			.jdbcAuthentication()
			.dataSource(dataSource);
			*/
		
		// Some users from custom database described in in sql files
		auth
			.jdbcAuthentication()
			.dataSource(dataSource)
			.usersByUsernameQuery(
			"select username, password, enabled from Users " +
			"where username=?")
			.authoritiesByUsernameQuery(
			"select username, authority from Authorities " +
			"where username=?");
		
		// Remote LDAP server
		/*
		auth.ldapAuthentication()
			.userSearchBase("ou=people")
			.userSearchFilter("(uid={0})")
			.groupSearchBase("ou=groups")
			.groupSearchFilter("member={0}")
			.passwordCompare()
			.passwordEncoder(new BCryptPasswordEncoder())
			.passwordAttribute("passcode")
			.contextSource()
			.url("ldap://tacocloud.com:389/dc=tacocloud,dc=com");
		*/
		
		/* Embedded LDAP server */
		/*
		auth
			.ldapAuthentication()
			.userSearchBase("ou=people")
			.userSearchFilter("(uid={0})")
			.groupSearchBase("ou=groups")
			.groupSearchFilter("member={0}")
			.passwordCompare()
			.passwordEncoder(new BCryptPasswordEncoder())
			.passwordAttribute("passcode")
			.contextSource()
			.root("dc=tacocloud,dc=com")
			.ldif("classpath:users.ldif");
		 */			
		
	}		
					
	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}

	/*
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// open h2 console
		http.authorizeRequests()
        	.antMatchers("/").permitAll()
        	.antMatchers("/h2_console/**").permitAll();
		http.csrf().disable();
		http.headers().frameOptions().disable();
	}		
	*/
	
}
