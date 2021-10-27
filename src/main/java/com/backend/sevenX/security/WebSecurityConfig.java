package com.backend.sevenX.security;

import com.backend.sevenX.utills.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;
import java.util.ArrayList;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    Environment environment;
    @Resource(name = "userAuthenticate")
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(new PasswordEncoder() {
                    @Override
                    public String encode(CharSequence rawPassword) {
                        return Encryption.sha1(rawPassword.toString());
                    }

                    @Override
                    public boolean matches(CharSequence rawPassword, String encodedPassword) {
                        return encode(rawPassword).equals(encodedPassword);
                    }
                });
    }

    @Bean
    public JwtAuthenticationFilter authenticationTokenFilterBean() {

        return new JwtAuthenticationFilter();
    }

    @Override
    protected void configure(HttpSecurity http) {
        try {
            http.cors().and().csrf().disable().
                authorizeRequests()
                .antMatchers(getOtherFilters()).permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

            http.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String[] get8080Filters() {
        ArrayList<String> list = new ArrayList<>();
        list.add("/rest/**");
        return list.toArray(new String[list.size()]);
    }

    private String[] getOtherFilters() {
        ArrayList<String> list = new ArrayList<>();
        //add all public endpoints here
        list.add("/hello-world");
        list.add(Constant.EndPoints.PUBLIC);
        list.add(Constant.EndPoints.SIGNUP);
        list.add(Constant.EndPoints.AdminSignUp);
        list.add(Constant.EndPoints.LOGIN);
        list.add(Constant.EndPoints.FORGOT_PASSWORD);
        list.add(Constant.EndPoints.RESET_PASSWORD);
        list.add(Constant.EndPoints.Documents);
        list.add(Constant.EndPoints.PACKAGESBYSCREENNAME);
        list.add(Constant.EndPoints.PACKAGESLIST);
        return list.toArray(new String[list.size()]);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers("/v2/api-docs", "/configuration/**", "/swagger-resources/**", "/swagger-ui.html", "/webjars/**", "/api-docs/**");
        super.configure(web);
    }

    boolean isPort8081() {
        if (environment.containsProperty("server.port")) {
            String portNo = environment.getProperty("server.port");
            return portNo.equalsIgnoreCase("8081");
        }
        return false;
    }
}
