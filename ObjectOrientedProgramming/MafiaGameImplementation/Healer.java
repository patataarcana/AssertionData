import java.util.ArrayList;

public class Healer extends Player {

    public Healer(){
        super(800, 3);
    }

    @Override
    public String getPrompt(){
        return "Choose a player to heal: ";
    }

    @Override
    public String getChosenMessage(){
        return "Healers have chosen someone to heal.";
    }

    @Override
    public String getErrorMessage(){
        return null;
    }

}

