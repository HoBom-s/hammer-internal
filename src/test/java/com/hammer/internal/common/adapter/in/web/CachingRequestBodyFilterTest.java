package com.hammer.internal.common.adapter.in.web;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.servlet.ServletException;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.util.ContentCachingRequestWrapper;

class CachingRequestBodyFilterTest {

    private final CachingRequestBodyFilter filter = new CachingRequestBodyFilter();

    @Test
    void wraps_request_with_content_caching_wrapper() throws ServletException, IOException {
        var request = new MockHttpServletRequest("POST", "/api");
        var response = new MockHttpServletResponse();
        var chain = new MockFilterChain();

        filter.doFilterInternal(request, response, chain);

        assertThat(chain.getRequest()).isInstanceOf(ContentCachingRequestWrapper.class);
    }

    @Test
    void does_not_double_wrap_if_already_wrapped() throws ServletException, IOException {
        var request = new MockHttpServletRequest("POST", "/api");
        var wrapped = new ContentCachingRequestWrapper(request);
        var response = new MockHttpServletResponse();
        var chain = new MockFilterChain();

        filter.doFilterInternal(wrapped, response, chain);

        assertThat(chain.getRequest()).isSameAs(wrapped);
    }
}
