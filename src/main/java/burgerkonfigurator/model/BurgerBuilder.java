package burgerkonfigurator.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class BurgerBuilder {

    private String id = UUID.randomUUID().toString();
    private String name;
    private Bun bun;
    private Patty patty;
    private Cheese cheese = Cheese.NONE;
    private Sauce sauce = Sauce.NONE;
    private final List<Vegetable> vegetables = new ArrayList<>();
    private final List<Extra> extras = new ArrayList<>();

    /** Pre-fills the builder from an existing burger; the id stays new. */
    public static BurgerBuilder from(Burger burger) {
        return new BurgerBuilder()
                .name(burger.getName())
                .bun(burger.getBun())
                .patty(burger.getPatty())
                .cheese(burger.getCheese())
                .sauce(burger.getSauce())
                .vegetables(burger.getVegetables())
                .extras(burger.getExtras());
    }

    /** Only for restoring persisted burgers; new burgers keep the generated id. */
    public BurgerBuilder id(String id) {
        this.id = id;
        return this;
    }

    public BurgerBuilder name(String name) {
        this.name = name;
        return this;
    }

    public BurgerBuilder bun(Bun bun) {
        this.bun = bun;
        return this;
    }

    public BurgerBuilder patty(Patty patty) {
        this.patty = patty;
        return this;
    }

    public BurgerBuilder cheese(Cheese cheese) {
        this.cheese = cheese == null ? Cheese.NONE : cheese;
        return this;
    }

    public BurgerBuilder sauce(Sauce sauce) {
        this.sauce = sauce == null ? Sauce.NONE : sauce;
        return this;
    }

    public BurgerBuilder vegetables(Collection<Vegetable> vegetables) {
        this.vegetables.clear();
        if (vegetables != null) {
            this.vegetables.addAll(vegetables);
        }
        return this;
    }

    public BurgerBuilder extras(Collection<Extra> extras) {
        this.extras.clear();
        if (extras != null) {
            this.extras.addAll(extras);
        }
        return this;
    }

    /** Price of the current selection, usable for a live preview. */
    public double calculatePrice() {
        double sum = 0;
        if (bun != null) sum += bun.getPrice();
        if (patty != null) sum += patty.getPrice();
        sum += cheese.getPrice() + sauce.getPrice();
        for (Vegetable v : vegetables) sum += v.getPrice();
        for (Extra e : extras) sum += e.getPrice();
        return Math.round(sum * 100) / 100.0;
    }

    /** @throws IllegalStateException if name, bun or patty is missing */
    public Burger build() {
        if (name == null || name.isBlank()) {
            throw new IllegalStateException("Der Name darf nicht leer sein.");
        }
        if (bun == null) {
            throw new IllegalStateException("Bitte ein Brot auswählen.");
        }
        if (patty == null) {
            throw new IllegalStateException("Ein Burger braucht mindestens ein Patty.");
        }
        return new Burger(id, name.trim(), bun, patty, cheese, sauce, vegetables, extras, calculatePrice());
    }
}
