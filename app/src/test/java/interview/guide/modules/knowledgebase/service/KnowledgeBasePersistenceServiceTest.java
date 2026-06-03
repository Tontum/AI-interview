package interview.guide.modules.knowledgebase.service;

import interview.guide.common.util.CurrentUserProvider;
import interview.guide.modules.knowledgebase.model.KnowledgeBaseEntity;
import interview.guide.modules.knowledgebase.repository.KnowledgeBaseRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("KnowledgeBasePersistenceService test")
class KnowledgeBasePersistenceServiceTest {

    @Mock
    private KnowledgeBaseRepository knowledgeBaseRepository;
    @Mock
    private CurrentUserProvider currentUserProvider;

    @InjectMocks
    private KnowledgeBasePersistenceService knowledgeBasePersistenceService;

    @Nested
    @DisplayName("saveKnowledgeBase")
    class SaveKnowledgeBaseTests {

        @Test
        @DisplayName("should set current user ID on creation")
        void shouldSetCurrentUserIdOnCreation() {
            when(currentUserProvider.requireCurrentUserId()).thenReturn(42L);
            MockMultipartFile file = new MockMultipartFile(
                "file", "test.pdf", "application/pdf", "content".getBytes());
            when(knowledgeBaseRepository.save(any(KnowledgeBaseEntity.class)))
                .thenAnswer(invocation -> {
                    KnowledgeBaseEntity kb = invocation.getArgument(0);
                    kb.setId(1L);
                    return kb;
                });

            KnowledgeBaseEntity result = knowledgeBasePersistenceService.saveKnowledgeBase(
                file, "Test KB", "Java", "key", "url", "hash123");

            assertNotNull(result);
            assertEquals(42L, result.getUserId());
            verify(knowledgeBaseRepository).save(any(KnowledgeBaseEntity.class));
        }
    }
}
