package ua.nure.kozina.SummaryTask4.entity;

import java.util.Date;

/**
 * The ForgotPasswordQuery entity class.
 *
 * @author V. Kozina-Kravchenko
 */
public class ForgotPasswordQuery {

    /**
     * The time after what forgot password query expires.
     */
    public static final long QUERY_UPTIME = 30*60*1000;

    /**
     * The value of a forgot password query id.
     */
    private long id;

    /**
     * An email of a user who forgot password.
     */
    private String email;

    /**
     * A generated string token to identify user and process a password reset.
     */
    private String token;

    /**
     * The date when query expires.
     */
    private Date dateExpire;

    /**
     * The flag variable shows was the password reset within this query.
     */
    private boolean reset;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getDateExpire() {
        return dateExpire;
    }

    public void setDateExpire(Date dateExpire) {
        this.dateExpire = dateExpire;
    }

    public boolean isReset() {
        return reset;
    }

    public void setReset(boolean reset) {
        this.reset = reset;
    }

}
