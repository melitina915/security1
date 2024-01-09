// 시큐리티 회원가입
// 시큐리티 로그인
// 시큐리티 권한처리
// 구글 로그인 준비
// 구글 회원 프로필 정보 받아보기
// Authentication 객체가 가질 수 있는 2가지 타입

package com.meli.security1.config;

// 1. 코드 받기 (인증이 되었다)
// 코드를 받았다는 것은 구글 로그인이 되어 정상적으로 인증됐다는 의미
// 2. 엑세스 토큰 (권한)
// 코드를 통해서 엑세스 토큰을 받으면 시큐리티 서버가 구글의 사용자 정보에 접근할 수 있는 권한이 생긴다.
// 3. 사용자 프로필 정보 가져오기
// 4-1. 해당 정보를 토대로 회원가입을 자동으로 진행시키기도 한다.
// 4-2. (이메일, 전화번호, 이름, 아이디) : 기본적인 정보
// 쇼핑몰 -> 집 주소
// 백화점몰 -> VIP 등급, 일반 등급
// 위와 같은 경우에는 추가적인 정보가 필요하기 때문에 추가적인 회원가입을 진행한다.
// 추가적인 정보 없이 기본적인 정보만 필요하다면 자동으로 회원가입을 진행할 수 있다.

import com.meli.security1.config.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
// 스프링 시큐리티 필터가 스프링 필터체인에 등록된다.
//@EnableGlobalMethodSecurity(securedEnabled = true)
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
// securedEnabled = true
// secured 어노테이션(@Secured) 활성화
// prePostEnabled = true
// preAuthorize 어노테이션(@preAuthorize)과
// postAuthorize 어노테이션(@postAuthorize) 활성화
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
public class SecurityConfig {

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

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
                                // .authenticated() : 인증만 하면 들어갈 수 있는 주소
                                // ROLE_USER를 가지고 있는 사용자가
                                // localhost:8080/manager 또는 localhost:8080/admin으로 가려고 하면
                                // 권한이 없기 때문에 접근할 수 없게 된다.
                                .requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER")
                                // /manager로 가면 ROLE_ADMIN 권한과 ROLE_MANAGER 권한이 있어야 한다는 의미
                                // http://localhost:8080/manager로 가면
                                // 자동으로 http://localhost:8080/loginForm 주소로 보내진다
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                // /admin으로 가면 ROLE_ADMIN 권한이 있어야 한다는 의미
                                // 테이블에서
                                // update user set role='ROLE_MANAGER' where id=2;
                                // update user set role='ROLE_ADMIN' where id=3;
                                // 각각 manager와 admin에게 권한 update해주고
                                // http://localhost:8080/manager
                                // http://localhost:8080/admin 등등 들어가보면 접근가능
                                .anyRequest().permitAll()
                                // 나머지는 모두 권한 허용이 되어있다
                )
                .formLogin((formLogin) ->
                // .formLogin()의 영향으로 인증이 필요하면 무조건 .loginPage() 안의 URL로 이동하게 되어있다.
                        formLogin
                                .loginPage("/loginForm")
                                // /loginForm으로 이동
                                //.usernameParameter("프론트에서 설정한 name")
                                // PrincipalDetailsService의 loadUserByUsername에서의
                                // 매개변수 이름을 프론트에서 설정한 name과 다르게 하려면
                                // SecurityConfig.java의 filterChain에서
                                // .usernameParameter("프론트에서 설정한 name")로 설정해줘야 한다.
                                .loginProcessingUrl("/login")
                                // /login 주소가 호출되면 시큐리티가 낚아채서 대신 로그인을 진행해준다.
                                // 시큐리티가 대신 진행해 주기 때문에 Controller에 /login을 만들지 않아도 된다.
                                .defaultSuccessUrl("/")
                                // 로그인에 성공한 다음에는 메인 페이지로 이동하도록 설정해준다.
                                // 또한 localhost:8080/logout 한 다음 localhost:8080/user를 실행하면
                                // 로그인 창으로 가게 되는게 로그인하면 localhost:8080/user로 가준다.
                                // 즉 별 입력이 없었으면 default인 메인 페이지로 보내주지만,
                                // 특정 페이지를 입력하면 로그인 한 뒤 해당 페이지로 보내준다는 것이다.
                )
                //.oauth2Login()
                //.loginPage("/loginForm")
                .oauth2Login(oauth2 ->
                        oauth2
                                .loginPage("/loginForm")
                                // 인증이 필요하면 무조건 .loginPage() 안의 URL로 이동하게 되어있다.
                                .userInfoEndpoint(userInfoEndpointConfig ->
                                // 구글 로그인이 완료된 뒤의 후처리가 필요함.
                                // 구글 로그인이 되면 코드를 받는 것이 아니라 엑세스 토큰 + 사용자 프로필 정보를 한 번에 받게 된다.
                                // 때문에 AuthClient 관련 라이브러리가 굉장히 편리하다
                                        userInfoEndpointConfig
                                                .userService(principalOauth2UserService))
                                                // .userService()에 넣어줄 타입이 OAuth2UserService가 돼야 한다.
                                                // 이를 PrincipalOauth2UserService.java에 만들어준다.
                                                // 이렇게 구글 로그인을 통해 보낸 엑세스 토큰 + 사용자 프로필 정보들이
                                                // PrincipalOauth2UserService.java의 loadUser 함수의 매개변수 userRequest에 리턴된다.

                );

        return http.build();

    }
}
