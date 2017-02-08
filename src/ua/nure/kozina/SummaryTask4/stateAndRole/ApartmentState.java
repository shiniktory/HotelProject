package ua.nure.kozina.SummaryTask4.stateAndRole;


/**
 * The enum contains all apartment states.
 *
 *  @author V. Kozina-Kravchenko
 */
public enum ApartmentState {

    FREE("state.free"), RESERVED("state.reserved"), OCCUPIED("state.occupied"),
    UNAVAILABLE("state.unavailable");

    /**
     * The localized key.
     */
    private final String localized;

    /**
     * Constructs a new apartment state with the specified key for localization.
     *
     * @param localized a key for localization
     */
    ApartmentState(String localized) {
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
     * Returns an apartment state instance appropriate to the specified id.
     *
     * @param id an apartment state appropriate to the id value
     * @return an apartment state instance appropriate to the specified id
     */
    public static ApartmentState getStateById(int id) {
        return ApartmentState.values()[id];
    }
}
