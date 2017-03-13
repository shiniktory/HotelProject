package ua.nure.kozina.SummaryTask4.entity;

import ua.nure.kozina.SummaryTask4.stateAndRole.OrderState;

import java.io.Serializable;
import java.util.Date;

/**
 * The RoomRequest entity class.
 *
 * @author V. Kozina-Kravchenko
 */
public class RoomRequest implements Serializable {

    /**
     * The room request id.
     */
    private long id;

    /**
     * An id of a user who left the room request.
     */
    private long userId;

    /**
     * The required count of places in the hotel room.
     */
    private int placeCount;

    /**
     * A required apartment class.
     */
    private ApartmentClass roomClass;

    /**
     * The date when client arrives.
     */
    private Date arrivalDate;

    /**
     * The date when client leaves the room.
     */
    private Date leavingDate;

    /**
     * A request state.
     */
    private OrderState state;

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

    public int getPlaceCount() {
        return placeCount;
    }

    public void setPlaceCount(int placeCount) {
        this.placeCount = placeCount;
    }

    public ApartmentClass  getRoomClass() {
        return roomClass;
    }

    public void setRoomClass(ApartmentClass  roomClass) {
        this.roomClass = roomClass;
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

    public OrderState getState() {
        return state;
    }

    public void setState(OrderState state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "RoomRequest: " +
                "id=" + id +
                ", userId=" + userId +
                ", placeCount=" + placeCount +
                ", roomClass=" + roomClass +
                ", arrivalDate=" + arrivalDate +
                ", leavingDate=" + leavingDate +
                ", state=" + state;
    }
}
