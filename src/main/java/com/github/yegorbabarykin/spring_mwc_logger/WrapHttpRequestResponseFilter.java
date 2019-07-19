package com.github.yegorbabarykin.spring_mwc_logger;

import org.springframework.web.cors.CorsUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WrapHttpRequestResponseFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(final HttpServletRequest servletRequest,
                                    final HttpServletResponse servletResponse,
                                    final FilterChain filterChain) throws ServletException, IOException {
        final HttpServletRequest request = wrapValidatableServletRequest(servletRequest);
        filterChain.doFilter(request, new WrappedHttpServletResponse(servletResponse));
    }

    private HttpServletRequest wrapValidatableServletRequest(final HttpServletRequest servletRequest) {
        final boolean wrapRequest = servletRequest.getContentLengthLong() <= Integer.MAX_VALUE &&
                !CorsUtils.isPreFlightRequest(servletRequest);
        return wrapRequest ? new ResettableRequestServletWrapper(servletRequest) : servletRequest;
    }

}