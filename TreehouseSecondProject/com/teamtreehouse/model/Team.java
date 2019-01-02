package com.teamtreehouse.model;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class Team implements Comparable<Team>, Serializable {
    private String mTeamName;
    private String mCoach;
    public Set<Player> mPlayers;

    public Team(String mTeamName, String mCoach) {
        this.mTeamName = mTeamName;
        this.mCoach = mCoach;
        this.mPlayers = new TreeSet<Player>();
    }

    public Map<String, List<Player>> playerHeight(){
        Map<String, List<Player>> playersHeightReport = new TreeMap<String, List<Player>>();

        List<Player> firstCategoryHeight = mPlayers.stream()
                .filter(player -> (player.getHeightInInches() >= 35 && player.getHeightInInches() <= 40))
                .collect(Collectors.toList());
        if(firstCategoryHeight.size() > 0){
            playersHeightReport.put("35 - 40", firstCategoryHeight);
        }

        List<Player> secondCategoryHeight = mPlayers.stream()
                .filter(player -> (player.getHeightInInches() >= 41 && player.getHeightInInches() <= 46))
                .collect(Collectors.toList());
        if(secondCategoryHeight.size() > 0){
            playersHeightReport.put("41 - 46", secondCategoryHeight);
        }

        List<Player> thirdCategoryHeight = mPlayers.stream()
                .filter(player -> (player.getHeightInInches() >= 47 && player.getHeightInInches() <= 50))
                .collect(Collectors.toList());
        if(thirdCategoryHeight.size() > 0){
            playersHeightReport.put("47 - 50", thirdCategoryHeight);
        }

        return playersHeightReport;
    }

    public Map<String, List<Player>> ExpInExpReport(){
        Map<String, List<Player>> ExInexPlayers = new TreeMap<>();

        ExInexPlayers.put("Experienced", mPlayers
        .stream()
        .filter(Player::isPreviousExperience)
        .collect(Collectors.toList()));

        ExInexPlayers.put("Inexperienced", mPlayers
                .stream()
                .filter(player -> !player.isPreviousExperience())
                .collect(Collectors.toList()));

        return ExInexPlayers;
    }

    //print the list of players in a team
    public void printRoster(){
        for(Player printPlayer: mPlayers){
            System.out.printf("%s %s%n",printPlayer.getLastName(),printPlayer.getFirstName());
        }
    }

    @Override
    public int compareTo(Team other) {
        if(equals(other)){
            return 0;
        }

        return mTeamName.compareTo(other.mTeamName);
    }
    @Override
    public String toString(){
        return String.format("%s coach: %s",mTeamName,mCoach);
    }
}
