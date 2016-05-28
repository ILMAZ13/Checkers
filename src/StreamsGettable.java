import java.io.ObjectInput;
import java.io.ObjectOutput;

public interface StreamsGettable {
    public ObjectInput getInputStream();
    public ObjectOutput getOutputStream();
    public void close();
}
