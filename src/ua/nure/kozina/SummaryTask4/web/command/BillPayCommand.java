package ua.nure.kozina.SummaryTask4.web.command;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ua.nure.kozina.SummaryTask4.DB.OrderManager;
import ua.nure.kozina.SummaryTask4.DB.UserManager;
import ua.nure.kozina.SummaryTask4.entity.Order;
import ua.nure.kozina.SummaryTask4.entity.User;
import ua.nure.kozina.SummaryTask4.exception.ApplicationException;
import ua.nure.kozina.SummaryTask4.exception.DBException;
import ua.nure.kozina.SummaryTask4.stateAndRole.OrderState;
import ua.nure.kozina.SummaryTask4.util.MailSender;
import ua.nure.kozina.SummaryTask4.constants.Path;
import ua.nure.kozina.SummaryTask4.util.MessageUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static ua.nure.kozina.SummaryTask4.constants.ErrorMessages.BILL_ALREADY_PAID;
import static ua.nure.kozina.SummaryTask4.constants.ErrorMessages.ERROR_MESSAGE_TYPE;
import static ua.nure.kozina.SummaryTask4.constants.ParametersAndAttributes.*;
import static ua.nure.kozina.SummaryTask4.constants.SuccessMessages.*;

/**
 * The BillPayCommand performs a client's order payment.
 *
 * @author V. Kozina-Kravchenko
 */
class BillPayCommand extends Command {

    private static final Logger LOGGER = LogManager.getLogger(BillCommand.class);

    /**
     * Constructs a new BillPayCommand instance with the specified redirect flag value.
     * If true after this command must be redirection, false - forward
     *
     * @param isRedirect the redirect flag value
     */
    public BillPayCommand(boolean isRedirect) {
        super(isRedirect);
    }

    @Override
    public String perform(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ApplicationException {
        LOGGER.debug("BillPayCommand started");
        HttpSession session = request.getSession();
        String locale = String.valueOf(session.getAttribute(LANGUAGE));
        MessageUtil mu = new MessageUtil(locale);
        long orderId = Long.parseLong(request.getParameter(ORDER_ID));
        OrderManager manager = new OrderManager();
        Order order = manager.getOrder(orderId);
        if (order != null) {
            try {
                payBill(order);
                session.setAttribute(SUCCESS_MESSAGE_TYPE, mu.getMessage(BILL_PAID));
            } catch (ApplicationException e) {
                String error = mu.getMessage(e.getMessage());
                LOGGER.error(error);
                session.setAttribute(ERROR_MESSAGE_TYPE, error);
            }
        }
        LOGGER.debug("BillPayCommand finished");
        return Path.CLIENT_ORDER_LIST;
    }

    /**
     * Pays the bill from the specified order.
     *
     * @param order the order to pay
     * @throws ApplicationException if bill has already been paid
     */
    private void payBill(Order order) throws ApplicationException {
        if (OrderState.CONFIRMED == order.getState()) {
            order.setState(OrderState.PAID);
            new OrderManager().updateOrder(order);
            informUser(order);
        } else {
            throw new ApplicationException(BILL_ALREADY_PAID);
        }
    }

    /**
     * Inform user about successfully paid order.
     *
     * @param order the specified order that was paid
     * @throws DBException
     */
    private void informUser(Order order) throws DBException {
        User user = new UserManager().getUser(order.getUserId());
        MailSender.getInstance().sendInvoiceMessage(user, order);
    }
}
