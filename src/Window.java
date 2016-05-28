import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Set;

public class Window extends Canvas implements Runnable, MouseListener{
    private Pos firstPos;
    private Message lastMessage;
    @Override
    public void mouseClicked(MouseEvent e) {
        if(myTurn) {
            int kWidth = (int) Math.round(getWidth()/8.0);//+
            int kHeight = (int) Math.round(getHeight()/8.0);//+

            if(gameTable.isBlack()) {
                clickedPos = new Pos(7 - e.getX() / kWidth, 7 - e.getY() / kHeight);//++
            }
            else{
                clickedPos = new Pos(e.getX() / kWidth, e.getY() / kHeight);//++
            }
            if (now == state.first) {
                if(gameMind.canChoose(clickedPos)){
                    firstPos = clickedPos;
                    now = state.second;
                    toSwitch = gameMind.getPossibleWays(clickedPos);
                }
                else {
                    toSwitch = null;
                }
            }
            else {
                if(gameMind.getPossibleWays(firstPos).contains(new InfoHolger(clickedPos))){
                    Pos tmp = null;
                    for(InfoHolger inf : gameMind.getPossibleWays(firstPos)){
                        if(inf.way.equals(clickedPos)){
                            tmp = inf.eat;
                            break;
                        }
                    }
                    if(gameMind.isMustEat() == (tmp != null)) {
                        Pos tmp2 = null;
                        Message m = new Message(firstPos, clickedPos, tmp, false);
                        gameTable.makeAMove(m);
                        gameMind.update();
                        if (tmp != null) {
                            Set<Pos> tmp3 = gameMind.getPossibleEats(clickedPos);
                            if (tmp3 != null) {
                                for (Pos pos : tmp3) {
                                    if (!pos.equals(tmp)) {
                                        tmp2 = pos;
                                    }
                                }
                            }
                        }
                        boolean f = tmp2 != null;
                        Message message = new Message(firstPos, clickedPos, tmp, f);
                        try {
                            streams.getOutputStream().writeObject(message);
                            toSwitch = null;
                            firstPos = null;
                            lastMessage = message;
                            now = state.first;
                            myTurn = f;
                            while (!render()) {
                            }
                        } catch (IOException e1) {
                            System.out.println("Cant send info");
                        }
                    }
                    else {
                        now = state.first;
                        mouseClicked(e);
                    }
                }
                else {
                    now = state.first;
                    mouseClicked(e);
                }
            }

        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    private enum state {
        first,
        second;
    }
    private Set<InfoHolger> toSwitch;
    private state now;
    private Pos clickedPos;
    private JFrame frame;
    private GameMind gameMind;
    private BufferedImage blackCheck;
    private BufferedImage whiteCheck;
    private BufferedImage queenCheck;
    private BufferedImage tableImage;
    private StreamsGettable streams;
    private GameTable gameTable;
    private boolean myTurn;
    private boolean isWaiting;

    public Window(GameMind gameMind, StreamsGettable streams) {
        now = state.first;
        this.streams = streams;
        this.gameMind = gameMind;
        gameTable = gameMind.getGameTable();
        myTurn = !gameTable.isBlack();
        JFrame frame = new JFrame("Checkers");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //выход из приложения по нажатию клавиши ESC
        frame.setLayout(new BorderLayout());
        frame.add(this, BorderLayout.CENTER); //добавляем холст на наш фрейм
        frame.pack();
        frame.setBounds(100,100,800,800);
        frame.setVisible(true);
    }

    @Override
    public void run() {
        prepare();
        gameMind.update();
        isWaiting = false;
        Messenger messenger = new Messenger();
        while (!render());
        while (!gameMind.isGameOver()) {
            render();
            if(!myTurn && !isWaiting){
                new Thread(messenger).start();//++
            }
            gameMind.update();
        }
        Message lastMes;
        if(gameTable.isBlack()){
            lastMes = new Message(new Pos(-1 , 1), null,null, false);
        }
        else {
            lastMes = new Message(new Pos(-1 , -1), null,null, false);
        }
        try {
            streams.getOutputStream().writeObject(lastMes);
            gameTable.makeAMove(lastMes);
        } catch (IOException e) {
            MAIN.LOGGER.writeAsError("Can't send final message");
        }
    }

    synchronized private void prepare(){
        try {
            this.addMouseListener(this);
            whiteCheck = ImageIO.read(MAIN.class.getResourceAsStream("white_checker.png"));
            blackCheck = ImageIO.read(MAIN.class.getResourceAsStream("black_checker.png"));
            queenCheck = ImageIO.read(MAIN.class.getResourceAsStream("gueen.png"));
            tableImage= ImageIO.read(MAIN.class.getResourceAsStream("table.jpg"));
            MAIN.LOGGER.write("Images loaded");
        } catch (IOException e) {
            MAIN.LOGGER.writeAsError("Can't load images");
            System.exit(-1);
        }
    }

    synchronized private boolean render(){
        BufferStrategy bs = this.getBufferStrategy();
        boolean flag;
        if(bs == null){
            createBufferStrategy(2);
            requestFocus();
            flag = false;
        }
        else {
            int kWidth = (int) Math.round(getWidth()/8.0);//+
            int kHeight = (int) Math.round(getHeight()/8.0);//+

            Graphics2D mainGraph = (Graphics2D) bs.getDrawGraphics();
            if(gameTable.isBlack()){
                mainGraph.rotate(Math.toRadians(180), getWidth() / 2.0, getHeight() / 2.0);
            }
            mainGraph.setColor(Color.green);
            mainGraph.setFont(new Font("Broadway", Font.TYPE1_FONT, 30));
            mainGraph.drawImage(tableImage,0,0,getWidth(),getHeight(),null);//++
            Checker[][] table = gameTable.getTable();
            if (firstPos != null){
                mainGraph.fillRect(firstPos.xPos*kWidth, firstPos.yPos*kHeight,  kWidth, kHeight);//++
            }
            if(toSwitch != null){
                for(InfoHolger info : toSwitch){
                    mainGraph.fillRect(info.way.xPos*kWidth, info.way.yPos*kHeight, kWidth, kHeight);//++
                }
            }
            for(int x = 0; x < 8; x++){
                for(int y = 0; y < 8; y++){
                    if (table[x][y] != null) {
                        if (table[x][y].isBlack) {
                            mainGraph.drawImage(blackCheck, x * kWidth, y * kHeight, kWidth, kHeight, null);//+
                        } else {
                            mainGraph.drawImage(whiteCheck, x * kWidth, y * kHeight, kWidth, kHeight, null);//++
                        }
                        if(table[x][y] != null) {
                            if (table[x][y].isQueen) {
                                if(gameTable.isBlack()){
                                    mainGraph.rotate(Math.toRadians(180), x * kWidth + (kWidth / 2.0) , y * kHeight + (kHeight / 2.0));//++
                                    mainGraph.drawImage(queenCheck, x * kWidth, y * kHeight, kWidth, kHeight, null);//++
                                    mainGraph.rotate(Math.toRadians(180), x * kWidth + (kWidth / 2.0) , y * kHeight + (kHeight / 2.0));//++
                                }
                                else {
                                    mainGraph.drawImage(queenCheck, x * kWidth, y * kHeight, kWidth, kHeight, null);//++
                                }
                            }
                        }
                    }
                }
            }
            if(myTurn) {
                if(gameTable.isBlack()){
                    mainGraph.rotate(Math.toRadians(180), getWidth() / 2.0, getHeight() / 2.0);
                    mainGraph.drawString("YOU", 10, 50);
                    mainGraph.rotate(Math.toRadians(180), getWidth() / 2.0, getHeight() / 2.0);
                }
                else {
                    mainGraph.drawString("YOU", 10, 50);
                }
            }
            mainGraph.dispose();
            bs.show();
            flag = true;
        }
        return flag;
    }

    private class Messenger implements Runnable{
        @Override
        public void run() {
            if (!myTurn) {
                isWaiting = true;
                Object tmp = null;
                Message message = null;
                try {
                    tmp = streams.getInputStream().readObject();
                    message = (Message) tmp;
                    if(message.getLastPosition().xPos == 100){
                        streams.getOutputStream().writeObject(lastMessage);
                        MAIN.LOGGER.write("Resending message");
                    }
                    else {
                        gameTable.makeAMove(message);
                        myTurn = !message.isHaveNextWay();
                    }
                } catch (ClassNotFoundException e1) {
                } catch (IOException e1) {
                } catch (Exception e1){
                    try {
                        streams.getOutputStream().writeObject(new Message(new Pos(100,100), null, null, false));
                        MAIN.LOGGER.writeAsError("Waiting for message resending");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            isWaiting = false;
        }
    }
}
