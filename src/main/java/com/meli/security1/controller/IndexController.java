// 시큐리티 설정
// 시큐리티 회원가입
// 시큐리티 권한처리
// 구글 로그인 준비

package com.meli.security1.controller;

import com.meli.security1.model.User;
import com.meli.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    @GetMapping("/user")
    public @ResponseBody String user() {
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
