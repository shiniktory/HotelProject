package ua.nure.kozina.SummaryTask4.constants;

/**
 * The Path class contains a string representations of references to pages and commands.
 *
 * @author V. Kozina-Kravchenko
 */
public class Path {

    public static final String AUTHORIZATION = "Authorization.jsp";
    public static final String REGISTRATION = "Registration.jsp";
    public static final String FORGOT_PASSWORD = "ForgotPassword.jsp";
    public static final String RESET_PASSWORD_PAGE = "ResetPassword.jsp";
    public static final String LEAVE_FEEDBACK = "LeaveFeedback.jsp";
    public static final String GUEST_BOOK = "GuestBook.jsp";
    public static final String GUEST_BOOK_COMMAND = "controller?command=guestBook";
    public static final String SETTINGS = "Settings.jsp";
    public static final String ADMIN_ORDERS_AND_REQUESTS_COMMAND = "controller?command=adminOrders";
    public static final String ADMIN_ORDERS_AND_REQUESTS = "/WEB-INF/pages/admin/OrdersAndRequests.jsp";
    public static final String ADMIN_ALL_ORDERS_PAGE = "/WEB-INF/pages/admin/AllOrders.jsp";
    public static final String CLIENT_CABINET = "/WEB-INF/pages/client/ClientCabinet.jsp";
    public static final String CLIENT_ORDER_LIST = "controller?command=clientOrders";
    public static final String CLIENT_BILL_COMMAND = "controller?command=bill";
    public static final String BILL_PAGE = "/WEB-INF/pages/client/Bill.jsp";
    public static final String CLIENT_REQUEST_COMMAND = "controller?command=request";
    public static final String CLIENT_REQUEST = "/WEB-INF/pages/client/Request.jsp";
    public static final String ROOM_LIST_COMMAND = "controller?command=rooms";
    public static final String CLIENT_ROOM_LIST = "/WEB-INF/pages/client/RoomsList.jsp";
    public static final String ERROR_PAGE = "/WEB-INF/pages/ErrorPage.jsp";

    private Path() {
    }
}
