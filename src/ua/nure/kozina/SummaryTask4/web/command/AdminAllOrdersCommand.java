package ua.nure.kozina.SummaryTask4.web.command;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ua.nure.kozina.SummaryTask4.DB.OrderManager;
import ua.nure.kozina.SummaryTask4.constants.Path;
import ua.nure.kozina.SummaryTask4.entity.Order;
import ua.nure.kozina.SummaryTask4.exception.ApplicationException;
import ua.nure.kozina.SummaryTask4.stateAndRole.OrderState;
import ua.nure.kozina.SummaryTask4.util.SorterUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static ua.nure.kozina.SummaryTask4.constants.ParametersAndAttributes.ORDERS;
import static ua.nure.kozina.SummaryTask4.constants.ParametersAndAttributes.STATES;

/**
 * The AdminAllOrdersCommand prepares the list of all orders.
 *
 * @author V. Kozina-Kravchenko
 */
class AdminAllOrdersCommand extends Command {

    private static final Logger LOGGER = LogManager.getLogger(AdminAllOrdersCommand.class);

    @Override
    public String perform(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ApplicationException {
        LOGGER.debug("AdminAllOrdersCommand started");
        List<Order> orders = new OrderManager().findAllOrders();
        SorterUtil.setSortOrdersByDateCreated(orders);
        request.setAttribute(ORDERS, orders);
        request.setAttribute(STATES, OrderState.values());
        LOGGER.debug("AdminAllOrdersCommand finished");
        return Path.ADMIN_ALL_ORDERS_PAGE;
    }
}
