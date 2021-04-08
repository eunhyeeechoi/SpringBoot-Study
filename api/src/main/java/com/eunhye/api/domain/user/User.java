package com.eunhye.api.domain.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Builder // builder 를 사용가능하게 함
@Entity // jpa Entity 표시
@Getter // user 필드값의 getter 를 생성
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 인자없는 생성자를 자동으로 생성
@AllArgsConstructor // 인자를 모두 갖춘 생성자를 자동생성

@Table(name = "user")
public class User implements UserDetails {
    @Id // primarykey  pk 생성전략을 DB에 위임
    //@ManyToOne(cascade = CascadeType.ALL)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long msrl;

    @Column(nullable = false, unique = true, length = 30)
    private String uid;

    @Column(nullable = false, length = 100)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return this.uid;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
