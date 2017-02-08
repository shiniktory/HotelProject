package ua.nure.kozina.SummaryTask4.web.command;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ua.nure.kozina.SummaryTask4.DB.ApartmentManager;
import ua.nure.kozina.SummaryTask4.entity.ApartmentClass;
import ua.nure.kozina.SummaryTask4.exception.ApplicationException;
import ua.nure.kozina.SummaryTask4.constants.Path;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static ua.nure.kozina.SummaryTask4.constants.ParametersAndAttributes.CLASSES_LIST;

/**
 * The RequestCommand prepares an information for client's request form.
 *
 * @author V. Kozina-Kravchenko
 */
class RequestCommand extends Command {

    private static final Logger LOGGER = LogManager.getLogger(RequestCommand.class);

    @Override
    public String perform(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ApplicationException {
        LOGGER.debug("RequestCommand started");
        List<ApartmentClass> classesList = new ApartmentManager().findApartmentClasses();
        LOGGER.trace("The size of apartment classes list " + classesList.size());
        request.setAttribute(CLASSES_LIST, classesList);
        LOGGER.debug("RequestCommand finished");
        return Path.CLIENT_REQUEST;
    }
}
