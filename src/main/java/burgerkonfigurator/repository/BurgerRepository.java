package burgerkonfigurator.repository;

import burgerkonfigurator.model.Burger;

import java.util.List;

public interface BurgerRepository {

    List<Burger> findAll();

    void save(Burger burger);

    void deleteById(String id);
}
