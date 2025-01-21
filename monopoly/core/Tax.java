package monopoly.core;

public class Tax extends Tile {
    private int tax;

    public Tax(int position, String name, int tax) {
        super(position, name, 0);
        this.tax = tax;
    }

    public int getTax() {
        return tax;
    }

    public String toString() {
        return super.toString() +  ". Pay " + tax;
    }
}
