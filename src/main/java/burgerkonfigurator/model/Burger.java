package burgerkonfigurator.model;

import java.util.List;

/** Immutable burger. Instances are created only via {@link BurgerBuilder}. */
public final class Burger {

    private final String id;
    private final String name;
    private final Bun bun;
    private final Patty patty;
    private final Cheese cheese;
    private final Sauce sauce;
    private final List<Vegetable> vegetables;
    private final List<Extra> extras;
    private final double price;

    Burger(String id, String name, Bun bun, Patty patty, Cheese cheese, Sauce sauce,
           List<Vegetable> vegetables, List<Extra> extras, double price) {
        this.id = id;
        this.name = name;
        this.bun = bun;
        this.patty = patty;
        this.cheese = cheese;
        this.sauce = sauce;
        this.vegetables = List.copyOf(vegetables);
        this.extras = List.copyOf(extras);
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Bun getBun() {
        return bun;
    }

    public Patty getPatty() {
        return patty;
    }

    public Cheese getCheese() {
        return cheese;
    }

    public Sauce getSauce() {
        return sauce;
    }

    public List<Vegetable> getVegetables() {
        return vegetables;
    }

    public List<Extra> getExtras() {
        return extras;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return name + " (" + String.format("%.2f", price) + " €)";
    }
}
