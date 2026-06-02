package interview.guide.modules.auth.service;

import interview.guide.common.exception.BusinessException;
import interview.guide.common.exception.ErrorCode;
import interview.guide.modules.auth.model.UserEntity;
import interview.guide.modules.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserEntity register(String email, String rawPassword, String nickname) {
        if (!StringUtils.hasText(email)) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "邮箱不能为空");
        }

        if (userRepository.existsByEmail(email)) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "邮箱已存在");
        }

        String normalizedNickname = StringUtils.hasText(nickname) ? nickname.trim() : email;

        UserEntity user = UserEntity.builder()
            .email(email)
            .passwordHash(passwordEncoder.encode(rawPassword))
            .nickname(normalizedNickname)
            .status(UserEntity.UserStatus.ACTIVE)
            .build();
        return userRepository.save(user);
    }

    public UserEntity login(String email, String rawPassword) {
        if (!StringUtils.hasText(email)) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "邮箱不能为空");
        }

        UserEntity user = userRepository.findByEmail(email)
            .orElseThrow(() -> new BusinessException(ErrorCode.BAD_REQUEST, "邮箱或密码错误"));

        if (user.getStatus() == UserEntity.UserStatus.DISABLED) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "账号已禁用");
        }
        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "邮箱或密码错误");
        }
        return user;
    }

    public UserEntity getCurrentUser(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "用户不存在"));
    }
}
