package ua.nure.kozina.SummaryTask4.web.command;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ua.nure.kozina.SummaryTask4.DB.UserManager;
import ua.nure.kozina.SummaryTask4.entity.Feedback;
import ua.nure.kozina.SummaryTask4.exception.ApplicationException;
import ua.nure.kozina.SummaryTask4.util.SorterUtil;
import ua.nure.kozina.SummaryTask4.constants.Path;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static ua.nure.kozina.SummaryTask4.constants.ParametersAndAttributes.*;

/**
 * The GuestBookCommand class prepares the list of all feedbacks.
 *
 * @author V. Kozina-Kravchenko
 */
class GuestBookCommand extends Command {

    private static final Logger LOGGER = LogManager.getLogger(GuestBookCommand.class);

    @Override
    public String perform(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ApplicationException {
        LOGGER.debug("GuestBookCommand started");
        List<Feedback> comments = new UserManager().findAllFeedbacks();
        LOGGER.trace("Comments count: " + comments.size());
        SorterUtil.sortFeedbacksByDateCreated(comments);
        request.setAttribute(COMMENTS, comments);
        LOGGER.debug("GuestBookCommand finished");
        return Path.GUEST_BOOK;
    }
}
