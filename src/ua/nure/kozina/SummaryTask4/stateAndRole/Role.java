package ua.nure.kozina.SummaryTask4.stateAndRole;

/**
 * The enum of user roles.
 *
 * @author V. Kozina-Kravchenko
 */
public enum Role {

    ADMIN("role.admin"), CLIENT("role.client");

    /**
     * The localized key.
     */
    private final String localized;

    /**
     * Constructs a new apartment state with the specified key for localization.
     *
     * @param localized a key for localization
     */
    Role(String localized) {
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
     * Returns a user role instance appropriate to the specified id.
     *
     * @param id a user role appropriate to the id value
     * @return a user role instance appropriate to the specified id
     */
    public static Role getRoleById(int id) {
        return Role.values()[id];
    }
}
