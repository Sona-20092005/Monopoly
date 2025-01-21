package monopoly.core;

public class Utility extends Tile {

    public Utility(int position, String name){
        super(position, name, 150);

    }

    public int calculateRent(int num, boolean bothOwned) {
        if(bothOwned) {
            return 10 * num;
        }
        else {
            return 4 * num;
        }
    }

    public String toString() {
        return super.toString() + " " + getPrice();
    }

    public int getPrice() {
        return super.getPrice();
    }
}

