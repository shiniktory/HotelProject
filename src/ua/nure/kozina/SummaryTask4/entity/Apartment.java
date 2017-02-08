package ua.nure.kozina.SummaryTask4.entity;

import ua.nure.kozina.SummaryTask4.stateAndRole.ApartmentState;

import java.io.Serializable;

/**
 * The Apartment entity class.
 *
 * @author V. Kozina-Kravchenko
 */
public class Apartment implements Serializable {

    /**
     * The number of the room in the hotel.
     */
    private int roomNumber;

    /**
     * The count of places in the room.
     */
    private int placeCount;

    /**
     * The description of the apartment class.
     */
    private ApartmentClass apartmentClass;

    /**
     * The current apartment state.
     */
    private ApartmentState state;

    /**
     * The room price per one day.
     */
    private double price;

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getPlaceCount() {
        return placeCount;
    }

    public void setPlaceCount(int placeCount) {
        this.placeCount = placeCount;
    }

    public ApartmentClass getApartmentClass() {
        return apartmentClass;
    }

    public void setApartmentClass(ApartmentClass apartmentClass) {
        this.apartmentClass = apartmentClass;
    }

    public ApartmentState getState() {
        return state;
    }

    public void setState(ApartmentState state) {
        this.state = state;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Apartment apartment = (Apartment) o;
        if (placeCount != apartment.placeCount) {
            return false;
        }
        if (Double.compare(apartment.price, price) != 0) {
            return false;
        }
        if (roomNumber != apartment.roomNumber) {
            return false;
        }
        if (apartmentClass != null ?
                !apartmentClass.equals(apartment.apartmentClass) :
                apartment.apartmentClass != null) {
            return false;
        }
        return state == apartment.state;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = roomNumber;
        result = 31 * result + placeCount;
        result = 31 * result + (apartmentClass != null ? apartmentClass.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Apartment{" +
                "roomNumber=" + roomNumber +
                ", placeCount=" + placeCount +
                ", apartmentClass=" + apartmentClass +
                ", state=" + state +
                ", price=" + price +
                '}';
    }
}
