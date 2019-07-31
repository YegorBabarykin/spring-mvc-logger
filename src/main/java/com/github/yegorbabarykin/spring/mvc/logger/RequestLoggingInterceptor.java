package com.github.yegorbabarykin.spring.mvc.logger;

import com.github.yegorbabarykin.spring.mvc.logger.common.OptionalCollection;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class RequestLoggingInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(final HttpServletRequest servletRequest,
                             final HttpServletResponse servletResponse,
                             final Object handler) throws Exception {
        Map<String, Collection<String>> headers = Collections.list(servletRequest.getHeaderNames())
                .stream()
                .distinct()
                .collect(Collectors.toMap(Function.identity(), h -> Collections.list(servletRequest.getHeaders(h))));
        final String requestLog = String.join(System.lineSeparator(),
                "Inbound Request:",
                "URI: " + servletRequest.getRequestURI() + "?" + servletRequest.getQueryString(),
                "Headers: " + headers,
                "Payload: " + getPayload(servletRequest)
        );
        if (log.isInfoEnabled()) {
            log.info("{}", requestLog);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        if (response instanceof WrappedHttpServletResponse && log.isInfoEnabled()) {
            log.info("{}", getMessage((WrappedHttpServletResponse) response));
        }
    }

    private String getMessage(WrappedHttpServletResponse response) {
        Map<String, Collection<String>> headers = OptionalCollection.streamOf(response.getHeaderNames())
                .distinct()
                .collect(Collectors.toMap(Function.identity(), response::getHeaders));
        return String.join(System.lineSeparator(),
                "Outbound Response:",
                "Status: " + response.getStatus(),
                "Headers: " + headers,
                "Payload: " + new String(response.getCopy())
        );
    }

    private String getPayload(final HttpServletRequest request) throws IOException {
        if (request instanceof ResettableRequestServletWrapper) {
            final ResettableRequestServletWrapper resettableRequest = (ResettableRequestServletWrapper) request;
            final String requestBody = IOUtils.toString(resettableRequest.getReader());
            resettableRequest.resetInputStream();
            return requestBody.substring(0, Math.min(16384, requestBody.length()));
        } else {
            return "[]";
        }
    }
}