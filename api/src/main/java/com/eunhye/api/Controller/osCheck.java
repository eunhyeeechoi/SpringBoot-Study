package com.eunhye.api.Controller;

import com.eunhye.api.advice.RestException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.sf.uadetector.UserAgentStringParser;
import net.sf.uadetector.service.UADetectorServiceFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
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
@Component
public class osCheck {

    // true false 로 체크
    //  @RequestMapping(value = "/user-agent", produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean Userinfo(@RequestHeader(value = "User-Agent") String userAgent) {
        UserAgentStringParser parser = UADetectorServiceFactory.getResourceModuleParser();
        if (userAgent.length() > 0) {
            if (parser.parse(userAgent).getOperatingSystem().getFamily().getName().isEmpty()) {
                //throw new RestException(100, "os 비어있다.");
                System.out.println("os 비어 있어 ");
                // false 를 리턴
            } else if (parser.parse(userAgent).getOperatingSystem().getVersionNumber().getMajor().isEmpty() ||
                    parser.parse(userAgent).getOperatingSystem().getVersionNumber().getMinor().isEmpty()) {
                System.out.println("os 버전 비어있어");
            } else if (parser.parse(userAgent).getVersionNumber().getMajor().isEmpty()
                    || parser.parse(userAgent).getVersionNumber().getMinor().isEmpty()) {
                //throw new RestException(101,"앱 버전이 비어있다.");
                System.out.println("앱 버전이 비어있을때");
            } else {
                // true 를 리턴
                System.out.println("os 종류 " + parser.parse(userAgent).getOperatingSystem().getFamily());
                System.out.println("os versioin " + parser.parse(userAgent).getOperatingSystem().getVersionNumber());
                System.out.println("app version" + parser.parse(userAgent).getVersionNumber());
                return true;
            }
        }
        return false;
    }
}
