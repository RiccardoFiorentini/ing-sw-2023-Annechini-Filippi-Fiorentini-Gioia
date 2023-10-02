# Prova Finale (Ingegneria del software)
### Copyright
### My Shelfie is a board game developed and published by Cranio Creations Srl. The graphic content of this project related to the board game product is used with the prior approval of Cranio Creations Srl for educational purposes only. Distribution, copying, or reproduction of the content and images in any form outside of the project, as well as the redistribution and publication of the content and images for purposes other than the aforementioned, are prohibited. Commercial use of such content is also prohibited.
---

## Group PSP004

### Members:
- __Annechini Alessandro__
- __Filippi Nicole__
- __Fiorentini Riccardo__
- __Gioia Pasquale__

  
### Implemented functionalities: 
- __Complete rules__
- __RMI + Socket Protocols__
- __TUI + GUI__
- __Group and private chat__
- __Resilience to disconnections__
- __Multiple matches__

---
### How to play:
First of all, the user is requested to write the server's IP, which can be read on the server's interface once it is running. The game can be played using both the command line interface and the graphic interface: the user selects the interface once the program is launched, following the instructions.

__CLI:__ The players choose the connection protocol they want to use following the instructions. After that, they need to type a username to play. The username can't contain spaces and cannot be already used by a connected player (if an invalid username is chosen, it will be suggested a valid username). The first player entering the waiting room can select the number of players for the match, typing a number between 2 and 4. After that, if enough players are waiting, the match starts. Once the match starts, the player whose turn is will be notified so they can play their turn by following the instructions. Every turn is divided into 3 phases: selection of the column, selection of the tiles, and buffer pickup. 

__Selection of the column:__ the player has to type a number that corresponds to one of the shelf's columns; values that are not numbers, that are not between 0 and 4 or that correspond to a full column are not accepted.

__Selection of the tiles:__ the player has to type twice two numbers separated by a space, the first pair of numbers is referred to the first chosen tile, which has to be a pickable one. The second pair of numbers needs to be referring to a tile that can be: the same as the first (one tile picked), adjacent to the first, and pickable (two tiles picked), on the same column or row with a maximum distance of two (three tiles picked), the selected tiles and the one between needs to be pickable. 

__Buffer pick up:__ the selected tiles are moved in the buffer where the player can choose in which order they want to put them in the column. They need to type the positions one at a time. The position needs to be a number between 0 and 2 and a valid one (with a tile in it). After that, the turn passes to the next player.

The players can also write a message in the public chat or a private message to another player, at any moment: 
- __Public message:__ they need to write "-m" and the message (separated by a space), the message can't be empty. 
- __Private message:__ they need to write "-pm", the name of the addressee separated by a space, and the message (that can't be empty). 

If every player disconnects, the last one remained, after playing their turn, will be a proclaimed winner if nobody reconnects in 60 seconds.

__GUI:__ The player has to choose the connection protocol they want to use to play by clicking on the correct button. After that, they need to choose a username to play. The username can't contain spaces and cannot be already used by a connected player (if an invalid username is chosen, it will be suggested a valid username). The first player who entered the waiting room can choose the number of players for the match by pushing the correct button. After that, if enough players are waiting, the match starts. Once the match is started, the player whose turn is will be notified by coloring their name in green, so they can play their turn: every turn is divided into 3 phases: selection of the column, selection of the tiles and buffer pick up. 

__Selection of the column:__ the player has to select one of the shelf's columns by clicking on it (it has to have at least one slot empty). 

__Selection of the tiles:__ the player has to select two tiles. The pickable tiles are darker than the not-pickable ones. The second tile selected can be the same as the first (one tile picked), adjacent to the first (two tiles picked), on the same column or row with a maximum distance of two (three tiles picked, the tile between needs to be pickable too). 

__Buffer pick up:__ the selected tiles are moved in the buffer where the player can choose in which order they want to put them in the column. They need to select one at a time. After that, the turn passes at the next player, whose name becomes green. 

The players can also write a message in the public chat or a private message to another player at any moment by using the chat: there is a box for the message (that can't be empty) and a selector (broadcast is the default and other players' usernames to send a private message).
If every player disconnects, the last one remained, after playing their turn, will be a proclaimed winner if nobody reconnects in 60 seconds.

### How to run:
First of all you need to save the two jars in a directory.
Then, open the terminal in that directory and type:
- "java -jar Server.jar" to launch the server program (that will print its ip address)
- "java -jar Client.jar" to launch the client program (where you need to insert the sever's ip address)
They can be also opened on different machines.

### Requisites:
You need to have a version of JRE equal or superior to 19.
To ensure that the CLI is displayed correctly, it is necessary to have an updated version of command prompt.
