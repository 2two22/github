package twotwo.github.dto.response;

import lombok.Builder;
import twotwo.github.domain.User;

//@Builder
//public record UserResponse(
//        Long id,
//        String nickName
//        ) {
//public static UserResponse from(User user) {
//        return UserResponse.builder()
//        .id(user.getId())
//        .nickName(user.getNickname())
//        .build();
//        }
//}

public class UserResponse {
    private Long id;
    private String nickName;

    public UserResponse(Long id, String nickName) {
        this.id = id;
        this.nickName = nickName;
    }

    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getNickname());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}