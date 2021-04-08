package com.eunhye.api.Controller;

import com.eunhye.api.advice.RestException;
import com.eunhye.api.config.security.JwtTokenProvider;
import com.eunhye.api.domain.measure.Measure;
import com.eunhye.api.domain.measure.MeasureRepository;
import com.eunhye.api.domain.user.User;
import com.eunhye.api.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.util.Collections;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final MeasureRepository measureRepository;
    private final OsCheck checkOs;

    // 회원가입
    @PostMapping(value = "/join")
    public Long join(@RequestBody Map<String, String> user) {
        return userRepository.save(User.builder()
                .uid(user.get("uid"))
                .password(passwordEncoder.encode(user.get("password")))
                .roles(Collections.singletonList("ROLE_USER"))
                .build()).getMsrl(); //최초가입시 User 로 설정
    }

    // 로그인
    @PostMapping("/login")
    public String login(@RequestHeader(value = "User-Agent") String userAgent, @RequestBody Map<String, String> user, HttpServletResponse response) {
        if (checkOs.Userinfo(userAgent)) {
            if (!user.get("uid").isEmpty() || !user.get("password").isEmpty()) {
                User member = userRepository.findByUid(user.get("uid")).orElseThrow(()
                        -> new RestException(403, "This id is not joined")); // db 에 id 가 존재하지 않을때
                // user 정보값이 전부 들어있을때만 로그인 가능
                if (!passwordEncoder.matches(user.get("password"), member.getPassword())) {
                    throw new RestException(403, "Wrong password");
                } else {
                    String token = "";
                    String date = "";
                    token = jwtTokenProvider.createToken(member.getUsername(), member.getRoles());
                    date = jwtTokenProvider.getExpirdate(token);
                    response.setHeader("Token", token); // 토큰은 헤더에 넣기
                    response.setHeader("expDate", date);
                    throw new RestException(200, date); // 상태와 만료일시
                }
            } else {
                throw new RestException(404, "ID or password is empty");
            }
        }
        // 모바일 앱버전, 모바일 디바이스 os 종류, 모바일 os 중 하나가 비었을때
        return "로그인 실패";
    }

    // 혈당 측정 값 저장
    @PostMapping("/measure")
    public String Measure(HttpServletRequest request, @RequestBody Map<String, String> Data, HttpServletResponse response) {
        String token = request.getHeader("Token");
        if (checkOs.Userinfo(request.getHeader("User-Agent"))) {
            if (jwtTokenProvider.validateToken(token)) {
                String userid = jwtTokenProvider.getUserPk(token);
                String oTime = Data.get("oTime");
                String pTime = Data.get("pTime");
                String bMeasure = Data.get("bMeasure");
                String memo = Data.get("memo");
                if (oTime.isEmpty() || pTime.isEmpty() || bMeasure.isEmpty()) {
                    throw new RestException(400, "measure info is not enough");
                    //측정 필수 데이터 부족
                } else {
                    // 측정 값을 저장
                    measureRepository.save(Measure.builder()
                            .bloodMeasurement(Integer.parseInt(bMeasure))
                            .measureMemo(memo)
                            .msrl(userid)
                            .oopTime(oTime)
                            .pauseTime(pTime)
                            .build()).getMiPk();
                    throw new RestException(200, "measure info is saved");
                    // 저장완료
                }
            } else {
                // 토큰이 만료 되었을때
                System.out.println("token expiration");
                throw new RestException(401, "token expiration");
            }
        }
        // 모바일 앱버전, 모바일 디바이스 os 종류, 모바일 os 중 하나가 비었을때
        return "데이터 저장 실패 ";
    }
}