package twotwo.github.service;

//import static zerobase.bud.common.type.ErrorCode.INVALID_INITIAL_VALUE;
//import static zerobase.bud.common.type.ErrorCode.INVALID_TOTAL_COMMIT_COUNT;
//import static zerobase.bud.common.type.ErrorCode.NOT_REGISTERED_GITHUB_USER_ID;
//import static zerobase.bud.member.util.MemberConstants.MAXIMUM_LEVEL_CODE;
//import zerobase.bud.common.exception.BudException;
import zerobase.bud.domain.CommitHistory;
import zerobase.bud.domain.GithubInfo;
import zerobase.bud.domain.Level;
import zerobase.bud.domain.Member;
import twotwo.github.dto.CommitCountByDate;
import twotwo.github.dto.CommitHistoryInfo;
//import zerobase.bud.repository.CommitHistoryRepository;
//import zerobase.bud.repository.GithubInfoRepository;
//import zerobase.bud.repository.LevelRepository;
//import zerobase.bud.repository.MemberRepository;
import twotwo.github.service.GithubApi;
import twotwo.github.client.UserClient;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import twotwo.github.dto.response.UserResponse;

@Slf4j
@RequiredArgsConstructor
@Service
public class GithubService {
    private final UserClient userClient;
    private final LevelRepository levelRepository;
    private final GithubInfoRepository githubInfoRepository;
    private final CommitHistoryRepository commitHistoryRepository;
    private final MemberRepository memberRepository;
    private final GithubApi githubApi;
    private static final int WEEKS_FOR_COMMIT_HISTORY = 16;
    long totalCommitCount;
    long remainCommitCountNextLevel;
    long todayCommitCount = 0;
    long consecutiveCommitDays = 0;
    long thisWeekCommitCount;

    // Feign 클라이언트를 사용하여 사용자 정보를 가져오는 서비스를 작성합니다.
    @Autowired
    public GithubService(UserClient userClient) {
        this.userClient = userClient;
    }
    public UserResponse getUserResponse(Long userId) {
        return userClient.getUserInfo(userId);
    }

    // UserResponse response = userClient.getUserInfo(userId);
    /*@Transactional
    public CommitHistoryInfo getCommitInfo(Member member) {

        GithubInfo githubInfo = githubInfoRepository.findByUserId(
                        member.getUserId())
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

            return CommitHistoryInfo.of(member.getNickname(), level);
        }*/

        CommitHistory latestCommitHistory = commitHistories.get(commitHistories.size() - 1);

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
                .filter(x -> !x.getCommitDate().isBefore(member.getCreatedAt().toLocalDate()))
                .map(CommitCountByDate::getCommitCount)
                .reduce(0L, Long::sum);

        Level level = getLevel(member, totalCommitCount);

        member.updateLevel(level);
        memberRepository.save(member);

        remainCommitCountNextLevel =
                level.getNextLevelStartCommitCount() - totalCommitCount;

        return CommitHistoryInfo.of(member.getNickname()
                , level.getLevelCode()
                , level.getImagePath()
                , remainCommitCountNextLevel
                , totalCommitCount
                , todayCommitCount
                , thisWeekCommitCount
                , consecutiveCommitDays
                , commits);
    }

    //이부분 수정 방법 생각하기
    private Level getLevel(Member member, long totalCommitCount) {
        Level level = member.getLevel();
        if (!MAXIMUM_LEVEL_CODE.equals(level.getLevelCode())) {
            level = levelRepository.
                    findByLevelStartCommitCountLessThanEqualAndNextLevelStartCommitCountGreaterThan(
                            totalCommitCount, totalCommitCount)
                    .orElseThrow(
                            () -> new BudException(INVALID_TOTAL_COMMIT_COUNT));
        }

        return level;
    }

    public String saveCommitInfoFromLastCommitDate(Member member) {
        GithubInfo githubInfo = githubInfoRepository.findByUserId(
                        member.getUserId())
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

}