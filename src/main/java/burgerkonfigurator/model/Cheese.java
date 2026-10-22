package burgerkonfigurator.model;

public enum Cheese {
    NONE("Kein Käse", 0.00),
    CHEDDAR("Cheddar", 0.80),
    GOUDA("Gouda", 0.70),
    BLUE("Blauschimmel", 1.00);

    private final String label;
    private final double price;

    Cheese(String label, double price) {
        this.label = label;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return label;
    }
}
