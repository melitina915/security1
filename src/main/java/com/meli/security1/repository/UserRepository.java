// 시큐리티 회원가입
// 시큐리티 로그인

package com.meli.security1.repository;

import com.meli.security1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

// CRUD 함수를 JpaRepository가 들고 있음
// @Repository라는 어노테이션이 없어도 IoC(자동으로 빈으로 등록)된다.
// 이유는 JpaRepository를 상속했기 때문
public interface UserRepository extends JpaRepository<User, Integer> {
    // Repository들은 기본적인 CRUD만 갖고 있기 때문에
    // user를 검색하려면 UserRepository에 findByUsername을 만들어줘야 한다.
    public User findByUsername(String username);
    // findBy는 규칙이고 Username은 문법이다.
    // select * from user where username = ? 쿼리가 호출된다.

    //public User findByEmail();
    // select * from user where email = ? 쿼리 실행

    // 이런 것들을 이용하는 방법들은 "Jpa Query methods" 검색

}
