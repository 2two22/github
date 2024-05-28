import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TokenController {

    private final TokenService tokenService;

    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/saveToken")
    public ResponseEntity<String> saveToken(@RequestHeader("Authorization") String token) {
        Long userId = tokenService.getId(token);
        // userId를 데이터베이스에 저장하거나 필요에 따라 처리합니다.
        // 예: userRepository.save(new User(userId, token));
        return ResponseEntity.ok("토큰이 성공적으로 저장되었습니다, 사용자 ID: " + userId);
    }
}
