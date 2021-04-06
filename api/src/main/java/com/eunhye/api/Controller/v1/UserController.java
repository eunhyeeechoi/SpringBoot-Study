package com.eunhye.api.Controller.v1;

import com.eunhye.api.entity.User;
import com.eunhye.api.repo.UserJpaRepo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ua_parser.Parser;

import java.util.List;

@RequiredArgsConstructor
@RestController // 결과값을 JSon 으로 출력
@RequestMapping
public class UserController {
    private final UserJpaRepo userJpaRepo;

    @GetMapping(value = "/user")
    public List<User> findAllUser() {
        return userJpaRepo.findAll();
    }

    @PostMapping(value = "/user")
    public User save() {
        User user = User.builder().uid("eun@naver.com").name("은").build();
        return userJpaRepo.save(user);
    }

    // header 의 정보를 json 방식으로 리턴받는것
    @GetMapping(value = "/headerinfo")
    public Hinfo JsonHeader(@RequestHeader HttpHeaders headers) {
        Hinfo header = new Hinfo();
        header.message = headers.toString();
        return header;
    }

    @GetMapping(value = "/headerinfo2", produces = MediaType.APPLICATION_JSON_VALUE)
    public Hinfo JsonHeader2(@RequestHeader(value = "User-Agent") String userAgent) {
        Hinfo header = new Hinfo();
        header.message = userAgent.toString();
        return header;
    }


    @Getter
    @Setter
    public static class Hinfo {
        private String message;
    }
}
