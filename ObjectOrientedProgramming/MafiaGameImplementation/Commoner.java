import java.util.ArrayList;

public class Commoner extends Player{

    private static ArrayList<Commoner> allCommoner;

    public Commoner(){
        super(1000, 4);
    }

    public Commoner(double myHP, int t){
        super(myHP, t);
    }

    @Override
    public String getPrompt(){
        return null;
    }

    @Override
    public String getChosenMessage(){
        return  null;
    }

    @Override
    public String getErrorMessage(){
        return null;
    }

}
