package twotwo.github.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import twotwo.github.dto.response.UserResponse;

// 다른 마이크로서비스의 API를 호출하기 위한 Feign 클라이언트를 정의합니다
@FeignClient(name = "userClient", url = "${feign.user}")
public interface UserClient {
    @GetMapping(value = "/member")
    UserResponse getUserInfo(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token);
}