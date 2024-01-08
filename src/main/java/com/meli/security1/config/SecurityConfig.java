// 시큐리티 회원가입

package com.meli.security1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
// 스프링 시큐리티 필터가 스프링 필터체인에 등록된다.
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
public class SecurityConfig {

    // 패스워드 암호화하는 빈 등록
    // 해당 메서드의 리턴되는 오브젝트를 IoC(빈)로 등록해준다.
    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    //@Override
    @Bean
//    protected void configure(HttpSecurity http) throws Exception {
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //http.csrf().disable();
        //http.authorizeRequests()
                //.antMatchers("/user/**").authenticated()
                //.antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                //.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                //.anyRequest().permitAll()
                //.and()
                //.formLogin*(
                //.loginPage("/login");

        //csrfConfig.disable()
        http
                .csrf(AbstractHttpConfigurer::disable
                )
                // protected void configure(HttpSecurity http) 함수 내부에 권한 설정법
                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests
                                .requestMatchers("/user/**").authenticated()
                                // /user로 가면 인증해야한다는 의미
                                // http://localhost:8080/user로 가면
                                // 자동으로 http://localhost:8080/loginForm 주소로 보내진다
                                .requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER")
                                // /manager로 가면 ROLE_ADMIN 권한과 ROLE_MANAGER 권한이 있어야 한다는 의미
                                // http://localhost:8080/manager로 가면
                                // 자동으로 http://localhost:8080/loginForm 주소로 보내진다
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                // /admin으로 가면 ROLE_ADMIN 권한이 있어야 한다는 의미
                                .anyRequest().permitAll()
                                // 나머지는 모두 권한 허용이 되어있다
                )
                .formLogin((formLogin) ->
                        formLogin
                                .loginPage("/loginForm")
                                // /loginForm으로 이동
                );

        return http.build();

    }
}
