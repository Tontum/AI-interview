package interview.guide.modules.knowledgebase.service;

import interview.guide.common.util.CurrentUserProvider;
import interview.guide.infrastructure.file.FileStorageService;
import interview.guide.infrastructure.mapper.KnowledgeBaseMapper;
import interview.guide.modules.knowledgebase.model.KnowledgeBaseEntity;
import interview.guide.modules.knowledgebase.model.KnowledgeBaseListItemDTO;
import interview.guide.modules.knowledgebase.repository.KnowledgeBaseRepository;
import interview.guide.modules.knowledgebase.repository.RagChatMessageRepository;
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
@DisplayName("KnowledgeBaseListService test")
class KnowledgeBaseListServiceTest {

    @Mock
    private KnowledgeBaseRepository knowledgeBaseRepository;
    @Mock
    private RagChatMessageRepository ragChatMessageRepository;
    @Mock
    private KnowledgeBaseMapper knowledgeBaseMapper;
    @Mock
    private FileStorageService fileStorageService;
    @Mock
    private CurrentUserProvider currentUserProvider;

    @InjectMocks
    private KnowledgeBaseListService knowledgeBaseListService;

    @Nested
    @DisplayName("listKnowledgeBases")
    class ListKnowledgeBasesTests {

        @Test
        @DisplayName("should filter by current user ID")
        void shouldFilterByCurrentUserId() {
            when(currentUserProvider.requireCurrentUserId()).thenReturn(42L);
            KnowledgeBaseEntity kb = new KnowledgeBaseEntity();
            kb.setUserId(42L);
            when(knowledgeBaseRepository.findAllByUserIdOrderByUploadedAtDesc(42L))
                .thenReturn(List.of(kb));
            KnowledgeBaseListItemDTO dto = new KnowledgeBaseListItemDTO(
                1L, "test.pdf", "Java", "test.pdf", 100L, "application/pdf",
                null, null, 1, 0, null, null, 0);
            when(knowledgeBaseMapper.toListItemDTOList(List.of(kb)))
                .thenReturn(List.of(dto));

            List<KnowledgeBaseListItemDTO> result = knowledgeBaseListService.listKnowledgeBases();

            assertEquals(1, result.size());
            verify(knowledgeBaseRepository).findAllByUserIdOrderByUploadedAtDesc(42L);
        }
    }
}
