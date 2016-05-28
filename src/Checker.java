public class Checker {
    public boolean isQueen;
    public boolean isBlack;

    public Checker(boolean isBlack) {
        this.isBlack = isBlack;
        isQueen = false;
    }

    public void doQueen(){
        isQueen = true;
    }
}
