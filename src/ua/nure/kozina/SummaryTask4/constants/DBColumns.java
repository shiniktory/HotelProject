package ua.nure.kozina.SummaryTask4.constants;

/**
 * The class contains the list of constants represents names of columns in the application database.
 *
 * @author V. Kozina-Kravchenko
 */
public class DBColumns {

    // ID as primary key in all tables
    public static final String ID = "id";

    // Table users
    public static final String LOGIN = "login";
    public static final String PASSWORD = "password";
    public static final String USER_ROLE_ID = "role_id";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String EMAIL = "eMail";

    // Table hotel
    public static final String ROOM_NUMBER = "number";
    public static final String PLACE_COUNT = "place_count";
    public static final String APARTMENT_CLASS = "class_id";
    public static final String APARTMENT_CLASS_NAME = "class";
    public static final String APARTMENT_STATE_ID = "state_id";
    public static final String PRICE = "price";

    // Tables orders and requests
    public static final String USER_ID = "user_id";
    public static final String ORDER_ROOM_NUMBER = "room_number";
    public static final String BILL = "bill";
    public static final String ORDER_STATE_ID = "order_state";
    public static final String REQUEST_STATE_ID = "request_state";
    public static final String ORDER_DATE_CREATION = "date_created";
    public static final String ARRIVAL_DATE = "arrival_date";
    public static final String LEAVING_DATE = "leaving_date";

    // Table feedbacks
    public static final String FEEDBACK_DATE_CREATION = "f_date";
    public static final String FEEDBACK_TEXT = "feedback";

    // Table forgot password
    public static final String TOKEN = "token";
    public static final String DATE_EXP = "date_exp";
    public static final String RESET = "reset";
}
