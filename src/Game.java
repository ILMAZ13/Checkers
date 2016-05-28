import javax.swing.*;

public class Game implements Runnable{
    private boolean isServer;
    private String address;
    private JFrame frame;

    public Game(boolean isServer, String address, JFrame frame) {
        this.isServer = isServer;
        this.address = address;
        this.frame = frame;
    }

    @Override
    public void run() {
        StreamsGettable streams;
        boolean isBlack;
        if(isServer){
            Server server = new Server();
            server.createServer();
            frame.setVisible(false);
            streams = server;
            isBlack = false;
        }
        else {
            Client client = new Client();
            client.connectTo(address);
            frame.setVisible(false);
            streams = client;
            isBlack = true;
        }
        GameTable gameTable = new GameTable(isBlack);
        GameMind gameMind = new GameMind(gameTable);
        Window window = new Window(gameMind, streams);

        new Thread(window).run();
    }
}
