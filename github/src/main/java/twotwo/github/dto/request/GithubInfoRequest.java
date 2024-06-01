package twotwo.github.dto.request;

import lombok.Getter;

@Getter
public class GithubInfoRequest {

    private String accessToken;
    private long memberId;
    private String username;
}
