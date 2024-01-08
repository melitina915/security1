// 시큐리티 회원가입
// 시큐리티 로그인

package com.meli.security1.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Data
// @Data
// Getter Setter 등
public class User {
    @Id
    // Primary Key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;
    private String email;
    private String role;
    // ROLE_USER, ROLE_ADMIN
    //private Timestamp loginDate;
    // 위와 같이 설정하여 로그인 할 때마다 날짜를 갱신해볼 수 있다.
    @CreationTimestamp
    private Timestamp createDate;
}
