package Controller;

import Model.*;
import View.GamePlaying;

import java.io.IOException;

public class GameController {
    public Player[] players;

    public GameController() throws IOException {

    }

    public void createPlayer(int playerNum) throws IOException {
        this.players = new Player[playerNum];
        for(int i =0; i<playerNum;i++){
            Player player = new Player(i);
            players[i] = player;
        }
    }
    public void initPlayerPos(int id) throws IOException {
        this.players[id].positionX = GamePlaying.map.start_x;
        this.players[id].positionY = GamePlaying.map.start_y;
        GamePlaying.map.getMap()[players[id].positionX][players[id].positionY].setCellPlayer(id);
    }



}
