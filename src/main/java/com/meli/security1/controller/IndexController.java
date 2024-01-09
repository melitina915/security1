// 시큐리티 설정
// 시큐리티 회원가입
// 시큐리티 권한처리
// 구글 로그인 준비
// Authentication 객체가 가질 수 있는 2가지 타입
// 구글 로그인 및 자동 회원가입 진행 완료

package com.meli.security1.controller;

import com.meli.security1.config.auth.PrincipalDetails;
import com.meli.security1.model.User;
import com.meli.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
// @Controller
// View를 리턴하겠다는 의미
public class IndexController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/test/login")
    public @ResponseBody String testLogin(
            Authentication authentication,
            // Authentication 객체 안에 DI(의존성 주입)를 한다.
            //@AuthenticationPrincipal UserDetails userDetails) {
            @AuthenticationPrincipal PrincipalDetails userDetails) {
            // @AuthenticationPrincipal 어노테이션을 통해 세션 정보에 접근할 수 있다
            // 이는 UserDetails 타입을 가지고 있고,
            // PrincipalDetails를 UserDetails 타입으로 implementation했기 때문에
            // PrincipalDetails 타입으로도 받을 수 있다
        // authentication에 DI(Dependency Injection 의존성 주입)를 하고
        // authentication 객체 안에 .getPrincipal()이 있고,
        // .getPrincipal() 자체는 사실 PrincipalDetails.java인데,
        // 이 .getPrincipal()이 리턴하는 타입이 오브젝트이기 때문에
        // (PrincipalDetails)로 다운캐스팅하여 principalDetails로 받아 .getUser()로 호출하면
        System.out.println("/test/login ==================");
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("authentication.getPrincipal():" + authentication.getPrincipal());
        System.out.println("authentication:" + principalDetails.getUser());
        // authentication을 (PrincipalDetails)로 다운캐스팅하는 과정을 통해 .getUser()를 할 수 있고

        System.out.println("userDetails.getUsername():" + userDetails.getUsername());
        System.out.println("userDetails.getUser():" + userDetails.getUser());
        // @AuthenticationPrincipal이라는 어노테이션을 통해 .getUser()를 찾을 수 있다.
        return "세션 정보 확인하기";
    }

    @GetMapping("/test/oauth/login")
    public @ResponseBody String testOAuthLogin(
            Authentication authentication,
            @AuthenticationPrincipal OAuth2User oauth) {
        System.out.println("/test/oauth/login ==================");
        //PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        // 구글 로그인을 통해 정보를 받기 위해서는 (OAuth2User)로 캐스팅해야 한다.
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        // authentication을 .getPrincipal()하여 (OAuth2User)로 다운캐스팅한다.
        System.out.println("authentication: " + oauth2User.getAttributes());
        // 위 정보는 PrincipalOauth2UserService.java에서 받는
        // super.loadUser(userRequest).getAttributes()와 같다.
        System.out.println("oauth2User: " + oauth.getAttributes());
        // {sub=113438845545584165642, name=윤채은, given_name=채은, family_name=윤, picture=https://lh3.googleusercontent.com/a/ACg8ocJ19-2R2LGMGh8GnAALuWF9oWeGF8K2wzVRgK5rG7aQYQ=s96-c, email=melitina915@sookmyung.ac.kr, email_verified=true, locale=ko, hd=sookmyung.ac.kr}
        // sub = 회원이 갖고 있는 구글의 Primary Key
        // 위의 타입들이 Map<String, Object> 타입으로
        // oauth2user.getAttributes()에 저장되어있다.
        // 위 정보들을 유저 오브젝트로 구성할 것이다.
        // 위 정보들을 오브젝트화 시키지 않고 Map<String, Object>로 통째로 넣은 것이다.
        // 이는 oauth2User.getAttributes()를 통해 받는 정보와 같다.
        // 따라서 Authentication을 통해서 정보를 받는 방법과
        // @AuthenticationPrincipal을 통해서 정보를 받는 방법이 있다.
        return "OAuth 세션 정보 확인하기";
    }

    // 스프링 시큐리티는 서버에 자기만의 시큐리티 세션이 있다.
    // 스프링 시큐리티 세션에 들어갈 수 있는 타입은 Authentication 객체밖에 없다.
    // 이 Authentication 객체를 Controller에서 필요할 때마다 DI(의존성 주입)를 할 수 있다.
    // Authentication 객체 안에 들어갈 수 있는 두 타입은 UserDetails 타입과 OAuth2User 타입이 있다.
    // 정리하자면 시큐리티가 갖고 있는 세션에는 무조건 Authentication 객체만 들어갈 수 있고,
    // Authentication 객체 안에 들어갈 수 있는 두 타입은 UserDetails 타입과 OAuth2User 타입밖에 없다.
    // 일반적인 로그인을 할 때 UserDetails 타입으로 Authentication 객체 안에 들어가게 되고
    // 구글 로그인 등의 OAuth 로그인을 할 때 OAuth2User 타입으로 Authentication 객체 안에 들어가게 된다.
    // 이렇게 세션이 생성되어 로그인이 된다.

    // localhost:8080/
    // localhost:8080
    @GetMapping({"", "/"})
    public String index() {
        return "index";
        // src/main/resources/templates/index.mustache
        // index 파일은 View가 된다

        // mustache
        // mustache는 Spring에서 사용하도록 권장하는 템플릿 머신이다
        // 머스테치 기본폴더 src/main/resources/
        // 뷰리졸버 설정 : templates (prefix), .mustache (suffix) 생략가능
    }

    // OAuth 로그인을 해도 PrincipalDetails 타입을 받을 수 있다.
    // 일반 로그인을 해도 PrincipalDetails 타입을 받을 수 있다.
    @GetMapping("/user")
    //public @ResponseBody String user() {
    // 일반적인 로그인을 할 때 아래와 같이 매개변수를 받아야 한다.
    //public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails userDetails) {
    // 구글 로그인을 할 때는 아래와 같이 매개변수를 받게 된다.
    //public @ResponseBody String user(@AuthenticationPrincipal OAuth2User oauth) {
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
    // 이렇게 Authentication을 할 때 UserDetails 또는 OAuth2User 타입이 오게 되므로
    // X라는 클래스를 하나 만들어 UserDetails, OAuth2User를 implementation(상속)받아
    // UserDetails와 OAuth2User의 부모로 X를 두면
    // Authentication 객체 자체가 UserDetails 또는 OAuth2User 타입이기만 하면 모두 올 수 있기 때문에
    // X에 UserDetails와 OAuth2User를 담은 것이다.
    // 이 X의 역할을 해줄 것이 PrincipalDetails이다.
        System.out.println("principalDetails: " + principalDetails.getUser());

        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager() {
        return "manager";
    }

    // 로그인
    // 스프링 시큐리티 해당 주소를 낚아채버린다 - SecurityConfig 파일 생성 후 작동 안함
    @GetMapping("/loginForm")
    public String loginForm() {
        return "loginForm";
    }

    // 회원가입을 할 수 있는 페이지
    @GetMapping("/joinForm")
    public String joinForm() {
        return "joinForm";
    }

    // 회원가입 진행
    @PostMapping("/join")
    //public @ResponseBody String join(User user) {
    public String join(User user) {
    // User user를 받는다
        System.out.println(user);
        user.setRole("ROLE_USER");
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        // user.setPassword에 encPassword를 넣고 저장한다.
        // 이렇게 하면 비밀번호가 그대로 DB에 들어가지 않고 인코딩되어 들어간다.
        // createDate에 @CreationTimeStamp 설정이 되어 있으므로
        // 이는 따로 만들어줄 필요가 없다
        userRepository.save(user);
        // 이렇게 하면 회원가입은 잘 되지만,
        // 비밀번호를 1234와 같이 설정할 수 있어 시큐리티로 로그인을 할 수 없다.
        // 이유는 패스워드 암호화가 되지 않았기 때문이다.
        //return "join";
        return "redirect:/loginForm";
        // redirect:를 붙이면 /loginForm 함수를 호출해준다.
    }

//    @GetMapping("/joinProc")
//    public @ResponseBody String joinProc() {
//        return "회원가입 완료됨!";
//    }

    @Secured("ROLE_ADMIN")
    // @Secured
    // 특정 메서드에 간단하게 걸고싶으면 간단하게 걸 수 있다
    // SecurityConfig.java의
    // @EnableMethodSecurity의 securedEnabled = true와 연결
    // ROLE 하나만 걸고싶으면 @Secured 사용,
    // ROLE 두 개 이상 걸고싶으면 @PreAuthorize 사용
    @GetMapping("/info")
    public @ResponseBody String info() {
        return "개인정보";
    }


    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    // @PreAuthorize()
    // data() 메소드가 실행되기 직전에 실행된다.
    // SecurityConfig.java의
    // @EnableMethodSecurity의 prePostEnabled = true와 연결
    // ROLE 하나만 걸고싶으면 @Secured 사용,
    // ROLE 두 개 이상 걸고싶으면 @PreAuthorize 사용
    // 위 두 개의 권한(ROLE)을 갖고 있으면
    // localhost:8080/data 접근 가능
    // @PostAuthorize()
    // 위 어노테이션은 data() 메소드가 실행된 후 실행된다.
    @GetMapping("/data")
    public @ResponseBody String data() {
        return "데이터정보";
    }
}
