package GDG.whatssue.global.argumentresolver;

import GDG.whatssue.domain.member.service.ClubMemberService;
import GDG.whatssue.domain.user.entity.KakaoDetails;
import GDG.whatssue.global.common.annotation.LoginMember;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;

@Slf4j
@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private ClubMemberService clubMemberService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasAnnotation = parameter.hasParameterAnnotation(LoginMember.class);

        boolean assignable = Long.class.isAssignableFrom(parameter.getParameterType());

        return hasAnnotation && assignable;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        Map<String, String> pathVariables = (Map<String, String>) request
            .getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        Long clubId = Long.parseLong(pathVariables.get("clubId"));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || ! (auth.getPrincipal() instanceof OAuth2User)) {
            log.warn("argumentresolver - @LoginMember 오류");
        }

        Long userId = ((KakaoDetails) auth.getPrincipal()).getUser().getUserId();

        //인터셉터에서 clubMember에 관한 체크 진행이 된 상태
        return clubMemberService.getClubMemberId(clubId, userId);
    }
}
