package interview.guide.modules.auth.dto;

import interview.guide.modules.auth.model.UserEntity;

public record AuthResponse(
    Long id,
    String email,
    String nickname
) {
    public static AuthResponse from(UserEntity user) {
        return new AuthResponse(user.getId(), user.getEmail(), user.getNickname());
    }
}
