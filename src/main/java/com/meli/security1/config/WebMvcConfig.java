// 시큐리티 설정

package com.meli.security1.config;

import org.springframework.boot.web.servlet.view.MustacheViewResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        MustacheViewResolver resolver = new MustacheViewResolver();
        // 해당 ViewResolver인 Mustache를 Override하여 재설정할 수 있다
        resolver.setCharset("UTF-8");
        // View의 인코딩은 UTF-8
        resolver.setContentType("text/html; charset=UTF-8");
        // 보내는 데이터 파일은 html 파일이며 이는 UTF-8이다
        resolver.setPrefix("classpath:/templates/");
        // classpath:은 대충 해당 프로젝트라고 생각하면 된다
        resolver.setSuffix(".html");
        // 이렇게 설정하면 .html 파일이라 해도 mustache라고 인식하게 된다

        registry.viewResolver(resolver);
        // registry로 viewResolver를 등록해준다



        // Spring Boot Security
        // 처음 실행하고 localhost:8080으로 가면 login 페이지로 가게 된다
        // 기본적으로 Spring Boot Security로 의존성을 설정하게 되면
        // 홈페이지로 들어가는 모든 주소가 바뀌어 인증이 필요한 홈페이지가 된다
        // Username은 user로 하고,
        // 비밀번호는 콘솔에 나오는 것을 복붙하면 된다
    }
}