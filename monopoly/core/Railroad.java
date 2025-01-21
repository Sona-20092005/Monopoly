package monopoly.core;

public class Railroad extends Tile {
    private int rentCost;

    public Railroad(int position, String name, int rent) {
        super(position, name, 200);
        this.rentCost = rent;

    }

    public int calculateRent(int numberOfOwned) {
        if(numberOfOwned == 4) {
            return 8 * rentCost;
        }
        else if (numberOfOwned == 3){
            return 4 * rentCost;
        }
        else if (numberOfOwned == 2) {
            return 2 * rentCost;
        }
        else {
            return rentCost;
        }
    }

    public String toString() {
        return super.toString() + " " + getPrice();
    }

    public int getPrice() {
        return super.getPrice();
    }

}
