package twotwo.github.dto.response;

import lombok.Builder;
import twotwo.github.domain.User;

@Builder
public record UserResponse(
        Long id,
        String nickName
        ) {
public static UserResponse from(User user) {
        return UserResponse.builder()
        .id(user.getId())
        .nickName(user.getNickname())
        .build();
        }
}
