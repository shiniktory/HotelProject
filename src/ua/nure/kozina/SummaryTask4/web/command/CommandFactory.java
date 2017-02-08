package ua.nure.kozina.SummaryTask4.web.command;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

import static ua.nure.kozina.SummaryTask4.constants.CommandNames.*;

/**
 * The CommandFactory class contains all types of available commands.
 *
 * @author V. Kozina-Kravchenko
 */
public class CommandFactory {

    private static final Logger LOGGER = Logger.getLogger(CommandFactory.class);

    /**
     * The list of commands associated with their names.
     */
    private static final Map<String, Command> commands = new HashMap<>();
    static {
        // No-user commands
        commands.put(LOGIN_COMMAND, new LoginCommand(true));
        commands.put(REGISTRATION_COMMAND, new RegistrationCommand(true));
        commands.put(FORGOT_PASSWORD_COMMAND, new ForgotPasswordCommand(true));
        commands.put(RESET_PASSWORD_REQUEST_COMMAND, new ResetPasswordRequestCommand(true));
        commands.put(RESET_PASSWORD, new ResetPasswordCommand(true));
        commands.put(GUEST_BOOK_COMMAND, new GuestBookCommand());

        // Client's commands
        commands.put(SORT_COMMAND, new SortCommand());
        commands.put(ROOMS_LIST_COMMAND, new RoomListCommand());
        commands.put(REQUEST_COMMAND, new RequestCommand());
        commands.put(MAKE_REQUEST_COMMAND, new MakeRequestCommand(true));
        commands.put(CLIENT_CANCEL_REQUEST_COMMAND, new ClientCancelRequestCommand(true));
        commands.put(ORDER_ROOM_COMMAND, new OrderRoomCommand(true));
        commands.put(CLIENT_ORDERS_COMMAND, new ClientOrdersCommand());
        commands.put(CONFIRM_ORDER_COMMAND, new ConfirmOrderCommand(true));
        commands.put(BILL_COMMAND, new BillCommand());
        commands.put(BILL_PAY_COMMAND, new BillPayCommand(true));
        commands.put(LEAVE_FEEDBACK_COMMAND, new LeaveFeedbackCommand(true));

        // Admin commands
        commands.put(PROCESS_ORDER_COMMAND, new ProcessRequestCommand(true));
        commands.put(ADMIN_ORDERS_COMMAND, new AdminOrdersCommand());
        commands.put(ADMIN_ALL_ORDERS_COMMAND, new AdminAllOrdersCommand());
        commands.put(ADMIN_CHANGE_ORDER_STATE, new AdminChangeOrderStateCommand(true));

        // Common commands
        commands.put(SETTINGS_COMMAND, new SettingsCommand(true));
        commands.put(LOCALE_COMMAND, new LocaleCommand(true));
        commands.put(LOGOUT_COMMAND, new LogoutCommand(true));
        LOGGER.trace("There are: " + commands.size() + "commands");
    }

    /**
     * Returns a new command instance appropriate to the specified command name.
     * Returns null if no such command found.
     *
     * @param command a command name
     * @return a new command instance appropriate to the specified command name
     */
    public static Command getCommand(String command) {
        Command c = null;
        if (command != null && !command.isEmpty()) {
            c = commands.get(command);
        }
        return c;
    }
}
