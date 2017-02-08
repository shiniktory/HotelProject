package ua.nure.kozina.SummaryTask4.stateAndRole;

/**
 * The enum contains all order and request states.
 *
 * @author V. Kozina-Kravchenko
 */
public enum OrderState {

    NEW("state.new"), CONFIRMED("state.confirmed"), PAID("state.paid"), CLOSED("state.closed");

    /**
     * The localized key.
     */
    private final String localized;

    /**
     * Constructs a new apartment state with the specified key for localization.
     *
     * @param localized a key for localization
     */
    OrderState(String localized) {
        this.localized = localized;
    }

    /**
     * Returns a key for localization.
     *
     * @return a key for localization
     */
    public String getLocalized() {
        return localized;
    }

    /**
     * Returns an order or request state instance appropriate to the specified id.
     *
     * @param id an order or request state appropriate to the id value
     * @return an order or request state instance appropriate to the specified id
     */
    public static OrderState getStateById(int id) {
        return OrderState.values()[id];
    }
}
