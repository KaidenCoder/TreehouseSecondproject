package com.teamtreehouse;

import com.teamtreehouse.model.Player;
import com.teamtreehouse.model.Players;
import com.teamtreehouse.model.Team;

import java.io.IOException;
import java.util.*;

public class Teams {
    private Team team;
    private List<Player> allPlayers = new ArrayList<>(Arrays.asList(Players.load()));
    private List<Team> teams;
    private Map<String, String> menu;
    private static final int MAX_TEAM_PLAYERS = 11;
    Scanner scanner = new Scanner(System.in);
    private static final String CREATE_TEAM = "CREATE";
    private static final String ADD_PLAYER = "ADD";
    private static final String REMOVE_TEAM = "REMOVE";
    private static final String HEIGHT_REPORT = "HEIGHT";
    private static final String BALANCE_REPORT = "BALANCE";
    private static final String ROSTER = "ROSTER";
    private static final String QUIT = "QUIT";


    public Teams(){
        teams= new ArrayList<>();
        menu= new LinkedHashMap<>();
        menu.put(CREATE_TEAM," create a new team");
        menu.put(ADD_PLAYER," add players to the team");
        menu.put(REMOVE_TEAM," remove players from the team");
        menu.put(HEIGHT_REPORT," print height report of all players of a team");
        menu.put(BALANCE_REPORT," print balance report");
        menu.put(ROSTER," print roster");
        menu.put(QUIT," exit the program");
    }

    public String promptForChoice(){
        for(Map.Entry<String,String> choice: menu.entrySet()){
            System.out.printf("%s-%s %n",
                    choice.getKey(),choice.getValue());
        }
        System.out.println("*******************************");
        String input = scanner.next();
        return input.trim().toUpperCase();
    }

    public  void chooseOption() throws IOException{
        String input;
        do{
            try{
              input = promptForChoice();
              switch (input){
                  case CREATE_TEAM:
                      createTeam();
                      break;
                  case ADD_PLAYER:
                      Team addToTeam= DisplayTeamList();
                      addPlayerToTeam(addToTeam);
                      break;
                  case REMOVE_TEAM:
                      Team removeFromTeam= DisplayTeamList();
                      removePlayerFromTeam(removeFromTeam);
                      break;
                  case HEIGHT_REPORT:
                      heightReport();
                      break;
                  case BALANCE_REPORT:
                      balanceReport();
                      break;
                  case ROSTER:
                      roster();
                      break;
                  case QUIT:
                      System.out.println("Report finished. Closing. Bye.");
                      break;
                  default:
                      System.out.printf("non valid choice: '%s'.Try again.%n",input);
                      break;
              }
            }catch (IOException ioe){
                System.out.println("Wrong option. Please select the above options.");
                throw new IOException();
            }
        }while (input != QUIT);
    }

    public void createTeam(){
        System.out.print("Enter your team name: ");
        String teamName = scanner.next();
        System.out.print("Enter your coach name: ");
        String coach = scanner.next();
        Team team = new Team(teamName, coach);
        int allTeamPlayers = teams.size() * team.mPlayers.size();
        int remainPlayers = allPlayers.size() - allTeamPlayers;
        if(remainPlayers >= MAX_TEAM_PLAYERS){
            teams.add(team);
            System.out.printf("Added %s to the list of teams with %s as its coach.%n%n", teamName, coach);
        } else{
            System.out.println("Sorry!You can't create more teams than there are available players");
        }
    }

    private void addPlayerToTeam(Team team) throws IOException{
        Set<Player> players = new TreeSet<>(allPlayers);
        Collections.sort(allPlayers);
        int playerIndex = promptForPlayerIndex(players);
        if(team.mPlayers.contains(allPlayers.get(playerIndex))){
            System.out.printf("The player is already in the team: %s %n",team);
        }
        if(team.mPlayers.size() < MAX_TEAM_PLAYERS){
            team.mPlayers.add(allPlayers.get(playerIndex)); //adding a player to the team
            allPlayers.remove(allPlayers.get(playerIndex)); //removing a player from the available market list.
            System.out.printf("The player is added to the team %s%n",team);
        } else{
            System.out.println("Sorry, you cannot add more than 11 players to the team.");
        }
    }

    //remove a player from a team
    private void removePlayerFromTeam(Team team)throws IOException{
        if(team.mPlayers.isEmpty()){
            System.out.println("the team is empty");
            System.exit(1);
        }
        List<Player>convertSet=new ArrayList<>(team.mPlayers);
        Collections.sort(convertSet);
        int index = promptForPlayerIndex(team.mPlayers);
        if(team.mPlayers.contains(convertSet.get(index))){
            team.mPlayers.remove(convertSet.get(index));
            allPlayers.add(convertSet.get(index));
            System.out.printf("The player is removed from the team %s%n",team);
        }else{
            System.out.printf("The player is not in the team: %s %n ",team);
        }
    }

    private void heightReport(){
        for(Team team:teams){
            if(team.mPlayers.isEmpty()){
                System.out.println("the team is empty");
                System.exit(1);
            }
            System.out.printf("%s:%n",team);
            for(Map.Entry<String,List<Player>>display:team.playerHeight().entrySet()){
                System.out.printf("%s - %s %n",
                        display.getKey(),
                        display.getValue());
            }
        }
    }

    private void balanceReport(){
        for(Team team:teams){
            if(team.mPlayers.isEmpty()){
                System.out.println("the team is empty");
                System.exit(1);
            }
            System.out.printf("%s:%n",team);
            for(Map.Entry<String,List<Player>>display:team.ExpInExpReport().entrySet()){
                System.out.printf("%s - %s %n",
                        display.getKey(),
                        display.getValue());
            }
        }
    }

    public void roster(){
        if(teams.size() == 0){
            System.out.println("the team list is empty");
            System.exit(1);
        }

        System.out.println("Available teams:");
        int index = promptForTeamIndex(teams);
        teams.get(index).printRoster();
    }

    private Team DisplayTeamList(){
        if(teams.size() == 0){
            System.out.println("The Team List is empty");
            System.exit(1);
        }
        System.out.println("Available Teams:");
        int index = promptForTeamIndex(teams);
        Team team =  teams.get(index);
        return team;
    }

    private int promptForPlayerIndex(Set<Player>selection){
        boolean isAcceptable = false;
        int selected = 0;
        int count = 1;
        for(Player choice:selection){
            System.out.printf("%d.) %s%n",count,choice);
            count++;
        }
        do{
            try{
                String makeChoice=scanner.next();
                selected=Integer.parseInt(makeChoice.trim());
                if(selected>selection.size()){
                    throw new IndexOutOfBoundsException("your choice doesn't match any player");
                }
                isAcceptable=true;
            }catch(IndexOutOfBoundsException e){
                System.out.printf("Oops! the player not in the list. Please try again. %n");
            }catch(IllegalArgumentException iae){
                System.out.printf("invalid input. Please try again. %n");
            }}while(!isAcceptable);
        return selected-1;

    }

    private int promptForTeamIndex(List<Team> selection){
        boolean isAcceptable = false;
        int selected = 0;
        int count = 1;
        Collections.sort(selection);
        for(Team choice:selection){
            System.out.printf("%d) %s%n",count,choice);
            count++;
        }

        do{
            try{
                String makeChoice = scanner.next();
                selected = Integer.parseInt(makeChoice.trim());
                if(selected > selection.size()){
                    throw new IndexOutOfBoundsException("not matching any TEAM");
                }
                isAcceptable = true;
            }catch(IndexOutOfBoundsException e){
                System.out.printf("Oops! the team not in the list. Please try again. %n");
            }catch(IllegalArgumentException iae){
                System.out.printf("invalid input. Please try again. %n");
            }
        }while(!isAcceptable);
        return selected -1;
    }
}
