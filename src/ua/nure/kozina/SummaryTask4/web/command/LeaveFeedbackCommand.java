package ua.nure.kozina.SummaryTask4.web.command;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ua.nure.kozina.SummaryTask4.DB.UserManager;
import ua.nure.kozina.SummaryTask4.entity.Feedback;
import ua.nure.kozina.SummaryTask4.entity.User;
import ua.nure.kozina.SummaryTask4.exception.ApplicationException;
import ua.nure.kozina.SummaryTask4.util.MessageUtil;
import ua.nure.kozina.SummaryTask4.constants.Path;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

import static ua.nure.kozina.SummaryTask4.constants.ErrorMessages.ERROR_MESSAGE_TYPE;
import static ua.nure.kozina.SummaryTask4.constants.SuccessMessages.FEEDBACK_LEFT;
import static ua.nure.kozina.SummaryTask4.constants.SuccessMessages.SUCCESS_MESSAGE_TYPE;
import static ua.nure.kozina.SummaryTask4.constants.ParametersAndAttributes.*;
import static ua.nure.kozina.SummaryTask4.validator.DataValidator.*;

/**
 * The LeaveFeedbackCommand processes the comment left by current logged user.
 *
 * @author V. Kozina-Kravchenko
 */
class LeaveFeedbackCommand extends Command {

    private static final Logger LOGGER = LogManager.getLogger(LeaveFeedbackCommand.class);

    /**
     * Constructs a new LeaveFeedbackCommand instance with the specified redirect flag.
     * If the value is true after this command must be redirection, false - forward.
     *
     * @param isRedirect the value of redirect flag
     */
    public LeaveFeedbackCommand(boolean isRedirect) {
        super(isRedirect);
    }

    @Override
    public String perform(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ApplicationException {
        LOGGER.debug("LeaveFeedbackCommand started");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(USER);
        String locale = String.valueOf(session.getAttribute(LANGUAGE));
        MessageUtil mu = new MessageUtil(locale);
        String path = Path.AUTHORIZATION;
        try {
            if (user != null) {
                path = Path.LEAVE_FEEDBACK;
                String text = request.getParameter(FEEDBACK);
                checkFeedback(text);
                checkCaptcha(request);
                processFeedback(text, user);
                session.setAttribute(SUCCESS_MESSAGE_TYPE, mu.getMessage(FEEDBACK_LEFT));
                path = Path.GUEST_BOOK_COMMAND;
            }
        } catch (ApplicationException e) {
            session.setAttribute(ERROR_MESSAGE_TYPE, mu.getMessage(e.getMessage()));
            LOGGER.error(e);
        }
        LOGGER.debug("LeaveFeedbackCommand finished");
        return path;
    }

    /**
     * Processes a specified feedback left by user by validating it and adding to the
     * database.
     *
     * @param comment a comment text to check and add to database
     * @param user a user who left the given comment
     * @throws ApplicationException if comment validation or adding to database failed
     */
    private void processFeedback(String comment, User user) throws ApplicationException {
        Feedback feedback = new Feedback();
        feedback.setText(comment);
        feedback.setUser(user);
        feedback.setDateCreated(new Date());
        new UserManager().leaveFeedback(feedback);
    }
}
