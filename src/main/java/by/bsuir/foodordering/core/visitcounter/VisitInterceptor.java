package by.bsuir.foodordering.core.visitcounter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@AllArgsConstructor
public class VisitInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(VisitInterceptor.class);

    private final VisitCounter visitCounter;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) {

        String requestUri = request.getRequestURI();

        String queryString = request.getQueryString();

        String fullIdentifier;
        if (queryString != null && !queryString.isEmpty()) {
            fullIdentifier = requestUri + "?" + queryString;
        } else {
            fullIdentifier = requestUri;
        }

        log.debug("Incrementing visit count for identifier: {}", fullIdentifier);

        visitCounter.incrementVisit(fullIdentifier);

        return true;
    }
}
