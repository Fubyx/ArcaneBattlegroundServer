package arc;

import shared.Entity;
import shared.Game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class GameLoopThread extends Thread{
    private final Game game;
    private final Socket client;
    private final int clientId;
    private final ObjectInputStream oIn;
    public GameLoopThread (Game game, Socket client, int clientId, ObjectInputStream oIn){
        this.game = game;
        this.client = client;
        this.clientId = clientId;
        this.oIn = oIn;
    }

    @Override
    public void run() {
        try {
            while (true){
                Entity e = (Entity)oIn.readObject();
                if(e.getId().equals("quit")){
                    game.removePlayer(clientId);
                    return;
                }else if(e.getId().equals("end")){
                    game.endGame(clientId);
                    return;
                }
                System.out.println("Recieved tap");
                game.publish(e);
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
