package monopoly.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import monopoly.core.*;


public class MonopolyUI extends JFrame {

    public static final int HEIGHT = 800;
    public static final int WIDTH = HEIGHT + 400;

    private Board board;
    BoardTile[][] tilesUI = new BoardTile[11][11];
    private int numberOfPlayers;

    public MonopolyUI() {
        super("Monopoly");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);


        JPanel mainPanel = new JPanel( );
        mainPanel.setLayout(new BorderLayout());

        JPanel monopolyBoard = new JPanel( );
        monopolyBoard.setLayout(new BorderLayout());
        monopolyBoard.setBackground(Color.WHITE);
        monopolyBoard.setPreferredSize((new Dimension(HEIGHT,HEIGHT)));

        mainPanel.add(monopolyBoard);

        JPanel assets = new JPanel( );
        assets.setLayout(new BorderLayout());
        assets.setBackground(Color.WHITE);
        assets.setPreferredSize((new Dimension(400,HEIGHT)));

        JTextArea assetsDisplay = new JTextArea();
        assetsDisplay.setFont(assetsDisplay.getFont().deriveFont(Font.BOLD));
        assets.add(assetsDisplay, BorderLayout.CENTER);

        JPanel textPanel = new JPanel(new FlowLayout());
        JPanel characterButtonPanel = new JPanel(new GridLayout(Player.Symbol.values().length / 2, 2));
        JPanel numberOfPlayersPanel = new JPanel(new GridLayout(2,1));
        JPanel characterChoicePanel = new JPanel(new GridLayout(2,1));

        JFrame gameEndedWindow = new JFrame("Thank you!");
        gameEndedWindow.setSize(300,300);
        gameEndedWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameEndedWindow.setLocationRelativeTo(null);

        JPanel finalResults = new JPanel(new GridLayout(2, 1));

        JTextArea finalAssetsDisplay = new JTextArea();
        JLabel finalWinner = new JLabel();

        finalResults.add(finalAssetsDisplay);
        finalResults.add(finalWinner);

        gameEndedWindow.add(finalResults);

        JPanel southPanel = new JPanel(new BorderLayout());

        JPanel currentActionsPanel = new JPanel(new GridLayout(3,1));

        JLabel turnDisplay = new JLabel();
        JTextArea message = new JTextArea();
        JPanel yesAndNoPanel = new JPanel(new FlowLayout());

        JButton yesButton = new JButton("yes");
        JButton noButton = new JButton("no");

        JButton endTurnButton = new JButton("End turn");
        JButton rollDiceButton = new JButton("Roll the dice!");

        yesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Player p = getPlayerBySymbol(board.getTurn());
                message.setText("");
                currentActionsPanel.remove(yesAndNoPanel);
                currentActionsPanel.add(endTurnButton);

                board.buyTheProperty(p, yesButton.getText());

            }
        });
        noButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Player p = getPlayerBySymbol(board.getTurn());
                message.setText("");
                currentActionsPanel.remove(yesAndNoPanel);
                currentActionsPanel.add(endTurnButton);
            }
        });

        yesAndNoPanel.add(yesButton);
        yesAndNoPanel.add(noButton);

        endTurnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                message.setText("Roll the dice!");
                updateTurn(turnDisplay);
                updateAssets(assetsDisplay);
                updateBoard();
                currentActionsPanel.remove(endTurnButton);
                currentActionsPanel.add(rollDiceButton);
            }
        });

        rollDiceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] diceRoll = Dice.rollDice();
                message.setText("You got: " + diceRoll[0] + " " + diceRoll[1]);
                Player p = getPlayerBySymbol(board.getTurn());
                p.moveSpaces(diceRoll[0] + diceRoll[1]);
                updateBoard();

                performMove(p, currentActionsPanel,
                        message, rollDiceButton, yesAndNoPanel, yesButton, noButton, endTurnButton, rollDiceButton);


            }
        });

        JButton rollDiceForUtility = new JButton("Roll");
        rollDiceForUtility.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                message.setText("You got: " + Dice.rollDice()[0] + " " + Dice.rollDice()[1]);
                int[] diceRoll = Dice.rollDice();
                int sumOfDice = diceRoll[0] + diceRoll[1];
                Player p = getPlayerBySymbol(board.getTurn());


                board.payForUtility(sumOfDice, p);

                currentActionsPanel.remove(rollDiceForUtility);
                currentActionsPanel.add(endTurnButton);
            }
        });


        JButton endGameButton = new JButton("Click to end the game");
        endGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                finalAssets(finalAssetsDisplay);
                showWinner(finalWinner);

                gameEndedWindow.setVisible(true);
                MonopolyUI.this.setVisible(false);

            }
        });

        currentActionsPanel.add(turnDisplay);
        currentActionsPanel.add(message);
        currentActionsPanel.add(rollDiceButton);

        southPanel.add(currentActionsPanel);
        assets.add(endGameButton, BorderLayout.NORTH);

        assets.add(southPanel, BorderLayout.SOUTH);

        JFrame numberOfPlayersWindow = new JFrame("Number of Players");
        numberOfPlayersWindow.setSize(300,150);
        numberOfPlayersWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        numberOfPlayersWindow.setLocationRelativeTo(null);

        JFrame characterChoiceWindow = new JFrame("Choose your characters");
        characterChoiceWindow.setSize(300,300);
        characterChoiceWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        characterChoiceWindow.setLocationRelativeTo(null);

        JLabel initialMessage = new JLabel("Welcome! How many players will play?");
        initialMessage.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < Player.Symbol.values().length; i++) {
            JButton characterButton = new JButton(Player.Symbol.values()[i].toString());
            characterButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String s = e.getActionCommand();
                    JButton thisButton = (JButton) e.getSource();

                    try {
                        board.addPlayer(Player.Symbol.valueOf(s));
                        numberOfPlayers++;
                        initialMessage.setText("Player " + (numberOfPlayers + 1) + " choose your symbol.");
                        thisButton.setBackground(Color.RED);

                        if(numberOfPlayers >= board.getPlayers().length) {
                            int gridDimension = ((board.getBoardSize() - 4) / 4) + 2;

                            Tile[][] reshapedBoard = reshapeBoardArray(board.getBoardTile());
                            JPanel boardContainer = new JPanel(new GridLayout(gridDimension, gridDimension, 0, 0));

                            for (int i = 0; i < gridDimension; i++) {
                                for (int j = 0; j < gridDimension; j++) {
                                    BoardTile tile = new BoardTile(reshapedBoard[i][j], i, j);
                                    tilesUI[i][j] = tile;
                                    boardContainer.add(tile);
                                }
                            }

                            monopolyBoard.add(boardContainer);
                            turnDisplay.setText("It's " + board.getTurn() + "'s turn");

                            updateAssets(assetsDisplay);

                            characterChoiceWindow.setVisible(false);
                            MonopolyUI.this.setVisible(true);

                            updateBoard();

                        }
                    }
                    catch(DublicatePlayersException exception) {
                        initialMessage.setText("You can't choose the same character");
                    }
                }
            });
            characterButtonPanel.add(characterButton);
        }

        JTextField field = new JTextField(15);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    numberOfPlayers = Integer.parseInt(field.getText());

                    if(!validNumberOfPlayers(numberOfPlayers)) {
                        initialMessage.setText("Number of players should be 2 to 8");
                        return;
                    }
                    board = new Board(numberOfPlayers);
                    numberOfPlayers = 0;

                    characterChoicePanel.add(initialMessage);
                    characterChoicePanel.add(characterButtonPanel);

                    characterChoiceWindow.setVisible(true);
                    numberOfPlayersWindow.setVisible(false);

                    initialMessage.setText("Player " + (numberOfPlayers + 1) + " choose your symbol.");


                }
                catch (NumberFormatException exception) {
                    initialMessage.setText("Invalid input");
                }

            }
        });
        textPanel.add(field);
        textPanel.add(submitButton);


        numberOfPlayersPanel.add(initialMessage);
        numberOfPlayersPanel.add(textPanel);


        numberOfPlayersWindow.add(numberOfPlayersPanel);
        characterChoiceWindow.add(characterChoicePanel);


        mainPanel.add(assets, BorderLayout.EAST);

        this.add(mainPanel);

        numberOfPlayersWindow.setVisible(true);


    }

    private void performMove(Player p, JPanel currentActionsPanel, JTextArea message, JButton rollDiceButton, JPanel yesAndNoPanel, JButton yesButton, JButton noButton, JButton endTurnButton, JButton rollDiceForUtility) {

        Board.Description description = board.getDescription(p);

        System.out.println(description);
        int position = p.getPosition();
        Tile t = board.getBoardTile()[position];
        int cost;

        if(description == null) {
            currentActionsPanel.remove(rollDiceButton);
            currentActionsPanel.add(endTurnButton);
            return;
        }

        switch (description) {
            case GO_TO_JAIL:
                p.goToJail(board);
                message.setText(message.getText() + "\nYou go to jail");
                currentActionsPanel.remove(rollDiceButton);
                currentActionsPanel.add(endTurnButton);
                break;

            case NOT_OWNED_PROPERTY:
                message.setText(message.getText() + "\nWould you like to buy the property " + t);
                currentActionsPanel.remove(rollDiceButton);
                currentActionsPanel.add(yesAndNoPanel);
                break;

            case MY_PROPERTY:
                message.setText(message.getText() + "\nYou own this property");
                currentActionsPanel.remove(rollDiceButton);
                currentActionsPanel.add(endTurnButton);
                break;

            case OWNED_PROPERTY:
                Property property = (Property) t;
                cost = property.calculateRent(board.areAllPropertiesOwned(property.getColor(), board.getPropertyOwner(position)));
                p.changeMoney(-cost);
                board.getPropertyOwner(position).changeMoney(cost);
                message.setText(message.getText() + "\nYou need to pay rent " + cost);

                currentActionsPanel.remove(rollDiceButton);
                currentActionsPanel.add(endTurnButton);
                break;

            case TAX:
                message.setText(message.getText() + "\nYou need to pay tax " + ((Tax) t).getTax());
                p.changeMoney(-((Tax) t).getTax());
                currentActionsPanel.remove(rollDiceButton);
                currentActionsPanel.add(endTurnButton);
                break;

            case NOT_OWNED_RAILROAD:
                message.setText(message.getText() + "\nWould you like to buy the railroad " + t);
                currentActionsPanel.remove(rollDiceButton);
                currentActionsPanel.add(yesAndNoPanel);
                break;

            case MY_RAILROAD:
                message.setText(message.getText() + "\nYou own this railroad");
                currentActionsPanel.remove(rollDiceButton);
                currentActionsPanel.add(endTurnButton);
                break;

            case OWNED_RAILROAD:
                Railroad railroad = (Railroad) t;
                cost = railroad.calculateRent(board.numberOfRailroadsOwned(board.getPropertyOwner(position)));
                p.changeMoney(cost);
                board.getPropertyOwner(position).changeMoney(cost);
                message.setText(message.getText() + "\nYou need to pay rent " + cost);
                currentActionsPanel.remove(rollDiceButton);
                currentActionsPanel.add(endTurnButton);

                break;

            case NOT_OWNED_UTILITY:
                message.setText(message.getText() + "\nIf you want to buy the utility " + t);
                currentActionsPanel.remove(rollDiceButton);
                currentActionsPanel.add(yesAndNoPanel);
                break;

            case MY_UTILITY:
                message.setText(message.getText() + "\nYou own this utility");
                currentActionsPanel.remove(rollDiceButton);
                currentActionsPanel.add(endTurnButton);
                break;

            case OWNED_UTILITY:
                Utility u = (Utility) t;

                message.setText(message.getText() + "\nYou need to pay rent. " + " \nTo do so you have to roll the dice.");
                currentActionsPanel.remove(rollDiceButton);
                currentActionsPanel.add(rollDiceForUtility);
                break;

            case COMMUNITY:
                message.setText(message.getText() + "\nYou get a Community Chest Card");
                message.setText(message.getText() + "\n" + board.getCommunityChestCard(p, board));
                currentActionsPanel.remove(rollDiceButton);
                currentActionsPanel.add(endTurnButton);
                break;
            case CHANCE:
                message.setText(message.getText() + "\nYou get a Chance Card");
                message.setText(message.getText() + "\n" + board.getChanceCards(p, board));
                currentActionsPanel.remove(rollDiceButton);
                currentActionsPanel.add(endTurnButton);
                break;

        }


    }

    private void updateBoard() {
        emptyAllPlayersOnBoards();
        updatePlayers();
        changeBoardTileIcons();
    }

    private void updatePlayers() {
        for(Player p: board.getPlayers()) {
            getBoardTileByPosition(p.getPosition()).addPlayers(p);
        }

    }

    private void emptyAllPlayersOnBoards() {
        for (int i = 0; i < tilesUI.length; i++) {
            for (int j = 0; j < tilesUI[0].length; j++) {
                tilesUI[i][j].emptyPlayers();
            }
        }
    }

    private void changeBoardTileIcons() {
        for (int i = 0; i < tilesUI.length; i++) {
            for (int j = 0; j < tilesUI[0].length; j++) {
                tilesUI[i][j].updateIcons();
            }
        }
    }


    private BoardTile getBoardTileByPosition(int position) {
        for (int i = 0; i < tilesUI.length; i++) {
            for (int j = 0; j < tilesUI[0].length; j++) {
                if(position == tilesUI[i][j].getPosition()) {
                    return tilesUI[i][j];
                }
            }
        }
        return null;
    }

    private Player getPlayerBySymbol(Player.Symbol symbol) {
        for(Player p: board.getPlayers()) {
            if(p.getSymbol() == symbol) {
                return p;
            }
        }
        return null;
    }


    private boolean validNumberOfPlayers(int number) {
        return (number >= 2 && number <= Player.Symbol.values().length);
    }

    private static Tile[][] reshapeBoardArray(Tile[] board) {
        int dimension = ((board.length - 4) / 4) + 2;
        Tile[][] reshaped = new Tile[dimension][dimension];

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                reshaped[i][j] = null;
            }
        }

        int i = dimension - 1;
        int j = dimension - 1;
        int k = 0;
        while (j >= 0) {
            reshaped[i][j] = board[k];
            j--;
            k++;
        }
        j++;
        k--;
        while (i >= 0) {
            reshaped[i][j] = board[k];
            i--;
            k++;
        }
        i++;
        k--;
        while (j < dimension) {
            reshaped[i][j] = board[k];
            j++;
            k++;
        }
        j--;
        k--;
        while (i < dimension - 1) {
            reshaped[i][j] = board[k];
            i++;
            k++;
        }
        return reshaped;
    }

    private void updateTurn(JLabel turnDisplay) {
        board.changeTurn();
        turnDisplay.setText("It's " + board.getTurn() + "'s turn");

    }

    private void updateAssets(JTextArea assetsDisplay) {
        String s = "";
        for (int i = 0; i < board.getPlayers().length; i++) {
            Player player = board.getPlayers()[i];
            s = s + "Player " + (i+1) + " (" + player + ") has " + board.getPlayers()[i].getMoney() + " money and ownes: \n";
            for(Tile tile: board.tilesOwnedByPlayer(player)) {
                s = s + tile + "\n";

            }
            s = s + " \n";
        }

        assetsDisplay.setText(s);

    }


    private void finalAssets(JTextArea assetsDisplay) {
        String s = "";
        for (int i = 0; i < board.getPlayers().length; i++) {
            Player player = board.getPlayers()[i];
            s = s + "Player " + (i+1) + " (" + player + ") has " + board.getAllAssetsOfPlayer(player) + ". \n";
        }

        assetsDisplay.setText(s);
    }

    private void showWinner(JLabel label) {
        ArrayList<Player> winners = new ArrayList<>();
        int max = 0;
        String s = "Winner(s) is ";

        for (int i = 0; i < board.getPlayers().length; i++) {
            Player player = board.getPlayers()[i];
            if (board.getAllAssetsOfPlayer(player) > max) {
                winners.clear();
                winners.add(player);
                max = board.getAllAssetsOfPlayer(player);
            }
            else if(board.getAllAssetsOfPlayer(player) == max) {
                winners.add(player);
            }
        }

        for(Player p: winners) {
            s += p.toString() + " ";
        }

        label.setText(s + "!!!");
    }
}

