package ua.nure.kozina.SummaryTask4.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * The Feedback entity class.
 *
 * @author V. Kozina-Krachenko
 */
public class Feedback implements Serializable {

    /**
     * The value of the feedback id.
     */
    private long id;

    /**
     * The user who left feedback.
     */
    private User user;

    /**
     * The date when feedback left.
     */
    private Date dateCreated;

    /**
     * The text of the current feedback.
     */
    private String text;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Feedback feedback = (Feedback) o;
        if (id != feedback.id) {
            return false;
        }
        if (dateCreated != null ?
                !dateCreated.equals(feedback.dateCreated) :
                feedback.dateCreated != null) {
            return false;
        }
        if (text != null ? !text.equals(feedback.text) : feedback.text != null) {
            return false;
        }
        return !(user != null ? !user.equals(feedback.user) : feedback.user != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (dateCreated != null ? dateCreated.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Feedback {" +
                "id=" + id +
                ", user=" + user +
                ", dateCreated=" + dateCreated +
                ", text='" + text + '\'' +
                '}';
    }
}
