import com.teamtreehouse.model.Player;
import com.teamtreehouse.model.Players;
import com.teamtreehouse.Teams;

import java.io.IOException;

public class LeagueManager {

    public static void main(String[] args) {
        Player[] players = Players.load();
        System.out.printf("There are currently %d registered players.%n", players.length);
        // Your code here!
        Teams team = new Teams();
        try{
            team.chooseOption();
        }catch(IOException ioe){
            System.out.println("Invalid Input.");
            ioe.printStackTrace();
        }
    }

}

