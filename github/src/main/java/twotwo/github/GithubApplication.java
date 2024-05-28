package twotwo.github;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import twotwo.github.dto.response.UserResponse;

@SpringBootApplication
@EnableFeignClients
public class GithubApplication {

	public static void main(String[] args) {
		// Spring Boot 애플리케이션 클래스에서 FeignClient를 활성화합니다.
		SpringApplication.run(GithubApplication.class, args);

		try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(FeignClientExample.class)) {
			// FeignClient Bean을 가져옴
			UserClient userClient = context.getBean(UserClient.class);

			// 사용자 ID
			Long userId = 123L;

			// FeignClient를 사용하여 다른 마이크로서비스에 요청하여 UserResponse를 받음
			UserResponse userResponse = userClient.getUserById(userId);

			// 받은 응답을 사용
			System.out.println("User ID: " + userResponse.getId());
			System.out.println("Nickname: " + userResponse.getNickname());
			System.out.println("Profile Path: " + userResponse.getProfilePath());
		}
	}
}
