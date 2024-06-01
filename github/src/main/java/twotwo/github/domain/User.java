package twotwo.github.domain;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nickname;

    public static User from(UserResponse response){
        return User.builder()
                .id(response.getId())
                .nickname(response.getNickName())
                .build();
    }
}