package ua.nure.kozina.SummaryTask4.web.command;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ua.nure.kozina.SummaryTask4.DB.RoomRequestManager;
import ua.nure.kozina.SummaryTask4.entity.RoomRequest;
import ua.nure.kozina.SummaryTask4.exception.ApplicationException;
import ua.nure.kozina.SummaryTask4.stateAndRole.OrderState;
import ua.nure.kozina.SummaryTask4.constants.Path;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ua.nure.kozina.SummaryTask4.constants.ParametersAndAttributes.REQUEST_ID;

/**
 * The class closes client's room request.
 *
 * @author V. Kozina-Kravchenko
 */
class ClientCancelRequestCommand extends Command {

    private static final Logger LOGGER = LogManager.getLogger(ClientCancelRequestCommand.class);

    /**
     * Constructs a new ClientCancelRequestCommand instance with the specified redirect flag value.
     * If true after this command must be redirection, false - forward
     * @param isRedirect the redirect flag value
     */
    public ClientCancelRequestCommand(boolean isRedirect) {
        super(isRedirect);
    }

    @Override
    public String perform(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ApplicationException {
        LOGGER.debug("ClientCancelRequest started");
        long requestId = Long.parseLong(request.getParameter(REQUEST_ID));
        RoomRequestManager manager = new RoomRequestManager();
        RoomRequest req = manager.getRequest(requestId);
        if (req != null && req.getState() != OrderState.CLOSED) {
            req.setState(OrderState.CLOSED);
            manager.updateRequest(req);
        }
        LOGGER.debug("ClientCancelRequest finished");
        return Path.CLIENT_ORDER_LIST;
    }
}
