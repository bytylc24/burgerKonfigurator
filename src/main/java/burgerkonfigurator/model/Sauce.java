package burgerkonfigurator.model;

public enum Sauce {
    NONE("Keine Sauce", 0.00),
    KETCHUP("Ketchup", 0.30),
    MAYO("Mayo", 0.30),
    BBQ("BBQ", 0.40),
    GARLIC("Knoblauch", 0.40);

    private final String label;
    private final double price;

    Sauce(String label, double price) {
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
