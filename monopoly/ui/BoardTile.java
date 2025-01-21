package monopoly.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import monopoly.core.*;

public class BoardTile extends JPanel{
    public static final int WIDTH = 30;
    public static final int HEIGHT = 30;
    private BufferedImage image;

    private ArrayList<Player> playersOnTheSquare = new ArrayList<>();
    private int position;

    public BoardTile(Tile template, int i, int j) {
        super(new GridLayout(3 ,3));
        String path = null;

        if(template != null) {
            path = "src/images/" + template.getName() +".png";
        }
        else if (i == 5 && j > 1 && j < 9){
            path = "src/images/Monopoly" + j + ".jpg";
        }

        if(path != null) {
            try{

                image = ImageIO.read(new File(path));
            }
            catch(IOException e) {
                System.out.println(path);
                System.out.println("IO excpetion");
            }
        }

        if(template == null) {
            this.position = -1;
            setBackground(new Color(208,233,218));

        }
        else {
            this.position = template.getPosition();
        }

        setPreferredSize(new Dimension(WIDTH,HEIGHT));

    }

    public void addPlayers(Player p) {
        playersOnTheSquare.add(p);
    }

    public void emptyPlayers() {
        playersOnTheSquare.clear();
        this.removeAll();
        this.revalidate();
        repaint();
    }

    public void updateIcons() {
        for(Player p: playersOnTheSquare) {
            ImageIcon imageIcon = new ImageIcon("src/images/" + p.getSymbol().toString() + ".png");
            Image newImg = imageIcon.getImage().getScaledInstance(22, 22,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
            imageIcon = new ImageIcon(newImg);

            this.add(new JLabel(imageIcon));
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null){

            g.drawImage(image,0,0, getWidth(), getHeight(),null);
        }
    }

    public int getPosition() {
        return position;
    }
}
