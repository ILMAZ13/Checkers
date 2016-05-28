import java.io.Serializable;

public class Pos implements Serializable{
    static final long serialVersionUID = 42L;
    public int xPos;
    public int yPos;

    public Pos(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }

    @Override
    public int hashCode() {
        return xPos * 13 + yPos;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj.getClass() == this.getClass()){
            Pos obj1 = (Pos) obj;
            return obj1.xPos == xPos && obj1.yPos == yPos;
        }
        else
            return false;
    }
}
