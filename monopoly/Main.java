package monopoly;

import monopoly.ui.MonopolyUI;
import monopoly.cli.Console;

public class Main {
    public static void main(String[] args) {
        if(args.length == 0) {
            MonopolyUI ui = new MonopolyUI();
        }
        else if(args[0].equals("-console")) {
            Console c = new Console();
            c.play();
        }
    }
}
