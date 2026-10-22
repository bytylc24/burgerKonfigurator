package burgerkonfigurator.model;

public enum Bun {
    SESAME("Sesam", 1.50),
    BRIOCHE("Brioche", 1.80),
    WHOLE_GRAIN("Vollkorn", 1.60),
    GLUTEN_FREE("Glutenfrei", 2.00);

    private final String label;
    private final double price;

    Bun(String label, double price) {
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
