package monopoly.core;

public class Dice {
    public static int[] rollDice() {
        int a = (int) (Math.random()*6 + 1);
        int b = (int) (Math.random()*6 + 1);
        return new int[]{a, b};
    }
}
