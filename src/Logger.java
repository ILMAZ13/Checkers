import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {
    private File logFile;

    public Logger() {
        logFile = new File("log.txt");
        try(FileWriter fileWriter = new FileWriter(logFile,false)){
            fileWriter.write("[Logging Started]\r\n");
        } catch (IOException e) {
            System.err.printf("Can't open log file\r\n");
        }
    }

    public void write(String str){
        try(FileWriter fileWriter = new FileWriter(logFile,true)){
            fileWriter.write(str + "\r\n");
        } catch (IOException e) {
            System.err.printf("Can't write log: %s\r\n", str);
        }
    }

    public void writeAsError(String str){
        try(FileWriter fileWriter = new FileWriter(logFile,true)){
            fileWriter.write("[ERROR] " + str + "\r\n");
        } catch (IOException e) {
            System.err.printf("Can't write ERROR log: %s\r\n", str);
        }
    }


}
