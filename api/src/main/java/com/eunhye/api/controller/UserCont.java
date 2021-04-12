package com.eunhye.api.controller;

import com.eunhye.api.config.security.JwtTokenProvider;
import com.eunhye.api.domain.measure.Measure;
import com.eunhye.api.domain.measure.MeasureRepository;
import com.eunhye.api.domain.user.User;
import com.eunhye.api.domain.user.UserRepository;
import com.eunhye.api.util.OsCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class UserCont {
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final MeasureRepository measureRepository;
    private final OsCheck service;

    // 이전버전 로그인
    @PostMapping("/login2")
    public boolean login2(@RequestHeader(value = "User-Agent") String userAgent, @RequestBody Map<String, String> user, HttpServletResponse response) {
        if (service.Userinfo(userAgent)) { // user 정보값이 전부 들어있을때만 로그인 가능
            // User member = userRepository.findByUid(user.get("uid")).orElseThrow(()
            //       -> new IllegalArgumentException("가입되지 않은 id 입니다."));
            User member = userRepository.findByUid(user.get("uid")).orElseThrow(()
                    -> new IllegalArgumentException("가입되지 않은 id 입니다."));

            String token = "";
            String date = "";
            // 비밀번호 틀리고 이런것들에 대한 예외처리가 아직 부족함 .. ㅠㅠ
            if (!passwordEncoder.matches(user.get("password"), member.getPassword())) {
                System.out.println("잘못된 비밀번호");
                response.setHeader("message", "Wrong password");
                response.setStatus(402);

                return false;
            } else {
                token = jwtTokenProvider.createToken(member.getUsername(), member.getRoles());
                date = jwtTokenProvider.getExpirdate(token);
                response.setHeader("Token", token);
                response.setHeader("expDate", date);
                response.setStatus(200);
                return true;
            }
        }
        response.setStatus(300);
        response.setHeader("message", "user info is not enough");
        return false;
    }

    // 이전버전 혈당체크 정보 저장
    @PostMapping("/measure2")
    public boolean Measure2(HttpServletRequest request, @RequestBody Map<String, String> Data, HttpServletResponse response) {
        String token = request.getHeader("Token");
        if (service.Userinfo(request.getHeader("User-Agent"))) {
            if (jwtTokenProvider.validateToken(token)) {
                //System.out.println(">>>>>>>>>>>>>>" + token);
                String userid = jwtTokenProvider.getUserPk(token);
                System.out.println(userid);
                System.out.println(Data);
                String oTime = Data.get("oTime");
                String pTime = Data.get("pTime");
                String bMeasure = Data.get("bMeasure");
                String memo = Data.get("memo");
                if (oTime.isEmpty() || pTime.isEmpty() || bMeasure.isEmpty()) {
                    System.out.println("세개중 하나가 비었어");
                    response.setHeader("message", "measure info in not enough");
                    response.setStatus(100);
                    return false; //false 를 리턴 해야함 무튼 저장이 안돼야해
                } else {
                    // 체크 값을 저장
                    measureRepository.save(Measure.builder()
                            .bloodMeasurement(Integer.parseInt(bMeasure))
                            .measureMemo(memo)
                            .msrl(userid)
                            .oopTime(oTime)
                            .pauseTime(pTime)
                            .build()).getMiPk();
                    return true; // 저장완료
                    //System.out.println(oTime + " / " + pTime + " / " + bMeasure + " / " + memo);
                }
            } else {
                //response.setStatus(100);
                response.setHeader("message", "token expiration");
                return false;
            }
        }
        response.setStatus(300);
        response.setHeader("message", "user info is not enough");
        return false;
    }
}
