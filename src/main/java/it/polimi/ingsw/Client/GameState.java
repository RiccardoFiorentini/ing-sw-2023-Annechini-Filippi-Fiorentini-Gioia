package it.polimi.ingsw.Client;

import it.polimi.ingsw.Model.*;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    //GAME PARAMETERS
    private int firstPlayerId;
    private int lastPlayerId;
    private final int[] commonGoalsId;
    private final int[] commonGoalsRemainingPoint;
    private final String[] commonGoalsDesc;
    private int currPlayerId;
    private BoardBean board;
    private ChatBean chat;
    private List<ShelfBean> shelves;
    private List<String> nicknames;
    private List<Integer> turnIds;
    private boolean[][] pickableTiles;
    private int numPlayers;

    private List<Integer> commonGoalPoints1;
    private List<Integer> commonGoalPoints2;
    private List<Boolean> connected;
    private Tile[] buffer;
    private List<Integer> finalPersonalGoalPoints;
    private List<Integer> finalColorGroupPoints;
    private List<Integer> finalPoints;

    /**
     * Class' constructor
     * @author Alessandro Annechini
    */
    public GameState(){
        firstPlayerId = -1;
        lastPlayerId = -1;
        currPlayerId = -1;
        commonGoalsId = new int[2];
        commonGoalsRemainingPoint = new int[2];
        commonGoalsDesc = new String[2];
    }

    /**
     * Method that returns the path to the image of a tile
     * @author Alessandro Annechini
     * @param tile The tile whose image is searched
     * @return The path of the image if the tile is not empty or blocked, null otherwise
     */
    public String getTileImagePath(Tile tile){
        String path = "/item tiles/";
        if(tile.isFree()) return null;
        switch(tile){
            case BLUE1 : return path + "Cornici1.1.png";
            case BLUE2 : return path + "Cornici1.2.png";
            case BLUE3 : return path + "Cornici1.3.png";
            case GREEN1: return path + "Gatti1.1.png";
            case GREEN2: return path + "Gatti1.2.png";
            case GREEN3: return path + "Gatti1.3.png";
            case ORANGE1: return path + "Giochi1.1.png";
            case ORANGE2: return path + "Giochi1.2.png";
            case ORANGE3: return path + "Giochi1.3.png";
            case WHITE1: return path + "Libri1.1.png";
            case WHITE2: return path + "Libri1.2.png";
            case WHITE3: return path + "Libri1.3.png";
            case PINK1: return path + "Piante1.1.png";
            case PINK2: return path + "Piante1.2.png";
            case PINK3: return path + "Piante1.3.png";
            case CYAN1: return path + "Trofei1.1.png";
            case CYAN2: return path + "Trofei1.2.png";
            case CYAN3: return path + "Trofei1.3.png";
            default: return null;
        }
    }

    /**
     * Method that returns the path to the image of a personal goal
     * @author Alessandro Annechini
     * @param pGoalId The personal goal whose image is searched (id form 1 to 12)
     * @return The path of the personal goal
     */
    public String getPersonalGoalImagePath(int pGoalId){
        String path = "/personal goal cards/Personal_Goals";
        if(pGoalId==1) return path + ".png";
        if(pGoalId>=2 && pGoalId<=12) return path + pGoalId + ".png";
        return null;
    }

    /**
     * Method that returns the path to the image of a common goal
     * @author Alessandro Annechini
     * @param cGoalId The common goal whose image is searched (id form 1 to 12)
     * @return The path of the common goal
     */
    public String getCommonGoalImagePath(int cGoalId){
        if(cGoalId>=1 && cGoalId<=12) return "/common goal cards/" + cGoalId +".jpg";
        return null;
    }

    /**
     * Method that returns the path to the image of a common goal scoring token
     * @author Alessandro Annechini
     * @param points The common goal scoring token whose image is searched (even number from 2 to 8)
     * @return The path of the scoring token
     */
    public String getScoringTokenImagePath(int points){
        if(points>=2 && points<=8 && points%2==0) return "/scoring tokens/scoring_"+points+".jpg";
        return null;
    }

    /**
     * Method that returns if a player with the corresponding id is connected
     * @author Alessandro Annechini
     * @param turnId The id of the player
     * @return true if connected, false otherwise
     */
    public boolean isConnected(int turnId){
        return turnId>=0 && turnId<this.connected.size() ? this.connected.get(turnId) : false;
    }

    /**
     * This method returns, after the game has finished, the id of the player in the specified positions
     * @param pos The position of the requested player
     * @return The turnId of the player (-1 if the position is not valid)
     */
    public int getTurnIdInPosition(int pos){
        if(finalPoints==null) return -1;
        if(pos<1 || pos>finalPoints.size()) return -1;

        List<Integer> tmpPoints = new ArrayList<>(finalPoints);
        List<Integer> tmpIds = new ArrayList<>();
        for(int i=0;i<finalPoints.size();i++) tmpIds.add(i);
        int currPos = 1;

        while(tmpPoints.size()>0){
            int max=-1,maxPos=-1;
            for(int i=0;i<tmpPoints.size();i++) {
                if(max < tmpPoints.get(i)){
                    max = tmpPoints.get(i);
                    maxPos = i;
                }
            }

            if(currPos == pos) return tmpIds.get(maxPos);

            tmpIds.remove(maxPos);
            tmpPoints.remove(maxPos);
            currPos++;
        }
        return -1;
    }

    /**
     * method that returns a string that describes buffer state
     * @author Nicole Filippi
     * @return String that describes the buffer
     */
    public String getPickableTilesInBufferString(){
        String res = "";
        if(buffer[0].isFree()){
            if(buffer[1].isFree()){
                if(!buffer[2].isFree()){
                    res = "2";
                }
            }else{
                if(buffer[2].isFree()){
                    res = "1";
                }else{
                    res = "1 or 2";
                }
            }
        }else{
            if(buffer[1].isFree()){
                if(buffer[2].isFree()){
                    res = "0";
                }else{
                    res = "0 or 2";
                }
            }else{
                if(buffer[2].isFree()){
                    res = "0 or 1";
                }else{
                    res = "0, 1 or 2";
                }
            }
        }
        return res;
    }

    public int getFirstPlayerId() {
        return firstPlayerId;
    }

    public void setFirstPlayerId(int firstPlayerId) {
        this.firstPlayerId = firstPlayerId;
    }

    public int[] getCommonGoalsId() {
        return commonGoalsId;
    }

    public void setCommonGoalsId(int pos,int id) {
        this.commonGoalsId[pos] = id;
    }

    public int[] getCommonGoalsRemainingPoint() {
        return commonGoalsRemainingPoint;
    }

    public void setCommonGoalsRemainingPoint(int pos,int remainingPoints) {
        this.commonGoalsRemainingPoint[pos] = remainingPoints;
    }

    public void setPickableTiles(boolean[][] pickableTiles){
        this.pickableTiles = pickableTiles;
    }
    public boolean[][] getPickableTiles(){
        return this.pickableTiles;
    }
    public String[] getCommonGoalsDesc() {
        return commonGoalsDesc;
    }

    public void setCommonGoalsDesc(int pos, String desc) {
        this.commonGoalsDesc[pos] = desc;
    }

    public int getCurrPlayerId() {
        return currPlayerId;
    }

    public void setCurrPlayerId(int currPlayerId) {
        this.currPlayerId = currPlayerId;
    }

    public BoardBean getBoard() {
        return board;
    }

    public void setBoard(BoardBean board) {
        this.board = board;
    }

    public ChatBean getChat() {
        return chat;
    }

    public void setChat(ChatBean chat) {
        this.chat = chat;
    }

    public List<ShelfBean> getShelves() {
        return shelves;
    }

    public void setShelves(List<ShelfBean> shelves) {
        this.shelves = shelves;
    }

    public List<String> getNicknames() {
        return nicknames;
    }

    public void setNicknames(List<String> nicknames) {
        this.nicknames = nicknames;
    }

    public List<Integer> getTurnIds() {
        return turnIds;
    }

    public void setTurnIds(List<Integer> turnIds) {
        this.turnIds = turnIds;
    }

    public List<Integer> getCommonGoalPoints1() {
        return commonGoalPoints1;
    }

    public void setCommonGoalPoints1(List<Integer> commonGoalPoints1) {
        this.commonGoalPoints1 = commonGoalPoints1;
    }

    public List<Integer> getCommonGoalPoints2() {
        return commonGoalPoints2;
    }

    public void setCommonGoalPoints2(List<Integer> commonGoalPoints2) {
        this.commonGoalPoints2 = commonGoalPoints2;
    }

    public List<Boolean> getConnected() {
        return connected;
    }

    public void setConnected(List<Boolean> connected) {
        this.connected = connected;
    }

    public Tile[] getBuffer() {
        return buffer;
    }

    public void setBuffer(Tile[] buffer) {
        this.buffer = buffer;
    }

    public int getLastPlayerId() {
        return lastPlayerId;
    }

    public void setLastPlayerId(int lastPlayerId) {
        this.lastPlayerId = lastPlayerId;
    }

    public List<Integer> getFinalPersonalGoalPoints() {
        return finalPersonalGoalPoints;
    }

    public void setFinalPersonalGoalPoints(List<Integer> finalPersonalGoalPoints) {
        this.finalPersonalGoalPoints = finalPersonalGoalPoints;
    }

    public List<Integer> getFinalColorGroupPoints() {
        return finalColorGroupPoints;
    }

    public void setFinalColorGroupPoints(List<Integer> finalColorGroupPoints) {
        this.finalColorGroupPoints = finalColorGroupPoints;
    }

    public List<Integer> getFinalPoints() {
        return finalPoints;
    }

    public void setFinalPoints(List<Integer> finalPoints) {
        this.finalPoints = finalPoints;
    }
    public int getNumPlayers() {
        return numPlayers;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

}
