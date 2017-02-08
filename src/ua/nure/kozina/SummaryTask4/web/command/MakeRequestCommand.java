package ua.nure.kozina.SummaryTask4.web.command;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ua.nure.kozina.SummaryTask4.DB.ApartmentManager;
import ua.nure.kozina.SummaryTask4.DB.RoomRequestManager;
import ua.nure.kozina.SummaryTask4.constants.ErrorMessages;
import ua.nure.kozina.SummaryTask4.entity.RoomRequest;
import ua.nure.kozina.SummaryTask4.entity.User;
import ua.nure.kozina.SummaryTask4.exception.ApplicationException;
import ua.nure.kozina.SummaryTask4.exception.DBException;
import ua.nure.kozina.SummaryTask4.stateAndRole.OrderState;
import ua.nure.kozina.SummaryTask4.constants.Path;
import ua.nure.kozina.SummaryTask4.util.MessageUtil;

import static ua.nure.kozina.SummaryTask4.constants.ErrorMessages.ERROR_MESSAGE_TYPE;
import static ua.nure.kozina.SummaryTask4.validator.DataValidator.*;
import static ua.nure.kozina.SummaryTask4.constants.ParametersAndAttributes.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The MakeRequestCommand processes entered by user parameters and creates a new
 * room request based on it.
 *
 * @author V. Kozina-Kravchenko
 */
class MakeRequestCommand extends Command {

    private static final Logger LOGGER = LogManager.getLogger(MakeRequestCommand.class);

    /**
     * Constructs a new MakeRequestCommand instance with the specified redirect flag.
     * If the value is true after this command must be redirection, false - forward.
     *
     * @param isRedirect the value of redirect flag
     */
    public MakeRequestCommand(boolean isRedirect) {
        super(isRedirect);
    }

    @Override
    public String perform(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ApplicationException {
        LOGGER.debug("MakeRequestCommand started");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(USER);
        String path = Path.CLIENT_ORDER_LIST;
        try {
            if (user != null) {
                processRequestParameters(request, user);
            } else {
                throw new ApplicationException(ErrorMessages.NEED_AUTHORIZATION);
            }
        } catch (ApplicationException | ParseException e) {
            LOGGER.error(e);
            String locale = String.valueOf(session.getAttribute(LANGUAGE));
            MessageUtil mu = new MessageUtil(locale);
            session.setAttribute(ERROR_MESSAGE_TYPE, mu.getMessage(e.getMessage()));
            path = Path.CLIENT_REQUEST_COMMAND;
        }
        LOGGER.debug("MakeRequestCommand finished");
        return path;
    }

    /**
     * Gets and verifies entered by user data and creates a new request based on the given
     * user parameters.
     *
     * @param request a request information for HTTP servlets
     * @param user a current logged user
     * @throws ApplicationException
     * @throws ParseException
     */
    private void processRequestParameters(HttpServletRequest request, User user)
            throws ApplicationException, ParseException {
        String arDateStr = request.getParameter(DATE_FROM);
        String lvDateStr = request.getParameter(DATE_TO);
        int placeCount = Integer.parseInt(request.getParameter(PLACE_COUNT));
        int apartmentClassId = Integer.parseInt(request.getParameter(APARTMENT_CLASS));
        DateFormat format = new SimpleDateFormat(USER_ENTERED_DATE_FORMAT);
        checkDate(arDateStr);
        Date arrivalDate = format.parse(arDateStr);
        checkDate(lvDateStr);
        Date leavingDate = format.parse(lvDateStr);
        verifyDates(arrivalDate, leavingDate);
        RoomRequest userRequest = makeRequest(user.getId(), placeCount, apartmentClassId,
                arrivalDate, leavingDate);
        new RoomRequestManager().makeRequest(userRequest);
        LOGGER.trace("Request id: " + userRequest.getId());
    }

    /**
     * Returns a new room request created based on specified entered by user parameters.
     *
     * @param userId an id of user who left room request
     * @param placeCount a required place count in the room
     * @param apartmentClassId a required room class id
     * @param arrivalDate an arrival user date
     * @param leavingDate a leaving user date
     * @return a new room request created based on specified entered by user parameters
     */
    private RoomRequest makeRequest(long userId, int placeCount, int apartmentClassId,
                                    Date arrivalDate, Date leavingDate) throws DBException {
        RoomRequest roomRequest = new RoomRequest();
        roomRequest.setUserId(userId);
        roomRequest.setPlaceCount(placeCount);
        roomRequest.setRoomClass(new ApartmentManager().getApartmentClassById(apartmentClassId));
        roomRequest.setState(OrderState.NEW);
        roomRequest.setArrivalDate(arrivalDate);
        roomRequest.setLeavingDate(leavingDate);
        return roomRequest;
    }
}
