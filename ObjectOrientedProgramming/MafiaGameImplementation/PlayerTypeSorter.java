import java.util.Comparator;

public class PlayerTypeSorter implements Comparator<Player>  {

    @Override
    public int compare(Player o1, Player o2) {
        return o1.getType() - o2.getType();
    }
}
