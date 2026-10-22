package burgerkonfigurator.model;

public enum Extra {
    BACON("Bacon", 1.20),
    FRIED_EGG("Spiegelei", 1.00),
    EXTRA_PATTY("Extra Patty", 2.50),
    AVOCADO("Avocado", 1.30);

    private final String label;
    private final double price;

    Extra(String label, double price) {
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
