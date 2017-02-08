package ua.nure.kozina.SummaryTask4.web.command;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ua.nure.kozina.SummaryTask4.DB.ApartmentManager;
import ua.nure.kozina.SummaryTask4.entity.Apartment;
import ua.nure.kozina.SummaryTask4.entity.ApartmentClass;
import ua.nure.kozina.SummaryTask4.exception.ApplicationException;
import ua.nure.kozina.SummaryTask4.exception.DBException;
import ua.nure.kozina.SummaryTask4.constants.Path;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static ua.nure.kozina.SummaryTask4.constants.ParametersAndAttributes.APARTMENTS_AND_CLASSES;

/**
 * The RoomListCommand prepares an information about all hotel apartments divided for categories.
 *
 * @author V. Kozina-Kravchenko
 */
class RoomListCommand extends Command {

    private static final Logger LOGGER = LogManager.getLogger(RoomListCommand.class);

    @Override
    public String perform(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ApplicationException {
        LOGGER.debug("RoomListCommand started");
        HttpSession session = request.getSession();
        Map<ApartmentClass, List<Apartment>> aps = getRoomsAndCategories();
        LOGGER.trace("Count of apartment categories: " + aps.size());
        session.setAttribute(APARTMENTS_AND_CLASSES, aps);
        LOGGER.debug("RoomListCommand finished");
        return Path.CLIENT_ROOM_LIST;
    }

    /**
     * Returns the list of apartment classes and associated with them apartment lists.
     *
     * @return the list of apartment classes and associated with them apartment lists
     * @throws DBException
     */
    private Map<ApartmentClass, List<Apartment>> getRoomsAndCategories() throws DBException {
        Map<ApartmentClass, List<Apartment>> rooms = new LinkedHashMap<>();
        ApartmentManager manager = new ApartmentManager();
        List<ApartmentClass> classes = manager.findApartmentClasses();
        for (ApartmentClass c : classes) {
            rooms.put(c, manager.findAllApartmentsByClassId(c.getId()));
        }
        return rooms;
    }
}
