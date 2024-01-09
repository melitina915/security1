// 구글 회원 프로필 정보 받아보기
// Authentication 객체가 가질 수 있는 2가지 타입
// 구글 로그인 및 자동 회원가입 진행 완료

package com.meli.security1.config.oauth;

import com.meli.security1.config.auth.PrincipalDetails;
import com.meli.security1.model.User;
import com.meli.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
    // SecurityConfig.java의 .userService()에 넣을 데이터 타입이 OAuth2UserService이므로
    // 해당 클래스 타입도 DefaultOAuth2UserService로 만들어준다.

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    // 구글로부터 받은 userRequest 데이터에 대한 후처리되는 함수
    // loadUser 함수에 의해 후처리가 된다.
    @Override
    // OAuth 로그인했을 때 Override 한 이유
    // 1. PrincipalDetails 타입으로 묶기 위해
    // 2. OAuth 로그인 시 회원가입을 강제로 진행시키기 위해
    // 함수 종료 시 @AuthenticationPrincipal 어노테이션이 만들어진다.
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    // 구글 로그인을 통해 보낸 엑세스 토큰 + 사용자 프로필 정보들이 매개변수 userRequest에 리턴된다.
        System.out.println("getClientRegistration:" + userRequest.getClientRegistration());
        // getClientRegistration : 서버의 기본 정보들이 들어가 있다.
        // registrationId로 어떤 OAuth로 로그인했는지 확인 가능
        System.out.println("getAccessToken:" + userRequest.getAccessToken());
        System.out.println("getAccessToken.getTokenValue:" + userRequest.getAccessToken().getTokenValue());
        // userRequest를 통해 받는 정보들
        // 구글 로그인 버튼 클릭 -> 구글 로그인 창 -> 로그인 완료 -> code 리턴 (OAuth-Client 라이브러리) -> AccessToken 요청
        // 구글 로그인 버튼 클릭
        // -> 구글 로그인 창
        // -> 로그인 완료
        // -> code를 리턴 (리턴한 code를 OAuth-Client 라이브러리가 받아준다)
        // -> (받은 code를 통해) AccessToken 요청 (이후 AccessToken을 받는다)
        // userRequest 정보 -> loadUser 함수 -> 구글로부터 회원 프로필을 받아준다.
        // userRequest 정보를 통해 loadUser 함수를 호출하고,
        // 회원 프로필을 구글로부터 받기 위해 사용되는 함수가 loadUser 함수이다.

        OAuth2User oAuth2User = super.loadUser(userRequest);
        //System.out.println("getAttributes:" + super.loadUser(userRequest).getAttributes());
        System.out.println("getAttributes:" + oAuth2User.getAttributes());
        // getAttributes:{sub=113438845545584165642, name=윤채은, given_name=채은, family_name=윤, picture=https://lh3.googleusercontent.com/a/ACg8ocJ19-2R2LGMGh8GnAALuWF9oWeGF8K2wzVRgK5rG7aQYQ=s96-c, email=melitina915@sookmyung.ac.kr, email_verified=true, locale=ko, hd=sookmyung.ac.kr}
        // sub : 구글의 회원가입하는 것 관련 Primary Key, ID 넘버같은 것
        // 구글 로그인을 한 회원을 아래와 같이
        // .getAttributes()를 통해 강제 회원가입 진행 예정
        // username = "google_113438845545584165642"
        // password = "암호화(겟인데어)"
        // 구글로 로그인하는 회원은 아이디와 비밀번호를 직접 쳐서 로그인하는 회원은 아니기 때문에 password에는 아무거나 들어가도 상관없다.
        // email = "melitina915@sookmyung.ac.kr"
        // 구글에서 받은 이메일 그대로 넣기
        // role = "ROLE_USER"
        // provider = "google"
        // providerId = 113438845545584165642
        // providerId에는 구글의 sub가 들어가게 한다.

        // 회원가입을 강제로 진행해볼 예정
        String provider = userRequest.getClientRegistration().getClientId();
        // google
        String providerId = oAuth2User.getAttribute("sub");
        // google의 Primary Key
        String username = provider + "_" + providerId;
        // google_113438845545584165642
        // username이 충돌되지 않도록 google_을 붙여준다.
        String password = bCryptPasswordEncoder.encode("겟인데어");
        // OAuth 로그인이기 때문에 필요없지만 만들어준다.
        // 크게 의미가 없는 코드이다.
        String email = oAuth2User.getAttribute("email");
        String role = "ROLE_USER";

        // 이미 해당 아이디로 회원가입되어있는지 확인
        User userEntity = userRepository.findByUsername(username);

        if(userEntity == null){
            System.out.println("구글 로그인이 최초입니다.");
            userEntity = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(userEntity);
        } else {
            System.out.println("구글 로그인을 이미 한 적이 있습니다. 당신은 이미 자동 회원 가입이 되어 있습니다.");
        }

        return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
        // 위 정보는 Authentication 객체 안에 들어가게 된다.
        // 일반 로그인을 하면 user만 들고 있겠지만,
        // OAuth 로그인을 하면 user와 attributes라는 맵을 같이 들고 있을 것이다.
    }
}
