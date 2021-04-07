package com.eunhye.api.Controller;

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
import java.util.Collections;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final MeasureRepository measureRepository;
    // private final ResponseService responseService;

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
    public String login(@RequestBody Map<String, String> user, HttpServletResponse response) {
        User member = userRepository.findByUid(user.get("uid"))
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 id 입니다."));
        String token = "";
        String date = "";
        // 비밀번호 틀리고 이런것들에 대한 예외처리가 아직 부족함 .. ㅠㅠ
        if (!passwordEncoder.matches(user.get("password"), member.getPassword())) {
            System.out.println("타긴하니?");
            response.setStatus(400);
            throw new IllegalArgumentException("잘못된 번호 입니다.");
            //response.sendError(100,"잘못된 비밀번호 입니다.");
        } else {
            token = jwtTokenProvider.createToken(member.getUsername(), member.getRoles());
            date = jwtTokenProvider.getExpiraate(token);
            //response.addHeader("만료일자", date);
            response.setHeader("Token", token);
            response.setHeader("expDate", date);
            response.setStatus(200);
        }
        return "로그인 성공";
    }

    /*
        @PostMapping("/measure")
        public String Measure(HttpServletRequest request, @RequestParam("oTime") String oTime, @RequestParam("pTime") String pTime, @RequestParam("bMeasure") String bMeasure, @RequestParam("memo") String memo, HttpServletResponse response) {
            String token = request.getHeader("Token");
            System.out.println(">>>>>>> 토큰 " + token);
            Long userpk = Long.valueOf(jwtTokenProvider.getUserPk(token));

            measureRepository.save(Measure.builder()
                    .oopTime(oTime)
                    .pauseTime(pTime)
                    .bloodMeasurement(Integer.parseInt(bMeasure))
                    .measureMemo(memo)
                    .msrl(userpk)
                    .build()).getMiPk();

            return "뭐야";
        }

     */
    /*
    @PostMapping("/mea")
    public String Mea(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("Token");
        System.out.println(">>>>>>> 토큰 " + token);
        String userpk = jwtTokenProvider.getUserPk(token);
        System.out.println(">>>>>>>>>>>pk " + userpk);
        return "안녕";
    }
     */

    @PostMapping("/measure")
    public boolean Measure(HttpServletRequest request, @RequestBody Map<String, String> Data, HttpServletResponse response) {
        String token = request.getHeader("Token");
        System.out.println(">>>>>>> 토큰 " + token);
        String userid = jwtTokenProvider.getUserPk(token);
        System.out.println(userid);
        System.out.println(Data);
        String oTime = Data.get("oTime");
        String pTime = Data.get("pTime");
        String bMeasure = Data.get("bMeasure");
        String memo = Data.get("memo");
        if (oTime.isEmpty() || pTime.isEmpty() || bMeasure.isEmpty()) {
            return false; //false 를 리턴 해야함 무튼 저장이 안돼야해
        } else {
            // 체크 값을 저장
            measureRepository.save(Measure.builder()
                    .bloodMeasurement(Integer.parseInt(bMeasure))
                    .measureMemo(memo)
                    .msrl(String.valueOf(userid))
                    .oopTime(oTime)
                    .pauseTime(pTime)
                    .build()).getMiPk();
            //System.out.println(oTime + " / " + pTime + " / " + bMeasure + " / " + memo);
        }
        return true; // 저장완료
    }
}
