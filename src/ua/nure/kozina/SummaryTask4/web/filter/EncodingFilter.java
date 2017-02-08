package ua.nure.kozina.SummaryTask4.web.filter;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * The EncodingFilter class designed to control a character encoding for correct data view.
 *
 * @author V. Kozina-Kravchenko
 */
@WebFilter(filterName = "EncodingFilter")
public class EncodingFilter implements Filter {

    /**
     * The string value of character encoding.
     */
    private String encoding;

    /**
     * The name of encoding initial parameter.
     */
    private static final String ENCODING_INIT_PARAM_NAME = "encoding";

    private static final Logger LOGGER = LogManager.getLogger(EncodingFilter.class);

    public void destroy() {
        LOGGER.debug("Encoding filter destroyed");
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        request.setCharacterEncoding(encoding);
        chain.doFilter(request, response);
    }

    public void init(FilterConfig config) throws ServletException {
        LOGGER.debug("EncodingFilter initialized");
        encoding = config.getInitParameter(ENCODING_INIT_PARAM_NAME);
        LOGGER.trace("Initial encoding " + encoding);
    }

}
