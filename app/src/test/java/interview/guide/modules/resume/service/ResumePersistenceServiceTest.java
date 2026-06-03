package interview.guide.modules.resume.service;

import interview.guide.common.util.CurrentUserProvider;
import interview.guide.infrastructure.file.FileHashService;
import interview.guide.infrastructure.mapper.ResumeMapper;
import interview.guide.modules.resume.model.ResumeEntity;
import interview.guide.modules.resume.repository.ResumeAnalysisRepository;
import interview.guide.modules.resume.repository.ResumeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ResumePersistenceService test")
class ResumePersistenceServiceTest {

    @Mock
    private ResumeRepository resumeRepository;
    @Mock
    private ResumeAnalysisRepository analysisRepository;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private ResumeMapper resumeMapper;
    @Mock
    private FileHashService fileHashService;
    @Mock
    private CurrentUserProvider currentUserProvider;

    @InjectMocks
    private ResumePersistenceService resumePersistenceService;

    @Nested
    @DisplayName("findAllResumes")
    class FindAllResumesTests {

        @Test
        @DisplayName("should filter by current user ID")
        void shouldFilterByCurrentUserId() {
            when(currentUserProvider.requireCurrentUserId()).thenReturn(42L);
            ResumeEntity resume = new ResumeEntity();
            resume.setUserId(42L);
            when(resumeRepository.findAllByUserIdOrderByUploadedAtDesc(42L))
                .thenReturn(List.of(resume));

            List<ResumeEntity> result = resumePersistenceService.findAllResumes();

            assertEquals(1, result.size());
            assertEquals(42L, result.get(0).getUserId());
            verify(resumeRepository).findAllByUserIdOrderByUploadedAtDesc(42L);
        }
    }

    @Nested
    @DisplayName("findById")
    class FindByIdTests {

        @Test
        @DisplayName("should filter by user ID when logged in")
        void shouldFilterByUserIdWhenLoggedIn() {
            when(currentUserProvider.getCurrentUserId()).thenReturn(42L);
            ResumeEntity resume = new ResumeEntity();
            resume.setId(1L);
            resume.setUserId(42L);
            when(resumeRepository.findByIdAndUserId(1L, 42L))
                .thenReturn(Optional.of(resume));

            Optional<ResumeEntity> result = resumePersistenceService.findById(1L);

            assertEquals(true, result.isPresent());
            assertEquals(42L, result.get().getUserId());
        }

        @Test
        @DisplayName("should fallback to findById when not logged in")
        void shouldFallbackWhenNotLoggedIn() {
            when(currentUserProvider.getCurrentUserId()).thenReturn(null);
            ResumeEntity resume = new ResumeEntity();
            resume.setId(1L);
            when(resumeRepository.findById(1L))
                .thenReturn(Optional.of(resume));

            Optional<ResumeEntity> result = resumePersistenceService.findById(1L);

            assertEquals(true, result.isPresent());
        }
    }
}
