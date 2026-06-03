package interview.guide.modules.interviewschedule.service;

import interview.guide.common.util.CurrentUserProvider;
import interview.guide.modules.interviewschedule.model.CreateInterviewRequest;
import interview.guide.modules.interviewschedule.model.InterviewScheduleDTO;
import interview.guide.modules.interviewschedule.model.InterviewScheduleEntity;
import interview.guide.modules.interviewschedule.model.InterviewStatus;
import interview.guide.modules.interviewschedule.repository.InterviewScheduleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("InterviewScheduleService test")
class InterviewScheduleServiceTest {

    @Mock
    private InterviewScheduleRepository repository;
    @Mock
    private CurrentUserProvider currentUserProvider;

    @InjectMocks
    private InterviewScheduleService interviewScheduleService;

    @Nested
    @DisplayName("create")
    class CreateTests {

        @Test
        @DisplayName("should set current user ID on creation")
        void shouldSetCurrentUserIdOnCreation() {
            when(currentUserProvider.requireCurrentUserId()).thenReturn(42L);
            CreateInterviewRequest request = new CreateInterviewRequest();
            request.setCompanyName("Test Company");
            request.setPosition("Developer");
            request.setInterviewTime(LocalDateTime.now().plusDays(1));
            request.setInterviewType("VIDEO");
            request.setMeetingLink("https://meet.google.com");
            request.setRoundNumber(1);
            request.setInterviewer("John");
            request.setNotes("Notes");
            when(repository.save(any(InterviewScheduleEntity.class)))
                .thenAnswer(invocation -> {
                    InterviewScheduleEntity entity = invocation.getArgument(0);
                    entity.setId(1L);
                    return entity;
                });

            InterviewScheduleDTO result = interviewScheduleService.create(request);

            assertNotNull(result);
            verify(repository).save(any(InterviewScheduleEntity.class));
        }
    }

    @Nested
    @DisplayName("getAll")
    class GetAllTests {

        @Test
        @DisplayName("should filter by current user ID when no date range")
        void shouldFilterByCurrentUserIdWhenNoDateRange() {
            when(currentUserProvider.requireCurrentUserId()).thenReturn(42L);
            InterviewScheduleEntity entity = new InterviewScheduleEntity();
            entity.setUserId(42L);
            when(repository.findAllByUserIdOrderByInterviewTimeDesc(42L))
                .thenReturn(List.of(entity));

            List<InterviewScheduleDTO> result = interviewScheduleService.getAll(null, null, null);

            assertEquals(1, result.size());
            verify(repository).findAllByUserIdOrderByInterviewTimeDesc(42L);
        }
    }
}
