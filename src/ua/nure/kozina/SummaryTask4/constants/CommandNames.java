package ua.nure.kozina.SummaryTask4.constants;

/**
 * The class contains names of all available commands.
 *
 * @author V. Kozina-Kravchenko
 */
public class CommandNames {

    // No-user commands
    public static final String LOGIN_COMMAND = "login";
    public static final String REGISTRATION_COMMAND = "registration";
    public static final String FORGOT_PASSWORD_COMMAND = "forgotPassword";
    public static final String RESET_PASSWORD_REQUEST_COMMAND = "resetPasswordRequest";
    public static final String RESET_PASSWORD = "resetPassword";
    public static final String GUEST_BOOK_COMMAND = "guestBook";
    public static final String LOCALE_COMMAND = "locale";

    // Client commands
    public static final String SORT_COMMAND = "sort";
    public static final String ROOMS_LIST_COMMAND = "rooms";
    public static final String REQUEST_COMMAND = "request";
    public static final String MAKE_REQUEST_COMMAND = "makeRequest";
    public static final String CLIENT_CANCEL_REQUEST_COMMAND = "clientCancelRequest";
    public static final String PROCESS_ORDER_COMMAND = "orderFromRequest";
    public static final String ORDER_ROOM_COMMAND = "order";
    public static final String CLIENT_ORDERS_COMMAND = "clientOrders";
    public static final String CONFIRM_ORDER_COMMAND = "confirmOrder";
    public static final String BILL_COMMAND = "bill";
    public static final String BILL_PAY_COMMAND = "billPay";
    public static final String LEAVE_FEEDBACK_COMMAND = "leaveFeedback";

    // Admin commands
    public static final String ADMIN_ORDERS_COMMAND = "adminOrders";
    public static final String ADMIN_CHANGE_ORDER_STATE = "changeOrderState";
    public static final String ADMIN_ALL_ORDERS_COMMAND = "allOrders";

    // Common commands
    public static final String SETTINGS_COMMAND = "settings";
    public static final String LOGOUT_COMMAND = "logout";

    private CommandNames() {
    }
}
