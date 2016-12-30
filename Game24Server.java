package game24;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.xml.stream.events.Namespace;
import static game24.CheckAnswer.*;

public class Game24Server {
    static int numPlayers = 4;
    ArrayList<PrintWriter> clientOutputStreams;
    Game24ServerFrame serverFrame;
    ArrayList<String> names;
    Vector<Integer> scores;// Player scores
    Stack<Integer> cards;
    int[] cardArr;
    private DataInputStream inData;
    int giveupNum;

    // Constructor
    public Game24Server() {
        serverFrame = new Game24ServerFrame();
        names = new ArrayList<String>();
        scores = new Vector<Integer>();
        giveupNum = 0;
        cards = new Stack<Integer>();
        for (int i = 0; i <= 51; i++) {
            cards.add(i);
        }
        Collections.shuffle(cards);
        System.out.println(cards);
        cardArr = new int[4];
    }

    // Inner class to handle new client
    public class ClientHandler implements Runnable {
        BufferedReader reader;
        Socket sock;

        // constructor
        public ClientHandler(Socket clientSocket) {
            try {
                sock = clientSocket;
                InputStreamReader isReader = new InputStreamReader(
                        sock.getInputStream());
                reader = new BufferedReader(isReader);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } // close

        // Keep reading from the client. If received a string,
        // take appropriate action according to the indicator string.
        // "new answer" for new submit
        // "new give up" for player give up
        public void run() {
            String message;
            try {
                while ((message = reader.readLine()) != null) {
                    System.out.println("Server received " + message);
                    if (message.equals("new answer")) {
                        String name = reader.readLine();
                        System.out.println("New answer from " + name);
                        message = reader.readLine();
                        // int result = 1;
                        // Check player formula
                        int result = CheckAnswer(message, cardArr);
                        if (result == 1) {
                            System.out.println("Answer from " + name + " ac");
                            for (int i = 0; i < numPlayers; i++) {
                                if (name.equals(names.get(i))) {
                                    scores.set(i, scores.get(i) + 1);
                                }
                            }
                            tellEveryone("system info");
                            tellEveryone(name + " wins this round");
                            tellEveryone("system info");
                            tellEveryone("Answer is " + message);

                            // Send new scores back
                            tellEveryone("new score");
                            for (int i = 0; i < numPlayers; i++) {
                                System.out.println("Score sent to player" + i);
                                tellEveryone(scores.get(i).toString());
                            }

                            // Start a new round
                            tellEveryone("new image");
                            for (int i = 0; i < numPlayers; i++) {
                                int currCard;
                                try {
                                    currCard = cards.pop();
                                } catch (EmptyStackException e) {
                                    tellEveryone("1");
                                    tellEveryone("1");
                                    tellEveryone("1");
                                    tellEveryone("1");
                                    tellEveryone("system info");
                                    int max = 0;
                                    for (int j = 0; j < numPlayers; j++) {
                                        if (scores.get(j) > scores.get(max))
                                            max = j;
                                    }
                                    tellEveryone("Game ends. " + names.get(max)
                                            + " wins!");
                                    tellEveryone("system info");
                                    tellEveryone("Congratulation!~~");
                                    return;
                                }
                                System.out.println("image sent: " + currCard);
                                cardArr[i] = currCard % 13 + 1;
                                tellEveryone(Integer.toString(currCard));
                                // tellEveryone(new Integer(0).toString());
                            }
                            // Refresh giveupNum
                            giveupNum = 0;
                        } else if (result == 0) {
                            System.out.println("WA from " + name);
                            tellEveryone("system info");
                            tellEveryone(name + " submitted wrong answer");
                        } else if (result == -1) {
                            System.out.println("Syntax error from " + name);
                            tellEveryone("system info");
                            tellEveryone(name
                                    + " submitted amswer with syntax error");
                        }
                    } else if (message.equals("new give up")) {
                        giveupNum++;
                        tellEveryone("system info");
                        String name = reader.readLine();
                        tellEveryone(name + " give up");
                        if (giveupNum == numPlayers - 1) {
                            tellEveryone("system info");
                            tellEveryone(
                                    "Most players give up, go to a new round");
                            // Start a new round
                            tellEveryone("new image");
                            // TODO: modify this! Send 4 ints from the deck
                            for (int i = 0; i < numPlayers; i++) {
                                int currCard;
                                try {
                                    currCard = cards.pop();
                                } catch (EmptyStackException e) {
                                    tellEveryone("1");
                                    tellEveryone("1");
                                    tellEveryone("1");
                                    tellEveryone("1");
                                    tellEveryone("system info");
                                    int max = 0;
                                    for (int j = 0; j < numPlayers; j++) {
                                        if (scores.get(j) > scores.get(max))
                                            max = j;
                                    }
                                    tellEveryone("Game ends. " + names.get(max)
                                            + " wins!");
                                    tellEveryone("system info");
                                    tellEveryone("Congratulation!~~");
                                    return;
                                }
                                System.out.println("image sent: " + currCard);
                                cardArr[i] = currCard % 13 + 1;
                                tellEveryone(Integer.toString(currCard));
                                // tellEveryone(new Integer(0).toString());
                            }
                            // Refresh giveupNum
                            giveupNum = 0;
                        }
                    } else if (message.equals("new chat")) {
                        System.out.println("Server got new chat");
                        message = reader.readLine();
                        tellEveryone("system info");
                        tellEveryone(message);
                    }
                } // close while
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } // close run
    }// close inner class

    public void go() {
        clientOutputStreams = new ArrayList<PrintWriter>();
        try {
            ServerSocket serverSock = new ServerSocket(8888);
            Socket[] clientSocket = new Socket[numPlayers];

            // Wait for all players to get connected
            for (int i = 0; i < numPlayers; i++) {
                clientSocket[i] = serverSock.accept();
                inData = new DataInputStream(clientSocket[i].getInputStream());
                PrintWriter writer = new PrintWriter(
                        clientSocket[i].getOutputStream());
                clientOutputStreams.add(writer);
                System.out.println("got a connection");
                // Read new player name
                String message = recvString();
                System.out.println(message);
                // Update player info
                names.add(message);
                scores.add(0);
            }

            // Send names to all players
            tellEveryone("name");
            System.out.println("Name indicator sent");
            for (int i = 0; i < numPlayers; i++) {
                tellEveryone(Game24Server.this.names.get(i));
            }
            // Distribute first round cards
            tellEveryone("new image");
            for (int i = 0; i < numPlayers; i++) {
                int currCard = cards.pop();
                System.out.println("image sent: " + currCard);
                cardArr[i] = currCard % 13 + 1;
                tellEveryone(Integer.toString(currCard));
            }

            // Start new threads
            for (int i = 0; i < numPlayers; i++) {
                Thread t = new Thread(new ClientHandler(clientSocket[i]));
                t.start();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    } // close
      // go

    // Send the message to everyone
    public void tellEveryone(String message) {
        Iterator it = clientOutputStreams.iterator();
        while (it.hasNext()) {
            try {
                PrintWriter writer = (PrintWriter) it.next();
                writer.println(message);
                writer.flush();
                System.out.println("Server sent: " + message);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } // end while
    } // close tellEveryone

    public String recvString() {
        Vector<Byte> byteVec = new Vector<Byte>();
        byte[] byteAry;
        byte recByte;
        String receivedString = "";

        try {
            // read the data coming in byte by byte
            // until the end flag 0
            recByte = inData.readByte();
            while (recByte != 0) {
                // System.out.println(recByte);
                byteVec.add(recByte);
                recByte = inData.readByte();
            }
            // System.out.println("Good");
            // change the byte array to string
            byteAry = new byte[byteVec.size()];
            for (int i = 0; i < byteVec.size(); i++) {
                byteAry[i] = byteVec.elementAt(i).byteValue();
            }
            // System.out.println("Size:" + byteAry.length);
            receivedString = new String(byteAry);
            System.out.println("Received String:" + receivedString);
        } catch (IOException ioe) {
            System.out.println("ERROR: receiving string from socket");
            System.exit(8);
        }
        return receivedString;
    }
}
