package uk.jinhy.server.service.user.presentation;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.NativeWebRequest;
import uk.jinhy.server.api.user.application.CurrentUser;
import uk.jinhy.server.api.user.domain.User;
import uk.jinhy.server.api.user.application.UserService;

@Component
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {
    private final UserService userService;

    public CurrentUserArgumentResolver(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class) && parameter.getParameterType().equals(User.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, org.springframework.web.bind.support.WebDataBinderFactory binderFactory) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthenticated");
        }
        String oauth2UserId = authentication.getName();
        try {
            return userService.getAuthUserInfo(oauth2UserId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthenticated");
        }
    }
}
