package monopoly.core;

public class Tile implements Cloneable{

    private int price;
    private int position;
    private String name;

    public Tile(int position, String name, int price) {
        this.position = position;
        this.name = name;
        this.price = price;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return position + " " + name;
    }

    public int getPrice(){
        return this.price;
    }

    public Tile clone(){
        try {
            Tile copy = (Tile) super.clone();
            return copy;
        }
        catch (CloneNotSupportedException e){
            return null;
        }
    }
}
