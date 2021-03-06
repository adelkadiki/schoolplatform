package com.application.system.security;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
public class SystemSecurity extends WebSecurityConfigurerAdapter{

	@Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private DataSource dataSource;

    @Value("${spring.queries.users-query}")
    private String usersQuery;

    @Value("${spring.queries.roles-query}")
    private String rolesQuery;
    
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth)throws Exception{
    	auth.
        jdbcAuthentication()
        .usersByUsernameQuery(usersQuery)
        .authoritiesByUsernameQuery(rolesQuery)
        .dataSource(dataSource)
        .passwordEncoder(bCryptPasswordEncoder);
    	
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception{
    	
    	http.authorizeRequests().antMatchers("/", "/userlogin", "/newuser", "/add", "/sumbitinfo").permitAll().antMatchers("/admin/**").hasAuthority("ADMIN")
    	.antMatchers("/user/**").hasAuthority("USER")
    	.antMatchers("/student/**").hasAuthority("STUDENT")
    	.anyRequest().authenticated().and().csrf().disable().formLogin().loginPage("/userlogin").defaultSuccessUrl("/").failureUrl("/userlogin?error=true")
    	.usernameParameter("id").passwordParameter("password").and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
    	.logoutSuccessUrl("/").and().exceptionHandling().accessDeniedPage("/accessdenied");
    }
	
}
