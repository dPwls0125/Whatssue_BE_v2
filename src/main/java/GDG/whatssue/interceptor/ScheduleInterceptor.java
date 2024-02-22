package GDG.whatssue.interceptor;

import GDG.whatssue.entity.Schedule;
import GDG.whatssue.repository.ScheduleRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduleInterceptor implements HandlerInterceptor {

    private final ScheduleRepository scheduleRepository;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Map pathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        Long clubId = Long.parseLong((String) pathVariables.get("clubId"));
        Long scheduleId = Long.parseLong((String) pathVariables.get("scheduleId"));

        log.info("Schedule Interceptor clubId = {}, scheduleId = {}", clubId, scheduleId);

        Schedule findSchedule = scheduleRepository.findById(scheduleId).orElse(null);

        /**
         * 존재하지 않는 schedule 또는 클럽에 유효하지 않은 schedule 일 시 예외
         * response 처리 TODO
         */
        if (findSchedule == null || findSchedule.getClub().getId() != clubId) {
            log.info("invalid ScheduleId");
            return false;
        }

        return true;
    }
}
