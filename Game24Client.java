package game24;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class Game24Client {
    String name;
    Game24ClientFrame clientFrame;
    // JTextArea incoming;
    BufferedReader reader;
    PrintWriter writer;
    Socket sock;

    public Game24Client() {
        clientFrame = new Game24ClientFrame();
        clientFrame.submitButton.addActionListener(new SubmitButtonListener());
        clientFrame.giveupButton.addActionListener(new GiveUpButtonListener());
        clientFrame.sendButton.addActionListener(new SendButtonListener());
        // user input Server IP Address
        InputDialog IPDialog = new InputDialog(clientFrame, "IP Input",
                "Enter Server IP #");
        String ipAddr = IPDialog.nameField.getText();
        // user name input
        InputDialog nameDialog = new InputDialog(clientFrame,
                "Player Name Input", "Enter your name");
        name = nameDialog.nameField.getText();
        setUpNetworking(ipAddr);
        writer.println(name + (char) 0);
        writer.flush();
        System.out.println("Client sent " + name);
    }

    public void go() {
        Thread readerThread = new Thread(new IncomingReader());
        readerThread.start();
    }

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
    }

    // Listener class for the submit button
    public class SubmitButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            // get the text from the text field and
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

    public class GiveUpButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            // get the text from the text field and
            // send it to the server using the writer (a PrintWriter)
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

    public class SendButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            // get the text from the text field and
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

    public class IncomingReader implements Runnable {
        public void run() {
            String message;
            try {
                System.out.println("Client ready to receive message");
                while ((message = reader.readLine()) != null) {
                    System.out.println("client received: " + message);
                    if (message.equals("name")) {
                        for (int i = 0; i < 4; i++) {
                            message = reader.readLine();
                            System.out.println("Name Received: " + message);
                            Game24Client.this.clientFrame.names[i] = message;
                            Game24Client.this.clientFrame.playerBorder[i] = BorderFactory
                                    .createTitledBorder(message);
                            Game24Client.this.clientFrame.players[i].setBorder(
                                    Game24Client.this.clientFrame.playerBorder[i]);
                        }
                        clientFrame.sendButton.setEnabled(true);
                    } else if (message.equals("new score")) {
                        for (int i = 0; i < 4; i++) {
                            message = reader.readLine();
                            System.out.println("Score Received: " + message);
                            Game24Client.this.clientFrame.pointLabel[i]
                                    .setText(message);
                        }
                    } else if (message.equals("new image")) {
                        for (int i = 0; i < 4; i++) {
                            message = reader.readLine();
                            System.out.println(
                                    "Image number Received: " + message);
                            // TODO: Update JButton image
                            java.net.URL imgURL = Game24Client.class
                                    .getResource("images/" + message + ".png");
                            ImageIcon image = new ImageIcon(imgURL);
                            Game24Client.this.clientFrame.cardButtons[i]
                                    .setIcon(image);
                        }
                        clientFrame.submitButton.setEnabled(true);
                        clientFrame.giveupButton.setEnabled(true);
                    } else if (message.equals("system info")) {
                        message = reader.readLine();
                        System.out.println("System info Received: " + message);
                        Game24Client.this.clientFrame.incoming
                                .append(message + "\n");
                    }
                } // close while
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } // close run
    } // close inner class
}// close outer class
