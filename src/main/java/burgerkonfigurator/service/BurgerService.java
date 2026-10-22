package burgerkonfigurator.service;

import burgerkonfigurator.model.Bun;
import burgerkonfigurator.model.Burger;
import burgerkonfigurator.model.BurgerBuilder;
import burgerkonfigurator.model.Cheese;
import burgerkonfigurator.model.Extra;
import burgerkonfigurator.model.Patty;
import burgerkonfigurator.model.Sauce;
import burgerkonfigurator.model.Vegetable;
import burgerkonfigurator.repository.BurgerRepository;

import java.util.Collection;
import java.util.List;

public class BurgerService {

    private final BurgerRepository repository;

    public BurgerService(BurgerRepository repository) {
        this.repository = repository;
    }

    public List<Burger> getAllBurgers() {
        return repository.findAll();
    }

    /** Creates and stores a new burger. */
    public Burger createBurger(String name, Bun bun, Patty patty, Cheese cheese, Sauce sauce,
                               Collection<Vegetable> vegetables, Collection<Extra> extras) {
        return store(new BurgerBuilder(), name, bun, patty, cheese, sauce, vegetables, extras);
    }

    /** "Edits" a burger: builds a new one from the original; the original stays untouched. */
    public Burger createVariant(Burger original, String name, Bun bun, Patty patty, Cheese cheese, Sauce sauce,
                                Collection<Vegetable> vegetables, Collection<Extra> extras) {
        return store(BurgerBuilder.from(original), name, bun, patty, cheese, sauce, vegetables, extras);
    }

    public void deleteBurger(Burger burger) {
        repository.deleteById(burger.getId());
    }

    /** Price preview for the UI without creating a burger. */
    public double previewPrice(Bun bun, Patty patty, Cheese cheese, Sauce sauce,
                               Collection<Vegetable> vegetables, Collection<Extra> extras) {
        return new BurgerBuilder().bun(bun).patty(patty).cheese(cheese).sauce(sauce)
                .vegetables(vegetables).extras(extras).calculatePrice();
    }

    private Burger store(BurgerBuilder builder, String name, Bun bun, Patty patty, Cheese cheese, Sauce sauce,
                         Collection<Vegetable> vegetables, Collection<Extra> extras) {
        builder.name(name).bun(bun).patty(patty).cheese(cheese).sauce(sauce)
                .vegetables(vegetables).extras(extras);
        Burger burger;
        try {
            burger = builder.build();
        } catch (IllegalStateException e) {
            throw new ValidationException(e.getMessage());
        }
        repository.save(burger);
        return burger;
    }
}
