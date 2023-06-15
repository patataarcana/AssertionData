import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class Game {

    private int numPlayers;
    private int mafia;
    private int detective;
    private int healer;
    private int commoner;
    private int userID;
    private Player user;
    private ArrayList<Player> others;
    private ArrayList<Player> allPlayers;
    private HashMap<Integer, Player> pMap;
    private int rounds;
    Round r;
    private static Random rand = new Random();

    public Game(int n){
        numPlayers = n;
        mafia = n/5;
        detective = n/5;
        healer = Math.max(1,n/10);
        commoner = numPlayers - (mafia + detective + healer);
        others = new ArrayList<>();
        allPlayers = new ArrayList<>();
        pMap = new HashMap<>();
        userID = 0;
        rounds = 0;
    }

    public void printMenu(){
        System.out.printf("Choose a Character\n1) Mafia\n2) Detective\n3) Healer\n4) Commoner\n5) Assign Randomly\n");
    }

    public void playGame(){
        while(playRound(++rounds)){
        }
        printResults();
    }

    public void makeGame(int option){
        assignRole(option);
        if(userID == 0)
            userID = 1 + rand.nextInt(numPlayers);
        for(int i = 0; i<numPlayers; i++){
            if(allPlayers.get(i).getID() == userID){
                user = allPlayers.get(i);
                i = numPlayers;
            }
        }
        if(user.getType() == 4){
            System.out.printf("You are Player%d.\n", userID);
            System.out.printf("You are a %s.\n", user.getPType());
            return;
        }
        for(int i = 0; i<numPlayers; i++){
            if(user.getType() == allPlayers.get(i).getType()){
                others.add(allPlayers.get(i));
            }
        }
        System.out.printf("You are Player%d.\n", userID);
        System.out.printf("You are a %s. Other %ss are: [", user.getPType(), user.getPType());
        int t = 0;
        for(int i = 0; i< others.size(); i++){
            if(userID != others.get(i).getID()) {
                if (i != others.size() - 1) {
                    System.out.printf("Player%d, ", others.get(i).getID());
                }
                else {
                    System.out.printf("Player%d]\n", others.get(i).getID());
                    t = 1;
                }
            }
        }
        if(t == 0){
            System.out.println("]");
        }
    }

    private void assignRole(int option){
        int i;
        for(i = 0; i<mafia; i++){
            allPlayers.add(new Mafia());
        }
        for(i = 0; i<detective; i++){
            allPlayers.add(new Detective());
        }
        for(i = 0; i<healer; i++){
            allPlayers.add(new Healer());
        }
        for(i = 0; i<commoner; i++){
            allPlayers.add(new Commoner());
        }
        Collections.shuffle(allPlayers);
        for(int j = 0; j<numPlayers; j++){
            allPlayers.get(j).setID(j+1);
            pMap.put(j+1, allPlayers.get(j));
            if(option == allPlayers.get(j).getType()) {
                if(userID == 0)
                    userID = allPlayers.get(j).getID();
            }
        }
    }

    public boolean playRound(int sno){
        ArrayList<Player> inGame = new ArrayList<>();
        for(Player p : allPlayers){
            if(p.isAlive()){
                inGame.add(p);
            }
        }
        r = new Round(sno, user, inGame);
        r.make();
        int result = r.start();
        if(result == -1){

        }
        else if(result == 0 || result == -98){
            System.out.println("\nGame Over.");
            if (result == 0) {
                System.out.println("The Mafias have won.");
            }
            else {
                System.out.println("The Mafias have lost.");
            }
            return false;
        }
        else{
            //game continue and vote out happened
            if(userID == result){
                System.out.println("You have been voted out.");
                user.setAlive(false);
                user.setHP(0);
                userID = 0;
            }
            else {
                System.out.printf("Player%d has been voted out.\n", result);
            }
            for(int i = 0; i<allPlayers.size(); i++){           //changes not in Round
                if(allPlayers.get(i).getID() == result){
                    allPlayers.get(i).setAlive(false);
                    allPlayers.get(i).setHP(0);
                }
            }
        }
        //------------------------------------------------------UNCOMMENT TO CHECK-----------------------------------------------------------6
//        for(int i = 0; i<numPlayers; i++){
//            System.out.println(allPlayers.get(i));
//        }
        result = r.gameOver();
        if(result == -1){
            //game continue but no vote out since caught
            return true;
        }
        else if(result == 0 || result == -98){
            System.out.println("\nGame Over.");
            if (result == 0) {
                System.out.println("The Mafias have won.");
            }
            else {
                System.out.println("The Mafias have lost.");
            }
            return false;
        }
        if(!user.isAlive()){
            user.setAlive(false);
            user.setHP(0);
            userID = 0;
        }
        return true;
    }

    private void printResults(){
        Collections.sort(allPlayers, new PlayerTypeSorter());
        String res = "";
        for(int i = 0; i<allPlayers.size(); i++){
            if(allPlayers.get(i).getType() == 1){
                if(i == mafia -1){
                    res += String.format("Player%d were Mafias.\n", allPlayers.get(i).getID());
                }
                else {
                    res += String.format("Player%d and ", allPlayers.get(i).getID());
                }
            }
            else if(allPlayers.get(i).getType() == 2){
                if(i == mafia + detective -1){
                    res += String.format("Player%d were Detectives.\n", allPlayers.get(i).getID());
                }
                else {
                    res += String.format("Player%d and ", allPlayers.get(i).getID());
                }
            }
            else if(allPlayers.get(i).getType() == 3){
                if(i == mafia + detective + healer -1){
                    res += String.format("Player%d were Healers.\n", allPlayers.get(i).getID());
                }
                else {
                    res += String.format("Player%d and ", allPlayers.get(i).getID());
                }
            }
            else if(allPlayers.get(i).getType() == 4){
                if(i == mafia + detective + healer + commoner -1){
                    res += String.format("Player%d were Commoners.\n", allPlayers.get(i).getID());
                }
                else {
                    res += String.format("Player%d and ", allPlayers.get(i).getID());
                }
            }
        }
        System.out.println(res);
    }


}
