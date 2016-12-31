package game24;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

// Client class for the calc 24 game
public class Game24Client {
    String name; // name of the player
    Game24ClientFrame clientFrame; // client GUI frame
    BufferedReader reader; // socket reader
    PrintWriter writer; // socket writer
    Socket sock; // client socket
    int numPlayers; // number of players in the game

    // Constructor
    public Game24Client() {
        // Initialize class fields
        clientFrame = new Game24ClientFrame();
        clientFrame.submitButton.addActionListener(new SubmitButtonListener());
        clientFrame.giveupButton.addActionListener(new GiveUpButtonListener());
        clientFrame.sendButton.addActionListener(new SendButtonListener());
        numPlayers = 4;
        // Prompt user to input Server IP Address
        InputDialog IPDialog = new InputDialog(clientFrame, "Server IP Input",
                "Enter Server IP #");
        String ipAddr = IPDialog.nameField.getText();
        // Prompt user to input player name
        InputDialog nameDialog = new InputDialog(clientFrame,
                "Player Name Input", "Enter your name");
        name = nameDialog.nameField.getText();
        // Set up connection with server.
        setUpNetworking(ipAddr);
        // Send player info to server
        writer.println(name + (char) 0);
        writer.flush();
        System.out.println("Client sent " + name);
    }// constructor close

    // Set up a thread for the client to keep receiving strings from the server.
    public void go() {
        Thread readerThread = new Thread(new IncomingReader());
        readerThread.start();
    }// close go

    // Set up connection with the server
    private void setUpNetworking(String ipAddr) {
        // make a Socket, then make a PrintWriter
        // assign the PrintWriter to writer instance variable
        try {
            sock = new Socket(ipAddr, 8888);
            InputStreamReader streamReader = new InputStreamReader(
                    sock.getInputStream());
            reader = new BufferedReader(streamReader);
            writer = new PrintWriter(sock.getOutputStream());
            System.out.println("networking established");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }// close setUpNetworking

    // Listener class for the submit button
    public class SubmitButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            // get the text from the formula text field and
            // send it to the server using the writer (a PrintWriter)
            try {
                writer.println("new answer");
                writer.println(name);
                writer.println(clientFrame.formula.getText());
                writer.flush();
                System.out.println(
                        name + " sent: " + clientFrame.formula.getText());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            clientFrame.formula.setText("");
            clientFrame.formula.requestFocus();
        }
    } // close SendButtonListener inner class

    // Listener class for give up button
    public class GiveUpButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            // send give up indicator to server using the writer (a
            // PrintWriter) and disable buttons to prevent players from
            // submitting new answers
            try {
                writer.println("new give up");
                writer.println(name);
                writer.flush();
                System.out.println(name + " sent: give up");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            clientFrame.formula.setText("");
            clientFrame.formula.requestFocus();
            clientFrame.submitButton.setEnabled(false);
            clientFrame.giveupButton.setEnabled(false);
        }
    } // close SendButtonListener inner class

    // Listener class for send message button
    public class SendButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            // get the text from the chatting text field and
            // send it to the server using the writer (a PrintWriter)
            try {
                writer.println("new chat");
                writer.println(name + ": " + clientFrame.outgoing.getText());
                writer.flush();
                System.out.println(
                        name + "sent: " + clientFrame.outgoing.getText());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            clientFrame.outgoing.setText("");
            clientFrame.formula.requestFocus();
        }
    } // close SendButtonListener inner class

    // Inner class to communicate with the server
    public class IncomingReader implements Runnable {
        // Keep reading from the server. If received a string,
        // take appropriate action according to the indicator string.
        // Indicator string protocol:
        // "name" for player names info
        // "new image" for new round card set up
        // "new scores" for player scores info
        // "system info" for new system announcement
        public void run() {
            String message;
            try {
                System.out.println("Client ready to receive message");
                while ((message = reader.readLine()) != null) {
                    System.out.println("client received: " + message);
                    // if "name", update the name of players on GUI.
                    if (message.equals("name")) {
                        for (int i = 0; i < numPlayers; i++) {
                            message = reader.readLine();
                            // System.out.println("Name Received: " + message);
                            // Game24Client.this.clientFrame.names[i] = message;
                            // Game24Client.this.clientFrame.playerBorder[i] =
                            // BorderFactory.createTitledBorder(message);
                            Game24Client.this.clientFrame.players[i].setBorder(
                                    BorderFactory.createTitledBorder(message));
                        }
                        clientFrame.sendButton.setEnabled(true);
                    } // end case "name"

                    // if "new score", update player scores on GUI
                    else if (message.equals("new score")) {
                        for (int i = 0; i < numPlayers; i++) {
                            message = reader.readLine();
                            System.out.println("Score Received: " + message);
                            Game24Client.this.clientFrame.pointLabel[i]
                                    .setText(message);
                        }
                    } // end case "new score"

                    // if "new image", update card images on GUI.
                    else if (message.equals("new image")) {
                        for (int i = 0; i < 4; i++) {
                            message = reader.readLine();
                            System.out.println(
                                    "Image number Received: " + message);
                            java.net.URL imgURL = Game24Client.class
                                    .getResource("images/" + message + ".png");
                            ImageIcon image = new ImageIcon(imgURL);
                            Game24Client.this.clientFrame.cardButtons[i]
                                    .setIcon(image);
                        }
                        clientFrame.submitButton.setEnabled(true);
                        clientFrame.giveupButton.setEnabled(true);
                    } // end case "new image"

                    // if "system info", update system announcement area
                    else if (message.equals("system info")) {
                        message = reader.readLine();
                        System.out.println("System info Received: " + message);
                        Game24Client.this.clientFrame.incoming
                                .append(message + "\n");
                    } // end case "system info"
                    
                    else if (message.equals("number of players")) {
                        message = reader.readLine();
                        numPlayers = Integer.parseInt(message);
                    }
                } // while
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } // close run
    } // close IncomingReader class
}// close outer class
