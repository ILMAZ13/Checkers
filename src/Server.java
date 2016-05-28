import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements StreamsGettable {
    public static final int PORT = 4444;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private ObjectOutput objectOutputStream;
    private ObjectInput objectInputStream;

    public boolean createServer(){
        try {
            MAIN.LOGGER.write("Creating Server...");
            serverSocket = new ServerSocket(PORT);
            MAIN.LOGGER.write("Server created");
        } catch (IOException e) {
            MAIN.LOGGER.writeAsError("Server don't created");
            return false;
        }
        try {
            MAIN.LOGGER.write("Waiting for client...");
            clientSocket = serverSocket.accept();
            MAIN.LOGGER.write("Client connected");
        } catch (IOException e) {
            MAIN.LOGGER.writeAsError("Can't connect to client");
            return false;
        }
        try {
            MAIN.LOGGER.write("Opening streams...");
            objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
            objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            MAIN.LOGGER.write("Streams opened");
        } catch (IOException e) {
            MAIN.LOGGER.writeAsError("Streams don't open");
            return false;
        }
        return true;
    }

    @Override
    public ObjectInput getInputStream() {
        return objectInputStream;
    }

    @Override
    public ObjectOutput getOutputStream() {
        return objectOutputStream;
    }

    @Override
    public void close() {
        try {
            objectInputStream.close();
            objectOutputStream.close();
            serverSocket.close();
            clientSocket.close();
        } catch (IOException e) {
            MAIN.LOGGER.writeAsError("Can't close server");
        }
    }
}
