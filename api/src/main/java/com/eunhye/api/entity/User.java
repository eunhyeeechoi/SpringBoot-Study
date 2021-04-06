package com.eunhye.api.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder // builder 를 사용가능하게 함
@Entity // jpa Entity 표시
@Getter // user 필드값의 getter 를 생성
@NoArgsConstructor // 인자없는 생성자를 자동으로 생성
@AllArgsConstructor // 인자를 모두 갖춘 생성자를 자동생성
@Table(name = "user")
public class User {
    @Id // primarykey
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // pk 생성전략을 DB에 위임
    private long msrl;
    @Column(nullable = false, unique = true, length = 30)
    private String uid;
    @Column(nullable = false, length = 100)
    private String name;


}
