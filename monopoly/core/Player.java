package monopoly.core;

public class Player {
    public enum Symbol {
        IRON,
        PENGUIN,
        HAT,
        SHOE,
        CAT,
        SHIP,
        CAR,
        CHAIR
    }

    private Symbol symbol;
    private int money;
    private int position;
    private int turnsInJail = 0;
    private int hasJailFreeCard = 0;

    public Player(Symbol symbol){
        this.symbol = symbol;
        this.money = 1500;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public int getPosition() {
        return position;
    }

    public int getMoney() {
        return money;
    }

    public void changeMoney(int amount) {
        this.money +=amount;
    }

    public void moveSpaces(int spaces) {
        this.position += spaces;

        if(position >= Board.BOARD_SIZE) {
            this.position = Board.BOARD_SIZE - this.position;
            changeMoney(200);
        }
        else if (position < 0 ) {
            this.position = Board.BOARD_SIZE + this.position;
        }
    }

    public void moveTo(int position) {
        if (this.position > position){
            money += 200;
        }
        this.position = position;
    }
    public void goToJail(Board board){
        this.position = board.getTilePosition("Jail");
        turnsInJail = 2;
    }

    public int getTurnsInJail() {
        return turnsInJail;
    }

    public void reduceTurnsInJail() {
        this.turnsInJail--;
    }


    public boolean getHasJailFreeCard() {
        if(hasJailFreeCard == 0) {
            return false;
        }
        return true;
    }
    public void addHasJailFreeCard() {
        hasJailFreeCard += 1;
    }
    public void reduceHasJailFreeCard() {
        hasJailFreeCard -= 1;
    }
    public void freeFromJail() {
        turnsInJail = 0;
    }


    public boolean equals(Object other) {
        if(other == null || other.getClass() != getClass()) {
            return false;
        }
        Player p = (Player) other;
        if (symbol == p.getSymbol()) {
            return true;
        }

        return false;
    }

    public String toString(){

        return symbol.toString();
    }

}
