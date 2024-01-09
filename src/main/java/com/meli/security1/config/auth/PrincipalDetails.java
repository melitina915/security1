// 시큐리티 로그인
// Authentication 객체가 가질 수 있는 2가지 타입
// 구글 로그인 및 자동 회원가입 진행 완료

package com.meli.security1.config.auth;

// 시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킨다.
// 로그인 진행이 완료되면 시큐리티 자신만의 session을 만들어 준다.
// (Security ContextHolder)에 세션 정보를 저장하는 식이다.
// 세션에 들어갈 수 있는 정보는
// 시큐리티가 가지고 있는 세션에 들어갈 수 있는 오브젝트가 정해져 있다.
// 오브젝트 타입 => Authentication 타입 객체
// Authentication 안에 User 정보가 있어야 한다.
// User 오브젝트 타입 => UserDetails 타입 객체

// Security Session => Authentication => UserDetails
// 시큐리티가 가진 세션 영역 : Security Session
// => 여기에 들어갈 수 있는 객체 : Authentication
// => 해당 객체 안에 들어가야 할 타입 : UserDetails

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.meli.security1.model.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Data
public class PrincipalDetails implements UserDetails, OAuth2User {
// PrincipalDetails가 UserDetails와 OAuth2User를 상속받게 해서
// 어느 쪽 타입을 받든 Authentication 객체 안에 들어갈 수 있도록 한다.
// PrincipalDetails 클래스를 만든 목적은 두 가지이다.
// 1. 시큐리티 세션에 들어갈 수 있는 타입은 Authentication 객체이다.
// Authentication 객체에 들어갈 수 있는 두 필드는 OAuth2User 타입과 UserDetails 타입이다.
// 회원가입을 하게 되면 User 오브젝트가 필요하다.
// 그런데 OAuth2User와 UserDetails는 User 오브젝트를 포함하고 있지 않다.
// 그래서 PrincipalDetails 클래스를 만들어 UserDetails를 implementation하여(상속받아서) User 오브젝트를 가질 수 있게 하였다.
// 그래서 Authentication에 UserDetails 대신 PrincipalDetails를 넣을 수 있게 한 것이다.
// 이렇게 하면 PrincipalDetails는 User 오브젝트를 가지고 있으므로 세션 정보에 접근할 수 있게 된다.
// 그런데 OAuth로 로그인할 때는 OAuth2User를 사용해야 하므로 프로그램이 굉장히 복잡해지는 문제가 되었다.
// 그래서 OAuth2User도 PrincipalDetails가 가질 수 있게 한 것이다.
// OAuth2User도 PrincipalDetails에 묶이면서 User 오브젝트를 가지게 되었다.
// 따라서 User 오브젝트를 가지고 있는 PrincipalDetails만 꺼내 쓰면 되는 상황이 되어 문제가 해결되었다.

    private User user;
    // 콤포지션
    private Map<String, Object> attributes;

    // 일반 로그인 시 사용하는 생성자
    public PrincipalDetails(User user) {
        this.user = user;
    }

    // OAuth 로그인 시 사용하는 생성자
    // OAuth2User로 로그인 할 때 Authentication에 PrincipalDetails를 저장할 것이다.
    // OAuth 로그인을 하면 attributes 정보와 user 정보를 가지게 될 것이다.
    // attributes 정보를 토대로 user 정보가 생성될 것이다.
    public PrincipalDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    // 해당 User의 권한을 리턴하는 곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        // 본래 유저의 권한은 user.Role()이지만
        // user.Role()은 String을 리턴하므로
        // 이를 리턴할 순 없다.
        // 때문에 함수의 타입으로 리턴시켜줘야한다.
        Collection<GrantedAuthority> collect = new ArrayList<>();
        // ArrayList는 Collection의 자식이므로 이를 리턴해주면 된다.
        collect.add(new GrantedAuthority() {
            // 여기 안에서는 String을 리턴해줄 수 있다.
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        // collect 안에 GrantedAuthority 타입을 add해줘야 한다.
        return collect;
        // collect 안에 user.getRole()이 추가됐으므로
        // collect를 리턴해주면 된다.
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // 계정이 만료되지 않았는지
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정이 잠기지 않았는지
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 계정의 비밀번호 변경이 1년이 지나지 않았는지
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정이 활성화 되었는지
    @Override
    public boolean isEnabled() {

        // 사이트에서 1년 동안 회원이 로그인하지 않으면
        // 휴면 계정으로 전환하기로 한 경우
        //user.getLoginDate();
        // 위를 체크하여
        // 현재시간 - 마지막으로 로그인한 시간
        // => 1년을 초과하면 return false; 해줄 수 있다.

        return true;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        //return attributes.get("sub");
        // 구글 회원 정보 PK가 sub이다.
        return null;
    }
}
