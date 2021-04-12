package com.eunhye.api.util;

import lombok.RequiredArgsConstructor;
import net.sf.uadetector.UserAgentStringParser;
import net.sf.uadetector.service.UADetectorServiceFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;

@RequiredArgsConstructor
@Component
public class OsCheck {

    // 헤더내의 모바일 앱 버전, 모바일 디바이스 OS 종류, 모바일 디바이스 OS 버전 존재 여부 확인
    // true false 로 체크
    public boolean Userinfo(@RequestHeader(value = "User-Agent") String userAgent) {
        UserAgentStringParser parser = UADetectorServiceFactory.getResourceModuleParser();
        if (userAgent.length() > 0) {
            if (parser.parse(userAgent).getOperatingSystem().getFamily().getName().isEmpty()) {
                System.out.println("os 비어 있어 ");
            } else if (parser.parse(userAgent).getOperatingSystem().getVersionNumber().getMajor().isEmpty() ||
                    parser.parse(userAgent).getOperatingSystem().getVersionNumber().getMinor().isEmpty()) {
                System.out.println("os 버전 비어있어");
            } else if (parser.parse(userAgent).getVersionNumber().getMajor().isEmpty()
                    || parser.parse(userAgent).getVersionNumber().getMinor().isEmpty()) {
                System.out.println("앱 버전이 비어있을때");
            } else {
                // true 를 리턴
                System.out.println("os 종류 " + parser.parse(userAgent).getOperatingSystem().getFamily());
                System.out.println("os versioin " + parser.parse(userAgent).getOperatingSystem().getVersionNumber());
                System.out.println("app version" + parser.parse(userAgent).getVersionNumber());
                // slf4j 로 변경 해야함
                return true;
            }
        }
        return false;
    }
}