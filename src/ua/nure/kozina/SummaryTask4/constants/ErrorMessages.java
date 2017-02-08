package ua.nure.kozina.SummaryTask4.constants;

/**
 * The ErrorMessages class contains a string constants that informs users about an error occurs.
 *
 * @author V. Kozina-Kravchenko
 */
public class ErrorMessages {
    // DB errors
    public static final String CANNOT_GET_DATA_SOURCE_CONNECTION = "err.connection.no_data_source";
    public static final String CANNOT_GET_CONNECTION = "err.connection.cannot connect";
    public static final String INSPECTOR_ERROR = "err.db_inspector";

    public static final String CANNOT_GET_ALL_USERS = "err.users.cannot_get";
    public static final String CANNOT_GET_USER_BY_ID = "err.user.id.cannot_get";
    public static final String CANNOT_GET_USER_BY_LOGIN = "err.user.login.cannot_get";
    public static final String CANNOT_GET_USER_BY_EMAIL = "err.user.email.cannot_get";

    public static final String CANNOT_GET_ALL_APARTMENT_CLASSES = "err.apartment_classes.cannot_get";
    public static final String CANNOT_GET_APARTMENT_CLASS_BY_ID = "err.apartment_class.id.cannot_get";
    public static final String CANNOT_GET_ALL_APARTMENTS = "err.apartments.cannot_get";
    public static final String CANNOT_GET_ALL_APARTMENTS_BY_CLASS_ID = "err.apartments.class_id.cannot_get";
    public static final String CANNOT_GET_ALL_APARTMENTS_BY_CLASS_AND_PLACE_COUNT = "err.apartments.place_count_and_class_id.cannot_get";
    public static final String CANNOT_GET_APARTMENT_BY_ROOM_NUMBER = "err.apartment.number.cannot_get";

    public static final String CANNOT_GET_ALL_ORDERS = "err.orders.cannot_get";
    public static final String CANNOT_GET_ALL_ORDERS_BY_USER_ID = "err.orders.user_id.cannot_get";
    public static final String CANNOT_GET_ALL_ORDERS_BY_STATE_ID = "err.orders.state.cannot_get";
    public static final String CANNOT_GET_ALL_ACTIVE_ORDERS = "err.active_orders.cannot_get";
    public static final String CANNOT_GET_ALL_ACTIVE_ORDERS_BY_USER_ID = "err.active_orders.user_id.cannot_get";
    public static final String CANNOT_GET_ORDER_BY_ID = "err.order.id.cannot_get";

    public static final String CANNOT_GET_ALL_ACTIVE_REQUESTS = "err.active_requests.cannot_get";
    public static final String CANNOT_GET_ALL_ACTIVE_USER_REQUESTS = "err.active_requests.user_id.cannot_get";
    public static final String CANNOT_GET_REQUEST_BY_ID = "err.request.id.cannot_get";

    public static final String CANNOT_GET_ALL_FEEDBACKS = "err.feedbacks.cannot_get";
    public static final String CANNOT_GET_FEEDBACK_BY_USER_ID = "err.feedback.user_id.cannot_get";
    public static final String CANNOT_GET_FORGOT_PASSWORD_QUERY = "err.forgot_password.cannot_get";

    public static final String CANNOT_ADD_USER = "err.user.cannot_add";
    public static final String CANNOT_ADD_APARTMENT = "err.apartment.cannot_add";
    public static final String CANNOT_ADD_APARTMENT_CLASS = "err.apartment_class.cannot_add";
    public static final String CANNOT_ADD_ORDER = "err.order.cannot_add";
    public static final String CANNOT_ADD_REQUEST = "err.request.cannot_add";
    public static final String CANNOT_LEAVE_FEEDBACK = "err.feedback.cannot_add";
    public static final String CANNOT_ADD_FORGOT_PASSWORD_QUERY = "err.forgot_password.cannot_add";

    public static final String CANNOT_UPDATE_USER = "err.user.cannot_update";
    public static final String CANNOT_UPDATE_APARTMENT = "err.apartment.cannot_update";
    public static final String CANNOT_UPDATE_ORDER = "err.order.cannot_update";
    public static final String CANNOT_UPDATE_REQUEST = "err.request.cannot_update";
    public static final String CANNOT_UPDATE_FORGOT_PASSWORD_QUERY = "err.forgot_password.query.cannot_update";

    public static final String CANNOT_ROLLBACK = "err.cannot_rollback";
    public static final String CANNOT_CLOSE_CONNECTION = "err.connection.cannot_close";
    public static final String CANNOT_CLOSE_STATEMENT = "err.statement.cannot_close";
    public static final String CANNOT_CLOSE_PREPARED_STATEMENT = "err.prepared_statement.cannot_close";
    public static final String CANNOT_CLOSE_RESULT_SET = "err.result_set.cannot_close";

    // Data validation error messages
    public static final String NO_USER_OR_QUERY_USED = "err.user.not_found";
    public static final String WRONG_LOGIN_LENGTH = "err.login.wrong_length";
    public static final String LOGIN_DOES_NOT_MATCHES_PATTERN = "err.login.must_contain";
    public static final String USER_IS_ALREADY_EXISTS = "err.login.user_exists";
    public static final String EMPTY_LOGIN = "err.login.empty";
    public static final String NO_USER_WITH_SUCH_LOGIN = "err.login.no_user";
    public static final String EMPTY_LOGIN_OR_PASSWORD = "err.login_or_pass.empty";
    public static final String PASSWORDS_ARE_NOT_EQUAL = "err.passwords.not_equal";
    public static final String WRONG_PASSWORD_LENGTH = "err.pass.wrong_length";
    public static final String PASSWORD_DOES_NOT_MATCHES_PATTERN = "err.pass.must_contain";
    public static final String EMPTY_PASSWORD = "err.pass.empty";
    public static final String WRONG_PASSWORD = "err.pass.wrong";
    public static final String WRONG_EMAIL_LENGTH = "err.email.wrong_length";
    public static final String USER_WITH_SUCH_EMAIL_IS_ALREADY_EXISTS = "err.email.user_exists";
    public static final String EMAIL_DOES_NOT_MATCHES_PATTERN = "err.email.doesnt_match_pattern";
    public static final String EMPTY_EMAIL = "err.email.empty";
    public static final String NO_USER_WITH_SUCH_EMAIL = "err.email.no_user";
    public static final String WRONG_NAME_LENGTH = "err.name.wrong_length";
    public static final String EMPTY_NAME = "err.name.empty";
    public static final String DATE_DOES_NOT_MATCHES_PATTERN = "err.date.wrong_format";
    public static final String DATE_DOES_NOT_MATCHES_PATTERN_WITH_CORRECT = "err.date.wrong_format_exmpl";
    public static final String EMPTY_DATE = "err.date.empty";
    public static final String ARRIVAL_DATE_IS_GREATER_LEAVING = "err.dates.wrong";
    public static final String DATE_HAS_ALREADY_PASSED = "err.date.passed";
    public static final String EMPTY_ROOM_NUMBER = "err.room_number.empty";
    public static final String EMPTY_FEEDBACK = "err.feedback.empty";
    public static final String FEEDBACK_TOO_LONG = "err.feedback.wrong_length";

    // Command errors
    public static final String ERROR_MESSAGE_TYPE = "errorMessage";
    public static final String BILL_ALREADY_PAID = "err.bill.paid";
    public static final String NEED_AUTHORIZATION = "err.need_auth";
    public static final String ROOM_IN_USAGE = "err.room_number.in_usage";
    public static final String REQUEST_ALREADY_CANCELED = "err.request.canceled";
    public static final String EMPTY_CAPTCHA = "err.captcha.empty";
    public static final String CAPTCHA_FAILED = "err.captcha.wrong";

    public static final String CANNOT_SEND_MESSAGE = "err.cannot_send_message";
    public static final String EXPIRED_QUERY = "err.forgot_password.expired";

    private ErrorMessages() {
    }
}
