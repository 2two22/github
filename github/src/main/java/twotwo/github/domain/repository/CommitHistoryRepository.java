package twotwo.github.domain.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import twotwo.github.CommitHistory;

@Repository
public interface CommitHistoryRepository extends
        JpaRepository<CommitHistory, Long> {

    Optional<CommitHistory> findFirstByGithubInfoIdOrderByCommitDateDesc(
            Long githubId);

    Optional<CommitHistory> findByGithubInfoIdAndCommitDate(Long githubId,
                                                            LocalDate commitDate);

    List<CommitHistory> findAllByGithubInfoIdAndCommitDateBetween(
            Long id, LocalDate minusWeeks, LocalDate now);

}