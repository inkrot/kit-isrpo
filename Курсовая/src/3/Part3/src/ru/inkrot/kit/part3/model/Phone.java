package ru.inkrot.kit.part3.model;

import java.util.Objects;

public class Phone extends DataBaseEntity {

    String name;
    int price;

    public Phone(int id, String name, int price) {
        super(id);
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString() {
        return name + ", " + price + " руб.";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Phone phone = (Phone) o;
        return getId() == phone.getId() && price == phone.price &&
                Objects.equals(name, phone.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }
}
