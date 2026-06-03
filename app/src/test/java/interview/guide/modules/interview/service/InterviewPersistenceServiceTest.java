package interview.guide.modules.interview.service;

import interview.guide.common.util.CurrentUserProvider;
import interview.guide.modules.interview.model.InterviewSessionEntity;
import interview.guide.modules.interview.repository.InterviewAnswerRepository;
import interview.guide.modules.interview.repository.InterviewSessionRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("InterviewPersistenceService test")
class InterviewPersistenceServiceTest {

    @Mock
    private InterviewSessionRepository sessionRepository;
    @Mock
    private InterviewAnswerRepository answerRepository;
    @Mock
    private ResumeRepository resumeRepository;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private CurrentUserProvider currentUserProvider;

    @InjectMocks
    private InterviewPersistenceService interviewPersistenceService;

    @Nested
    @DisplayName("findAll")
    class FindAllTests {

        @Test
        @DisplayName("should filter by current user ID")
        void shouldFilterByCurrentUserId() {
            when(currentUserProvider.requireCurrentUserId()).thenReturn(42L);
            InterviewSessionEntity session = new InterviewSessionEntity();
            session.setUserId(42L);
            when(sessionRepository.findAllByUserIdOrderByCreatedAtDesc(42L))
                .thenReturn(List.of(session));

            List<InterviewSessionEntity> result = interviewPersistenceService.findAll();

            assertEquals(1, result.size());
            assertEquals(42L, result.get(0).getUserId());
            verify(sessionRepository).findAllByUserIdOrderByCreatedAtDesc(42L);
        }
    }
}
