package game24;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.Box;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultCaret;

public class Game24ClientFrame extends JFrame {
    String[] names;
    int numPlayers;
    // int [] points ;
    JPanel[] players;
    TitledBorder[] playerBorder;
    JLabel[] pointLabel;
    JButton[] cardButtons;
    JButton[] operationButtons;
    JButton submitButton;
    JButton giveupButton;
    JButton sendButton;
    // String IPAddr;
    // String name;
    JTextField formula;
    JTextArea incoming;
    JTextField outgoing;
    public Game24ClientFrame() {
        super("24 Game");
        setLayout(new BoxLayout(this.getContentPane(), BoxLayout.LINE_AXIS));
        /*
         * //user input Server IP Address InputDialog IPDialog = new
         * InputDialog(this, "IP Input", "Enter Server IP #"); IPAddr =
         * IPDialog.nameField.getText(); //user name input InputDialog
         * nameDialog = new InputDialog(this, "Player Name Input",
         * "Enter your name"); name = nameDialog.nameField.getText();
         */
        // send the user name to server
        // theClient.sendString(name);
        // receive names from the server and display the player names
        // TO DO : receive names
        /*
         * playerNumber=theClient.recvInt(); names=new String [playerNumber];
         * for (int i=0;i<playerNumber;i++){ names[i]=theClient.recvString(); }
         */

        numPlayers = 4;
        names = new String[numPlayers];

        for (int i = 0; i < numPlayers; i++) {
            names[i] = i + "";
        }
        Box leftBox=new Box(BoxLayout.PAGE_AXIS);
        JPanel playersPan = new JPanel(new GridLayout(1, names.length));
        players = new JPanel[names.length];

        // pointLabel is to display players' current points
        pointLabel = new JLabel[names.length];
        // points array is to store players' current points
        // points = new int[numPlayers];

        playerBorder = new TitledBorder[4];
        for (int j = 0; j < names.length; j++) {
            playerBorder[j] = BorderFactory.createTitledBorder(names[j]);
            playerBorder[j].setTitleJustification(TitledBorder.LEFT);
            players[j] = new JPanel();
            players[j].setBorder(playerBorder[j]);
            pointLabel[j] = new JLabel("0");
            // points[j] = 0;
            players[j].add(pointLabel[j]);
            playersPan.add(players[j]);
        }

        leftBox.add(playersPan);

        // display cards
        // TO DO : receive pictures and add listener
        JPanel cardPan = new JPanel(new GridLayout(1, 4));
        cardButtons = new JButton[4];
        for (int i = 0; i < 4; i++) {
            //cardButtons[i] = new JButton("card" + i);
            //cardPan.add(cardButtons[i]);
            java.net.URL imgURL = Game24ClientMain.class.getResource("images/"+i+".png");
            ImageIcon image = new ImageIcon(imgURL);
            cardButtons[i] = new JButton(image);
            cardPan.add(cardButtons[i]);
        }
        leftBox.add(cardPan);

        // add operations
        // TO DO: add listener
        JPanel operationsPan = new JPanel(new GridLayout(1, 6));
        operationButtons = new JButton[6];
        operationButtons[0] = new JButton("+");
        operationButtons[1] = new JButton("-");
        operationButtons[2] = new JButton("*");
        operationButtons[3] = new JButton("/");
        operationButtons[4] = new JButton("(");
        operationButtons[5] = new JButton(")");
        for (int i = 0; i < 6; i++) {
            operationsPan.add(operationButtons[i]);
        }
        leftBox.add(operationsPan);

        // add JTextField to receive formula
        // TO DO: send formula to the server
        formula = new JTextField(40);
        String resolution = formula.getText();
        leftBox.add(formula);

        // add submit bottom
        // TO DO: add listener
        JPanel bottomPan = new JPanel(new FlowLayout());
        submitButton = new JButton("submit");
        giveupButton = new JButton("give up");
        submitButton.setEnabled(false);
        giveupButton.setEnabled(false);
        bottomPan.add(submitButton);
        bottomPan.add(giveupButton);
        leftBox.add(bottomPan);
        this.add(leftBox);
        Box rightBox=new Box(BoxLayout.PAGE_AXIS);
        incoming = new JTextArea(20, 25);
        incoming.setLineWrap(true);
        incoming.setWrapStyleWord(true);
        incoming.setEditable(false);
        
        DefaultCaret caret = (DefaultCaret)incoming.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        incoming.append("System Announcement\n");
        incoming.append("Please wait for other players to get connected\n");
        JScrollPane qScroller = new JScrollPane(incoming);
        qScroller.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        qScroller.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        outgoing = new JTextField(25);
        sendButton = new JButton("Send");
        sendButton.setEnabled(false);
        //Add the Scroller
        rightBox.add(qScroller);
        rightBox.add(outgoing);
        rightBox.add(sendButton);
        this.add(rightBox);
        this.setVisible(true);
        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Set shortCut for submit button 
        JRootPane rootPane = SwingUtilities.getRootPane(submitButton); 
        rootPane.setDefaultButton(submitButton);
    }

}
