package arc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientCommThread extends Thread{
    private final Socket client;

    public ClientCommThread(Socket client){
        this.client = client;
    }

    @Override
    public void run() {
        ObjectInputStream oIn = null;
        ObjectOutputStream oOut = null;
        try {
            oIn = new ObjectInputStream(client.getInputStream());
            oOut = new ObjectOutputStream(client.getOutputStream());
            //Send id to client
            int clientId = Server.incClientId();
            oOut.write(clientId);
            Game game;
            //Read create or join (create = -1; join = gameId)
            int msg = oIn.readInt();
            if(msg == -1){
                game = new Game(Server.incGameId(), new Player(client, clientId, "", this));
                Server.games.add(game);

                //Send gameId to client
                oOut.write(game.getGameId());

                msg = oIn.read();//client sends start or close game TODO: implement close game
                if(msg == 1){
                    game.start();
                }else{
                    return;
                }
            }else{
                boolean res = Server.games.get(Server.games.indexOf(new Game(msg, null))).addPlayer(new Player(client, clientId, "", this));
                oOut.writeBoolean(res);
                game = Server.games.get(Server.games.indexOf(new Game(msg, null)));
            }
            new GameLoopThread(game, client, clientId).start();
            while(!this.isInterrupted()){//Gameloop
                game.wait();
                oOut.writeObject(game.getTapToPublish());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }


    }
}
