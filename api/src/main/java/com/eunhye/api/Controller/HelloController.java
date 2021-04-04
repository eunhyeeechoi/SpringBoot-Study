package com.eunhye.api.Controller;


import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {
    // 1. 화면에 hellowolrd 가 출력됌
    @GetMapping(value = "/hellowolrd/string")
    @ResponseBody
    public String helloworldString() {
        return "helloworld";
    }

    // 2. 화면에 {message:"hellowolrd"} 라고 출력
    @GetMapping(value = "/helloworld/json")
    @ResponseBody
    public Hello helloworldJson() {
        Hello hello = new Hello();
        hello.message = "helloo~~~~";
        return hello;
    }

    //3. 화면에 helloworld.ftl 의 내용 출력
    @GetMapping(value = "/helloworld/page")
    public String helloworld() {
        return "helloworld page";
    }

    @Getter
    @Setter
    public static class Hello {
        private String message;
    }
}