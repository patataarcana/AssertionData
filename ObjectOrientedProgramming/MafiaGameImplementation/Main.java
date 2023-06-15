import java.util.Scanner;

public class Main {

    static Scanner read = new Scanner(System.in) ;

    public static void main(String args[]) {

        System.out.println("Welcome To Mafia");
        System.out.println("Enter Number of players:");
        int n;
        while(true){
            n = takeIntInput();
            if(n < 6){
                System.out.println("Minimum number of players is 6, Enter Again");
            }
            else{
                break;
            }
        }
        Game newGame = new Game(n);
        int opt;
        while(true){
            newGame.printMenu();
            opt = takeIntInput();
            if(opt < 1 || opt > 5){
                System.out.println("There are only 5 Options in the Menu, Enter Valid Option");
            }
            else{
                break;
            }
        }
        newGame.makeGame(opt);
        newGame.playGame();
    }

    public static int takeIntInput() {
        while (true){
            String input = read.next();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Input is not an Integer, Enter Again");
            }
        }
    }
}
