package twotwo.github.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import twotwo.community.dto.response.UserResponse;

@FeignClient(value = "user", url = "localhost:8081")
public interface UserClient {
    @GetMapping(value = "/users/{userId}")
    UserResponse getUserInfo(@PathVariable Long userId);
}