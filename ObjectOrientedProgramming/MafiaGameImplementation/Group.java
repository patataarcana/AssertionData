import java.util.ArrayList;
import java.util.Random;

public class Group<T extends Player> {

    private ArrayList<T> grp;
    private Random rand;

    public Group(){
        grp = new ArrayList<T>();
        rand = new Random();
    }

    public void add(T obj){
        grp.add(obj);
    }

    public T get(int i){
        return grp.get(i);
    }

    public int size(){
        return grp.size();
    }

    public void print(){
        System.out.println("Printing group");
        for(T o : grp){
            System.out.println(o);
        }
    }

    public int checkContains(int x){
//        print();
        for(int i = 0; i<grp.size(); i++){
            if(grp.get(i).getID() == x){
                return i;
            }
        }
        return -1;
    }

    public int chooser(int inputType, ArrayList<Integer> in){
        int toRet;
        if(inputType == grp.get(0).getType()){
            while(true) {
                System.out.print(grp.get(0).getPrompt());
                toRet = Main.takeIntInput();
                if (in.contains(toRet)) {
                    if(inputType == 3){
                        break;
                    }
                    else {
                        if (checkContains(toRet) == -1) {
                            break;
                        } else {
                            System.out.println(grp.get(0).getErrorMessage());
                        }
                    }
                } else {
                    System.out.printf("Player%d is not in the Game.\n", toRet);
                }
            }
        }
        else {
            while(true) {
                toRet = in.get(rand.nextInt(in.size()));
                if(grp.get(0).getType() == 3){
                    System.out.println(grp.get(0).getChosenMessage());
                    break;
                }
                else {
                    if (checkContains(toRet) == -1) {
                        System.out.println(grp.get(0).getChosenMessage());
                        break;
                    }
                }
            }
        }
        return toRet;
    }

    public void removeDead(){
        for(int i = 0; i< grp.size(); i++){
            if(!grp.get(i).isAlive()){
                grp.remove(i);
            }
        }
    }


}
