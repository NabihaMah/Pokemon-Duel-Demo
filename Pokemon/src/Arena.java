// Pokemon Arena
// Nabiha Mahboob
// This Program is a game of Pokemon. The player has 4 Pokemon that fight all possible enemy Pokemon until one of the
// two teams have no Pokemon left. The game occurs in battles agains one enemy at a time. The team with Pokemon left
// wins and is crowned Trainer Supreme.

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

// the game occurs in this class
public class Arena {

    static ArrayList<Pokemon> allPokemon = new ArrayList<Pokemon>(); // List off all Pokemon and then enemy Pokemon
    static ArrayList<Pokemon> team = new ArrayList<Pokemon>(); // List of the player's team
    static Pokemon userPokemon; // The player's Pokemon
    static Pokemon aIPokemon; // The opponent Pokemon
    static final int user = 0; // the player
    static final int aI = 1; // the computer


    public static void main(String [] args){
        load(); // loads the Pokemon
        pickFour(); // user picks Pokemon
        while(allPokemon.size() > 0 && team.size() > 0){
            battle(); // battle starts
            heal(); // heals pokemon
        }
        gameOver(); // declares winner
    }

    // Loads all the Pokemon from the txt file into an arraylist of Pokemon
    public static void load(){
        // tries to get each line
        try{
            Scanner infile = new Scanner(new File("pokemon.txt"));
            int num =Integer.parseInt(infile.nextLine());

            for (int i=0; i<num; i++){
                allPokemon.add(new Pokemon(infile.nextLine()));
            }

        }
        catch(FileNotFoundException ex){
            System.out.println(ex);
        }
    }

    // User picks their team of 4 Pokemon
    public static void pickFour(){
        // Prints all possible Pokemon to pick from
        for (int i = 0; i< allPokemon.size(); i++) {
            System.out.print(i + ":  ");
            System.out.println(allPokemon.get(i));
        }

        Scanner input = new Scanner(System.in);
        for (int i=0; i<4; i++) {
            System.out.println("Enter a Pokemon");
            int j = input.nextInt();
            if(team.contains(allPokemon.get(j))){
                System.out.println("Cannot repeat Pokemon");
                i -= 1;
            }
            else {
                team.add(allPokemon.get(j));
            }
        }
            allPokemon.remove(team.get(0));
            allPokemon.remove(team.get(1));
            allPokemon.remove(team.get(2));
            allPokemon.remove(team.get(3));
    }

    public static void battle(){
        int randomStart = randint(0,1);
        playerPick(); // player picks  a Poemon to begin
        computerPick(); // random computer Pokemon
        int turn = randomStart; // randomly picks who goes first
        while (aIPokemon.awake()  && team.size()> 0){
            if (turn == user){
                userTurn();
            }
            else if (turn == aI){
                aITurn();
            }
            turn = user + aI - turn ; // changes who's turn it is
        }
        end();
    }



    public static void userTurn(){
        System.out.println("It Is Your Turn");
        Scanner input = new Scanner (System.in);
        if(userPokemon.awake()) { // Checks if a Pokemon is awake
            if (userPokemon.stunned) { // Passes the user's turn if the Pokemon is stunned
                System.out.println("Your Pokemon is stunned, Your turn will be skipped");
                pass();
                userPokemon.stunned = false;
            } else { // If the Pokemon is not stunned
                System.out.println("Pick an action to perform");
                System.out.println("0: Attack\n1: Retreat\n2: Pass");
                int i = input.nextInt(); // Gets an input for the user to perform an action
                if (i == 0) { // Carries out an attack
                    if (userPokemon.attackAvailable() != 0) { // Checks if an attack is available
                        userPokemon.attackSelect(); // Prints possible attacks
                        System.out.println("Pick an attack");
                        int j = input.nextInt(); // User picks an attack
                        userPokemon.attack(aIPokemon, j); // Performs chosen attack
                    }
                    else { // If there are no attacks available a new action is picked
                        System.out.println("Not enough energy to attack, pick a different move");
                        i = input.nextInt();
                        if(i==1){
                            retreat(); // retreats
                        }
                        else if (i==2){
                            pass(); // passes turn
                        }
                    }
                } else if (i == 1) { // Carries out a retreat
                    retreat();
                } else if (i == 2) { // Passes the user's turn
                    pass();
                }
                else { // if the move is invalid it asks for a new move
                    System.out.println("Please enter a valid move");
                    i = input.nextInt();
                }
            }

        }
        else{// If the Pokemon is asleep it gets replaced
            for(int j=0; j<team.size(); j++) { // Prints possible replacements
                System.out.print(j + ":  ");
                System.out.println(team.get(j));
            }
            System.out.println("Pokemon has no more hp, Pick a new Pokemon from your team");
            int k = input.nextInt(); // User picks the Pokemon to replace with
            userPokemon = team.get(k); // gets new Pokemon
            team.remove(userPokemon); // removes asleep Pokemon

        }
        userPokemon.recharge(); // Adds energy to the current Pokemon
       for (int j=0; j<team.size(); j++){
           team.get(j).recharge(); // Adds energy to all the user's Pokemon
       }
       healthStats(); // shows the health of the Pokemon
    }

    // Performs a retreat
    public static void retreat(){
        for(int j=0; j<team.size(); j++) {
            System.out.print(j + ":  "); // Prints possible replacements
            System.out.println(team.get(j));
        }
        System.out.println("Which Pokemon would you like to replace with");
        Scanner input = new Scanner(System.in);
        int l = input.nextInt(); // User picks replacement Pokemon
        team.add(userPokemon);
        userPokemon = team.get(l); // Switches current and chosen Pokemon
        team.remove(userPokemon);
    }

    // Performs the computer's turn
    public static void aITurn(){

       int i = randint(0, aIPokemon.attackAvailable()-1); // picks random attack
        System.out.println("It Is Your Opponent's Turn");
        if (!aIPokemon.stunned){ // if the Pokemon is not stunned
                if (aIPokemon.attackAvailable() != 0) { // if there is an attack available
                    System.out.print("Opponent Attacked: ");
                    aIPokemon.attack(userPokemon, i); // Carries out the attack

                }
                else{
                    System.out.println("Opponent Passed");
                }
       }
             else{ // if no attacks can be performed
                System.out.println("Opponent Passed");
                 pass(); // the turn is passed
                 aIPokemon.stunned = false; // Pokemon is no longer stunned
             }
        aIPokemon.recharge();
             healthStats(); // shows the health of the Pokemon in battle
    }

    public static void healthStats(){
        System.out.println("Your Pokemon:  hp:" + userPokemon.getHP() + " energy:" + userPokemon.getEnergy());
        System.out.println("Opponent Pokemon:  hp:" + aIPokemon.getHP() + " energy:" + aIPokemon.getEnergy());
    }

    // heals Pokemon
    public static void heal(){
        for (int i=0; i<team.size(); i++){
            if (team.get(i).awake()) {
                team.get(i).endHeal(); //Awake Pokemon are healed
            }
        }
    }

    // Gets a random integer
    public static int randint(int low, int high){
        return (int)(Math.random()*(high-low+1)) + low;
    }

    //Picks a random computer Pokemon
    public static void computerPick(){
        Collections.shuffle(allPokemon); // Shuffles array list of computer Pokemons
        aIPokemon = allPokemon.get(0); // Gets the first Pokemon
        System.out.println("Your opponent's Pokemon is: " + aIPokemon);
    }

    // user picks a Pokemon
    public static void playerPick(){
        for (int i = 0; i< team.size(); i++) {
            System.out.print(i + ":  "); // Prints all Pokemon possibilities
            System.out.println(team.get(i));
        }
        System.out.println("Pick a Pokemon to start with");
        Scanner input = new Scanner(System.in);
        int i = input.nextInt(); // user picks a Pokemon to use
        userPokemon = team.get(i);
        team.remove(i); // Pokemon is removed from the team
        System.out.println("Your Pokemon is: " + userPokemon);
    }

    // Ends the battle
    public static void end(){
        if (!aIPokemon.awake()){ // if the computer Pokemon has no more hp, a new round starts
            allPokemon.remove(aIPokemon);
            team.add(userPokemon);
        }
    }

    // Passes the turn
    public static void pass(){
    }

    // Once the game ends someone is crowned the winner
    public static void gameOver(){
        if (team.size()==0){ // if no more player Pokemon are awake, player loses
            System.out.println("You lost, your opponent is Trainer Supreme");
        }
        else if (allPokemon.size()==0){ // if no more enemy Pokemons are awake, player wins
            System.out.println("Congratulations! you won you are now Trainer Supreme");
        }
    }
}





