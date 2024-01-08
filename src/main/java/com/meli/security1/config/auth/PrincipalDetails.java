// 시큐리티 로그인

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

import com.meli.security1.model.User;

@Data
public class PrincipalDetails implements UserDetails {

    private User user;
    // 콤포지션

    // 생성자
    public PrincipalDetails(User user) {
        this.user = user;
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
}
