package interview.guide.modules.resume.repository;

import interview.guide.modules.resume.model.ResumeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 简历Repository
 */
@Repository
public interface ResumeRepository extends JpaRepository<ResumeEntity, Long> {
    
    Optional<ResumeEntity> findByFileHash(String fileHash);
    
    boolean existsByFileHash(String fileHash);

    Optional<ResumeEntity> findByFileHashAndUserId(String fileHash, Long userId);

    List<ResumeEntity> findAllByUserIdOrderByUploadedAtDesc(Long userId);

    Optional<ResumeEntity> findByIdAndUserId(Long id, Long userId);
}
