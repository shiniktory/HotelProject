package ua.nure.kozina.SummaryTask4.constants;

/**
 * The ParametersAndAttributes class contains all names of session or request parameters and attributes.
 *
 * @author V. Kozina-Kravchenko
 */
public class ParametersAndAttributes {

    // User data
    public static final String LOGIN = "login";
    public static final String PASSWORD = "password";
    public static final String OLD_PASSWORD = "oldPassword";
    public static final String PASSWORD_1 = "password1";
    public static final String PASSWORD_2 = "password2";
    public static final String EMAIL = "email";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String FEEDBACK = "feedbackText";
    public static final String SECURITY_CODE = "seccode";
    public static final String I_CAPTCHA = "icaptcha";
    public static final String COMMON_ROLE = "COMMON";
    public static final String TOKEN = "tui";
    public static final String QUERY = "query";

    // Entities
    public static final String USER = "user";
    public static final String TEMP_USER = "userTMP";
    public static final String COMMENTS = "comments";
    public static final String ORDERS = "orders";
    public static final String REQUESTS = "requests";
    public static final String STATES = "states";
    public static final String REQUESTS_AND_SUITABLE_NUMBERS = "requestsAndNumbers";
    public static final String CLASSES_LIST = "classesList";
    public static final String APARTMENTS_AND_CLASSES = "aps";
    public static final String ORDER_USERS = "orderUsers";
    public static final String REQUEST_USERS = "requestUsers";

    // Page parameters
    public static final String LANGUAGE = "language";
    public static final String CURRENT_PAGE = "currentPage";
    public static final String COMMAND = "command";

    // Order and request data
    public static final String ORDER_ID = "orderId";
    public static final String ORDER = "order";
    public static final String NEW_ORDER_STATE = "newOrderState";
    public static final String ROOM_NUMBER = "roomNumber";
    public static final String DATE_FROM = "dateFrom";
    public static final String DATE_TO = "dateTo";
    public static final String USER_ENTERED_DATE_FORMAT = "yyyy-MM-dd";
    public static final String PLACE_COUNT = "placeCount";
    public static final String APARTMENT_CLASS = "apartmentClass";
    public static final String REQUEST_ID = "requestId";

    // Operation parameters
    public static final String OPERATION = "operationName";
    public static final String CREATE_OPERATION = "create";
    public static final String CONFIRM_OPERATION = "confirm";
    public static final String CANCEL_OPERATION = "cancel";

    // Sorting parameters
    public static final String SORT_TYPE = "sortType";
    public static final String SORTING = "sort";
    public static final String PRICE_SORT_TYPE = "price";
    public static final String PLACE_COUNT_SORT_TYPE = "placeCount";
    public static final String STATUS_SORT_TYPE = "status";

    public static final String HASH_ALGORITHM = "MD5";

    private ParametersAndAttributes() {
    }
}
