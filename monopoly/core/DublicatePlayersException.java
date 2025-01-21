package monopoly.core;

public class DublicatePlayersException extends Exception{

    public DublicatePlayersException() {
        super("You cannot choose already chosen Symbol");
    }
    public DublicatePlayersException(String message) {
        super(message);
    }
}
