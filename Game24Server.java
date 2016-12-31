package game24;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.xml.stream.events.Namespace;
import static game24.CheckAnswer.*;

// Server class for the calc 24 game
public class Game24Server {
    static int numPlayers = 4; // number of players in the game
    ArrayList<PrintWriter> clientOutputStreams; // Array of printwriters for
                                                // sending message to clients
    Game24ServerFrame serverFrame; // Server GUI Frame
    ArrayList<String> names; // Player names
    Vector<Integer> scores; // Player scores
    Stack<Integer> cards; // Deck of cards
    int[] cardArr; // An array of size 4 to store the cards for the current
                   // round
    private DataInputStream inData;
    int giveupNum; // Number of players give up in the current round

    // Constructor
    public Game24Server() {
        // Initialize class fields
        serverFrame = new Game24ServerFrame();
        names = new ArrayList<String>();
        scores = new Vector<Integer>();
        giveupNum = 0;
        cardArr = new int[4];
        // Create a deck of cards and then shuffle the deck
        cards = new Stack<Integer>();
        for (int i = 0; i <= 51; i++) {
            cards.add(i);
        }
        Collections.shuffle(cards);
        // System.out.println(cards);
    }// constructor close

    // Inner class to handle one new client
    public class ClientHandler implements Runnable {
        BufferedReader reader; // Buffered reader to be linked with client
                               // socket's input stream
        Socket sock; // Client socket

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
        } // constructor close

        // Keep reading from the client. If received a string,
        // take appropriate action according to the indicator string.
        // Indicator string protocol:
        // "new answer" for new submit
        // "new give up" for player give up
        // "new chat" for chatting room message
        public void run() {
            String message;
            try {
                while ((message = reader.readLine()) != null) {
                    System.out.println("Server received " + message);
                    // If "new answer"
                    if (message.equals("new answer")) {
                        // Get player info
                        String name = reader.readLine();
                        System.out.println("New answer from " + name);
                        // Check player formula
                        message = reader.readLine();
                        int result = CheckAnswer(message, cardArr);
                        // If the answer is accepted, update player scores, goto
                        // a new round
                        if (result == 1) {
                            System.out.println("Answer from " + name + " ac");
                            // Update scores
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
                                System.out.println("Score sent to player " + i);
                                tellEveryone(scores.get(i).toString());
                            }

                            // Start a new round. If the deck is empty, game
                            // ends and send game result to clients and
                            // terminate
                            // the game.
                            tellEveryone("new image");
                            for (int i = 0; i < 4; i++) {
                                int currCard;
                                try {
                                    currCard = cards.pop();
                                } catch (EmptyStackException e) {
                                    tellEveryone("1");
                                    tellEveryone("1");
                                    tellEveryone("1");
                                    tellEveryone("1");
                                    tellEveryone("system info");
                                    // Get the winner
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
                                } // Try-catch block
                                System.out.println("image sent: " + currCard);
                                // Update cardArr
                                cardArr[i] = currCard % 13 + 1;
                                tellEveryone(Integer.toString(currCard));
                            } // for
                              // Refresh giveupNum
                            giveupNum = 0;
                        } // end of case result == 1

                        // If the player submitted wrong answer, return an
                        // informative message and keep running the current
                        // round.
                        else if (result == 0) {
                            System.out.println("WA from " + name);
                            tellEveryone("system info");
                            tellEveryone(name + " submitted wrong answer");
                        } // end of case result == 1

                        // If the player submitted answer with syntax error,
                        // return an informative message and keep running the
                        // current round.
                        else if (result == -1) {
                            System.out.println("Syntax error from " + name);
                            tellEveryone("system info");
                            tellEveryone(name
                                    + " submitted amswer with syntax error");
                        } // end of case result == -1
                    } // end of case message.equal("new answer")

                    // If a client sends give up. Check the number of players
                    // give up in the current round. If all players selected
                    // give up, goto next round.
                    else if (message.equals("new give up")) {
                        giveupNum++;
                        tellEveryone("system info");
                        String name = reader.readLine();
                        tellEveryone(name + " give up");
                        // If all players give up
                        if (giveupNum == numPlayers) {
                            tellEveryone("system info");
                            tellEveryone(
                                    "All players give up, go to a new round");
                            // Start a new round. If the deck is empty, game
                            // ends and send game result to clients and
                            // terminate the game.
                            tellEveryone("new image");
                            for (int i = 0; i < 4; i++) {
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
                                } // end of try-catch block
                                System.out.println("image sent: " + currCard);
                                // Update cardArr
                                cardArr[i] = currCard % 13 + 1;
                                tellEveryone(Integer.toString(currCard));
                            } // for
                              // Refresh giveupNum
                            giveupNum = 0;
                        } // if
                    } // case of message.equals("new give up")

                    // If a client sends chatting room message, send it to
                    // every other client
                    else if (message.equals("new chat")) {
                        System.out.println("Server got new chat");
                        message = reader.readLine();
                        tellEveryone("system info");
                        tellEveryone(message);
                    } // Case of message.equals("new chat")
                } // close while
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } // close run
    }// close inner ClientHandler class

    // This function starts the server and wait for clients to get connected.
    // After the connection is properly set up, it creates a independent thread
    // for each client socket so that the server can handle multiple clients
    // concurrently.
    public void go() {
        clientOutputStreams = new ArrayList<PrintWriter>();
        // Establish the server socket and set up connections with client socket
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
            
            // Send number of players to all clients
            tellEveryone("number of players");
            tellEveryone(Integer.toString(numPlayers));
            // Send names to all clients
            tellEveryone("name");
            // System.out.println("Name indicator sent");
            for (int i = 0; i < numPlayers; i++) {
                tellEveryone(Game24Server.this.names.get(i));
            }
            // Send initial Scores to all clients
            tellEveryone("new score");
            for (int i = 0; i < numPlayers; i++) {
                tellEveryone(Game24Server.this.scores.get(i).toString());
            }
            // Distribute first round cards
            tellEveryone("new image");
            for (int i = 0; i < 4; i++) {
                int currCard = cards.pop();
                System.out.println("image sent: " + currCard);
                cardArr[i] = currCard % 13 + 1;
                tellEveryone(Integer.toString(currCard));
            }

            // Start new threads for each of client sockets
            for (int i = 0; i < numPlayers; i++) {
                Thread t = new Thread(new ClientHandler(clientSocket[i]));
                t.start();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } // end of try-catch block
    } // close go

    // Send the message to every client socket
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

    // Read a line from the client socket/
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
    } // close recvString
}// close Game24Server class
