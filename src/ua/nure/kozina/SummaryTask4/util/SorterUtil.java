package ua.nure.kozina.SummaryTask4.util;

import ua.nure.kozina.SummaryTask4.entity.Apartment;
import ua.nure.kozina.SummaryTask4.entity.Feedback;
import ua.nure.kozina.SummaryTask4.entity.Order;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * The class designed for sorting lists by different criteria.
 *
 * @author V. Kozina-Kravchenko
 */
public class SorterUtil {

    /**
     * The Comparator instance compares by apartments price.
     */
    private static final Comparator<Apartment> SORT_BY_PRICE = new Comparator<Apartment>() {
        @Override
        public int compare(Apartment o1, Apartment o2) {
            return (int) Math.round(o1.getPrice() - o2.getPrice());
        }
    };

    /**
     * The Comparator instance compares by apartments place count.
     */
    private static final Comparator<Apartment> SORT_BY_PLACE_COUNT = new Comparator<Apartment>() {
        @Override
        public int compare(Apartment o1, Apartment o2) {
            return o1.getPlaceCount() - o2.getPlaceCount();
        }
    };

    /**
     * The Comparator instance compares by apartments class.
     */
    private static final Comparator<Apartment> SORT_BY_CLASS = new Comparator<Apartment>() {
        @Override
        public int compare(Apartment o1, Apartment o2) {
            return o1.getApartmentClass().getName().compareTo(
                    o2.getApartmentClass().getName());
        }
    };

    /**
     * The Comparator instance compares by apartments status.
     */
    private static final Comparator<Apartment> SORT_BY_STATUS = new Comparator<Apartment>() {
        @Override
        public int compare(Apartment o1, Apartment o2) {
            return o1.getState().ordinal() - o2.getState().ordinal();
        }
    };

    /**
     * The Comparator instance compares feedbacks by creation date.
     */
    private static final Comparator<Feedback> SORT_COMMENTS_BY_DATE_CREATED = new Comparator<Feedback>() {
        @Override
        public int compare(Feedback o1, Feedback o2) {
            return (int) (o2.getDateCreated().getTime() - o1.getDateCreated().getTime());
        }
    };

    /**
     * The Comparator instance compares orders by creation date.
     */
    private static final Comparator<Order> SORT_ORDERS_BY_DATE_CREATED = new Comparator<Order>() {
        @Override
        public int compare(Order o1, Order o2) {
            return (int) (o2.getDateCreation().getTime() - o1.getDateCreation().getTime());
        }
    };

    /**
     * Sorts the specified list of apartments by price.
     *
     * @param apartments the list of apartments to sort
     */
    public static void sortByPrice(List<Apartment> apartments) {
        Collections.sort(apartments, SORT_BY_PRICE);
    }

    /**
     * Sorts the specified list of apartments by place count.
     *
     * @param apartments the list of apartments to sort
     */
    public static void sortByPlaceCount(List<Apartment> apartments) {
        Collections.sort(apartments, SORT_BY_PLACE_COUNT);
    }

    /**
     * Sorts the specified list of apartments by class.
     *
     * @param apartments the list of apartments to sort
     */
    public static void sortByClass(List<Apartment> apartments) {
        Collections.sort(apartments, SORT_BY_CLASS);
    }

    /**
     * Sorts the specified list of apartments by status.
     *
     * @param apartments the list of apartments to sort
     */
    public static void sortByStatus(List<Apartment> apartments) {
        Collections.sort(apartments, SORT_BY_STATUS);
    }

    /**
     * Sorts the specified list of feedbacks by their creation date.
     *
     * @param feedbacks the list of feedbacks to sort
     */
    public static void sortFeedbacksByDateCreated(List<Feedback> feedbacks) {
        Collections.sort(feedbacks, SORT_COMMENTS_BY_DATE_CREATED);
    }

    /**
     * Sorts the specified list of orders by their creation date.
     *
     * @param orders the list of orders to sort
     */
    public static void setSortOrdersByDateCreated(List<Order> orders) {
        Collections.sort(orders, SORT_ORDERS_BY_DATE_CREATED);
    }

    private SorterUtil() {
    }
}
