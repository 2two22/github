package twotwo.github.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import twotwo.github.dto.response.UserResponse;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String nickname;

    public static User from(UserResponse response){
        return User.builder()
                .id(response.id())
                .nickname(response.nickName())
                .build();
    }
}