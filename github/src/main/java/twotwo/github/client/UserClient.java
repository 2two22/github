package twotwo.github.client;

import twotwo.github.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// 다른 마이크로서비스의 API를 호출하기 위한 Feign 클라이언트를 정의합니다
@FeignClient(value = "user", url = "localhost:8080/github")
public interface UserClient {
    @GetMapping(value = "/users/{userId}")
    UserResponse getUserInfo(@PathVariable Long userId);
}