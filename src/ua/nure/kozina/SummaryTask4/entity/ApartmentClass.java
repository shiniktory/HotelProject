package ua.nure.kozina.SummaryTask4.entity;

import java.io.Serializable;

/**
 * The Apartment class entity.
 *
 * @author V. Kozina-Kravchenko
 */
public class ApartmentClass implements Serializable {

    /**
     * The apartment class id.
     */
    private int id;

    /**
     * The string value of the apartment class name.
     */
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ApartmentClass that = (ApartmentClass) o;
        if (id != that.id) {
            return false;
        }
        return !(name != null ? !name.equals(that.name) : that.name != null);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ApartmentClass{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
