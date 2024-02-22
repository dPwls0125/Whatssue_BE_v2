package GDG.whatssue.config;

import GDG.whatssue.interceptor.ScheduleInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final ScheduleInterceptor scheduleInterceptor;


    /**
     * PathMatcher를 따로 구현해서 HttpMethod 별로 excludePatterns 구현하기 TODO
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("add ScheduleInterceptor");
        registry.addInterceptor(scheduleInterceptor)
            .order(1)
            .addPathPatterns("/api/{clubId}/schedules/{scheduleId}");
    }
}
