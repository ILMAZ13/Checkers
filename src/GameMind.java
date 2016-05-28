import java.util.*;

public class GameMind {
    private GameTable gameTable;
    private Checker[][] table;
    private Map<Pos, Set<InfoHolger>> possibleWays;
    private Map<Pos, Set<Pos>> possibleEats;
    private boolean mustEat;

    public GameMind(GameTable gameTable) {
        this.gameTable = gameTable;
    }

    public boolean canChoose(Pos pos){
        table = gameTable.getTable();
        if(table[pos.xPos][pos.yPos] != null){
            return table[pos.xPos][pos.yPos].isBlack == gameTable.isBlack();
        }
        else
            return false;
    }

    synchronized private void addPossibleWays(Pos pos){
        if(table[pos.xPos][pos.yPos] == null){
            return;
        }
        if(!table[pos.xPos][pos.yPos].isQueen) {
            if(!gameTable.isBlack()){
                try {//--
                    if (table[pos.xPos - 1][pos.yPos - 1] == null) {
                        possibleWays.get(pos).add(new InfoHolger(new Pos(pos.xPos - 1, pos.yPos - 1)));
                    }
                } catch (ArrayIndexOutOfBoundsException e) {}

                try {//+-
                    if (table[pos.xPos + 1][pos.yPos - 1] == null) {
                        possibleWays.get(pos).add(new InfoHolger(new Pos(pos.xPos + 1, pos.yPos - 1)));
                    }
                } catch (ArrayIndexOutOfBoundsException e) {}
            }
            else {
                try {//++
                    if (table[pos.xPos + 1][pos.yPos + 1] == null) {
                        possibleWays.get(pos).add(new InfoHolger(new Pos(pos.xPos + 1, pos.yPos + 1)));
                    }
                } catch (ArrayIndexOutOfBoundsException e) {}

                try {//-+
                    if (table[pos.xPos - 1][pos.yPos + 1] == null) {
                        possibleWays.get(pos).add(new InfoHolger(new Pos(pos.xPos - 1, pos.yPos + 1)));
                    }
                } catch (ArrayIndexOutOfBoundsException e) {}
            }
        }
        else {
            int x = pos.xPos;
            int y = pos.yPos;
            while (x >= 0 && x < 8 && y >= 0&& y < 8){
                x++;
                y++;
                try {
                    if (table[x][y] == null) {
                        possibleWays.get(pos).add(new InfoHolger(new Pos(x, y)));
                    } else
                        break;
                }
                catch (ArrayIndexOutOfBoundsException e){}
            }
            x = pos.xPos;
            y = pos.yPos;
            while (x >= 0 && x < 8 && y >= 0&& y < 8){
                x--;
                y++;
                try {
                    if (table[x][y] == null) {
                        possibleWays.get(pos).add(new InfoHolger(new Pos(x, y)));
                    } else
                        break;
                }
                catch (ArrayIndexOutOfBoundsException e){}
            }
            x = pos.xPos;
            y = pos.yPos;
            while (x >= 0 && x < 8 && y >= 0&& y < 8){
                x++;
                y--;
                try {
                    if (table[x][y] == null) {
                        possibleWays.get(pos).add(new InfoHolger(new Pos(x, y)));
                    } else
                        break;
                }
                catch (ArrayIndexOutOfBoundsException e){}
            }
            x = pos.xPos;
            y = pos.yPos;
            while (x >= 0 && x < 8 && y >= 0&& y < 8){
                x--;
                y--;
                try {
                    if (table[x][y] == null) {
                        possibleWays.get(pos).add(new InfoHolger(new Pos(x, y)));
                    } else
                        break;
                }
                catch (ArrayIndexOutOfBoundsException e){}
            }
        }
    }

    synchronized private void testForEats(Pos pos){
        possibleEats.put(pos, Collections.synchronizedSet(new HashSet<>()));
        possibleWays.put(pos, Collections.synchronizedSet(new HashSet<>()));
        if(table[pos.xPos][pos.yPos] == null){
            return;
        }
        if(!table[pos.xPos][pos.yPos].isQueen){
            try{//--
                if((table[pos.xPos-1][pos.yPos-1].isBlack != gameTable.isBlack()) && (table[pos.xPos-2][pos.yPos-2] == null)){
                    possibleWays.get(pos).add(new InfoHolger(new Pos(pos.xPos-2,pos.yPos-2), new Pos(pos.xPos-1,pos.yPos-1)));
                    possibleEats.get(pos).add(new Pos(pos.xPos-1,pos.yPos-1));
                }
            } catch (ArrayIndexOutOfBoundsException e){}
            catch (NullPointerException e1){}

            try{//+-
                if((table[pos.xPos+1][pos.yPos-1].isBlack != gameTable.isBlack()) && (table[pos.xPos+2][pos.yPos-2] == null)){
                    possibleWays.get(pos).add(new InfoHolger(new Pos(pos.xPos+2,pos.yPos-2), new Pos(pos.xPos+1,pos.yPos-1)));
                    possibleEats.get(pos).add(new Pos(pos.xPos+1,pos.yPos-1));              }
            } catch (ArrayIndexOutOfBoundsException e){}
            catch (NullPointerException e1){}

            try{//-+
                if((table[pos.xPos-1][pos.yPos+1].isBlack != gameTable.isBlack()) && (table[pos.xPos-2][pos.yPos+2] == null)){
                    possibleWays.get(pos).add(new InfoHolger(new Pos(pos.xPos-2,pos.yPos+2), new Pos(pos.xPos-1,pos.yPos+1)));
                    possibleEats.get(pos).add(new Pos(pos.xPos-1,pos.yPos+1));              }
            } catch (ArrayIndexOutOfBoundsException e){}
            catch (NullPointerException e1){}

            try{//++
                if((table[pos.xPos+1][pos.yPos+1].isBlack != gameTable.isBlack()) && (table[pos.xPos+2][pos.yPos+2] == null)){
                    possibleWays.get(pos).add(new InfoHolger(new Pos(pos.xPos+2,pos.yPos+2), new Pos(pos.xPos+1,pos.yPos+1)));
                    possibleEats.get(pos).add(new Pos(pos.xPos+1,pos.yPos+1));            }
            } catch (ArrayIndexOutOfBoundsException e){}
            catch (NullPointerException e1){}

        }
        else{
            int x = pos.xPos;
            int y = pos.yPos;
            Pos eatPos = null;
            boolean f = false;
            while(x >= 0 && x < 8 && y >= 0 && y < 8){
                x++;
                y++;
                try {
                    if (table[x][y] != null) {
                        if (table[x][y].isBlack != gameTable.isBlack() && !f) {
                            if (table[x + 1][y + 1] == null) {
                                eatPos = new Pos(x, y);
                                possibleEats.get(pos).add(eatPos);
                                f = true;
                            } else
                                break;
                        } else
                            break;
                    } else if (f) {
                        possibleWays.get(pos).add(new InfoHolger(new Pos(x, y), eatPos));
                    }
                }
                catch (ArrayIndexOutOfBoundsException e){}
            }
            x = pos.xPos;
            y = pos.yPos;
            eatPos = null;
            f = false;
            while(x >= 0 && x < 8 && y >=0 && y < 8){
                x--;
                y--;
                try {
                    if (table[x][y] != null) {
                        if (table[x][y].isBlack != gameTable.isBlack() && !f) {
                                if (table[x - 1][y - 1] == null) {
                                    eatPos = new Pos(x, y);
                                    possibleEats.get(pos).add(eatPos);
                                    f = true;
                                } else
                                    break;
                        } else
                    break;
                } else if (f) {
                    possibleWays.get(pos).add(new InfoHolger(new Pos(x, y), eatPos));
                }
                } catch (ArrayIndexOutOfBoundsException e) {
                }
            }
            x = pos.xPos;
            y = pos.yPos;
            eatPos = null;
            f = false;
            while(x >= 0 && x < 8 && y >=0 && y < 8){
                x--;
                y++;
                try {
                    if (table[x][y] != null) {
                        if (table[x][y].isBlack != gameTable.isBlack() && !f) {
                            if (table[x - 1][y + 1] == null) {
                                eatPos = new Pos(x, y);
                                possibleEats.get(pos).add(eatPos);
                                f = true;
                            } else
                                break;
                        } else
                            break;
                    } else if (f) {
                        possibleWays.get(pos).add(new InfoHolger(new Pos(x, y), eatPos));
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                }
            }
            x = pos.xPos;
            y = pos.yPos;
            eatPos = null;
            f = false;
            while(x >= 0 && x < 8 && y >=0 && y < 8){
                x++;
                y--;
                try {
                    if (table[x][y] != null) {
                        if (table[x][y].isBlack != gameTable.isBlack() && !f) {
                                if (table[x + 1][y - 1] == null) {
                                    eatPos = new Pos(x, y);
                                    possibleEats.get(pos).add(eatPos);
                                    f = true;
                                } else
                                    break;
                        } else
                    break;
                } else if (f) {
                    possibleWays.get(pos).add(new InfoHolger(new Pos(x, y), eatPos));
                }
                } catch (ArrayIndexOutOfBoundsException e) {
                }
            }
        }
    }

    synchronized public void update(){
        mustEat = false;
        table = gameTable.getTable();
        possibleWays = Collections.synchronizedMap(new HashMap<>());
        possibleEats = Collections.synchronizedMap(new HashMap<>());
        for(int x = 0; x < 8; x++){
            for(int y = 0; y < 8; y++){
                if(table[x][y] != null) {
                    if(table[x][y].isBlack == gameTable.isBlack()) {
                        Pos pos = new Pos(x, y);
                        testForEats(pos);
                        addPossibleWays(pos);
                        if (!possibleEats.get(pos).isEmpty()) {
                            mustEat = true;
                        }
                    }
                }
            }
        }
    }

    synchronized public Set<InfoHolger> getPossibleWays(Pos pos) {
        boolean f = possibleWays.containsKey(pos);
        return possibleWays.get(pos);
    }

    synchronized public Set<Pos> getPossibleEats(Pos pos) {
        return possibleEats.get(pos);
    }

    public boolean isMustEat() {
        return mustEat;
    }

    public boolean isGameOver(){
        update();
        boolean flag = true;
        for(int x = 0; x < 8; x++){
            for(int y = 0; y < 8; y++){
                if(table[x][y] != null){
                    if(table[x][y].isBlack == gameTable.isBlack()){
                        if(!possibleWays.get(new Pos(x,y)).isEmpty()){
                            flag = false;
                        }
                    }
                }
            }
        }
        if(flag){
            System.out.println("You LOOSE");
        }
        return flag;
    }

    public GameTable getGameTable(){
        return gameTable;
    }
}
