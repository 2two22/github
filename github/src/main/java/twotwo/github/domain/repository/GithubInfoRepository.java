package twotwo.github.domain.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import twotwo.github.domain.GithubInfo;

@Repository
public interface GithubInfoRepository extends JpaRepository<GithubInfo, Long> {

    Optional<GithubInfo> findByMemberId(Long memberId);

    @Modifying
    @Query(value = "delete from github_info where member_id=:memberId", nativeQuery = true)
    void deleteByMemberId(@Param("memberId") Long memberId);
}