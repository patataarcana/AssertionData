import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Round {

    private int sno;
    private int kill, test, heal;
    private Group<Mafia> M;
    private Group<Detective> D;
    private Group<Healer> H;
    private Group<Commoner> C;
    private ArrayList<Integer> in;
    private ArrayList<Player> all;
    private Random rand;
    private Player user;

    public Round(int sno, Player user, ArrayList<Player> all) {
        this.sno = sno;
        this.user = user;
        this.all = all;
        M = new Group<Mafia>();
        D = new Group<Detective>();
        H = new Group<Healer>();
        C = new Group<Commoner>();
        in = new ArrayList<>();
        rand = new Random();
    }

    public void make() {
        System.out.printf("\nRound %d\n", sno);
        for(Player p : all) {
            if (p.getType() == 1) {
                M.add((Mafia) p);
            } else if (p.getType() == 2) {
                D.add((Detective) p);
            } else if (p.getType() == 3) {
                H.add((Healer) p);
            } else {
                C.add((Commoner) p);
            }
            in.add(p.getID());
        }
        Collections.sort(in);
        System.out.printf("%d players are remaining: ", all.size());
        for(int i = 0; i<in.size(); i++){
            if(i != in.size()-1){
                System.out.printf("Player%d, ", in.get(i));
            }
            else{
                System.out.printf("Player%d ", in.get(i));
            }
        }
        System.out.println("are alive.");
    }

    public int start(){
        int inputType = user.getType();
        if(M.size() > 0) {
            if ( user.isAlive())
                kill = M.chooser(inputType, in);
            else
                kill = M.chooser(0, in);
        }
        else{
            kill = -1;
            System.out.println("Mafias have chosen their target.");
        }
        if(D.size() > 0) {
            if (user.isAlive())
                test = D.chooser(inputType, in);
            else
                test = D.chooser(0, in);
        }
        else{
            test = -1;
            System.out.println("Detectives have chosen a player to test.");
        }
        if(H.size() > 0) {
            if (user.isAlive())
                heal = H.chooser(inputType, in);
            else
                heal = H.chooser(0, in);
        }
        else{
            heal = -1;
            System.out.println("Healers have chosen someone to heal.");
        }
        //-----------------------------UNCOMMENT TO CHECK------------------------------------------------------------------------------------
//        System.out.printf("Kill %d Test %d Heal %d\n", kill, test, heal);
        System.out.println("--End of Actions--");
        boolean voteOut = doCalc();
        int result = gameOver();
        if(result == -1) {
            if (!voteOut) {
                return voting();
            }
        }
        return result;
    }

    private boolean doCalc(){
        Player target;
        //target will always be there
        if(D.checkContains(kill) != -1) {
            target = D.get(D.checkContains(kill));
        }
        else if(H.checkContains(kill) != -1) {
            target = H.get(H.checkContains(kill));
        }
        else {
            target = C.get(C.checkContains(kill));
        }
        double mafiaSum = 0;
        int hp0 = 0;
        for(int i = 0; i<M.size(); i++){
            mafiaSum += M.get(i).getHP();
            if(M.get(i).getHP() > 0){
                hp0++;
            }
        }
        double xy = target.getHP()/(double)hp0;
//        System.out.println("xy " + xy);
        PlayerHPComparator hpc = new PlayerHPComparator();
        Player p0 = new Commoner(0,4);
        Player p1 = new Commoner(mafiaSum, 4);
        Player p2 = new Commoner(xy, 4);
        if(hpc.compare(target, p1) < 0){
            target.setHP(0);
        }
        else{
            target.setHP(target.getHP() - mafiaSum);
        }
        for(int i = 0; i<M.size(); i++){
            if(hpc.compare(M.get(i), p2) <= 0){
                mafiaSum -= M.get(i).getHP();
                M.get(i).setHP(0);
                hp0--;
            }
        }
        if(hp0 != M.size()) {
            for (int i = 0; i < M.size(); i++) {
                if (hpc.compare(M.get(i), p0) > 0)
                    M.get(i).setHP(M.get(i).getHP() - mafiaSum / (double) hp0);
            }
        }
        else {
            for (int i = 0; i < M.size(); i++) {
                M.get(i).setHP(M.get(i).getHP() - xy);
            }
        }
        if(heal != -1) {
            if (D.checkContains(heal) != -1) {
                D.get(D.checkContains(heal)).setHP(D.get(D.checkContains(heal)).getHP() + 500);
            } else if (H.checkContains(heal) != -1) {
                H.get(H.checkContains(heal)).setHP(H.get(H.checkContains(heal)).getHP() + 500);
            } else if (M.checkContains(heal) != -1) {
                M.get(M.checkContains(heal)).setHP(M.get(M.checkContains(heal)).getHP() + 500);
            } else {
                C.get(C.checkContains(heal)).setHP(C.get(C.checkContains(heal)).getHP() + 500);
            }
        }
        Player p3 = new Mafia();
        boolean caught = false;
        if(test != -1) {
            if (M.checkContains(test) != -1) {
                if (M.get(M.checkContains(test)).equals(p3)) {
                    M.get(M.checkContains(test)).setAlive(false);
                    M.get(M.checkContains(test)).setHP(0);
                    in.remove(new Integer(M.get(M.checkContains(test)).getID()));
                    if(user.getID() == M.get(M.checkContains(test)).getID())
                        System.out.printf("You have been voted out.\n");
                    else
                        System.out.printf("Player%d has been voted out.\n", M.get(M.checkContains(test)).getID());
                    caught = true;
                }
            }
            //-------------------------------------------UNCOMMENT TO CHECK-------------------------------------------------------------------
//            System.out.println("Caught? " + caught);
        }
        boolean ded = false;
        for(int i = 0; i< all.size(); i++){
            //-------------------------------------------UNCOMMENT TO CHECK-------------------------------------------------------------------
//            System.out.println(all.get(i));
            if(all.get(i).getHP() == 0 && all.get(i).getType()!=1){
                all.get(i).setAlive(false);
                if(user.getID() == all.get(i).getID()){
                    System.out.printf("You died.\n");
                    in.remove(new Integer(all.get(i).getID()));
                }
                else {
                    System.out.printf("Player%d died.\n", all.get(i).getID());
                    in.remove(new Integer(all.get(i).getID()));
                }
                ded = true;
            }
        }
        if(!ded){
            System.out.println("No one died.");
        }
        return caught;
    }

    public int gameOver(){
        M.removeDead();
        D.removeDead();
        H.removeDead();
        C.removeDead();
        if(M.size() == (D.size() + H.size() + C.size())){
            return 0;
            //mafia win
        }
        else if(M.size() == 0){
            return -98;
            //mafia lose
        }
        else{
            return -1;
            //game continues
        }
    }

    public int voting(){
        int myVote;
        while(user.isAlive()) {
            System.out.print("Select a person to vote out: ");
            myVote = Main.takeIntInput();
            if(in.contains(myVote)) {
                break;
            }
            else{
                System.out.printf("Player%d is not in the Game.\n", myVote);
            }
        }
        int otherVote;
        otherVote = in.get(rand.nextInt(in.size()));
        return otherVote;
    }

}
