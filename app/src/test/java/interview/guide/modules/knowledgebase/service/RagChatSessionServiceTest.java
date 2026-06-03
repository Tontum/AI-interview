package interview.guide.modules.knowledgebase.service;

import interview.guide.common.util.CurrentUserProvider;
import interview.guide.infrastructure.mapper.KnowledgeBaseMapper;
import interview.guide.infrastructure.mapper.RagChatMapper;
import interview.guide.modules.knowledgebase.model.RagChatDTO.SessionListItemDTO;
import interview.guide.modules.knowledgebase.model.RagChatSessionEntity;
import interview.guide.modules.knowledgebase.repository.KnowledgeBaseRepository;
import interview.guide.modules.knowledgebase.repository.RagChatMessageRepository;
import interview.guide.modules.knowledgebase.repository.RagChatSessionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("RagChatSessionService test")
class RagChatSessionServiceTest {

    @Mock
    private RagChatSessionRepository sessionRepository;
    @Mock
    private RagChatMessageRepository messageRepository;
    @Mock
    private KnowledgeBaseRepository knowledgeBaseRepository;
    @Mock
    private KnowledgeBaseQueryService queryService;
    @Mock
    private RagChatMapper ragChatMapper;
    @Mock
    private KnowledgeBaseMapper knowledgeBaseMapper;
    @Mock
    private KnowledgeBaseQueryProperties queryProperties;
    @Mock
    private CurrentUserProvider currentUserProvider;

    @InjectMocks
    private RagChatSessionService ragChatSessionService;

    @Nested
    @DisplayName("listSessions")
    class ListSessionsTests {

        @Test
        @DisplayName("should filter by current user ID")
        void shouldFilterByCurrentUserId() {
            when(currentUserProvider.requireCurrentUserId()).thenReturn(42L);
            RagChatSessionEntity session = new RagChatSessionEntity();
            session.setUserId(42L);
            when(sessionRepository.findAllByUserIdOrderByUpdatedAtDesc(42L))
                .thenReturn(List.of(session));
            SessionListItemDTO dto = new SessionListItemDTO(1L, "Test", 0, null, null, false);
            when(ragChatMapper.toSessionListItemDTO(session))
                .thenReturn(dto);

            List<SessionListItemDTO> result = ragChatSessionService.listSessions();

            assertEquals(1, result.size());
            verify(sessionRepository).findAllByUserIdOrderByUpdatedAtDesc(42L);
        }
    }
}
