// 시큐리티 회원가입
// 시큐리티 로그인
// 구글 회원 프로필 정보 받아보기
// Authentication 객체가 가질 수 있는 2가지 타입
// 구글 로그인 및 자동 회원가입 진행 완료

package com.meli.security1.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Data
// @Data
// Getter Setter 등
@NoArgsConstructor
// Default Constructor 필요
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
    private String provider;
    // provider = "google
    private String providerId;
    // providerId = 113438845545584165642
    // providerId에는 구글의 sub가 들어가게 한다.
    // 일반적인 사용자인지 OAuth 로그인한 사용자인지 구분을 위해 추가
    //private Timestamp loginDate;
    // 위와 같이 설정하여 로그인 할 때마다 날짜를 갱신해볼 수 있다.
    @CreationTimestamp
    private Timestamp createDate;

    // OAuth 로그인을 통해 강제로 회원가입시키기 위한 생성자
    @Builder
    //public User(int id, String username, String password, String email, String role, String provider, String providerId, Timestamp createDate) {
    public User(String username, String password, String email, String role, String provider, String providerId, Timestamp createDate) {
        //this.id = id;
        // 회원가입을 할 것이므로 id는 지운다.
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
        this.createDate = createDate;
    }
}
