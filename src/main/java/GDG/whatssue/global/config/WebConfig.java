package GDG.whatssue.global.config;

import GDG.whatssue.global.argumentresolver.LoginUserArgumentResolver;
import GDG.whatssue.global.interceptor.ClubCheckInterceptor;
import GDG.whatssue.global.interceptor.ScheduleCheckInterceptor;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final ClubCheckInterceptor clubCheckInterceptor;
    private final ScheduleCheckInterceptor scheduleCheckInterceptor;
    private final LoginUserArgumentResolver loginUserArgumentResolver;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(clubCheckInterceptor)
            .order(1)
            .addPathPatterns("/api/clubs/{clubId}/**")
            .excludePathPatterns("/api/clubs/join/**", "/api/clubs/my", "/api/clubs");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginUserArgumentResolver);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "http://3.34.58.135:3000", "https://whatssue.app")
                .allowedHeaders("Content-Type", "X-AUTH-TOKEN", "Authorization", "Bearer")
                .allowedMethods(HttpMethod.POST.name(), HttpMethod.GET.name(), HttpMethod.DELETE.name(), HttpMethod.PUT.name(), HttpMethod.PATCH.name(), HttpMethod.OPTIONS.name())
                .allowCredentials(true);
//                .exposedHeaders("Authorization", "X-AUTH-TOKEN", "Bearer")
//                .maxAge(3000);
    }
}
