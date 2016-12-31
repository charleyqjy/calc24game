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

// Calc 24 game GUI mainframe class
public class Game24ClientFrame extends JFrame {
    JPanel[] players; // Player panel
    JLabel[] pointLabel; // JLabels for player points
    JButton[] cardButtons; // JButtons for cards
    JButton[] operationButtons;// Buttons for valid operations
    JButton submitButton; // Submit answer button
    JButton giveupButton; // Give up button
    JButton sendButton; // Send chatting message button
    JTextField formula; // JTextField for answer input
    JTextArea incoming; // JTextArea for system information
    JTextField outgoing; // JTextField for chatting message input

    // constructor
    public Game24ClientFrame() {
        super("Calc 24 Game");
        setLayout(new BoxLayout(this.getContentPane(), BoxLayout.LINE_AXIS));
        // left box to hold game frame
        Box leftBox = new Box(BoxLayout.PAGE_AXIS);

        // players Panel to hold player info labels.
        JPanel playersPan = new JPanel(new GridLayout(1, 4));
        // initialize player labels
        players = new JPanel[4];
        pointLabel = new JLabel[4];
        for (int i = 0; i < 4; i++) {
            players[i] = new JPanel();
            players[i].setBorder(BorderFactory.createTitledBorder("--"));
            pointLabel[i] = new JLabel("N/A");
            players[i].add(pointLabel[i]);
            playersPan.add(players[i]);
        }
        leftBox.add(playersPan);

        // display cards
        JPanel cardPan = new JPanel(new GridLayout(1, 4));
        cardButtons = new JButton[4];
        for (int i = 0; i < 4; i++) {
            java.net.URL imgURL = Game24ClientMain.class
                    .getResource("images/" + i + ".png");
            ImageIcon image = new ImageIcon(imgURL);
            cardButtons[i] = new JButton(image);
            cardPan.add(cardButtons[i]);
        }
        leftBox.add(cardPan);

        // show available operations above the formula text area
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
        formula = new JTextField(40);
        leftBox.add(formula);

        // add submit bottom
        JPanel bottomPan = new JPanel(new FlowLayout());
        submitButton = new JButton("submit");
        giveupButton = new JButton("give up");
        submitButton.setEnabled(false);
        giveupButton.setEnabled(false);
        bottomPan.add(submitButton);
        bottomPan.add(giveupButton);
        leftBox.add(bottomPan);
        this.add(leftBox);

        // right box holds system info dash board and chatting message input
        // area
        Box rightBox = new Box(BoxLayout.PAGE_AXIS);
        incoming = new JTextArea(20, 25);
        incoming.setLineWrap(true);
        incoming.setWrapStyleWord(true);
        incoming.setEditable(false);
        // Set auto scroll down for JTextArea
        DefaultCaret caret = (DefaultCaret) incoming.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        incoming.append("System Announcement\n");
        incoming.append("Please wait for other players to get connected\n");
        JScrollPane qScroller = new JScrollPane(incoming);
        qScroller.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        qScroller.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        // Initialize chatting message input text field
        outgoing = new JTextField(25);
        sendButton = new JButton("Send");
        sendButton.setEnabled(false);
        // Add the components to right box
        rightBox.add(qScroller);
        rightBox.add(outgoing);
        rightBox.add(sendButton);
        this.add(rightBox);
        this.setVisible(true);
        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Set shortCut for submit button (press "enter" for quick submit )
        JRootPane rootPane = SwingUtilities.getRootPane(submitButton);
        rootPane.setDefaultButton(submitButton);
    }

}
