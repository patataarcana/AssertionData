import java.util.ArrayList;

public class Detective extends Player {

    public Detective(){
        super(800, 2);
    }

    @Override
    public String getPrompt(){
        return "Choose a player to test: ";
    }

    @Override
    public String getChosenMessage(){
        return "Detectives have chosen a player to test.";
    }

    @Override
    public String getErrorMessage(){
        return "You cannot test a detective.";
    }

}
