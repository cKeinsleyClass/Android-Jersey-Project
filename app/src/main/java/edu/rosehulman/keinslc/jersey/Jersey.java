package edu.rosehulman.keinslc.jersey;

/**
 * Created by keinslc on 12/11/2016.
 */

public class Jersey {
    private String playerName;
    private int playerNumber;
    private boolean isRed;

    public Jersey(){
        playerName = "Android";
        playerNumber = 17;
        isRed = true;
    }

    public String getName(){
        return playerName;
    }
    public void setName(String name){
        playerName = name;
    }
    public int getNumber(){
        return playerNumber;
    }
    public void setNumber(int number){
        playerNumber = number;
    }
    public boolean getIsRed(){
        return isRed;
    }
    public void setIsRed(boolean isRed){
        this.isRed = isRed;
    }
}
