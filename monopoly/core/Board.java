package monopoly.core;

import java.util.*;

public class Board {

    public enum Color {
        BROWN, SILVER, PINK, ORANGE, RED, YELLOW, GREEN, BLUE
    }

    public enum Description {
        NOT_OWNED_PROPERTY, MY_PROPERTY, OWNED_PROPERTY,

        TAX, GO_TO_JAIL,

        NOT_OWNED_RAILROAD, MY_RAILROAD, OWNED_RAILROAD,

        NOT_OWNED_UTILITY, MY_UTILITY, OWNED_UTILITY,

        COMMUNITY, CHANCE
    }

    public static final int BOARD_SIZE = 40;
    public static final int CARD_IN_DECK_SIZE = 16;
    private Tile[] board;
    private Card[] chanceDeck;
    private Card[] communityChestDeck;
    private Player[] propertiesOwned;
    private Player[] players;
    private int indexForChanceCards = 0;
    private int indexForCommunityChestCards = 0;

    private Player.Symbol turn;

    public Board(int numOfPlayers) {
        this.board = new Tile[BOARD_SIZE];
        board[0] = new Tile(0, "Go", 0);
        board[1] = new Property(1, "Mediterranean Avenue", Color.BROWN, 60, 2);
        board[2] = new Tile(2, "Community Chest", 0);
        board[3] = new Property(3, "Baltic Avenue", Color.BROWN, 60, 4);
        board[4] = new Tax(4, "Income Tax", 200);
        board[5] = new Railroad(5, "Reading Railroad",  25);
        board[6] = new Property(6, "Oriental Avenue", Color.SILVER, 100, 6);
        board[7] = new Tile(7, "Chance", 0);
        board[8] = new Property(8, "Vermont Avenue", Color.SILVER, 100, 6);
        board[9] = new Property(9, "Connecticut Avenue", Color.SILVER, 120, 8);
        board[10] = new Tile(10, "Jail", 0);
        board[11] = new Property(11, "St. Charles Place", Color.PINK, 140, 10);
        board[12] = new Utility(12, "Electric Company");
        board[13] = new Property(13, "States Avenue", Color.PINK, 140, 10);
        board[14] = new Property(14, "Virginia Avenue", Color.PINK, 160, 12);
        board[15] = new Railroad(15, "Pennsylvania Railroad", 25);
        board[16] = new Property(16, "St. James Place", Color.ORANGE, 180, 14);
        board[17] = new Tile(17, "Community Chest", 0);
        board[18] = new Property(18, "Tennessee Avenue", Color.ORANGE, 180, 14);
        board[19] = new Property(19, "New York Avenue", Color.ORANGE, 200, 16);
        board[20] = new Tile(20, "Free Parking", 0);
        board[21] = new Property(21, "Kentucky Avenue", Color.RED, 220, 18);
        board[22] = new Tile(22, "Chance", 0);
        board[23] = new Property(23, "Indiana Avenue", Color.RED, 220, 18);
        board[24] = new Property(24, "Illinois Avenue", Color.RED, 240, 20);
        board[25] = new Railroad(25, "B&O Railroad",  25);
        board[26] = new Property(26, "Atlantic Avenue", Color.YELLOW, 260, 22);
        board[27] = new Property(27, "Ventnor Avenue", Color.YELLOW, 260, 22);
        board[28] = new Utility(28, "Water Works");
        board[29] = new Property(29, "Marvin Gardens", Color.YELLOW, 280, 24);
        board[30] = new Tile(30, "Go To Jail", 0);
        board[31] = new Property(31, "Pacific Avenue", Color.GREEN, 300, 26);
        board[32] = new Property(32, "North Carolina Avenue", Color.GREEN, 300, 26);
        board[33] = new Tile(33, "Community Chest", 0);
        board[34] = new Property(34, "Pennsylvania Avenue", Color.GREEN, 320, 28);
        board[35] = new Railroad(35, "Short Line",  25);
        board[36] = new Tile(36, "Chance", 0);
        board[37] = new Property(37, "Park Place", Color.BLUE, 350, 35);
        board[38] = new Tax(38, "Luxury Tax", 100);
        board[39] = new Property(39, "Boardwalk", Color.BLUE, 400, 50);

        this.propertiesOwned = new Player[BOARD_SIZE];
        this.players = new Player[numOfPlayers];

        this.chanceDeck = new Card[CARD_IN_DECK_SIZE];
        this.communityChestDeck = new Card[CARD_IN_DECK_SIZE];

        for (int i = 0; i < CARD_IN_DECK_SIZE; i++) {
            chanceDeck[i] = Card.values()[i];
            communityChestDeck[i] = Card.values()[i + CARD_IN_DECK_SIZE];
        }

        shuffle(chanceDeck);
        shuffle(communityChestDeck);

    }

    public int getTilePosition(String name) {
        for (Tile tile : this.board) {
            if (name.equalsIgnoreCase(tile.getName())) {
                return tile.getPosition();
            }
        }
        return -1;
    }

    public Tile getTileByPosition(int position){
        return board[position];
    }

    private void shuffle(Card[] deck) {

        List<Card> deckList = Arrays.asList(deck);

        Collections.shuffle(deckList);

        deckList.toArray(deck);

    }

    public void addPlayer(Player.Symbol s) throws DublicatePlayersException {
        if(players[0] == null) {
            turn = s;
        }

        int firstNullIndex = 0;
        for(int i = 0; i < players.length; i++) {
            if(players[i] == null) {
                firstNullIndex = i;
                break;
            }
            if(players[i].getSymbol() == s) {
                throw new DublicatePlayersException();
            }
        }
        players[firstNullIndex] = new Player(s);
    }

    public Player[] getPlayers() {
        return players;
    }

    public Description getDescription(Player p) {
        Tile t = board[p.getPosition()];
        Scanner sc = new Scanner(System.in);
        if (t.getClass() == Property.class) {
            if (getPropertyOwner(p.getPosition()) == null) {
                return Description.NOT_OWNED_PROPERTY;
            }
            else if (getPropertyOwner(p.getPosition()).equals(p)) {
                return Description.MY_PROPERTY;
            }
            else {
                return Description.OWNED_PROPERTY;
            }
        }
        else if (t.getClass() == Tax.class) {
            return Description.TAX;
        }
        else if (t.getClass() == Railroad.class) {
            if (getPropertyOwner(p.getPosition()) == null) {
                return Description.NOT_OWNED_RAILROAD;
            }
            else if (getPropertyOwner(p.getPosition()).equals(p)) {
                return Description.MY_RAILROAD;
            }
            else {
                return Description.OWNED_RAILROAD;
            }
        }
        else if (t.getClass() == Utility.class) {
            if (propertiesOwned[p.getPosition()] == p) {
                return Description.MY_UTILITY;
            }
            else {
                if (propertiesOwned[p.getPosition()] == null) {
                    return Description.NOT_OWNED_UTILITY;
                }
                else {
                    return Description.OWNED_UTILITY;
                }
            }
        }
        else {
            if (t.getName().equals("Community Chest")) {

                return Description.COMMUNITY;
            }
            if (t.getName().equals("Chance")) {
                return Description.CHANCE;
            }
            if (t.getName().equals("Go To Jail")) {
                return Description.GO_TO_JAIL;
            }

        }
        return null;
    }

    public Player getPropertyOwner(int tileCoordinate) {
        return propertiesOwned[tileCoordinate];
    }

    public boolean areAllPropertiesOwned(Color color, Player p) {;
        for (int i = 0; i < board.length; i++) {

            if(board[i].getClass() == Property.class && (color == ((Property) board[i]).getColor())){
                if (!p.equals(getPropertyOwner(i))) {
                    return false;
                }
            }

        }
        return true;
    }

    public int numberOfRailroadsOwned(Player p) {
        int numberOfRailroads = 0;
        String[] array = {"Reading Railroad","Pennsylvania Railroad","B&O Railroad", "Short Line"};
        for (int i = 0; i < array.length; i++) {
            if (propertiesOwned[getTilePosition(array[i])] == p){
                numberOfRailroads ++;
            }
        }
        return numberOfRailroads;
    }

    public int numberOfUtilitiesOwned(Player p){
            if( propertiesOwned[getTilePosition("Electric Company")] == p &&
                propertiesOwned[getTilePosition("Water Works")] == p) {
                return 2;
            } else if(propertiesOwned[getTilePosition("Electric Company")] == p ||
                propertiesOwned[getTilePosition("Water Works")] == p){
                return 1;
            }
            else return 0;
    }

    public void buyTheProperty(Player p, String answer){
        if (answer.equals("yes")){
            propertiesOwned[p.getPosition()] = p;
            p.changeMoney(-getTileByPosition(p.getPosition()).getPrice());
        }
    }

    public int getBoardSize() {
        return board.length;
    }

    public Tile[] getBoardTile() {

        Tile[] newArr =  new Tile[board.length];
        for (int i = 0; i < board.length; i++) {
            newArr[i] = board[i].clone();
        }
        return newArr;

    }

    public Player.Symbol getTurn() {
        return turn;
    }

    public void changeTurn() {
        System.out.print("positions ");
        for (int i = 0; i < players.length; i++) {
            System.out.print(players[i].getPosition() + " ");
        }
        System.out.println();

        int turnIndex = -1;
        for (int i = 0; i < players.length; i++) {
            if(players[i].getSymbol() == turn){
                turnIndex = i + 1;

            }
        }
        while(true) {
            if(turnIndex >= players.length) {
                for(int i = 0; i < players.length; i++) {
                    if(players[i].getTurnsInJail() == 0) {
                        turn = players[i].getSymbol();
                        return;
                    }
                    else {
                        if(players[i].getHasJailFreeCard()) {
                            players[i].freeFromJail();
                            turn = players[i].getSymbol();
                            return;
                        }
                        else {
                            players[i].reduceTurnsInJail();
                        }
                    }
                }
            }
            else {
                for(int i = turnIndex; i < players.length; i++) {
                    if(players[i].getTurnsInJail() == 0) {
                        turn = players[i].getSymbol();
                        return;
                    }
                    else {
                        if(players[i].getHasJailFreeCard()) {
                            players[i].freeFromJail();
                            turn = players[i].getSymbol();
                            return;
                        }
                        else {
                            players[i].reduceTurnsInJail();
                        }
                    }
                }
                for(int i = 0; i < turnIndex; i++) {
                    if(players[i].getTurnsInJail() == 0) {
                        turn = players[i].getSymbol();
                        return;
                    }
                    else {
                        if(players[i].getHasJailFreeCard()) {
                            players[i].freeFromJail();
                            turn = players[i].getSymbol();
                            return;
                        }
                        else {
                            players[i].reduceTurnsInJail();
                        }
                    }
                }
            }
        }
    }

    public String getCommunityChestCard(Player player, Board board){
        if (indexForCommunityChestCards >= 16){
            indexForCommunityChestCards = indexForCommunityChestCards - 16;
        }

        Card card = board.communityChestDeck[indexForCommunityChestCards];
        card.execute(player,board);

        indexForCommunityChestCards++;

        if (indexForCommunityChestCards >= 16){
            indexForCommunityChestCards = indexForCommunityChestCards - 16;
        }

        return card.getMessage();

    }

    public String getChanceCards(Player player, Board board){
        if (indexForChanceCards >= 16){
            indexForChanceCards = indexForChanceCards - 16;
        }
        Card card = board.chanceDeck[indexForChanceCards];
        card.execute(player, board);

        indexForChanceCards++;

        return card.getMessage();
    }

    public ArrayList<Tile> tilesOwnedByPlayer(Player p){
        ArrayList<Tile> tilesOwned = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
           if( propertiesOwned[i] == p ){
               tilesOwned.add(board[i]);
           }
        }
        return tilesOwned;
    }

    public int getAllAssetsOfPlayer(Player player) {
        int money = 0;

        for(Player p: players) {
            if(p == player) {
                for(Tile tile: tilesOwnedByPlayer(p)) {
                    money += tile.getPrice();
                }
            }
        }
        return money + player.getMoney();
    }

    public void payForUtility(int sum, Player p) {

        int position = p.getPosition();
        Tile t = getBoardTile()[position];

        boolean hasBoth = numberOfUtilitiesOwned(getPropertyOwner(position)) == 2;
        int rent = ((Utility) t).calculateRent(sum, hasBoth);
        p.changeMoney(-rent);
        getPropertyOwner(position).changeMoney(rent);
    }
}

