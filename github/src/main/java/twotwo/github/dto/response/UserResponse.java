// 사용자의 아이디, 닉네임, 프로필 경로를 포함하는 간단한 DTO(Data Transfer Object)를 정의합니다.
package twotwo.community.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String nickname;
    private String profilePath;
}
