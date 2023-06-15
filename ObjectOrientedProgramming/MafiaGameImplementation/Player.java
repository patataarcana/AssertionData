public abstract class Player {

    private int PID;
    private double HP;
    private int type;
    private String pType;
    private boolean alive;

    public Player(double myHP, int myType){
        HP = myHP;
        type = myType;
        alive = true;
        if(type == 1){
            pType = "Mafia";
        }
        else if(type == 2){
            pType = "Detective";
        }
        else if(type == 3){
            pType = "Healer";
        }
        else {
            pType = "Commoner";
        }
    }

    public int getID(){
        return PID;
    }

    public void setID(int id){
        PID = id;
    }

    public double getHP(){
        return HP;
    }

    public void setHP(double myHP){
        HP = myHP;
    }

    public int getType(){
        return type;
    }

    public String getPType() { return pType; }

    public boolean isAlive(){
        return alive;
    }

    public void setAlive(boolean stat){
        alive = stat;
    }

    public String toString(){
        return String.format("Player%d - %s with HP %.2f Alive? %s", PID, pType, HP, alive);
    }

    @Override
    public boolean equals(Object o){
        if(o.getClass() == getClass())
            return true;
        return false;
    }

    abstract public String getPrompt();

    abstract public String getChosenMessage();

    abstract  public String getErrorMessage();

}
