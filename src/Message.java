import java.io.Serializable;

public class Message implements Serializable {
    static final long serialVersionUID = 42L;
    private Pos lastPosition;
    private Pos nextPosition;
    private Pos diePosition;
    private boolean haveNextWay;
    /*
    last pos -1 1  => white win

    last pos -1 -1 => black win

    last pos 100 100 resend message
     */
    public Message(Pos lastPosition, Pos nextPosition, Pos diePosition, boolean haveNextWay) {
        this.lastPosition = lastPosition;
        this.nextPosition = nextPosition;
        this.diePosition = diePosition;
        this.haveNextWay = haveNextWay;
    }

    public Pos getLastPosition() {
        return lastPosition;
    }

    public Pos getNextPosition() {
        return nextPosition;
    }

    public boolean isHaveNextWay() {
        return haveNextWay;
    }

    public Pos getDiePosition() {
        return diePosition;
    }
}
