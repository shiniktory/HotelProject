package ua.nure.kozina.SummaryTask4.web.command;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ua.nure.kozina.SummaryTask4.DB.ApartmentManager;
import ua.nure.kozina.SummaryTask4.entity.Apartment;
import ua.nure.kozina.SummaryTask4.entity.ApartmentClass;
import ua.nure.kozina.SummaryTask4.exception.ApplicationException;
import ua.nure.kozina.SummaryTask4.exception.DBException;
import ua.nure.kozina.SummaryTask4.util.SorterUtil;
import ua.nure.kozina.SummaryTask4.constants.Path;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static ua.nure.kozina.SummaryTask4.constants.ParametersAndAttributes.*;

/**
 * The SortCommand class performs the sorting of a hotel apartments lists.
 *
 * @author V. Kozina-Kravchenko
 */
class SortCommand extends Command {

    private static final Logger LOGGER = LogManager.getLogger(SortCommand.class);

    @Override
    public String perform(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ApplicationException {
        LOGGER.debug("SortCommand started");
        HttpSession session = request.getSession();
        String sortType = request.getParameter(SORT_TYPE);
        LOGGER.trace("Type of sort: " + sortType);
        Map<ApartmentClass, List<Apartment>> aps = getSortedApartments(sortType);
        session.setAttribute(APARTMENTS_AND_CLASSES, aps);
        session.setAttribute(SORTING, sortType);
        LOGGER.debug("SortCommand finished");
        return Path.CLIENT_ROOM_LIST;
    }

    /**
     * Returns a sorted list of apartment classes and associated with them lists of
     * hotel apartments.
     *
     * @param sortType the sorting type
     * @return a sorted list of apartment classes and associated with them lists of
     *         hotel apartments
     * @throws DBException
     */
    private Map<ApartmentClass, List<Apartment>> getSortedApartments(String sortType) throws DBException {
        Map<ApartmentClass, List<Apartment>> aps = new LinkedHashMap<>();
        ApartmentManager manager = new ApartmentManager();
        for (ApartmentClass c : manager.findApartmentClasses()) {
            List<Apartment> apartments = manager.findAllApartmentsByClassId(c.getId());
            sort(apartments, sortType);
            aps.put(c, apartments);
        }
        return aps;
    }

    /**
     * Sorts the specified apartment list in the given sort type order.
     *
     * @param apartments the list of apartments to sort
     * @param sortType   a string representation of a sort type
     */
    private void sort(List<Apartment> apartments, String sortType) {
        switch (sortType) {
            case PRICE_SORT_TYPE:
                SorterUtil.sortByPrice(apartments);
                break;
            case PLACE_COUNT_SORT_TYPE:
                SorterUtil.sortByPlaceCount(apartments);
                break;
            case STATUS_SORT_TYPE:
                SorterUtil.sortByStatus(apartments);
        }
    }
}
