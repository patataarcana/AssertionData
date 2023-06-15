public class Mafia extends Player {

    Mafia(){
        super(2500, 1);
    }

    @Override
    public String getPrompt(){
        return "Choose a player to kill: ";
    }

    @Override
    public String getChosenMessage(){
        return "Mafias have chosen their target.";
    }

    @Override
    public String getErrorMessage(){
        return "You cannot kill a mafia.";
    }

}
