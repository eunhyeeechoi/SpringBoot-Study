package com.eunhye.api.Controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.sf.uadetector.UserAgentStringParser;
import net.sf.uadetector.service.UADetectorServiceFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ua_parser.Parser;
import ua_parser.Client;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

@RequiredArgsConstructor
@RestController
public class ServiceController {

    // true false 로 체크
    @RequestMapping(value = "/user-agent", produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean Userinfo(@RequestHeader(value = "User-Agent") String userAgent, HttpServletResponse response) {
        UserAgentStringParser parser = UADetectorServiceFactory.getResourceModuleParser();
        if (userAgent.length() > 0) {
            if (parser.parse(userAgent).getOperatingSystem().getFamily().getName().isEmpty()) {
                System.out.println("os 비어 있어 ");
                return false;
                // false 를 리턴
            } else if (parser.parse(userAgent).getOperatingSystem().getVersionNumber().getMajor().isEmpty() ||
                    parser.parse(userAgent).getOperatingSystem().getVersionNumber().getMinor().isEmpty()) {
                System.out.println("os 버전 비어있어");
                return false;
            } else if (parser.parse(userAgent).getVersionNumber().getMajor().isEmpty()
                    || parser.parse(userAgent).getVersionNumber().getMinor().isEmpty()) {
                System.out.println("앱 버전이 비어있을때");
                return false;
            } else {
                // true 를 리턴
                System.out.println("os 종류 " + parser.parse(userAgent).getOperatingSystem().getFamily());
                System.out.println("os versioin " + parser.parse(userAgent).getOperatingSystem().getVersionNumber());
                System.out.println("app version" + parser.parse(userAgent).getVersionNumber());
            }
        } else { //userAgent 가 아예 없을때
            return false;
            //false 를 리턴
        }
        return true;
    }

    // 지워도 되는것
    @RequestMapping("/user2")
    public Uinfo checkAgent(HttpServletRequest requeset, HttpServletResponse response) {
        Enumeration<String> userAgents = requeset.getHeaders("user-agent");
        Uinfo user = new Uinfo();
        while (userAgents.hasMoreElements()) {
            String agnet = userAgents.nextElement();
            Parser parser = new Parser();
            Client userInfo = parser.parse(agnet);
            String os = "";
            String osver = userInfo.os.family;

            if (userInfo.userAgent.family.isEmpty() || userInfo.userAgent.family == "Other") {
                os = String.valueOf(userInfo.userAgent.family.length());
                //os = "비어있음";
            } else {
                os = userInfo.userAgent.family;

            }

            user.message = os + osver;
        }
        return user;
    }


    @Getter
    @Setter
    public static class Uinfo {
        private String message;
    }

}
