public class InfoHolger {
    public Pos way;
    public Pos eat;

    public InfoHolger(Pos way, Pos eat) {
        this.way = way;
        this.eat = eat;
    }

    public InfoHolger(Pos way) {
        this.way = way;
    }

    @Override
    public int hashCode() {
        return way.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj.getClass() == this.getClass()){
            InfoHolger temp = (InfoHolger) obj;
            return way.equals(temp.way);
        }
        else
            return false;
    }
}
