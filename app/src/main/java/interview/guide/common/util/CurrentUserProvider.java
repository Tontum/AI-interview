package interview.guide.common.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class CurrentUserProvider {

    private static final String USER_ID_KEY = "USER_ID";

    public Long getCurrentUserId() {
        ServletRequestAttributes attrs =
            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            return null;
        }
        HttpServletRequest request = attrs.getRequest();
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        Object userId = session.getAttribute(USER_ID_KEY);
        return userId instanceof Long ? (Long) userId : null;
    }

    public Long requireCurrentUserId() {
        Long userId = getCurrentUserId();
        if (userId == null) {
            throw new interview.guide.common.exception.BusinessException(
                interview.guide.common.exception.ErrorCode.UNAUTHORIZED, "未登录");
        }
        return userId;
    }
}
