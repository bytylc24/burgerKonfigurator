package burgerkonfigurator.model;

public enum Patty {
    BEEF("Rind", 3.50),
    CHICKEN("Hendl", 3.00),
    VEGGIE("Veggie", 3.00),
    VEGAN("Vegan", 3.20);

    private final String label;
    private final double price;

    Patty(String label, double price) {
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
