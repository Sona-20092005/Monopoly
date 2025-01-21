package monopoly.core;

public class Property extends Tile {

    private Board.Color color;
    private int rentCost;

    public Property(int position, String name, Board.Color color, int cost, int rentCost) {
        super(position, name, cost);
        this.color = color;
        this.rentCost = rentCost;
    }

    public Board.Color getColor() {
        return color;
    }

    public int calculateRent(boolean allOwned) {
        if(allOwned) {
            return 2 * rentCost;
        }
        else {
            return rentCost;
        }
    }

    public String toString() {

        return super.toString() + " " + color + " " + getPrice();
    }

    public int getPrice() {
        return super.getPrice();
    }
}
