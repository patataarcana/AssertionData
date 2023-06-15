import java.util.Comparator;

public class PlayerHPComparator implements Comparator<Player> {
    @Override
    public int compare(Player o1, Player o2) {
        return (int)o1.getHP() - (int)o2.getHP();
    }
}
