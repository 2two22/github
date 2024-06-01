package twotwo.github.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import twotwo.github.domain.Level;
import twotwo.github.domain.UserLevel;

import java.util.Optional;

@Repository
public interface UserLevelRepository  extends JpaRepository<UserLevel, Long> {

    Optional<UserLevel> findByUserId(Long userId);
}
