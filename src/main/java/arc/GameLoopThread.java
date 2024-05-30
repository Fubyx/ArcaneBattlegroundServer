package arc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class GameLoopThread extends Thread{
    private final Game game;
    private final Socket client;
    private final int clientId;
    public GameLoopThread (Game game, Socket client, int clientId){
        this.game = game;
        this.client = client;
        this.clientId = clientId;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream oIn = new ObjectInputStream(client.getInputStream());
            while (true){
                Entity e = (Entity)oIn.readObject();
                if(e.getId().equals("quit")){
                    game.removePlayer(clientId);
                    return;
                }else if(e.getId().equals("end")){
                    game.endGame(clientId);
                    return;
                }
                game.publish(e);
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
