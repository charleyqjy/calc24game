package game24;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;

import static game24.Game24Server.numPlayers;

import java.awt.FlowLayout;
public class Game24ServerFrame extends JFrame {
    JButton startButton;
    //private String[] names;
    //int[] points;
    //JLabel[] pointLabel;
    //private JTextField formula;
    public Game24ServerFrame() {
        super("24 Game Server");
        setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
        
        // user input number of players
        InputDialog playerDialog = new InputDialog(this, "playerNumber Input",
                "Enter number of players");
        numPlayers = Integer.valueOf(playerDialog.nameField.getText());

        // receive names from the server and display the player names
        // TO DO : receive names
        /*
         * playerNumber=theClient.recvInt(); names=new String [playerNumber];
         * for (int i=0;i<playerNumber;i++){ names[i]=theClient.recvString(); }
         */
        this.setLayout(new FlowLayout());
        startButton = new JButton("Start the game!");
        this.add(startButton);
        this.pack();
        this.setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
