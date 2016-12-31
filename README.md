This java project implements the classical calculate 24 playing card game. It allows 1 player to practice alone or 2-4 multiple players to compete under the same local network.

The whole repository includes 2 readily runnableJava executable files (.jar files), project source code (.java files) and images used for GUI under /image folder.

The program uses Java socket to create a server client architecture. Both client and server program comes with a graphic user interface.

To start playing the game, open Calc24GameServer.jar on the server computer and input the number of expected players. For each player to get connected, open Calc24GameClient.jar. Enter the ip address of the server computer and also your name.

Right now, the game will start automatically once all players are successfully connected to the server computer, and ends after 13 rounds (a whole deck of poker cards not including Jokers). To start a new game, please restart both the server program and client programs. The "start game" button on the server GUI is left to be developed to ease the bother.

The client program GUI contains a text input area under a row of available operators in the game. To submit an answer in the game, input your formula there and click the submit answer button (or press "enter" on keyboard). Players can skip a round if all players clicked the give up button. The client GUI also implements a chatting room functionality. Players can send chat messages to other players by typing in the text area below the system announcement dashboard and then click the send button.
