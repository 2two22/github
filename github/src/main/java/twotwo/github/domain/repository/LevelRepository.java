package twotwo.github.domain.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import twotwo.github.domain.Level;

@Repository
public interface LevelRepository extends JpaRepository<Level, Long> {

    Optional<Level> findByLevelStartCommitCountLessThanEqualAndNextLevelStartCommitCountGreaterThan(
            long totalCommitCount, long totalCommitCountSecond);

    Optional<Level> findByLevelStartCommitCount(long levelStartCommitCount);
}