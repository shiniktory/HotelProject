package ua.nure.kozina.SummaryTask4.web.tag;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.smx.captcha.IBackgroundProducer;
import org.smx.captcha.Producer;
import org.smx.captcha.impl.FactorySimpleMathImpl;
import org.smx.captcha.impl.GridBackgroundProducer;
import ua.nure.kozina.SummaryTask4.constants.ParametersAndAttributes;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import static ua.nure.kozina.SummaryTask4.constants.ErrorMessages.ERROR_MESSAGE_TYPE;

/**
 * The CaptchaTag class provides a generation a new captcha image using Google iCaptcha.
 */
public class CaptchaTag extends TagSupport {

   private static final Logger LOGGER = LogManager.getLogger(CaptchaTag.class);

    @Override
    public int doStartTag() throws JspException {

        HttpSession session = pageContext.getSession();
        OutputStream os = null;
        try {
            os = pageContext.getResponse().getOutputStream();

            Properties props = new Properties();
            String ext = "png";
            props.put("format", ext);
            props.put("font", "Helvetica");
            props.put("fontsize", "28");
            props.put("min-width", "180");
            props.put("padding-x", "25");
            props.put("padding-y", "25");
            props.put("curve", "false");

            FactorySimpleMathImpl solver = (FactorySimpleMathImpl) Producer.forName(
                    "org.smx.captcha.impl.FactorySimpleMathImpl");

            Properties instProps;
            instProps = new Properties();
            instProps.put("min", "2");
            instProps.put("max", "5");
            instProps.put("symbols", "1");

            solver.setProperties(instProps);

            IBackgroundProducer backGrid = new GridBackgroundProducer();
            Properties backProp = new Properties();
            backProp.put("background", "E3F1FD");
            backProp.put("frequency", "18");
            backGrid.setProperties(backProp);

            solver.setBackGroundImageProducer(backGrid);
            Producer.render(os, solver, props);

            session.setAttribute(ParametersAndAttributes.I_CAPTCHA,
                    solver.getHashCode(solver.getLastWord()));

        } catch (Exception e) {
            LOGGER.error(e);
            session.setAttribute(ERROR_MESSAGE_TYPE, e.getMessage());
        } finally {
            flush(os);
            close(os);
        }

        return super.doStartTag();
    }

    private void flush(OutputStream os) {
        if (os != null) {
            try {
                os.flush();
            } catch (IOException e) {
                LOGGER.error(e);
            }
        }
    }

    private void close(OutputStream os) {
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                LOGGER.error(e);
            }
        }
    }
}
