package com.eunhye.api.Controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.sf.uadetector.UserAgentStringParser;
import net.sf.uadetector.service.UADetectorServiceFactory;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ua_parser.Parser;
import ua_parser.Client;

@RequiredArgsConstructor
@RequestMapping("/")
@RestController
public class ServiceController {

    @RequestMapping(value = "/user-agent", method = RequestMethod.GET)
    public Uinfo Userinfo(@RequestHeader(value = "User-Agent") String userAgent) {
        Uinfo userinfo = new Uinfo();
        UserAgentStringParser parser = UADetectorServiceFactory.getResourceModuleParser();
        String os = String.valueOf(parser.parse(userAgent).getOperatingSystem().getFamily()); // os 종류
        String osver = String.valueOf(parser.parse(userAgent).getOperatingSystem().getVersionNumber()); // os 버전
        String appver = String.valueOf(parser.parse(userAgent).getVersionNumber()); // 모바일앱 버전
        userinfo.message = os + " / " + osver + " / " + " / " + appver;
        return userinfo;
    }

    @Getter
    @Setter
    public static class Uinfo {
        private String message;
    }

}
