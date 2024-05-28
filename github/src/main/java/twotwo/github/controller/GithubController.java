package twotwo.github.controller;

import zerobase.bud.domain.Member;
import twotwo.github.dto.CommitHistoryInfo;
import twotwo.github.service.GithubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import twotwo.github.dto.response.UserResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/github")
public class GithubController {

    // 이 서비스를 이용하여 사용자 정보를 가져오는 컨트롤러를 작성합니다.
    private final GithubService githubService;

    public GithubController(GithubService githubService) {
        this.githubService = githubService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long userId) {
        UserResponse userResponse = githubService.getUserResponse(userId);
        return ResponseEntity.ok(userResponse);
    }
}
/*@PostMapping
    public ResponseEntity<String> saveCommitInfoFromLastCommitDate(@AuthenticationPrincipal Member member) {
        return ResponseEntity.ok(githubService.saveCommitInfoFromLastCommitDate(member));
    }

    @GetMapping
    public CommitHistoryInfo getCommitInfo(@AuthenticationPrincipal Member member) {
        return githubService.getCommitInfo(member);
    }*/