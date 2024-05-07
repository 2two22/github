import twotwo.community.client.UserClient;

@Service
@RequiredArgsConstructor
public class PostService {
    private final UserClient userClient;
    UserResponse response = userClient.getUserInfo(userId);
}