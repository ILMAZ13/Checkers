import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class StartFrame extends Canvas{
    private JFrame frame;
    private JRadioButton radioButtonS;
    private JRadioButton radioButtonC;
    private JTextArea textArea;
    private boolean isServerChoosed;
    private String address = "";
    private JLabel label;

    public void start(){
        frame = new JFrame("Choose server or client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(100,100,400,400);
        MyListener listenerS = new MyListener(true);
        isServerChoosed = true;
        MyListener listenerC = new MyListener(false);

        try {
            label = new JLabel(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        label.setSize(150,20);
        label.setLocation(100,50);
        frame.add(label);
        label.setVisible(false);

        textArea = new JTextArea(0,0);
        textArea.setSize(150,20);
        textArea.setLocation(100,200);
        textArea.setVisible(false);
        frame.add(textArea);
        JButton button = new JButton("Start");
        button.setSize(150,50);
        button.setLocation(100,250);
        button.addActionListener(new MyActListener());
        frame.add(button);
        radioButtonS = new JRadioButton("Server", true);
        radioButtonC = new JRadioButton("Client", false);
        radioButtonS.addChangeListener(listenerS);
        radioButtonC.addChangeListener(listenerC);
        frame.setLayout(null);
        frame.setResizable(false);
        radioButtonS.setSize(100,30);
        radioButtonS.setLocation(100,100);
        radioButtonC.setSize(100,30);
        radioButtonC.setLocation(100,150);
        frame.add(radioButtonS);
        frame.add(radioButtonC);
        frame.setVisible(true);
    }
    private class MyListener implements ChangeListener{

        private boolean isServer;

        public MyListener(boolean isServer){
            this.isServer = isServer;
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            if (isServer){
                if(((JRadioButton)e.getSource()).isSelected()){
                    radioButtonC.setSelected(false);
                    isServerChoosed = true;
                    textArea.setVisible(false);
                }
            }
            else {
                if(((JRadioButton)e.getSource()).isSelected()){
                    radioButtonS.setSelected(false);
                    isServerChoosed = false;
                    textArea.setVisible(true);
                }
            }
        }
    }
    private class MyActListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(isServerChoosed){
                label.setVisible(true);
            }
            else {
                address = textArea.getText();
            }
            Game game = new Game(isServerChoosed,address, frame);
            new Thread(game).start();
        }
    }
}
