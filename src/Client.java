import java.io.*;
import java.net.Socket;

public class Client implements StreamsGettable {
    public static final int PORT = 4444;
    private Socket serverSocket;
    private ObjectOutput outputStream;
    private ObjectInput inputStream;

    public boolean connectTo(String host){
        try {
            MAIN.LOGGER.write("Connecting to server...");
            serverSocket = new Socket(host, PORT);
            MAIN.LOGGER.write("Connected to server");
        } catch (IOException e) {
            MAIN.LOGGER.writeAsError("Can't connect to the server");
            return false;
        }
        try {
            MAIN.LOGGER.write("Opening streams...");
            outputStream = new ObjectOutputStream(serverSocket.getOutputStream());
            inputStream = new ObjectInputStream(serverSocket.getInputStream());
            MAIN.LOGGER.write("Streams opened");
        } catch (IOException e) {
            MAIN.LOGGER.writeAsError("Can't open streams");
            return false;
        }
        return true;
    }
    @Override
    public ObjectInput getInputStream() {
        return inputStream;
    }

    @Override
    public ObjectOutput getOutputStream() {
        return outputStream;
    }

    @Override
    public void close() {
        try {
            outputStream.close();
            inputStream.close();
            serverSocket.close();
        } catch (IOException e) {
            MAIN.LOGGER.writeAsError("Can't close client");
        }
    }
}
