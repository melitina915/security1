// 시큐리티 로그인
// Authentication 객체가 가질 수 있는 2가지 타입
// 구글 로그인 및 자동 회원가입 진행 완료

package com.meli.security1.config.auth;

import com.meli.security1.model.User;
import com.meli.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// SecurityConfig.java의 시큐리티 설정에서
// loginProcessingUrl("/login");을 해놓았기 때문에
// /login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC되어있는
// loadUserByUsername 함수가 실행된다.
// 때문에 implements UserDetailsService를 해주어
// UserDetailsService 타입으로 꼭 만들어주어야 한다.
@Service
// @Service를 해주면 PrincipalDetailsService가 IoC로 등록되어
// loadUserByUsername이 자동으로 호출된다.
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    // 시큐리티 session(내부 Authentication(내부 UserDetails)) = Authentication(내부 UserDetails) = UserDetails
    // 시큐리티 session에 들어갈 수 있는 것은 Authentication 타입
    // Authentication 타입에는 UserDetails 타입이 들어가야 한다.
    // 따라서 해당 UserDetails가 Authentication 내부로 들어가고
    // session 내부에는 이를 담은 Authentication이 들어간다.
    // 이렇게 되면 로그인이 완료된다.
    // 함수 종료 시 @AuthenticationPrincipal 어노테이션이 만들어진다.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // loginForm.html에서 name="username"으로 했기 때문에
    // 매개변수도 username으로 동일하게 해야한다.
    // 아니면 값 매칭이 되지 않는다.
    // 매개변수 이름을 다르게 하려면 SecurityConfig.java의
    // filterChain에서 .usernameParameter("프론트에서 설정한 name")로 설정해줘야 한다.
        System.out.println("username:" + username);
        // logout하려면 스프링 시큐리티의 디폴트 로그아웃 주소인
        // localhost:8080/logout을 실행해주면 로그아웃된다.
        User userEntity = userRepository.findByUsername(username);
        // userRepository에서 username이 있는지 확인해봐야 한다.
        // 그런데 Repository들은 기본적인 CRUD만 갖고 있기 때문에
        // user를 검색하려면 UserRepository에 findByUsername을 만들어줘야 한다.

        // user가 있는 경우
        if (userEntity != null) {
            return new PrincipalDetails(userEntity);
            // PrincipalDetails 안에 꼭 user 오브젝트를 넣어줘야 한다.
            // loadUserByUsername이 리턴될 때 PrincipalDetails가 Authentication 객체 안에 들어간다.
        }
        // user가 없는 경우
        return null;
    }
}
