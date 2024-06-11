package twotwo.github.service;

import twotwo.github.domain.UserLevel;
import twotwo.github.domain.repository.UserLevelRepository;
import twotwo.github.dto.request.GithubInfoRequest;
import twotwo.github.exception.BudException;
import twotwo.github.domain.CommitHistory;
import twotwo.github.domain.GithubInfo;
import twotwo.github.domain.Level;
import twotwo.github.dto.CommitCountByDate;
import twotwo.github.dto.CommitHistoryInfo;
import twotwo.github.domain.repository.CommitHistoryRepository;
import twotwo.github.domain.repository.GithubInfoRepository;
import twotwo.github.domain.repository.LevelRepository;
import twotwo.github.client.UserClient;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import twotwo.github.dto.response.UserResponse;
import twotwo.github.util.TokenProvider;
import static twotwo.github.exception.ErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class GithubService {
    public static final String MAXIMUM_LEVEL_CODE = "찬란한_나무";
    private final LevelRepository levelRepository;
    private final GithubInfoRepository githubInfoRepository;
    private final CommitHistoryRepository commitHistoryRepository;
    private final UserLevelRepository userLevelRepository;
    private final GithubApi githubApi;
    private static final int WEEKS_FOR_COMMIT_HISTORY = 16;
    private final UserClient userClient;
    private final TokenProvider tokenProvider;


    @Transactional
    public CommitHistoryInfo getCommitInfo(String token) {
        Long userId = tokenProvider.getId(token);
        UserResponse response = userClient.getUserInfo(token);

        GithubInfo githubInfo = githubInfoRepository.findByMemberId(userId)
                .orElseThrow(() -> new BudException(NOT_REGISTERED_GITHUB_USER_ID));

        List<CommitHistory> commitHistories = commitHistoryRepository
                .findAllByGithubInfoIdAndCommitDateBetween(
                        githubInfo.getId()
                        , LocalDate.now().minusWeeks(WEEKS_FOR_COMMIT_HISTORY)
                        , LocalDate.now()
                );

        if (commitHistories.isEmpty()) {
            Level level = levelRepository.findByLevelStartCommitCount(0)
                    .orElseThrow(() -> new BudException(INVALID_INITIAL_VALUE));

            return CommitHistoryInfo.of(response.getNickName(), level);
        }

        long totalCommitCount;
        long remainCommitCountNextLevel;
        long todayCommitCount = 0;
        long consecutiveCommitDays = 0;
        long thisWeekCommitCount;

        CommitHistory latestCommitHistory =
                commitHistories.get(commitHistories.size() - 1);

        if (LocalDate.now().isEqual(latestCommitHistory.getCommitDate())) {
            todayCommitCount = latestCommitHistory.getCommitCount();
            consecutiveCommitDays = latestCommitHistory
                    .getConsecutiveCommitDays();
        }

        LocalDate firstDayOfWeek = LocalDate.now().with(DayOfWeek.MONDAY);

        thisWeekCommitCount = commitHistories.stream()
                .filter(commitHistory -> !commitHistory.getCommitDate().isBefore(firstDayOfWeek))
                .mapToLong(CommitHistory::getCommitCount).sum();

        List<CommitCountByDate> commits = commitHistories.stream()
                .map(CommitCountByDate::from)
                .collect(Collectors.toList());

        totalCommitCount = commits.stream()
                .filter(x -> !x.getCommitDate().isBefore(githubInfo.getCreatedAt().toLocalDate()))
                .map(CommitCountByDate::getCommitCount)
                .reduce(0L, Long::sum);

        Level level = getLevel(userId, totalCommitCount);
//        기존 코드 member 의존성 제거하기 위함
//        member.updateLevel(level);
//        memberRepository.save(member);
        saveLevel(userId, level);

        remainCommitCountNextLevel =
                level.getNextLevelStartCommitCount() - totalCommitCount;

        return CommitHistoryInfo.of(response.getNickName()
                , level.getLevelCode()
                , level.getImagePath()
                , remainCommitCountNextLevel
                , totalCommitCount
                , todayCommitCount
                , thisWeekCommitCount
                , consecutiveCommitDays
                , commits);
    }

    public UserLevel saveLevel (Long userId, Level level) {
        UserLevel userLevel = userLevelRepository.findByUserId(userId)
                .orElse(UserLevel.builder()
                        .userId(userId)
                        .level(level)
                        .build());
        userLevel.updateLevel(level);
        userLevelRepository.save(userLevel);
        return userLevel;
    }

    private Level getLevel(Long userId, long totalCommitCount) {
        Optional<UserLevel> optionalUserLevel = userLevelRepository.findByUserId(userId);
        if (optionalUserLevel.isPresent()) {
            Level level = optionalUserLevel.get().getLevel();
            if (MAXIMUM_LEVEL_CODE.equals(level.getLevelCode())) {
                return level;
            }
        }
        return levelRepository.
                findByLevelStartCommitCountLessThanEqualAndNextLevelStartCommitCountGreaterThan(
                        totalCommitCount, totalCommitCount)
                .orElseThrow(
                        () -> new BudException(INVALID_TOTAL_COMMIT_COUNT));
    }

    public String saveCommitInfoFromLastCommitDate(String token) {
        Long userId = tokenProvider.getId(token);
        GithubInfo githubInfo = githubInfoRepository.findByMemberId(userId)
                .orElseThrow(() -> new BudException(NOT_REGISTERED_GITHUB_USER_ID));

        return githubApi.saveCommitInfoFromLastCommitDate(
                githubInfo, getLastCommitDate(githubInfo)
        );
    }

    private LocalDate getLastCommitDate(GithubInfo githubInfo) {
        return commitHistoryRepository.findFirstByGithubInfoIdOrderByCommitDateDesc(
                githubInfo.getId())
                .stream()
                .map(CommitHistory::getCommitDate)
                .findFirst()
                .orElse(LocalDate.now().minusWeeks(WEEKS_FOR_COMMIT_HISTORY));
    }
//  깃헙 정보 저장
    public GithubInfo saveGithubInfo(String token, GithubInfoRequest request) {
        Long userId = tokenProvider.getId(token);
        Optional<GithubInfo> optionalGithubInfo = githubInfoRepository.findByMemberId(userId);
        GithubInfo githubInfo;
        if(optionalGithubInfo.isPresent()) {
            githubInfo = optionalGithubInfo.get();
            githubInfo.update(request.getUsername(), request.getAccessToken());
        }
        else githubInfo = GithubInfo.builder()
                .memberId(userId)
                .accessToken(request.getAccessToken())
                .username(request.getUsername())
                .build();
        return githubInfoRepository.save(githubInfo);
    }

}