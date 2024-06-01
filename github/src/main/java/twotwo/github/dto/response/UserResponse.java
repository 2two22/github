package twotwo.github.dto.response;

import lombok.Builder;
import twotwo.github.domain.User;

@Builder
public record UserResponse(
        Long id,
        String nickname
        ) {
public static UserResponse from(User user) {
        return UserResponse.builder()
        .id(user.getId())
        .nickname(user.getNickname())
        .build();
        }
}
