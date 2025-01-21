package monopoly.cli;

import monopoly.core.*;

import java.util.Scanner;
public class Console {
    private Board board;

    public void play() {
        boolean gameHasEnded = false;
        Scanner sc = new Scanner(System.in);
        initializePlayers();

        while (!gameHasEnded) {
            for (int i = 0; i < board.getPlayers().length; i++) {
                Player p =  board.getPlayers()[i];
                print();
                System.out.println("Type 'roll' to roll the dice: ");
                String command = sc.nextLine();
                while (!command.equals("roll")) {
                    System.out.println("Type 'roll' to roll the dice: ");
                    command = sc.nextLine();
                }
                int[] dice = Dice.rollDice();

                System.out.println("Player " + (i + 1) + " rolled dice: " + dice[0] + " " + dice[1]);
                p.moveSpaces(dice[0] + dice[1]);
                print();
                performMove(p);

                System.out.println("Type 'end' to end your turn: ");
                command = sc.nextLine();
                while (!command.equals("end")) {
                    System.out.println("Type 'end' to end your turn: ");
                    command = sc.nextLine();
                }
            }

        }
    }

    private void print() {
        printAssets();
        System.out.println();
        for(int i = 0; i < Board.BOARD_SIZE; i++ ){
            Tile currentTile = board.getBoardTile()[i];

            System.out.print(currentTile.toString() + " ");
            for(Player p: board.getPlayers()) {
                if (i == p.getPosition()) {
                    System.out.print("\u001B[35m" + p + " \u001B[0m");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    private void initializePlayers() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter number of players: ");
        int numberOfPlayers = sc.nextInt();
        while (!validNumberOfPlayers(numberOfPlayers)) {
            System.out.println("Game needs 2-8 players");
            numberOfPlayers = sc.nextInt();
        }

        board = new Board(numberOfPlayers);

        System.out.println("Choose your characters from these options: ");

        for (int i = 0; i < Player.Symbol.values().length; i++) {
            System.out.println(i+1 + ": " + Player.Symbol.values()[i]);
        }
        for (int i = 1; i <= numberOfPlayers; i++) {
            System.out.println("Player " + i + " choose your symbol: ");
            boolean validChoice = false;
            while (!validChoice) {
                try {
                    int chosenSymbol = sc.nextInt() - 1;
                    checkSymbol(chosenSymbol);
                    board.addPlayer(Player.Symbol.values()[chosenSymbol]);
                    validChoice = true;
                } catch (DublicatePlayersException e) {
                    System.out.println(e.getMessage());
                } catch (InvalidSymbolException e) {
                    System.out.println(e.getMessage());
                }
            }

        }

    }
    
    private void printAssets() {
        System.out.println();
        for (int i = 0; i < board.getPlayers().length; i++) {
            Player player = board.getPlayers()[i];
            System.out.println("Player " + (i+1) + " has " + board.getPlayers()[i].getMoney() + " money and ownes: ");
            for(Tile tile: board.tilesOwnedByPlayer(player)) {
                System.out.println(tile);
            }
            System.out.println();
        }
    }

    private boolean validNumberOfPlayers(int number) {
        return (number >= 2 && number <= Player.Symbol.values().length);
    }

    private void checkSymbol(int n) throws InvalidSymbolException {
        if (n < 0 || n >= Player.Symbol.values().length){
            throw new InvalidSymbolException();
        }
    }

    private void performMove(Player p){
       Board.Description description = board.getDescription(p);
       int position = p.getPosition();
       Tile t = board.getBoardTile()[position];
       int cost;

       Scanner sc = new Scanner(System.in);

       switch (description){
           case NOT_OWNED_PROPERTY :
               System.out.println("Would you like to buy the property " + t);
               System.out.println("Type 'yes' or 'no'");
               board.buyTheProperty(p, sc.nextLine());
               break;

           case MY_PROPERTY:
               System.out.println("You own this property");
               break;

           case OWNED_PROPERTY:
               Property property = (Property) t;
               cost = ((Property) t).calculateRent(board.areAllPropertiesOwned(property.getColor(), board.getPropertyOwner(position)));
               p.changeMoney(-cost);
               board.getPropertyOwner(position).changeMoney(cost);
               System.out.println("You need to pay rent " + cost);
               break;

           case TAX:
               p.changeMoney(-((Tax) t).getTax());
               break;

           case NOT_OWNED_RAILROAD:
               System.out.println("Would you like to buy the railroad " + t);
               System.out.println("Type 'yes' or 'no'");
               board.buyTheProperty(p, sc.nextLine());
               break;

           case MY_RAILROAD:
               System.out.println("You own this railroad");
               break;

           case OWNED_RAILROAD:
               Railroad railroad = (Railroad) t;
               cost = railroad.calculateRent(board.numberOfRailroadsOwned(board.getPropertyOwner(position)));
               p.changeMoney(cost);
               board.getPropertyOwner(position).changeMoney(cost);
               System.out.println("You need to pay rent " + cost);
               break;

           case NOT_OWNED_UTILITY:
               System.out.println("If you want to buy the utility " + t + " enter 'yes' if not 'no'");
               String answer = sc.next();
               board.buyTheProperty(p, answer);
               break;

           case MY_UTILITY:
               System.out.println("You own this utility");
               break;

           case OWNED_UTILITY:
               Utility u = (Utility) t;

               System.out.println("You need to pay rent");
               System.out.println("To do so you have to roll the dice. Enter 'roll' to roll it");
               String command = sc.next();
               while (!command.equals("roll")){
                   System.out.println("You have to roll the dice. Enter 'roll' to roll it");
                   command = sc.next();
               }
               int[] diceRoll =  Dice.rollDice();
               int sumOfDice = diceRoll[0] + diceRoll[1];
               boolean hasBoth = board.numberOfUtilitiesOwned(board.getPropertyOwner(position)) == 2;
               int rent = u.calculateRent(sumOfDice, hasBoth);
               p.changeMoney(-rent);
               board.getPropertyOwner(position).changeMoney(rent);

           case COMMUNITY:
               System.out.println("You get a Community Chest Card");
               System.out.println(board.getCommunityChestCard(p,board));
               break;
           case CHANCE:
               System.out.println("You get a Chance Card");
               System.out.println(board.getChanceCards(p,board));
               break;
       }
    }
