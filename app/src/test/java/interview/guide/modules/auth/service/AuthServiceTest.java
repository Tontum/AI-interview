package interview.guide.modules.auth.service;

import interview.guide.common.exception.BusinessException;
import interview.guide.modules.auth.model.UserEntity;
import interview.guide.modules.auth.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService test")
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Nested
    @DisplayName("register")
    class RegisterTests {

        @Test
        @DisplayName("should reject duplicate email")
        void shouldRejectDuplicateEmail() {
            when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

            BusinessException exception = assertThrows(BusinessException.class, () ->
                authService.register("test@example.com", "Password123!", "nick")
            );

            assertEquals(400, exception.getCode());
            assertEquals("邮箱已存在", exception.getMessage());
            verify(userRepository, never()).save(any());
        }

        @Test
        @DisplayName("should hash password before saving")
        void shouldHashPasswordBeforeSaving() {
            when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
            when(passwordEncoder.encode("Password123!")).thenReturn("hashed-password");
            when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

            UserEntity saved = authService.register("test@example.com", "Password123!", "nick");

            assertNotNull(saved);
            assertEquals("test@example.com", saved.getEmail());
            assertEquals("nick", saved.getNickname());
            assertEquals("hashed-password", saved.getPasswordHash());
            verify(passwordEncoder).encode("Password123!");
        }

        @Test
        @DisplayName("should reject missing email")
        void shouldRejectMissingEmail() {
            BusinessException exception = assertThrows(BusinessException.class, () ->
                authService.register("", "Password123!", "nick")
            );

            assertEquals("邮箱不能为空", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("login")
    class LoginTests {

        @Test
        @DisplayName("should reject bad password")
        void shouldRejectBadPassword() {
            UserEntity user = UserEntity.builder()
                .email("test@example.com")
                .passwordHash("hashed-password")
                .status(UserEntity.UserStatus.ACTIVE)
                .build();
            when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
            when(passwordEncoder.matches("wrong-password", "hashed-password")).thenReturn(false);

            BusinessException exception = assertThrows(BusinessException.class, () ->
                authService.login("test@example.com", "wrong-password")
            );

            assertEquals("邮箱或密码错误", exception.getMessage());
        }

        @Test
        @DisplayName("should reject disabled account")
        void shouldRejectDisabledAccount() {
            UserEntity user = UserEntity.builder()
                .email("test@example.com")
                .passwordHash("hashed-password")
                .status(UserEntity.UserStatus.DISABLED)
                .build();
            when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

            BusinessException exception = assertThrows(BusinessException.class, () ->
                authService.login("test@example.com", "Password123!")
            );

            assertEquals("账号已禁用", exception.getMessage());
        }

        @Test
        @DisplayName("should return user on valid login")
        void shouldReturnUserOnValidLogin() {
            UserEntity user = UserEntity.builder()
                .email("test@example.com")
                .passwordHash("hashed-password")
                .status(UserEntity.UserStatus.ACTIVE)
                .build();
            when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
            when(passwordEncoder.matches("Password123!", "hashed-password")).thenReturn(true);

            UserEntity result = authService.login("test@example.com", "Password123!");

            assertEquals("test@example.com", result.getEmail());
        }
    }
}
