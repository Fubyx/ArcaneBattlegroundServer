package arc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientCommThread extends Thread {
    private final Socket client;

    public ClientCommThread(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        ObjectInputStream oIn = null;
        ObjectOutputStream oOut = null;
        try {
            oOut = new ObjectOutputStream(client.getOutputStream());
            oOut.flush();
            oIn = new ObjectInputStream(client.getInputStream());
            //Send id to client
            int clientId = Server.incClientId();
            oOut.writeInt(clientId);
            oOut.flush();
            System.out.println("Client ID sent");
            Game game;
            //Read create or join (create = -1; join = gameId)
            int msg = oIn.readInt();
            System.out.println(msg);
            if (msg == -1) {
                game = new Game(Server.incGameId(), new PlayerInfo(client, clientId, "", this));
                Server.games.add(game);

                //Send gameId to client
                oOut.writeInt(game.getGameId());
                oOut.flush();
                msg = oIn.read();//client sends start or close game TODO: implement close game
                if (msg == 1) {
                    game.start();
                } else {
                    return;
                }
            } else {
                boolean res = Server.games.get(Server.games.indexOf(new Game(msg, null))).addPlayer(new PlayerInfo(client, clientId, "", this));
                oOut.writeBoolean(res);
                oOut.flush();
                game = Server.games.get(Server.games.indexOf(new Game(msg, null)));
            }
            new GameLoopThread(game, client, clientId).start();
            while (!this.isInterrupted()) {//Gameloop
                synchronized (game) {
                    game.wait(); // you still need to synchronize you bigot
                }
                oOut.writeObject(game.getTapToPublish());
                oOut.flush();
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }


    }
}
