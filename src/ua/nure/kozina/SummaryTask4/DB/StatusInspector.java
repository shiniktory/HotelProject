package ua.nure.kozina.SummaryTask4.DB;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ua.nure.kozina.SummaryTask4.entity.Apartment;
import ua.nure.kozina.SummaryTask4.entity.Order;
import ua.nure.kozina.SummaryTask4.exception.DBException;
import ua.nure.kozina.SummaryTask4.constants.ErrorMessages;
import ua.nure.kozina.SummaryTask4.stateAndRole.ApartmentState;
import ua.nure.kozina.SummaryTask4.stateAndRole.OrderState;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The class schedules an inspection of apartment and order statuses relative to the current date.
 * While inspection runs StatusInspector can cancel an order if it was not paid in 2 days,
 * change apartment state if client arrived or left the room and close an appropriate order.
 *
 * @author V. Kozina-Kravchenko
 */
public class StatusInspector {

    /**
     * Order timeout equals 2 days. After this time if order is not paid it will be closed.
     */
    private static final long ORDER_TIMEOUT = 2 * 24 * 60 * 60 * 1000;

    private static final Logger LOGGER = LogManager.getLogger(StatusInspector.class);

    /**
     * A single-threaded executor that runs an inspection command periodically.
     */
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    /**
     * The count of TimeUnits after what the first inspection starts.
     */
    private static final int INSPECTION_FIRST_START_TIMEOUT = 1;
    /**
     * The count of TimeUnits after what inspection repeats.
     */
    private static final int INSPECTION_TIMEOUT = 60;

    /**
     * Starts an inspection order and apartment statuses relative to the current date.
     */
    public void checkOrderStatuses() {
        LOGGER.debug("StatusInspector#checkOrderStatuses started");
        final Runnable inspector = new Runnable() {
            @Override
            public void run() {
                LOGGER.debug("StatusInspector started check");
                try {
                    OrderManager orderManager = new OrderManager();
                    List<Order> activeOrders = orderManager.findAllActiveOrders();

                    for (Order order : activeOrders) {
                        OrderState state = order.getState();
                        if (state == OrderState.NEW || state == OrderState.CONFIRMED) {
                            checkTimeoutDate(order, orderManager);
                        }
                        if (state == OrderState.PAID) {
                            checkReservedDates(order, orderManager);
                        }
                    }
                } catch (DBException e) {
                    LOGGER.error(ErrorMessages.INSPECTOR_ERROR, e);
                }
                LOGGER.debug("StatusInspector finished check");
            }
        };
        scheduler.scheduleAtFixedRate(inspector, INSPECTION_FIRST_START_TIMEOUT,
                INSPECTION_TIMEOUT, TimeUnit.MINUTES);
        LOGGER.debug("StatusInspector#checkOrderStatuses finished");
    }

    /**
     * Stops an inspector running.
     */
    public void interrupt() {
        scheduler.shutdownNow();
    }

    /**
     * Checks an order for payment in required terms and closes this order if terms passed.
     *
     * @param order        an order to inspect
     * @param orderManager a database manager
     * @throws DBException
     */
    private void checkTimeoutDate(Order order, OrderManager orderManager) throws DBException {
        long timeoutDate = order.getDateCreation().getTime() + ORDER_TIMEOUT;
        if (timeoutDate < System.currentTimeMillis()) {
            order.setState(OrderState.CLOSED);
            orderManager.updateOrder(order);
        }
    }

    /**
     * Checks the reservation dates from the specified order and modifies order state relative to
     * this dates.
     *
     * @param order     an order to inspect
     * @param orManager a database manager instance
     * @throws DBException
     */
    private void checkReservedDates(Order order, OrderManager orManager) throws DBException {
        long arrivalDate = order.getArrivalDate().getTime();
        long leavingDate = order.getLeavingDate().getTime();
        long currentDate = System.currentTimeMillis();

        if (arrivalDate < currentDate && leavingDate > currentDate) {
            ApartmentManager apManager = new ApartmentManager();
            Apartment apartment = apManager.getApartment(order.getRoomNumber());
            apartment.setState(ApartmentState.OCCUPIED);
            apManager.updateApartment(apartment);
        }
        if (leavingDate < currentDate) {
            order.setState(OrderState.CLOSED);
            orManager.updateOrder(order);
        }
    }
}
