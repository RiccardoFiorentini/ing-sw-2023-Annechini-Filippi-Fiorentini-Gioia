# Prova Finale (Ingegneria del software)

## Group PSP004

### Members:
- __Annechini Alessandro__
- __Filippi Nicole__
- __Fiorentini Riccardo__
- __Gioia Pasquale__

  
### Implemented functionalities: 
- __Whole rules__
- __RMI + Socket Protocols__
- __TUI + GUI__
- __Group and private chat__
- __Resilience to disconnections__
- __Multiple matches__

---
### How to play:
First of all is requested the user has to write server's ip, that can be read once the server is started.
The game can be played using both the command line interface and the graphic interface: the user has to select the interface once the program is launched, following the instructions.
CLI:
The player has to choose the connection protocol they want to use to play, by following the instructions. After that they need to choose a username to play. The username can't contain spaces and doesn't have to be duplicated (if a not valid username is chosen, is will be suggested a valid username). The first player entered in the waiting room can choose the number of players for the match, typing a number between 2 and 4. After that, if enough players are waiting, the match starts.
Once the match is started, the player whose turn is, will be notified so they can play their turn by following the instructions. Every turn is divided in 3 phases: selection of the column, selection of the tiles and buffer pick up.
Selection of the column: the player has to type a number that corresponds one of the shelf's column, are not accepted values that are not numbers, that are not  between 0 and 4 or that correspond to a full column.
Selection of the tiles: the player has to type twice two numbers separated by a space, the first numbers are referred to the first chosen tile, that has to be a pickable one. The second numbers needs to be referring to a tile that can be: the same of the first (one tile picked), adjacent to the first and pickable (two tiles picked), on the same column or row with a maximum distance of two (three tiles picked), the selected tiles and the one between needs to be pickable.
Buffer pick up: the selected tiles are moved in the buffer where the player can choose in which order they want to put them in the column. They need to type the positions one at time. The position needs to be a number between 0 and 2 and a valid one (with a tile in it).
After that, the turn passes at the next player.
The players can also write a message in the public chat or a private message to another player, in any moment:
Public message: they need to write "-m" and the message (separated by a space), the message can't be empty.
Private message: they need to write "-pm", the name of the addressee separated by a space and the message (that can't be empty).
If every player disconnects, the last one remained, after playing their turn, will be proclamated winner if nobody reconnects in 60 seconds.

GUI:
The player has to choose the connection protocol they want to use to play, pushing the correct button. After that they need to choose a username to play. The username can't contain spaces and doesn't have to be duplicated (if a not valid username is chosen, is will be suggested a valid username). The first player entered in the waiting room can choose the number of players for the match by pushing the correct button. After that, if enough players are waiting, the match starts.
Once the match is started, the player whose turn is, will be notified by coloring their name in green, so they can play their turn: every turn is divided in 3 phases: selection of the column, selection of the tiles and buffer pick up.
Selection of the column: the player has to select one of the shelf's column by clicking on it (it has to have at least one slot empty).
Selection of the tiles: the player has to select two tiles. The pickable tiles are darker than the not pickable one.The second tile selected can be: the same of the first (one tile picked), adjacent to the first (two tiles picked), on the same column or row with a maximum distance of two (three tiles picked, the tile between needs to be pickable too).
Buffer pick up: the selected tiles are moved in the buffer where the player can choose in which order they want to put them in the column. They need to select one at time.
After that, the turn passes at the next player, whose name become green.
The players can also write a message in the public chat or a private message to another player in any moment by using the chat, there is a box for the message (that can't be empty) and a selector (broadcast is the defaul and other players' usernames to send a private message).
If every player disconnects, the last one remained, after playing their turn, will be proclamated winner if nobody reconnects in 60 seconds.

### How to run:
First of all you need to save the two jars in a directory.
Then, open the terminal in that directory and type:
"java -jar Server.jar" to launch the server program (that will print its ip address)
"java -jar Client.jar" to launch the client program (where you need to insert the sever's ip address)
They can be also open on different machines.

### Requisites:
You need to have a version of JRE equals or superior to 19.
For the CLI to run properly, you need to use a Windows 11 command prompt or Hyper, a third part command prompt.
