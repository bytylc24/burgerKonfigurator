package burgerkonfigurator.model;

public enum Vegetable {
    LETTUCE("Salat", 0.30),
    TOMATO("Tomate", 0.40),
    ONION("Zwiebel", 0.20),
    PICKLES("Essiggurke", 0.30),
    JALAPENO("Jalapeño", 0.40);

    private final String label;
    private final double price;

    Vegetable(String label, double price) {
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
