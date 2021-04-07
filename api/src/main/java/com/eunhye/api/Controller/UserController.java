package com.eunhye.api.Controller;

import com.eunhye.api.config.security.JwtTokenProvider;
import com.eunhye.api.domain.user.User;
import com.eunhye.api.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;
import java.util.Collections;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class UserController {
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final JwtTokenProvider jwtTokenProvider;
    @Autowired
    private final UserRepository userRepository;

    // 회원가입
    @PostMapping(value = "/join",produces = MediaType.APPLICATION_JSON_VALUE)
    public Long join(@RequestBody Map<String, String> user) {
        return userRepository.save(User.builder()
                .uid(user.get("uid"))
                .password(passwordEncoder.encode(user.get("password")))
                .roles(Collections.singletonList("ROLE_USER"))
                .build()).getMsrl(); //최초가입시 User 로 설정
    }

    // 로그인
    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> user) {
        User member = userRepository.findByUid(user.get("uid"))
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 id 입니다."));
        if (!passwordEncoder.matches(user.get("password"), member.getPassword())) {
            throw new IllegalArgumentException("잘못된 번호 입니다.");
        }
        return jwtTokenProvider.createToken(member.getUsername(), member.getRoles());
    }
}
