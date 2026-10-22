package burgerkonfigurator.repository;

import burgerkonfigurator.model.Bun;
import burgerkonfigurator.model.Burger;
import burgerkonfigurator.model.BurgerBuilder;
import burgerkonfigurator.model.Cheese;
import burgerkonfigurator.model.Extra;
import burgerkonfigurator.model.Patty;
import burgerkonfigurator.model.Sauce;
import burgerkonfigurator.model.Vegetable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JsonBurgerRepository implements BurgerRepository {

    private static final Logger LOG = Logger.getLogger(JsonBurgerRepository.class.getName());
    private static final Type LIST_TYPE = new TypeToken<List<BurgerData>>() { }.getType();

    /** Plain persistence shape; burgers are rebuilt through the builder on load. */
    private record BurgerData(String id, String name, Bun bun, Patty patty, Cheese cheese, Sauce sauce,
                              List<Vegetable> vegetables, List<Extra> extras) { }

    private final Path file;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final List<Burger> burgers = new ArrayList<>();

    public JsonBurgerRepository(Path file) {
        this.file = file;
        load();
    }

    @Override
    public List<Burger> findAll() {
        return List.copyOf(burgers);
    }

    @Override
    public void save(Burger burger) {
        burgers.removeIf(b -> b.getId().equals(burger.getId()));
        burgers.add(burger);
        persist();
    }

    @Override
    public void deleteById(String id) {
        if (burgers.removeIf(b -> b.getId().equals(id))) {
            persist();
        }
    }

    private void load() {
        if (!Files.exists(file)) {
            LOG.info("No data file found, starting with an empty list: " + file);
            return;
        }
        try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            List<BurgerData> data = gson.fromJson(reader, LIST_TYPE);
            if (data == null) {
                return;
            }
            int skipped = 0;
            for (BurgerData d : data) {
                try {
                    burgers.add(new BurgerBuilder().id(d.id()).name(d.name()).bun(d.bun()).patty(d.patty())
                            .cheese(d.cheese()).sauce(d.sauce())
                            .vegetables(d.vegetables()).extras(d.extras()).build());
                } catch (RuntimeException e) {
                    skipped++;
                }
            }
            if (skipped > 0) {
                LOG.warning(skipped + " invalid burger entries were skipped while loading.");
            }
        } catch (IOException | RuntimeException e) {
            burgers.clear();
            LOG.log(Level.WARNING, "Data file is unreadable or corrupt, starting empty: " + file, e);
        }
    }

    private void persist() {
        List<BurgerData> data = burgers.stream()
                .map(b -> new BurgerData(b.getId(), b.getName(), b.getBun(), b.getPatty(), b.getCheese(),
                        b.getSauce(), b.getVegetables(), b.getExtras()))
                .toList();
        try (Writer writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            gson.toJson(data, LIST_TYPE, writer);
        } catch (IOException e) {
            throw new RepositoryException("Burger konnten nicht gespeichert werden: " + e.getMessage(), e);
        }
    }
}
