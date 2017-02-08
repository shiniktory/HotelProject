package ua.nure.kozina.SummaryTask4.web.command;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ua.nure.kozina.SummaryTask4.DB.OrderManager;
import ua.nure.kozina.SummaryTask4.constants.Path;
import ua.nure.kozina.SummaryTask4.entity.Order;
import ua.nure.kozina.SummaryTask4.exception.ApplicationException;
import ua.nure.kozina.SummaryTask4.stateAndRole.OrderState;
import ua.nure.kozina.SummaryTask4.util.MessageUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static ua.nure.kozina.SummaryTask4.constants.ErrorMessages.ERROR_MESSAGE_TYPE;
import static ua.nure.kozina.SummaryTask4.constants.ParametersAndAttributes.LANGUAGE;
import static ua.nure.kozina.SummaryTask4.constants.ParametersAndAttributes.NEW_ORDER_STATE;
import static ua.nure.kozina.SummaryTask4.constants.ParametersAndAttributes.ORDER_ID;
import static ua.nure.kozina.SummaryTask4.constants.SuccessMessages.ORDER_STATE_CHANGED;
import static ua.nure.kozina.SummaryTask4.constants.SuccessMessages.SUCCESS_MESSAGE_TYPE;

/**
 * The AdminChangeOrderStateCommand class allows admin to change order states manually.
 *
 * @author V. Kozina-Kravchenko
 */
class AdminChangeOrderStateCommand extends Command {

    private static final Logger LOGGER = LogManager.getLogger(AdminChangeOrderStateCommand.class);

    /**
     * Constructs a new AdminOrderStateCommand instance with the specified redirect flag.
     * If the value is true after this command must be redirection, false - forward.
     *
     * @param isRedirect the value of redirect flag
     */
    public AdminChangeOrderStateCommand(boolean isRedirect) {
        super(isRedirect);
    }

    @Override
    public String perform(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ApplicationException {
        LOGGER.debug("AdminChangeOrderStateCommand started");
        HttpSession session = request.getSession();
        String locale = String.valueOf(session.getAttribute(LANGUAGE));
        MessageUtil mu = new MessageUtil(locale);
        try {
            long orderId = Long.parseLong(request.getParameter(ORDER_ID));
            OrderState newState = OrderState.valueOf(request.getParameter(NEW_ORDER_STATE));
            OrderManager manager = new OrderManager();
            Order order = manager.getOrder(orderId);
            if (checkOrderAndStates(order, newState)) {
                order.setState(newState);
                manager.updateOrder(order);
                session.setAttribute(SUCCESS_MESSAGE_TYPE, mu.getMessage(ORDER_STATE_CHANGED));
            }
        } catch (ApplicationException e) {
            session.setAttribute(ERROR_MESSAGE_TYPE, mu.getMessage(e.getMessage()));
        }
        LOGGER.debug("AdminChangeOrderStateCommand finished");
        return Path.ADMIN_ORDERS_AND_REQUESTS_COMMAND;
    }

    /**
     * Checks the specified order and an order state to change for.
     *
     * @param order an order to check state
     * @param newState a new order state
     * @return true if states are not null, order is not closed and states are not equal
     */
    private boolean checkOrderAndStates(Order order, OrderState newState) {
        return order != null && newState != null &&
                order.getState() != newState;
    }
}
