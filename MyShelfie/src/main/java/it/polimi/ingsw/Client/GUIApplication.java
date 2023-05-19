package main.java.it.polimi.ingsw.Client;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.java.it.polimi.ingsw.Connection.ClientConnectionHandler;
import main.java.it.polimi.ingsw.Controller.Command;
import main.java.it.polimi.ingsw.Controller.CommandType;
import main.java.it.polimi.ingsw.Controller.Response;
import main.java.it.polimi.ingsw.Model.*;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class GUIApplication extends Application{

    private GameState state;
    private String playerNickname;
    private int playerTurnId;
    private Stage stage;
    private Parent root;
    private ImageView imageViewWallpaper;
    private ImageView endWallpaper;
    private StackPane endStackPane;
    private GridPane resultPane;
    private Text winner;

    private GridPane specificPointsPopup;

    /*
    private HashMap<String, Integer> finalLeaderbord;
    private List<Integer> finalPoints;
    */

    private Label nicknameLabel;
    private Image wallpaper;
    private Image title;
    private TextField textField;
    private ImageView imageViewTitle;
    private StackPane stackPane;
    private VBox vBox;
    private Scene scene;
    private Rectangle2D bounds;
    private Label messages;

    private Button button1;
    private Button button2;
    private Button button3;
    private Button buttonRMI;
    private Button buttonSocket;
    private HBox hBox;
    private HBox hBoxNet;
    private FadeTransition fadeTransition1;
    private FadeTransition fadeTransition2;
    private FadeTransition fadeTransition3;
    private FadeTransition fadeTransitionRMI;
    private FadeTransition fadeTransitionSocket;
    private ImageView board;
    private ImageView playerShelf;
    private double stdScreenRatio;
    private double boardTileRatio = 0.1008;
    private double shelfTileRatio = 0.127;
    private ImageView[][] boardTiles;
    private ImageView[][] playerShelfTiles;
    int numPlayers=4;
    private List<ImageView> otherPlayerShelves;
    private List<ImageView[][]> otherPlayersTiles;
    private ClientConnectionHandler cch;

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        setupMenu();
    }

    /**
     * Set up the first scene
     * @author Fiorentini Riccardo
     */
    public void setupMenu(){

        Screen screen = Screen.getPrimary();
        bounds = screen.getVisualBounds();
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());
        Font font = new Font("Calibri", 33);

        double ratio = bounds.getWidth()*0.05;

        button1 = new Button("2");
        button2 = new Button("3");
        button3 = new Button("4");
        button1.setPrefWidth(ratio);
        button1.setMaxHeight(ratio);
        button2.setPrefWidth(ratio);
        button2.setMaxHeight(ratio);
        button3.setPrefWidth(ratio);
        button3.setMaxHeight(ratio);
        button1.setFont(font);
        button2.setFont(font);
        button3.setFont(font);
        button1.setStyle("-fx-background-color: white;");
        button2.setStyle("-fx-background-color: white;");
        button3.setStyle("-fx-background-color: white;");

        fadeTransition1 = new FadeTransition(Duration.seconds(1), button1);
        fadeTransition1.setFromValue(0.0);
        fadeTransition1.setToValue(1.0);
        fadeTransition1.setAutoReverse(true);
        fadeTransition1.setCycleCount(1);

        fadeTransition2 = new FadeTransition(Duration.seconds(1), button2);
        fadeTransition2.setFromValue(0.0);
        fadeTransition2.setToValue(1.0);
        fadeTransition2.setAutoReverse(true);
        fadeTransition2.setCycleCount(1);

        fadeTransition3 = new FadeTransition(Duration.seconds(1), button3);
        fadeTransition3.setFromValue(0.0);
        fadeTransition3.setToValue(1.0);
        fadeTransition3.setAutoReverse(true);
        fadeTransition3.setCycleCount(1);

        hBox = new HBox(10, button1, button2, button3);
        hBox.setPadding(new Insets(10));
        hBox.setVisible(false);
        hBox.setAlignment(Pos.CENTER);

        //RMI e SOCKET Buttons
        buttonRMI = new Button("RMI");
        buttonSocket = new Button("SOCKET");
        buttonRMI.setPrefWidth(ratio*3);
        buttonRMI.setMaxHeight(ratio);
        buttonSocket.setPrefWidth(ratio*3);
        buttonSocket.setMaxHeight(ratio);
        buttonRMI.setFont(font);
        buttonSocket.setFont(font);
        buttonRMI.setStyle("-fx-background-color: white;");
        buttonSocket.setStyle("-fx-background-color: white;");

        fadeTransitionRMI = new FadeTransition(Duration.seconds(2), buttonRMI);
        fadeTransitionRMI.setFromValue(0.0);
        fadeTransitionRMI.setToValue(1);
        fadeTransitionRMI.setAutoReverse(true);
        fadeTransitionRMI.setCycleCount(1);

        fadeTransitionSocket = new FadeTransition(Duration.seconds(2), buttonSocket);
        fadeTransitionSocket.setFromValue(0.0);
        fadeTransitionSocket.setToValue(1);
        fadeTransitionSocket.setAutoReverse(true);
        fadeTransitionSocket.setCycleCount(1);

        DropShadow shadow = new DropShadow();
        buttonSocket.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, e -> buttonSocket.setEffect(shadow));
        buttonRMI.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, e -> buttonRMI.setEffect(shadow));
        buttonSocket.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, e -> buttonSocket.setEffect(null));
        buttonRMI.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, e -> buttonRMI.setEffect(null));

        buttonSocket.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            startConnection(2);
        });

        buttonRMI.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            startConnection(1);
        });

        hBoxNet = new HBox(10, buttonRMI, buttonSocket);
        hBoxNet.setPadding(new Insets(10));
        hBoxNet.setAlignment(Pos.CENTER);

        wallpaper = new Image(getClass().getResource("/misc/sfondo parquet.jpg").toString());
        imageViewWallpaper = new ImageView(wallpaper);
        stackPane = new StackPane(imageViewWallpaper);
        imageViewWallpaper.setFitHeight(bounds.getHeight());
        imageViewWallpaper.setFitWidth(bounds.getWidth());
        StackPane.setAlignment(imageViewWallpaper, Pos.CENTER);

        title = new Image(getClass().getResource("/Publisher material/Title 2000x618px.png").toString());
        imageViewTitle = new ImageView(title);
        imageViewTitle.setPreserveRatio(true);
        imageViewTitle.setFitHeight(bounds.getHeight()/4);
        StackPane.setAlignment(imageViewTitle, Pos.TOP_CENTER);
        stackPane.getChildren().add(imageViewTitle);

        nicknameLabel = new Label("Choose the network protocol");
        nicknameLabel.setFont(font);
        messages = new Label("");
        messages.setFont(font);
        textField = new TextField();
        textField.setFont(font);
        textField.setOnAction((event)->{if(textField.isEditable()){
           doLogin(textField.getText());
        }});

        textField.setEditable(false);
        textField.setOnMouseClicked((event)->{textField.setEditable(true);});
        textField.setMaxWidth(bounds.getWidth()/3);
        vBox = new VBox(10.0, (Node) nicknameLabel);
        vBox.setMaxWidth(bounds.getWidth());
        vBox.setMaxHeight(bounds.getHeight()/3);
        vBox.getChildren().add(textField);
        vBox.getChildren().add(messages);
        vBox.setAlignment(Pos.CENTER);
        textField.setVisible(false);
        stackPane.getChildren().add(vBox);
        stackPane.getChildren().add(hBox);
        stackPane.getChildren().add(hBoxNet);
        StackPane.setMargin(vBox, new Insets(0, 0, 50, 0));

        scene = new Scene(stackPane, bounds.getWidth(), bounds.getHeight());
        scene.setOnMouseClicked((event)->{textField.setEditable(false);});

        stage.setTitle("Access");
        stage.setScene(scene);
        stage.sizeToScene();
        fadeTransitionRMI.play();
        fadeTransitionSocket.play();
        stage.show();
    }

    /**
     * Starts the View of the game, creates the thread that handles the responses from the server
     * @author Nicole Filippi
     */
    public void startConnection(int type) {
        try {
            cch = Client.createConnection(type);
        } catch (RemoteException ex) {
            //TODO
        }
        nicknameLabel.setText("Choose the nickname");
        textField.setVisible(true);
        vBox.setVisible(true);
        hBoxNet.setVisible(false);

        while (true) {
            Response resp = null;
            try {
                resp = cch.getNextResponse();
            } catch (Exception e) {
                //TODO
            }

            final Response response = resp;

            if (response != null) {
                new Thread(() -> handleResponse(response)).start();

            }
        }
    }

    /**
     * Method that handles responses sent by the server to the client
     * @author Nicole Filippi
     * @param resp is the response sent from the server
     */
    public void handleResponse(Response resp) {
        if(resp == null) return;
        switch(resp.getResponseType()) {
            case LOGIN_ERROR:
                nicknameResponse(false, resp.getStrParameter("newnickname"));
                break;

            case LOGIN_CONFIRMED:
                nicknameResponse(true, resp.getStrParameter("nickname"));
                playerNickname = resp.getStrParameter("nickname");
                waitingRoom();
                break;

            case ASK_PLAYERS_NUM:
                if (resp.getStrParameter("result").equals("success")) {  //accepted value
                    waitingRoom();
                }
                break;

            case GAME_STARTED:
                Command command = new Command(CommandType.GAME_JOINED);
                sendCommand(command);
                state.setBuffer(new Tile[]{Tile.EMPTY,Tile.EMPTY,Tile.EMPTY});
                state.setFirstPlayerId(resp.getIntParameter("firstPlayerId"));
                state.setCommonGoalsId(0, resp.getIntParameter("commongoal1"));
                state.setCommonGoalsId(1, resp.getIntParameter("commongoal2"));
                state.setCommonGoalsRemainingPoint(0, resp.getIntParameter("commongoalsremainingpoint1"));
                state.setCommonGoalsRemainingPoint(1, resp.getIntParameter("commongoalsremainingpoint2"));
                state.setCurrPlayerId(resp.getIntParameter("currentplayer"));
                state.setBoard((BoardBean) resp.getObjParameter("board"));
                state.setChat((ChatBean)resp.getObjParameter("chat"));
                state.setShelves((List<ShelfBean>)resp.getObjParameter("shelves"));
                state.setNicknames((List<String>)resp.getObjParameter("nicknames"));
                state.setTurnIds((List<Integer>)resp.getObjParameter("turnIds"));
                for(int i=0; i<state.getTurnIds().size(); i++){
                    if((state.getNicknames().get(i)).equals(playerNickname))
                        playerTurnId=state.getTurnIds().get(i);
                }
                state.setPersonalGoal((TileColor[][])resp.getObjParameter("personalgoalmatrix"));
                state.setCommonGoalPoints1((List<Integer>)resp.getObjParameter("commongoalpoints1"));
                state.setCommonGoalPoints2((List<Integer>)resp.getObjParameter("commongoalpoints2"));
                state.setConnected((List<Boolean>)resp.getObjParameter("connected"));
                setupGameScreen();
                break;

            case NEW_MEX_CHAT:
                state.setChat((ChatBean) resp.getObjParameter("chat"));
                //TODO
                break;

            case NEW_TURN:
                state.setCurrPlayerId(resp.getIntParameter("CurrentPlayerId"));
                if(playerTurnId==state.getCurrPlayerId()){
                    //TODO
                }else{
                    //TODO
                }
                //TODO
                break;

            case SELECT_COLUMN_RESULT:
                if("success".equals(resp.getStrParameter("result"))){ //andato a buon fine
                    //TODO
                }else{
                    //TODO
                }
                //TODO
                break;

            case SELECT_TILE_RESULT:
                if("success".equals(resp.getStrParameter("result"))){ //andato a buon fine
                    //TODO
                    //if(first tile){}
                    //else{
                    //  buffer=(Tile[]) resp.getObjParameter("buffer");
                    // }
                }else{
                    //TODO
                }
                //TODO
                break;

            case PUT_IN_COLUMN_RESULT:
                if("success".equals(resp.getStrParameter("result"))){ //andato a buon fine
                    //TODO
                    state.setBuffer((Tile[]) resp.getObjParameter("buffer"));
                    if(resp.getIntParameter("turnFinished")==0) {
                        //TODO
                    }else{
                        //TODO
                    }
                }else{
                    //TODO
                }
                //TODO
                break;

            case UPDATE_BOARD:
                state.setBoard((BoardBean)resp.getObjParameter("board"));
                //TODO
                break;

            case UPDATE_PLAYER_SHELF:
                state.getShelves().set(resp.getIntParameter("playerid"),(ShelfBean)resp.getObjParameter("shelf"));
                //TODO
                break;

            case PLAYER_DISCONNECTED:
                state.getConnected().set(resp.getIntParameter("playerid"),false);
                //TODO
                break;

            case PLAYER_RECONNECTED:
                state.getConnected().set(resp.getIntParameter("playerid"),true);
                //TODO
                break;

            case ONLY_ONE_CONNECTED:
                //TODO
                break;

            case ONLY_ONE_CONNECTED_TIMER:
                //TODO
                break;

            case GAME_ENDED:
                if(resp.getIntParameter("interrupted")==0){ //finished correctly
                    state.setFinalPoints((List<Integer>)resp.getObjParameter("finalPoints"));
                    state.setFinalPersonalGoalPoints((List<Integer>)resp.getObjParameter("finalPoints"));
                    state.setFinalColorGroupPoints((List<Integer>)resp.getObjParameter("finalPoints"));
                    List<Integer> tmpPoints = new ArrayList<>(state.getFinalPoints());
                    List<String> tmpNick = new ArrayList<>(state.getNicknames());
                    List<Integer> resPoints = new ArrayList<>();
                    List<String> resNick = new ArrayList<>();
                    int max, maxPos;
                    while(tmpNick.size()>0){
                        max=-1;
                        maxPos=-1;
                        for(int i=0; i<tmpNick.size(); i++){
                            if(tmpPoints.get(i)>max) {
                                max = tmpPoints.get(i);
                                maxPos = i;
                            }
                        }
                        resPoints.add(max);
                        resNick.add(tmpNick.get(maxPos));
                        tmpPoints.remove((Integer)max);
                        tmpNick.remove(maxPos);
                    }
                    //TODO
                }
                else{
                    //TODO
                }
                //TODO
                break;

            case COMMON_GOAL_WON:
                if(resp.getIntParameter("commongoalid")==0) {
                    state.getCommonGoalPoints1().set(resp.getIntParameter("playerid"), resp.getIntParameter("pointswon"));
                    state.setCommonGoalsRemainingPoint(0, resp.getIntParameter("remainingpoints"));
                }else{
                    state.getCommonGoalPoints2().set(resp.getIntParameter("playerid"), resp.getIntParameter("pointswon"));
                    state.setCommonGoalsRemainingPoint(1, resp.getIntParameter("remainingpoints"));
                }
                //TODO
                break;

            case SHELF_COMPLETED:
                state.setLastPlayerId(resp.getIntParameter("playerid"));
                //TODO
        }
    }


    /**
     * Method that sends a given command to the server
     * @author Nicole Filippi
     * @param command is the command that has to be sent
     */
    public void sendCommand(Command command){
        try{
            cch.sendCommand(command);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Method that sends the command login to the server
     * @author Nicole Filippi
     * @param nickname the nickname chosen by the user
     */
    public void doLogin(String nickname) {
        if (nickname == null || nickname.equals("")) {
            //TODO handle error
            return;
        }
        Command command = new Command(CommandType.LOGIN);
        command.setStrParameter("nickname", nickname);
        sendCommand(command);
    }

    /**
     * Method that sends the command players num to the server
     * @author Nicole Filippi
     * @param num the number of players chosen by the user
     */
    public void doAskNumberPlayer(int num){
        Command command = new Command(CommandType.PLAYERS_NUM);
        command.setIntParameter("num", num);
        sendCommand(command);
    }

    /**
     * Method that sends the command select column to the server, checks if is user's turn
     * @author Nicole Filippi
     * @param column the column chosen by the user
     */
    public void doSelectColumn(int column) {
        if(playerTurnId==state.getCurrPlayerId()){
            Command command = new Command(CommandType.SELECT_COLUMN);
            command.setIntParameter("value", column);
            sendCommand(command);
        }
    }

    /**
     * Method that sends the command select tile to the server, checks if is user's turn
     * @author Nicole Filippi
     * @param row the row of the tile chosen by the user
     * @param col the column of the tile chosen by the user
     */
    public void doSelectTile(int row, int col) {
        if(playerTurnId==state.getCurrPlayerId()){
            Command command = new Command(CommandType.SELECT_TILE);
            command.setIntParameter("row", row);
            command.setIntParameter("col", col);
            sendCommand(command);
        }
    }

    /**
     * Method that sends the command put in column to the server, checks if is user's turn
     * @author Nicole Filippi
     * @param index the index of the buffer chosen by the user
     */
    public void doPutInColumn(int index) {
        if(playerTurnId==state.getCurrPlayerId()){
            Command command = new Command(CommandType.PUT_IN_COLUMN);
            command.setIntParameter("index", index);
            sendCommand(command);
        }
    }

    /**
     * Method that sends the command send mex chat to the server
     * @author Nicole Filippi
     * @param message the text of the message written by the sender
     * @param receiver the receiver of the private message chosen by the sender (null if broadcast)
     */
    public void doSendMex(String message, String receiver) {
        if (message == null || message.equals("")) {
            //TODO handle error
            return;
        }
        Command command = new Command(CommandType.SEND_MEX_CHAT);
        command.setStrParameter("text", message);
        command.setStrParameter("receiver", receiver); //null if broadcast message
        sendCommand(command);
    }


    /**
     * It handles the server's response when the user choose the nickname
     * @param valid true if the nickname is valid
     * @param newNick if the nickname already exists it's a valid substitute
     * @author Fiorentini Riccardo
     */
    public void nicknameResponse(Boolean valid, String newNick){
        Font font = new Font("Calibri", 33);
        textField.setEditable(false);
        if(valid){
            textField.setOnMouseClicked((event)->{textField.setEditable(false);});
            messages.setText("Your nickname " + newNick + " is valid!");
        } else {
            if(newNick==null){
                textField.setOnMouseClicked((event)->{textField.setEditable(true);});
                messages.setText("Your nickname is empty, try again!");
            }else{
                textField.setOnMouseClicked((event)->{textField.setEditable(true);});
                messages.setText("Your nickname is not valid, you can use " + newNick + " or try again!");
                textField.setText(newNick);
            }
        }
    }

    /**
     * show and handle numPlayer buttons
     * @param number to handle server response
     * @author Fiorentini Riccardo
     */
    public void askNumberPlayer(Integer number){
        nicknameLabel.setText("Choose number of player");
        hBox.setVisible(true);
        textField.setVisible(false);
        messages.setVisible(false);
        DropShadow shadow = new DropShadow();

        fadeTransition1.play();
        fadeTransition2.play();
        fadeTransition3.play();

        button1.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, e -> button1.setEffect(shadow));
        button2.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, e -> button2.setEffect(shadow));
        button3.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, e -> button3.setEffect(shadow));
        button1.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, e -> button1.setEffect(null));
        button2.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, e -> button2.setEffect(null));
        button3.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, e -> button3.setEffect(null));

        button1.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->  doAskNumberPlayer(2));
        button2.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->  doAskNumberPlayer(3));
        button3.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->  doAskNumberPlayer(4));
    }



    /**
     * This method prepares all the elements in the Game scene
     * @author Alessandro Annechini
     */
    public void setupGameScreen(){
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        stdScreenRatio = bounds.getWidth()/bounds.getHeight();

        board = new ImageView(new Image(getClass().getResource("/boards/livingroom.png").toString()));
        boardTiles = new ImageView[9][9];
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                ImageView tile = new ImageView(new Image(getClass().getResource("/item tiles/Cornici1.1.png").toString()));
                boardTiles[i][j] = tile;
            }
        }
        playerShelf = new ImageView(new Image(getClass().getResource("/boards/bookshelf_orth.png").toString()));

        playerShelfTiles = new ImageView[6][5];
        for(int i=0;i<6;i++){
            for(int j=0;j<5;j++){
                ImageView tile = new ImageView(new Image(getClass().getResource("/item tiles/Cornici1.1.png").toString()));
                playerShelfTiles[i][j] = tile;
            }
        }

        otherPlayerShelves = new ArrayList<>();
        otherPlayersTiles = new ArrayList<>();
        for(int k=0;k<numPlayers-1;k++){
            ImageView shelf = new ImageView(new Image(getClass().getResource("/boards/bookshelf_orth.png").toString()));
            otherPlayerShelves.add(shelf);
            ImageView[][] tmp = new ImageView[6][5];
            for(int i=0;i<6;i++){
                for(int j=0;j<5;j++){
                    ImageView tile = new ImageView(new Image(getClass().getResource("/item tiles/Cornici1.1.png").toString()));
                    tmp[i][j] = tile;
                }
            }
            otherPlayersTiles.add(tmp);
        }

        Pane boardPane = new Pane(board);
        Pane shelfPane = new Pane();
        shelfPane.getChildren().add(playerShelf);
        for(ImageView im : otherPlayerShelves) shelfPane.getChildren().add(im);

        Pane tilePane = new Pane();
        for(ImageView[] el : boardTiles){
            for(ImageView i : el) tilePane.getChildren().add(i);
        }
        for(ImageView[] arr : playerShelfTiles){
            for(ImageView elem : arr) tilePane.getChildren().add(elem);
        }
        for(ImageView[][] mat : otherPlayersTiles){
            for(ImageView[] arr : mat){
                for(ImageView elem : arr) tilePane.getChildren().add(elem);
            }
        }
        ImageView background = new ImageView(new Image(getClass().getResource("/misc/sfondo parquet.jpg").toString()));

        //ORDER OF PANELS
        StackPane stackPane = new StackPane(background,boardPane,tilePane,shelfPane);

        stackPane.setOnMouseClicked((event)->{
            for(int i = stackPane.getChildren().size()-1;i>=0 && !event.isConsumed();i--){
                if(stackPane.getChildren().get(i).getOnMouseClicked()!=null){
                    stackPane.getChildren().get(i).fireEvent(event);
                }
            }
        });

        playerShelf.setOnMouseClicked((e)->System.out.println("CLICK SHELF!!!"));
        board.setOnMouseClicked((e)->System.out.println("CLICK BOARD!!!"));

        scene = new Scene(stackPane,bounds.getWidth(),bounds.getHeight());
        stage.setScene(scene);
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.show();

        stage.widthProperty().addListener((x)->setGameGraphicsProportions());
        stage.heightProperty().addListener((x)->setGameGraphicsProportions());
        setGameGraphicsProportions();
    }

    /**
     * This method sets the positions of the Game
     * elements with respect to the window size
     * @author Alessandro Annechini
     */
    public void setGameGraphicsProportions(){
        board.setFitWidth(Math.min(stage.getWidth()/stdScreenRatio,stage.getHeight()) * 0.8 );
        board.setFitHeight(board.getFitWidth());
        board.setX(stage.getWidth()*0.44 - board.getFitWidth()/2);
        board.setY(stage.getHeight()*0.93 - board.getFitHeight());

        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                ImageView t = boardTiles[i][j];
                double margin = 1.03;
                t.setFitHeight(board.getFitHeight()*boardTileRatio/margin);
                t.setFitWidth(board.getFitWidth()*boardTileRatio/margin);
                t.setX(board.getX()+board.getFitWidth()*0.041+t.getFitWidth()*j*margin + t.getFitWidth()*(margin-1)/2);
                t.setY(board.getY()+board.getFitHeight()*0.046+t.getFitHeight()*i*margin + t.getFitHeight()*(margin-1)/2);
            }
        }

        playerShelf.setFitWidth(Math.min(stage.getWidth()/stdScreenRatio,stage.getHeight()) * 0.5 );
        playerShelf.setFitHeight(playerShelf.getFitWidth()*((double)1411/1414));
        playerShelf.setX(stage.getWidth()*0.96 - playerShelf.getFitWidth());
        playerShelf.setY(stage.getHeight()*0.9 - playerShelf.getFitHeight());

        setTilesPositionInShelf(-1);

        for(int k=0;k<numPlayers-1;k++){
            ImageView shelf = otherPlayerShelves.get(k);
            shelf.setFitWidth(Math.min(stage.getWidth()/stdScreenRatio,stage.getHeight()) * 0.275 );
            shelf.setFitHeight(shelf.getFitWidth()*((double)1411/1414));
            shelf.setX(stage.getWidth()*0.032);
            shelf.setY(stage.getHeight()*0.032 + shelf.getFitHeight()*1.13*k);

            setTilesPositionInShelf(k);
        }
    }

    /**
     * This method sets the positions of the tiles' ImageViews
     * @author Alessandro Annechini
     * @param pos -1 if the shelf corresponds to the client's player, the position in the otherPlayerShelf otherwise
     */
    public void setTilesPositionInShelf(int pos){
        ImageView shelf = pos < 0 ? playerShelf : otherPlayerShelves.get(pos);
        ImageView[][] matrix = pos < 0 ? playerShelfTiles : otherPlayersTiles.get(pos);
        for(int i=0;i<6;i++){
            for(int j=0;j<5;j++){
                ImageView t = matrix[i][j];
                t.setFitHeight(shelf.getFitHeight()*shelfTileRatio);
                t.setFitWidth(t.getFitHeight());
                t.setX(shelf.getX()+shelf.getFitWidth()*0.12+t.getFitWidth()*j*1.265);
                t.setY(shelf.getY()+shelf.getFitHeight()*0.065+t.getFitHeight()*i*1.082);
            }
        }
    }

    /**
     * This method defines end game scene
     * @author Pasquale Gioia
     */
    public void setupEndGameScreen(){
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());

        endStackPane = new StackPane();
        endWallpaper = new ImageView(new Image(getClass().getResource("/Publisher material/Display_5.jpg").toString()));
        endWallpaper.setFitHeight(bounds.getHeight());
        endWallpaper.setFitWidth(bounds.getWidth());
        StackPane.setAlignment(endWallpaper, Pos.CENTER);
        endStackPane.getChildren().add(endWallpaper);

        resultPane = new GridPane();

        /*
        SET RESULTPANE BACKGROUND COLOR -- NOT WORKING??
        Color backgroundColor = Color.rgb(0, 0, 255, 0.5);
        resultPane.setBackground(new Background(new BackgroundFill(backgroundColor, null, null)));
        */

        resultPane.setHgap(25);
        resultPane.setVgap(10);

        //Setting column properties for leaderboard pane
        ColumnConstraints col1Constraints = new ColumnConstraints();
        col1Constraints.setHgrow(Priority.ALWAYS);
        col1Constraints.setPercentWidth(90);

        ColumnConstraints col2Constraints = new ColumnConstraints();
        col2Constraints.setHgrow(Priority.ALWAYS);
        col2Constraints.setPercentWidth(10);
        resultPane.getColumnConstraints().addAll(col1Constraints, col2Constraints);




        String[] typeOfPointEntryNames = {"Personal goal:", "Common goal 1:", "Common goal 2:", "Group points:", "Final point:"};
        //TODO For each player (in arrival order) specific point array --
        int[][] specificPoints = {
                {4, 3, 0, 2, 0},
                {5, 6, 2, 5, 1},
                {3, 1, 0, 5, 0},
                {2, 2, 3, 1, 0}};

        //TODO A list of Popup pointsPopup ? one popup for each player

        Popup[] pointsPopupList = new Popup[4];

        for(int i=0; i<4; i++) {
            GridPane specificPointsPopup = new GridPane();
            specificPointsPopup.setMaxWidth(50);
            specificPointsPopup.setMaxHeight(50);

            for (int j = 0; j < 5; j++) {
                Text specificPointsEntry = new Text(String.valueOf(specificPoints[i][j]));
                specificPointsEntry.setTextAlignment(TextAlignment.RIGHT);
                specificPointsEntry.setFont(Font.font("Helvetica", 35));
                Text typeOfPointEntry = new Text(typeOfPointEntryNames[j]);
                typeOfPointEntry.setTextAlignment(TextAlignment.LEFT);
                typeOfPointEntry.setFont(Font.font("Helvetica", 35));


                specificPointsPopup.add(typeOfPointEntry, 0, j);
                specificPointsPopup.add(specificPointsEntry, 1, j);


                //Setting popup properties
                //specificPointsPopup.getColumnConstraints().addAll(col1Constraints, col2Constraints);
                specificPointsPopup.setHgap(25);
                specificPointsPopup.setStyle("-fx-background-color: white;");
            }
            Popup popup = new Popup();
            popup.getContent().add(specificPointsPopup);
            pointsPopupList[i] = popup;

        }





        //TODO List of arrival nicknames (in descending order)
        String[] playerEntryNames = {"Pasquale", "Nicole", "Alessandro", "Riccardo"};


        //TODO List of arrival scoring points (in descending order)
        int[] points = {12, 9, 8, 7};

        //TODO Change 4(for) into numPlayers
        for(int i=0; i<4; i++) {
            Text playerEntry = new Text(playerEntryNames[i]);
            playerEntry.setTextAlignment(TextAlignment.CENTER);
            if(i==0){

                playerEntry.setFont(Font.font("Calibri", FontWeight.EXTRA_BOLD, 50));
                playerEntry.setFill(Color.GREEN);
            } else {
                playerEntry.setFont(Font.font("Calibri", 50));
            }
            resultPane.add(playerEntry, 0, i);
        }
        for(int i=0; i<4; i++) {
            Text pointsEntry = new Text(String.valueOf(points[i]));
            pointsEntry.setTextAlignment(TextAlignment.RIGHT);

            if(i==0){

                pointsEntry.setFont(Font.font("Calibri", FontWeight.EXTRA_BOLD, 50));
                pointsEntry.setFill(Color.GREEN);
            } else {
                pointsEntry.setFont(Font.font("Calibri", 50));
            }
            resultPane.add(pointsEntry, 1, i);


            int finalI = i;
            pointsEntry.setOnMouseEntered(event -> {
                // Shows popup when mouse gets over score
                pointsPopupList[finalI].show(pointsEntry, event.getScreenX()+35, event.getScreenY()+10);


            });
            int finalI1 = i;
            pointsEntry.setOnMouseExited(event -> {
                pointsPopupList[finalI1].hide();
            });
        }

        resultPane.setStyle("-fx-background-color: white;");
        StackPane.setAlignment(resultPane, Pos.CENTER);
        resultPane.setMaxWidth(bounds.getWidth()/4);
        resultPane.setMaxHeight(bounds.getWidth()/7);
        endStackPane.getChildren().add(resultPane);


        //TODO player with highest score
        winner = new Text("Pasquale WON!!!!");
        winner.setFont(Font.font("Calibri", FontWeight.EXTRA_BOLD, 100));
        winner.setFill(Color.BLUE);

        StackPane.setAlignment(winner, Pos.TOP_CENTER);
        endStackPane.getChildren().add(winner);

        scene = new Scene(endStackPane, bounds.getWidth(), bounds.getHeight());
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
        stage.setTitle("Game has ended");
    }

}
