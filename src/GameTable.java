import javax.swing.*;

public class GameTable {
    private Checker[][] table;
    private boolean isBlack;
    /*
    b b b b
    _ _ _ _
    w w w w
     */
    public GameTable( boolean isBlack) {
        this.isBlack = isBlack;
        table = new Checker[8][8];
        for(int y = 0; y < 3; y++){
            for(int x = 0; x < 8; x++){
                if((x+y) % 2 == 1){
                    table[x][y] = new Checker(true);
                }
            }
        }
        for(int y = 5; y < 8; y++){
            for(int x = 0; x < 8; x++){
                if((x+y) % 2 == 1){
                    table[x][y] = new Checker(false);
                }
            }
        }
    }

    synchronized public Checker[][] getTable(){
        synchronized (table) {
            return table.clone();
        }
    }

    public boolean isBlack() {
        return isBlack;
    }

    synchronized public void makeAMove(Message message){
        if(message.getLastPosition().xPos == -1){
            if(message.getLastPosition().yPos == -1){
                JOptionPane.showMessageDialog(null, "Black is win");
            }
            else {
                JOptionPane.showMessageDialog(null, "White is win");
            }
            System.exit(0);
        }
        else {
            Checker checker = table[message.getLastPosition().xPos][message.getLastPosition().yPos];
            table[message.getLastPosition().xPos][message.getLastPosition().yPos] = null;
            table[message.getNextPosition().xPos][message.getNextPosition().yPos] = checker;
            if (message.getDiePosition() != null) {
                table[message.getDiePosition().xPos][message.getDiePosition().yPos] = null;
            }
            for (int x = 0; x < 8; x++) {
                if (table[x][0] != null) {
                    if (!table[x][0].isBlack) {
                        table[x][0].doQueen();
                    }
                }
                if (table[x][7] != null) {
                    if (table[x][7].isBlack) {
                        table[x][7].doQueen();
                    }
                }
            }
        }
    }


}
