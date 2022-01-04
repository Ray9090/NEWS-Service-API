package com.newsservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	
	@Qualifier("userDetailsServiceImpl")
    @Autowired
    private UserDetailsService userDetailsService;

	@Autowired
	private BCryptPasswordEncoder bcryptPasswordEncoder;


//    @Bean(name = "passwordEncoder")
//    @Qualifier("passwordEncoder")
//    public PasswordEncoder passwordEncoder() {
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//    }

    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        return authenticationManager();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bcryptPasswordEncoder);
    }

	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http.csrf().disable();
		http
        .authorizeRequests()
        	.antMatchers("/", "/authentication", "/h2-console/**").permitAll()
            .antMatchers("/static/main/js/css/**", "/js/**", "/registration").permitAll()
                .antMatchers("/admin*").hasRole("ADMIN")
            .anyRequest().authenticated()
            .and()
        .formLogin()
            .loginPage("/login")
            .permitAll()
            .and()
            .exceptionHandling().accessDeniedPage("/403")
        .and()
        .logout()
            .permitAll();
		//For h2-console 
		http.csrf().disable();
	    http.headers().frameOptions().disable();
	    
//		http
//		.cors()
//			.and()
//		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//			.and()
//		.csrf().disable()
//		.authorizeRequests()
//			.antMatchers("/", "/h2-console/**", "/console/**","/error", "/api/all", "/api/auth/**", "/oauth2/**").permitAll()
//			.antMatchers("/users/login", "/users/register").permitAll()
//			.antMatchers("/", "/home").permitAll()
//			.and()
//			.authorizeRequests().antMatchers("/h2-console/**").permitAll()
//		.anyRequest()
//			.authenticated()
//			.and()
//			.formLogin()
//	        .loginPage("/login")
//	            .permitAll()
//	            .failureUrl("/login?error=true")
//	            .defaultSuccessUrl("/users")
//	            .usernameParameter("email")
//	            .passwordParameter("password")
//	            .and()
//	        .logout()
//	            .permitAll();
        
     // for h2-console purposes

		
	}
	
//	// This bean is load the user specific data when form login is used.
//		@Override
//		public UserDetailsService userDetailsService() {
//			return userDetailsService;
//		}
//
//		@Bean
//		public PasswordEncoder passwordEncoder() {
//			return new BCryptPasswordEncoder(10);
//		}
//
//		@Bean(BeanIds.AUTHENTICATION_MANAGER)
//		@Override
//		public AuthenticationManager authenticationManagerBean() throws Exception {
//			return super.authenticationManagerBean();
//		}
}
