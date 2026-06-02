package interview.guide.modules.auth.controller;

import interview.guide.common.result.Result;
import interview.guide.modules.auth.dto.LoginRequest;
import interview.guide.modules.auth.dto.RegisterRequest;
import interview.guide.modules.auth.dto.AuthResponse;
import interview.guide.modules.auth.model.UserEntity;
import interview.guide.modules.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final String USER_ID_KEY = "USER_ID";
    private final AuthService authService;

    @PostMapping("/register")
    public Result<AuthResponse> register(@Valid @RequestBody RegisterRequest request,
                                         HttpServletRequest httpRequest) {
        UserEntity user = authService.register(request.email(), request.password(), request.nickname());
        authenticateUser(httpRequest, user);
        return Result.success(AuthResponse.from(user));
    }

    @PostMapping("/login")
    public Result<AuthResponse> login(@Valid @RequestBody LoginRequest request,
                                      HttpServletRequest httpRequest) {
        UserEntity user = authService.login(request.email(), request.password());
        authenticateUser(httpRequest, user);
        return Result.success(AuthResponse.from(user));
    }

    @PostMapping("/logout")
    public Result<Void> logout(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return Result.success(null);
    }

    @GetMapping("/me")
    public Result<AuthResponse> me(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);
        if (session == null || session.getAttribute(USER_ID_KEY) == null) {
            return Result.error(401, "未登录");
        }
        Long userId = (Long) session.getAttribute(USER_ID_KEY);
        UserEntity user = authService.getCurrentUser(userId);
        return Result.success(AuthResponse.from(user));
    }

    private void authenticateUser(HttpServletRequest httpRequest, UserEntity user) {
        httpRequest.getSession(true).setAttribute(USER_ID_KEY, user.getId());
    }
}
