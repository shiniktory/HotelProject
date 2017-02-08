package ua.nure.kozina.SummaryTask4.entity;

import ua.nure.kozina.SummaryTask4.stateAndRole.OrderState;

import java.io.Serializable;
import java.util.Date;

/**
 * The Order entity class.
 *
 * @author V. Kozina-Kravchenko
 */
public class Order implements Serializable {

    /**
     * The order id value.
     */
    private long id;

    /**
     * An id of a user who ordered the room.
     */
    private long userId;

    /**
     * The ordered room number.
     */
    private int roomNumber;

    /**
     * The current order state.
     */
    private OrderState state;

    /**
     * The order bill value.
     */
    private double bill;

    /**
     * The date when order created.
     */
    private Date dateCreation;

    /**
     * The date when client arrive.
     */
    private Date arrivalDate;

    /**
     * The date when client left the room.
     */
    private Date leavingDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public OrderState getState() {
        return state;
    }

    public void setState(OrderState state) {
        this.state = state;
    }

    public double getBill() {
        return bill;
    }

    public void setBill(double bill) {
        this.bill = bill;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public Date getLeavingDate() {
        return leavingDate;
    }

    public void setLeavingDate(Date leavingDate) {
        this.leavingDate = leavingDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Order order = (Order) o;
        if (Double.compare(order.bill, bill) != 0) {
            return false;
        }
        if (id != order.id) {
            return false;
        }
        if (roomNumber != order.roomNumber) {
            return false;
        }
        if (userId != order.userId) {
            return false;
        }
        if (arrivalDate != null ?
                !arrivalDate.equals(order.arrivalDate) :
                order.arrivalDate != null) {
            return false;
        }
        if (dateCreation != null ?
                !dateCreation.equals(order.dateCreation) :
                order.dateCreation != null) {
            return false;
        }
        if (leavingDate != null ?
                !leavingDate.equals(order.leavingDate) :
                order.leavingDate != null) {
            return false;
        }
        return state == order.state;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (userId ^ (userId >>> 32));
        result = 31 * result + roomNumber;
        result = 31 * result + (state != null ? state.hashCode() : 0);
        temp = Double.doubleToLongBits(bill);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (dateCreation != null ? dateCreation.hashCode() : 0);
        result = 31 * result + (arrivalDate != null ? arrivalDate.hashCode() : 0);
        result = 31 * result + (leavingDate != null ? leavingDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", userId=" + userId +
                ", roomNumber=" + roomNumber +
                ", state=" + state +
                ", bill=" + bill +
                ", dateCreation=" + dateCreation +
                ", arrivalDate=" + arrivalDate +
                ", leavingDate=" + leavingDate +
                '}';
    }
}
