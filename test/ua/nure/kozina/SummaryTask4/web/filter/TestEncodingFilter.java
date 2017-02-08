package ua.nure.kozina.SummaryTask4.web.filter;

import org.junit.BeforeClass;
import org.junit.Test;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class TestEncodingFilter {

    private static final EncodingFilter filter = new EncodingFilter();
    private static final String ENCODING = "UTF-8";

    @BeforeClass
    public static void setUp() throws ServletException {
        FilterConfig config = mock(FilterConfig.class);
        when(config.getInitParameter("encoding")).thenReturn(ENCODING);
        filter.init(config);
    }

    @Test
    public void testDoFilter() throws IOException, ServletException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        filter.doFilter(request, response, chain);
        verify(request).setCharacterEncoding(ENCODING);
        verify(chain).doFilter(request, response);
    }

    @Test
    public void testFilterDestroy() {
        filter.destroy();
    }
}
