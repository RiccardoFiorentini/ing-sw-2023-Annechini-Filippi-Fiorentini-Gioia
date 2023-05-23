package main.java.it.polimi.ingsw.Client;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
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
import main.java.it.polimi.ingsw.Model.BoardBean;
import main.java.it.polimi.ingsw.Model.ChatBean;
import main.java.it.polimi.ingsw.Model.ShelfBean;
import main.java.it.polimi.ingsw.Model.Tile;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static main.java.it.polimi.ingsw.Client.ClientState.*;

public class GUIApplication extends Application{

    private GameState state;
    private ClientState cState;
    private String playerNickname;
    private int playerTurnId;
    private Stage stage;
    int numSeconds;
    private Stage chatStage;
    TextArea chatArea = new TextArea();
    private boolean isChatOpen;
    private ImageView imageViewWallpaper;
    private ImageView endWallpaper;
    private StackPane endStackPane;
    private GridPane resultPane;
    private Text winner;

    private GridPane specificPointsPopup;

    private int personalGoalId;
    private Label nicknameLabel;
    private Image wallpaper;
    private Image title;
    private TextField textField;
    private ImageView imageViewTitle;
    private StackPane stackPane;
    private VBox vBox;
    private Scene scene;
    private Scene chatScene;
    private Rectangle2D bounds;
    private Label messages;

    private Button button1;
    private Button button2;
    private Button button3;
    private Button buttonRMI;
    private Button buttonSocket;
    private HBox hBox;
    private HBox hBoxNet;
    private HBox hBoxWaiting;
    private HBox hboxBuffer;
    private Pane columns;
    private Pane bufferPane;

    private ImageView tile1Buffer;
    private ImageView tile2Buffer;
    private ImageView tile3Buffer;

    private Pane chatButtonPane;
    private ImageView chat;
    private ImageView playerChair;
    private List<ImageView> othersChair;
    private List<Text> allNickTexts;
    private List<Rectangle> rectangles;

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
    private List<ImageView> otherPlayerShelves;
    private List<ImageView[][]> otherPlayersTiles;

    private ImageView personalGoal;
    private ImageView commonGoal1;
    private ImageView commonGoal2;
    private ImageView tokenCommonGoal1;
    private ImageView tokenCommonGoal2;
    private ImageView tokenLastPlayerId;
    private ImageView[] playerPointsTokens;
    private List<ImageView[]> otherPlayersPointsTokens;
    private Boolean firsEnd;
    private ClientConnectionHandler cch;
    private ImageView finalPointsBg;
    private List<Popup> specificPointsPopups;

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
        firsEnd = true;
        cState = BEFORE_LOGIN;
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

        hBox.setAlignment(Pos.CENTER);

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
        imageViewTitle.setFitHeight(bounds.getHeight()/3.5);
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

        hBoxWaiting = new HBox(10);
        textField.setEditable(false);
        textField.setOnMouseClicked((event)->{textField.setEditable(true);});
        textField.setMaxWidth(bounds.getWidth()/3);
        vBox = new VBox(10.0, (Node) nicknameLabel);
        vBox.setMaxWidth(bounds.getWidth());
        vBox.setMaxHeight(bounds.getHeight()/3);
        vBox.getChildren().add(textField);
        vBox.getChildren().add(messages);
        vBox.setAlignment(Pos.CENTER);

        stackPane.getChildren().add(hBoxWaiting);
        stackPane.getChildren().add(vBox);
        stackPane.getChildren().add(hBox);
        stackPane.getChildren().add(hBoxNet);
        StackPane.setMargin(vBox, new Insets(0, 0, 50, 0));

        scene = new Scene(stackPane, bounds.getWidth(), bounds.getHeight());
        scene.setOnMouseClicked((event)->{textField.setEditable(false);});

        stage.widthProperty().addListener(event -> setUpLoginProportion());

        textField.setVisible(false);
        hBox.setVisible(false);
        hBoxWaiting.setVisible(false);

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
            System.exit(1);
        }
        nicknameLabel.setText("Choose the nickname");
        textField.setVisible(true);
        hBoxNet.setVisible(false);
        hBox.setVisible(false);
        hBoxWaiting.setVisible(false);
        messages.setVisible(true);
        messages.setText("");
        new Thread(()-> {
            while (true) {
                Response resp = null;
                try {
                    resp = cch.getNextResponse();
                } catch (Exception e) {
                    System.exit(1);
                }

                final Response response = resp;

                if (response != null) {
                    new Thread(() -> handleResponse(response)).start();

                }
            }
        }).start();
    }

    /**
     * It resizes all the objects in the login stage if it changes its dimensions
     * @author Fiorentini Riccardo
     */
    public void setUpLoginProportion(){
        button1.setPrefWidth(stage.getWidth()*0.05);
        button1.setMaxHeight(stage.getWidth()*0.05);
        button2.setPrefWidth(stage.getWidth()*0.05);
        button2.setMaxHeight(stage.getWidth()*0.05);
        button3.setPrefWidth(stage.getWidth()*0.05);
        button3.setMaxHeight(stage.getWidth()*0.05);
        buttonRMI.setMaxHeight(stage.getWidth()*0.05);
        buttonSocket.setMaxHeight(stage.getWidth()*0.05);
        buttonRMI.setPrefWidth(stage.getWidth()*0.15);
        buttonSocket.setPrefWidth(stage.getWidth()*0.15);
        vBox.setMaxWidth(stage.getWidth());
        vBox.setMaxHeight(stage.getHeight()/3);
        hBox.setMaxWidth(stage.getWidth());
        hBox.setMaxHeight(stage.getHeight()/3);
        hBoxNet.setMaxWidth(stage.getWidth());
        hBoxNet.setMaxHeight(stage.getHeight()/3);
        textField.setMaxWidth(stage.getWidth()/3);
        imageViewTitle.setPreserveRatio(true);
        imageViewTitle.setFitHeight(stage.getHeight()/3.5);
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
                Platform.runLater(() ->nicknameResponse(false, resp.getStrParameter("newnickname")));
                break;

            case LOGIN_CONFIRMED:
                Platform.runLater(() -> nicknameResponse(true, resp.getStrParameter("nickname")));
                playerNickname = resp.getStrParameter("nickname");
                if(cState != ASK_PLAYERS_NUM){
                    Platform.runLater(() -> waitingRoom());
                }
                break;

            case ASK_PLAYERS_NUM:
                cState = ASK_PLAYERS_NUM;
                if(resp.getStrParameter("result") == null){
                    Platform.runLater(()->askNumberPlayer());
                } else if (resp.getStrParameter("result").equals("success")) {  //accepted value
                    Platform.runLater(() -> waitingRoom());
                }
                break;

            case GAME_STARTED:
                state = new GameState();
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
                personalGoalId = resp.getIntParameter("personalgoal");
                state.setCommonGoalPoints1((List<Integer>)resp.getObjParameter("commongoalpoints1"));
                state.setCommonGoalPoints2((List<Integer>)resp.getObjParameter("commongoalpoints2"));
                state.setConnected((List<Boolean>)resp.getObjParameter("connected"));
                state.setNumPlayers(state.getNicknames().size());
                Platform.runLater(()->setupGameScreen());
                Command command = new Command(CommandType.GAME_JOINED);
                sendCommand(command);
                break;

            case NEW_MEX_CHAT:
                state.setChat((ChatBean) resp.getObjParameter("chat"));
                updateChat();
                break;

            case NEW_TURN:
                state.setCurrPlayerId(resp.getIntParameter("CurrentPlayerId"));
                state.setPickableTiles((boolean[][]) resp.getObjParameter("pickableTiles"));
                if(playerTurnId==state.getCurrPlayerId()){
                    cState = SELECT_COLUMN;
                    Platform.runLater(()->updateBoard());
                    Platform.runLater(()->updateShelf(playerTurnId));
                }else{
                    cState = MATCH_IDLE;
                }
                Platform.runLater(()->updateNameColor());
                break;

            case SELECT_COLUMN_RESULT:
                if("success".equals(resp.getStrParameter("result"))){ //andato a buon fine
                    cState = SELECT_FIRST_TILE;
                    Platform.runLater(()->updateBoard());
                    Platform.runLater(()->updateShelf(playerTurnId));
                }else{
                    cState = SELECT_COLUMN;
                }
                break;

            case SELECT_TILE_RESULT:
                if("success".equals(resp.getStrParameter("result"))){
                    if(resp.getObjParameter("buffer") == null){
                        cState = SELECT_SECOND_TILE;
                        state.setPickableTiles((boolean[][]) resp.getObjParameter("pickableTiles"));
                        Platform.runLater(()->updateBoard());
                    }else{
                        cState = PUT_IN_COLUMN;
                        state.setBuffer((Tile[]) resp.getObjParameter("buffer"));

                        Platform.runLater(()->updateBuffer());
                    }
                }
                break;

            case PUT_IN_COLUMN_RESULT:
                if("success".equals(resp.getStrParameter("result"))){ //andato a buon fine
                    state.setBuffer((Tile[]) resp.getObjParameter("buffer"));
                    if(resp.getIntParameter("turnFinished")==0) {
                        cState = PUT_IN_COLUMN;
                    }else{
                        cState = MATCH_IDLE;
                    }
                }else{
                    cState = PUT_IN_COLUMN;
                }
                Platform.runLater(()->updateBuffer());
                break;

            case UPDATE_BOARD:
                state.setBoard((BoardBean)resp.getObjParameter("board"));
                Platform.runLater(()->updateBoard());
                break;

            case UPDATE_PLAYER_SHELF:
                state.getShelves().set(resp.getIntParameter("playerid"),(ShelfBean)resp.getObjParameter("shelf"));
                Platform.runLater(()->updateShelf(resp.getIntParameter("playerid")));
                break;

            case PLAYER_DISCONNECTED:
                state.getConnected().set(resp.getIntParameter("playerid"),false);
                if(state.getNicknames().get(resp.getIntParameter("playerid")).equals(playerNickname)){
                    Platform.runLater(()->signal("Player disconnected", "You are disconnected, reconnect with your current\n nickname to enter again in the same game"));
                }else{
                    Platform.runLater(()->signal("Player disconnected","The player " + state.getNicknames().get(resp.getIntParameter("playerid")) + " is disconnected"));
                }
                Platform.runLater(()->updateNameColor());
                break;

            case PLAYER_RECONNECTED:
                state.getConnected().set(resp.getIntParameter("playerid"),true);
                firsEnd = true;
                Platform.runLater(()->signal("Player reconnected","The player " + state.getNicknames().get(resp.getIntParameter("playerid")) + " reconnected"));
                Platform.runLater(()->updateNameColor());
                break;

            case ONLY_ONE_CONNECTED:
                break;

            case ONLY_ONE_CONNECTED_TIMER:
                if(firsEnd == true){
                    Platform.runLater(() ->signalTimer(resp.getIntParameter("timermilliseconds")));
                    firsEnd = false;
                }
                break;

            case GAME_ENDED:
                if(resp.getIntParameter("interrupted")==0){ //finished correctly
                    state.setFinalPoints((List<Integer>)resp.getObjParameter("finalPoints"));
                    state.setFinalPersonalGoalPoints((List<Integer>)resp.getObjParameter("finalPoints"));
                    state.setFinalColorGroupPoints((List<Integer>)resp.getObjParameter("finalPoints"));
                    Platform.runLater(()-> setupEndGameScreen());
                } else {
                    Platform.runLater(()-> setupInterruptedEndGameScreen());
                }
                break;

            case COMMON_GOAL_WON:
                if(resp.getIntParameter("commongoalid")==0) {
                    state.getCommonGoalPoints1().set(resp.getIntParameter("playerid"), resp.getIntParameter("pointswon"));
                    state.setCommonGoalsRemainingPoint(0, resp.getIntParameter("remainingpoints"));
                }else{
                    state.getCommonGoalPoints2().set(resp.getIntParameter("playerid"), resp.getIntParameter("pointswon"));
                    state.setCommonGoalsRemainingPoint(1, resp.getIntParameter("remainingpoints"));
                }
                Platform.runLater(()->updateCommonGoalsPoints());
                break;

            case SHELF_COMPLETED:
                state.setLastPlayerId(resp.getIntParameter("playerid"));
                Platform.runLater(()->updateCommonGoalsPoints());
        }
    }

    /**
     * Method used to signal disconnections and reconnections
     * @param header title of the message
     * @param message you want display
     * @author Fiorentini Riccardo
     */
    public void signal(String header, String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Attention!");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.setResizable(true);
        alert.showAndWait();
    }

    /**
     * Method used to signal disconnections and reconnections
     * @param milliseconds you have to wait before winning
     * @author Fiorentini Riccardo
     */
    public void signalTimer(int milliseconds){
        numSeconds = (int)(milliseconds/1000);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Attention!");
        alert.setHeaderText("You are the last one connected");
        alert.setResizable(true);
        alert.setContentText("Wait " + numSeconds + " to win");
        Timer countdownTimer = new Timer();
        countdownTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                numSeconds--;
                if (numSeconds <= 0) {
                    countdownTimer.cancel();
                    countdownTimer.purge();
                    Platform.runLater(() -> {
                        alert.close();
                    });
                } else {
                    Platform.runLater(() -> {
                        alert.setContentText("Wait " + numSeconds + " to win");
                    });
                }
            }
        }, 1000, 1000);

        alert.showAndWait();
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
        nicknameLabel.setVisible(true);
        vBox.setVisible(true);
        messages.setVisible(true);

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
     * @author Fiorentini Riccardo
     */
    public void askNumberPlayer(){
        stackPane = new StackPane(imageViewWallpaper);
        scene = new Scene(stackPane, bounds.getWidth(), bounds.getHeight());
        stackPane.getChildren().add(imageViewTitle);
        nicknameLabel.setText("Choose number of player");
        stackPane.getChildren().add(nicknameLabel);
        VBox tmp = new VBox(10.0, nicknameLabel, hBox);
        tmp.setAlignment(Pos.CENTER);
        hBox.setVisible(true);
        stackPane.getChildren().add(tmp);

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

        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method prepares all the elements in the Game scene
     * @author Alessandro Annechini, Pasquale Gioia
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
        for(int k=0;k<state.getNumPlayers()-1;k++){
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
        Pane tilePane = new Pane();
        Pane tokenPane = new Pane();
        bufferPane = new Pane();

        chatButtonPane = new Pane();

        shelfPane.getChildren().add(tilePane);
        tilePane.getChildren().add(boardPane);

        tokenPane.getChildren().add(shelfPane);

        shelfPane.getChildren().add(playerShelf);
        for(ImageView im : otherPlayerShelves) shelfPane.getChildren().add(im);

        rectangles = new ArrayList<>();
        columns = new Pane();
        for(int i=0; i<5; i++){
            rectangles.add(new Rectangle());
            rectangles.get(i).setFill(Color.TRANSPARENT);
            rectangles.get(i).setStroke(Color.ANTIQUEWHITE);
            rectangles.get(i).setStrokeWidth(3);
            columns.getChildren().add(rectangles.get(i));
        }
        columns.setVisible(false);
        shelfPane.getChildren().add(columns);

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

        String commonGoal1Path = state.getCommonGoalImagePath(state.getCommonGoalsId()[0]);
        String commonGoal2Path = state.getCommonGoalImagePath(state.getCommonGoalsId()[1]);
        commonGoal1 = new ImageView(new Image(getClass().getResource(commonGoal1Path).toString()));
        commonGoal2 = new ImageView(new Image(getClass().getResource(commonGoal2Path).toString()));

        String personalGoalPath = state.getPersonalGoalImagePath(personalGoalId);
        personalGoal = new ImageView(new Image(getClass().getResource(personalGoalPath).toString()));

        String remainingPoints1Path = state.getScoringTokenImagePath(state.getCommonGoalsRemainingPoint()[0]);
        tokenCommonGoal1 = new ImageView(new Image(getClass().getResource(remainingPoints1Path).toString()));
        String remainingPoints2Path = state.getScoringTokenImagePath(state.getCommonGoalsRemainingPoint()[1]);
        tokenCommonGoal2 = new ImageView(new Image(getClass().getResource(remainingPoints2Path).toString()));
        tokenLastPlayerId = new ImageView(new Image(getClass().getResource("/scoring tokens/end game.jpg").toString()));

        tile1Buffer = new ImageView(new Image(getClass().getResource("/item tiles/Cornici1.1.png").toString()));
        tile2Buffer = new ImageView(new Image(getClass().getResource("/item tiles/Cornici1.1.png").toString()));
        tile3Buffer = new ImageView(new Image(getClass().getResource("/item tiles/Cornici1.1.png").toString()));
        hboxBuffer = new HBox(tile1Buffer, tile2Buffer, tile3Buffer);

        hboxBuffer.setPadding(new Insets(10));
        hboxBuffer.setSpacing(10);
        hboxBuffer.setStyle("-fx-border-color: #5E462B;-fx-border-width: 2");
        hboxBuffer.setBackground(Background.EMPTY);
        bufferPane.getChildren().add(hboxBuffer);

        chat = new ImageView(new Image(getClass().getResource("/external/chat.png").toString()));
        chatButtonPane = new Pane(chat);

        playerChair = new ImageView(new Image(getClass().getResource("/misc/firstplayertoken.png").toString()));

        playerPointsTokens = new ImageView[3];
        for(int i=0;i<3;i++) playerPointsTokens[i] = new ImageView(new Image(getClass().getResource("/scoring tokens/end game.jpg").toString()));

        othersChair = new ArrayList<>();
        otherPlayersPointsTokens = new ArrayList<>();
        for(int i=0; i<state.getNumPlayers()-1; i++){
            ImageView chair = new ImageView(new Image(getClass().getResource("/misc/firstplayertoken.png").toString()));
            otherPlayersPointsTokens.add(new ImageView[3]);
            for(int j=0;j<3;j++)
                otherPlayersPointsTokens.get(i)[j] = new ImageView(new Image(getClass().getResource("/scoring tokens/end game.jpg").toString()));
            othersChair.add(chair);
            boardPane.getChildren().add(othersChair.get(i));
            for(int j=0;j<3;j++) boardPane.getChildren().add(otherPlayersPointsTokens.get(i)[j]);
        }

        allNickTexts = new ArrayList<>();

        for(int i=0; i<state.getNumPlayers(); i++) {
            Text playerNickText = new Text(state.getNicknames().get(i));
            shelfPane.getChildren().add(playerNickText);
            allNickTexts.add(playerNickText);
        }

        tokenPane.getChildren().add(tokenLastPlayerId);
        tokenPane.getChildren().add(tokenCommonGoal1);
        tokenPane.getChildren().add(tokenCommonGoal2);

        boardPane.getChildren().add(commonGoal1);
        boardPane.getChildren().add(commonGoal2);
        boardPane.getChildren().add(personalGoal);
        boardPane.getChildren().add(bufferPane);
        boardPane.getChildren().add(chatButtonPane);
        boardPane.getChildren().add(playerChair);
        for(int i=0;i<3;i++) boardPane.getChildren().add(playerPointsTokens[i]);

        //ORDER OF PANELS
        StackPane stackPane = new StackPane(background,tokenPane);

        stackPane.setOnMouseClicked((event)->{
            for(int i = stackPane.getChildren().size()-1;i>=0 && !event.isConsumed();i--){
                if(stackPane.getChildren().get(i).getOnMouseClicked()!=null){
                    stackPane.getChildren().get(i).fireEvent(event);
                }
            }
        });

        playerShelf.setOnMouseClicked((e)->System.out.println("CLICK SHELF!!!"));
        board.setOnMouseClicked((e)->System.out.println("CLICK BOARD!!!"));
        tile1Buffer.setOnMouseClicked((e)->System.out.println("CLICK BUFFER1 TILE!!"));
        tile2Buffer.setOnMouseClicked((e)->System.out.println("CLICK BUFFER2 TILE!!"));
        tile3Buffer.setOnMouseClicked((e)->System.out.println("CLICK BUFFER3 TILE!!"));
        chat.setOnMouseClicked((e) -> {
            if(!isChatOpen){
                openChatWindow();
                isChatOpen = true;
            }
        });

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

        updateNameColor();
        updateCommonGoalsPoints();
        updateChairs();
        for(int i=0;i<state.getNumPlayers();i++) updateShelf(i);
        updateBoard();
        updateBuffer();
    }

    /**
     * This method sets the positions of the Game
     * elements with respect to the window size
     * @author Alessandro Annechini, Pasquale Gioia
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

        for(int k=0;k<state.getNumPlayers()-1;k++){
            ImageView shelf = otherPlayerShelves.get(k);
            shelf.setFitWidth(Math.min(stage.getWidth()/stdScreenRatio,stage.getHeight()) * 0.275 );
            shelf.setFitHeight(shelf.getFitWidth()*((double)1411/1414));
            shelf.setX(stage.getWidth()*0.032);
            shelf.setY(stage.getHeight()*0.032 + shelf.getFitHeight()*1.13*k);

            setTilesPositionInShelf(k);
        }

        //PersonalGoal, CommonGoal position and proportion
        commonGoal1.setFitHeight(Math.min(stage.getWidth()/stdScreenRatio,stage.getHeight())*0.163);
        commonGoal1.setFitWidth(commonGoal1.getFitHeight()*1.5169);
        commonGoal1.setX(stage.getWidth()*0.695);
        commonGoal1.setY(stage.getHeight()*0.041);

        commonGoal2.setFitHeight(Math.min(stage.getWidth()/stdScreenRatio,stage.getHeight())*0.163);
        commonGoal2.setFitWidth(commonGoal2.getFitHeight()*1.5169);
        commonGoal2.setX(stage.getWidth()*0.695);
        commonGoal2.setY(stage.getHeight()*0.051+commonGoal1.getFitHeight());

        personalGoal.setFitHeight(Math.min(stage.getWidth()/stdScreenRatio,stage.getHeight())*0.338);
        personalGoal.setFitWidth(personalGoal.getFitHeight()*0.6589);
        personalGoal.setX(stage.getWidth()*0.85);
        personalGoal.setY(stage.getHeight()*0.037);

        //Token positions and proportion
        tokenLastPlayerId.setFitHeight(board.getFitHeight()*0.107);
        tokenLastPlayerId.setFitWidth(tokenLastPlayerId.getFitHeight());
        tokenLastPlayerId.setX(board.getFitWidth()*0.808+board.getX());
        tokenLastPlayerId.setY(board.getFitHeight()*0.695+board.getY());
        tokenLastPlayerId.setRotate(8.9);

        tokenCommonGoal1.setFitHeight(commonGoal1.getFitHeight()*0.5);
        tokenCommonGoal1.setFitWidth(tokenCommonGoal1.getFitHeight());
        tokenCommonGoal1.setX(commonGoal1.getFitWidth()*0.57+commonGoal1.getX());
        tokenCommonGoal1.setY(commonGoal1.getFitHeight()*0.225+commonGoal1.getY());
        tokenCommonGoal1.setRotate(-8.7);

        tokenCommonGoal2.setFitHeight(commonGoal2.getFitHeight()*0.5);
        tokenCommonGoal2.setFitWidth(tokenCommonGoal2.getFitHeight());
        tokenCommonGoal2.setX(commonGoal2.getFitWidth()*0.57+commonGoal2.getX());
        tokenCommonGoal2.setY(commonGoal2.getFitHeight()*0.225+commonGoal2.getY());
        tokenCommonGoal2.setRotate(-8.7);

        hboxBuffer.setPrefHeight(Math.min(board.getFitWidth()/stdScreenRatio,stage.getHeight())*0.25);
        hboxBuffer.setPrefWidth(board.getFitHeight()*0.37);
        hboxBuffer.setLayoutX(stage.getWidth()*0.44);
        hboxBuffer.setLayoutY(board.getY()- board.getFitHeight()*0.156);

        tile1Buffer.setFitWidth(hboxBuffer.getPrefWidth()*0.3333333);
        tile1Buffer.setFitHeight(hboxBuffer.getPrefHeight()*0.87);
        tile2Buffer.setFitWidth(hboxBuffer.getPrefWidth()*0.3333333);
        tile2Buffer.setFitHeight(hboxBuffer.getPrefHeight()*0.87);
        tile3Buffer.setFitWidth(hboxBuffer.getPrefWidth()*0.3333333);
        tile3Buffer.setFitHeight(hboxBuffer.getPrefHeight()*0.87);

        chat.setFitHeight((Math.min(board.getFitWidth()/stdScreenRatio,stage.getHeight())*0.28));
        chat.setFitWidth(board.getFitHeight()*0.24);
        chat.setX(stage.getWidth()*0.27);
        chat.setY(board.getY()- board.getFitHeight()*0.156);

        playerChair.setFitHeight(Math.min(playerShelf.getFitWidth()/stdScreenRatio,playerShelf.getFitHeight())*0.32);
        playerChair.setFitWidth(playerChair.getFitHeight()*0.935828877005348);
        playerChair.setX(playerShelf.getX()-playerShelf.getFitWidth()*0.11);
        playerChair.setY(playerShelf.getY()+playerShelf.getFitHeight()*0.70);

        for(int i=0; i<state.getNumPlayers()-1; i++) {
            othersChair.get(i).setFitHeight(Math.min(otherPlayerShelves.get(i).getFitWidth() / stdScreenRatio, otherPlayerShelves.get(i).getFitHeight()) * 0.25);
            othersChair.get(i).setFitWidth(othersChair.get(0).getFitHeight() * 0.935828877005348);
            othersChair.get(i).setX(otherPlayerShelves.get(i).getX() - otherPlayerShelves.get(i).getFitWidth() * 0.11);
            othersChair.get(i).setY(otherPlayerShelves.get(i).getY() + otherPlayerShelves.get(i).getFitHeight() * 0.74);
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
        Text text = pos < 0 ? allNickTexts.get(playerTurnId) : allNickTexts.get(pos < playerTurnId ? pos : pos + 1);
        text.setFont(new Font("Calibri",shelf.getFitWidth()*0.08));
        text.setX(shelf.getX() + shelf.getFitWidth()/2 - text.getLayoutBounds().getWidth()/2);
        text.setY(shelf.getY() + shelf.getFitHeight()*1.08);

        if(pos<0){
            for(int i=0; i<5; i++){
                rectangles.get(i).setX(matrix[0][i].getX());
                rectangles.get(i).setY(matrix[0][i].getY());
                rectangles.get(i).setWidth(matrix[0][i].getFitWidth());
                rectangles.get(i).setHeight(playerShelf.getFitHeight()*0.85);
            }
        }

        ImageView[] tokens = pos < 0 ? playerPointsTokens : otherPlayersPointsTokens.get(pos);
        for(int i=0;i<3;i++){
            tokens[i].setFitHeight(shelf.getFitHeight()*shelfTileRatio*0.6);
            tokens[i].setFitWidth(tokens[i].getFitHeight());
            tokens[i].setX(shelf.getX() - shelf.getFitWidth()*0.1);
            tokens[i].setY(shelf.getY() + shelf.getFitHeight()*(i*0.15 + 0.2));
        }
    }

    /**
     * This method defines end game scene
     * @author Pasquale Gioia
     */
    public void setupEndGameScreen(){
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        stdScreenRatio = bounds.getWidth()/bounds.getHeight();
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());

        endStackPane = new StackPane();
        endWallpaper = new ImageView(new Image(getClass().getResource("/Publisher material/Display_5.jpg").toString()));
        endWallpaper.setFitHeight(bounds.getHeight());
        endWallpaper.setFitWidth(bounds.getWidth());
        endWallpaper.setEffect(new GaussianBlur(20));
        StackPane.setAlignment(endWallpaper, Pos.CENTER);

        finalPointsBg = new ImageView(new Image(getClass().getResource("/misc/base_pagina2.jpg").toString()));
        finalPointsBg.setFitWidth(bounds.getWidth()*0.4);
        finalPointsBg.setFitHeight(bounds.getHeight()*0.35);
        Rectangle clip = new Rectangle( finalPointsBg.getFitWidth(), finalPointsBg.getFitHeight() );
        clip.setArcWidth(20);
        clip.setArcHeight(20);
        finalPointsBg.setClip(clip);
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        WritableImage image = finalPointsBg.snapshot(parameters, null);
        finalPointsBg.setClip(null);
        finalPointsBg.setEffect(new DropShadow(20, Color.BLACK));
        finalPointsBg.setImage(image);
        StackPane.setAlignment(finalPointsBg, Pos.CENTER);

        endStackPane.getChildren().add(endWallpaper);
        endStackPane.getChildren().add(finalPointsBg);

        resultPane = new GridPane();

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

        winner = new Text(state.getNicknames().get(state.getTurnIdInPosition(1)) + " WON!!!!");
        winner.setFont(Font.font("Calibri", FontWeight.EXTRA_BOLD, 100));
        winner.setFill(Color.BLUE);
        winner.setY(bounds.getHeight()*0.2);
        winner.setX(bounds.getWidth()*0.5 - winner.getLayoutBounds().getWidth()*0.5);
        winner.setStroke(Color.BLACK);
        winner.setFill(Color.WHITE);
        Pane winnerPane = new Pane(winner);
        endStackPane.getChildren().add(winnerPane);

        String[] typeOfPointEntryNames = {"Personal goal:", "Common goal 1:", "Common goal 2:", "Group points:", "Final point:"};
        //For each player (in arrival order) specific point array --
        List<int[]> specificPoints = new ArrayList<>();
        for(int i=0; i<state.getNumPlayers(); i++){
            int[] points = new int[5];
            points[0] = state.getFinalPersonalGoalPoints().get(i);
            points[1] = state.getCommonGoalPoints1().get(i);
            points[2] = state.getCommonGoalPoints2().get(i);
            points[3] = state.getFinalColorGroupPoints().get(i);
            if(i==state.getLastPlayerId())
                points[4] = 1;
            else
                points[4] = 0;
            specificPoints.add(points); //in ordine di turn id
        }

        List<Popup> pointsPopupList = new ArrayList<>();
        specificPointsPopups = new ArrayList<>();
        for(int i=0; i < state.getNumPlayers(); i++) {
            GridPane specificPointsPopup = new GridPane();
            specificPointsPopup.setMaxWidth(50);
            specificPointsPopup.setMaxHeight(50);

            for (int j = 0; j < 5; j++) {
                Text specificPointsEntry = new Text(String.valueOf(specificPoints.get(state.getTurnIdInPosition(i+1))[j]));
                specificPointsEntry.setTextAlignment(TextAlignment.RIGHT);
                specificPointsEntry.setFont(Font.font("Helvetica", 35));
                specificPointsEntry.setFill(Color.WHITE);
                Text typeOfPointEntry = new Text(typeOfPointEntryNames[j]);
                typeOfPointEntry.setTextAlignment(TextAlignment.LEFT);
                typeOfPointEntry.setFont(Font.font("Helvetica", 35));
                typeOfPointEntry.setFill(Color.WHITE);

                specificPointsPopup.add(typeOfPointEntry, 0, j);
                specificPointsPopup.add(specificPointsEntry, 1, j);
            }

            //Setting popup properties
            //specificPointsPopup.getColumnConstraints().addAll(col1Constraints, col2Constraints);
            specificPointsPopup.setHgap(25);
            specificPointsPopup.setStyle("-fx-background-color: black;");
            specificPointsPopup.setOpacity(0.8);

            Popup popup = new Popup();
            popup.getContent().add(specificPointsPopup);
            pointsPopupList.add(popup);
            specificPointsPopups.add(popup);
        }

        //List of arrival nicknames (in descending order)
        String[] playerEntryNames = new String[state.getNumPlayers()];
        for(int i=0; i<state.getNumPlayers(); i++){
            playerEntryNames[i]=state.getNicknames().get(state.getTurnIdInPosition(i+1));
        }

        // List of arrival scoring points (in descending order)
        List<Integer> finalPoints = new ArrayList<>();
        for(int i=0; i<state.getNumPlayers(); i++){
            finalPoints.add(state.getFinalPoints().get(state.getTurnIdInPosition(i+1)));
        }

        for(int i=0; i<state.getNumPlayers(); i++) {
            Text playerEntry = new Text(playerEntryNames[i]);
            playerEntry.setTextAlignment(TextAlignment.CENTER);
            Text pointsEntry = new Text(String.valueOf(finalPoints.get(i)));
            pointsEntry.setTextAlignment(TextAlignment.RIGHT);

            if(i==0){
                //FIRST PLAYER
                playerEntry.setFont(Font.font("Calibri", FontWeight.EXTRA_BOLD, 50));
                playerEntry.setFill(Color.GREEN);
                pointsEntry.setFont(Font.font("Calibri", FontWeight.EXTRA_BOLD, 50));
                pointsEntry.setFill(Color.GREEN);

            } else {
                //Other players
                playerEntry.setFont(Font.font("Calibri", 50));
                pointsEntry.setFont(Font.font("Calibri", 50));
                playerEntry.setFill(Color.WHITE);
                pointsEntry.setFill(Color.WHITE);
            }
            resultPane.add(playerEntry, 0, i);
            resultPane.add(pointsEntry, 1, i);

            //SCORE POPUP
            int finalI = i;
            pointsEntry.setOnMouseMoved(event -> {
                // Shows popup when mouse gets over score
                pointsPopupList.get(finalI).show(pointsEntry, event.getScreenX()+35, event.getScreenY()+10);
            });
            pointsEntry.setOnMouseExited(event -> {
                pointsPopupList.get(finalI).hide();
            });
        }

        StackPane.setAlignment(resultPane, Pos.CENTER);
        resultPane.setMaxWidth(bounds.getWidth()/4);
        resultPane.setMaxHeight(bounds.getWidth()/7);
        endStackPane.getChildren().add(resultPane);

        scene = new Scene(endStackPane, bounds.getWidth(), bounds.getHeight());
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
        stage.setTitle("Game has ended");

        stage.widthProperty().addListener( (x)->setEndGameGraphicsProportions() );
        stage.heightProperty().addListener( (x)->setEndGameGraphicsProportions() );
        setEndGameGraphicsProportions();
    }

    /**
     * This method sets the positions of the End Game
     * elements with respect to the window size
     * @author Pasquale Gioia,Alessandro Annechini
     */
    public void setEndGameGraphicsProportions(){
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        double stdHeight = Math.min(stage.getWidth()/stdScreenRatio,stage.getHeight());
        double stdHeightRatio = Math.min(stage.getWidth()/stdScreenRatio,stage.getHeight())/bounds.getHeight();

        finalPointsBg.setFitWidth(stdHeight*stdScreenRatio*0.4);
        finalPointsBg.setFitHeight(finalPointsBg.getFitWidth()/(stdScreenRatio*0.4)*0.35);

        resultPane.setHgap(25*stdHeightRatio);
        resultPane.setVgap(10*stdHeightRatio);

        //player with the highest score
        winner.setFont(Font.font("Calibri", FontWeight.EXTRA_BOLD, 100*stdHeightRatio));
        winner.setY(stage.getHeight()*0.2);
        winner.setX(stage.getWidth()*0.5 - winner.getLayoutBounds().getWidth()*0.5);

        for(Popup p : specificPointsPopups){
            for(Node t : p.getContent()){
                for(Node tt : ((GridPane)t).getChildren())
                    ((Text) tt).setFont(Font.font("Helvetica", 35*stdHeightRatio));
                ((GridPane)t).setHgap(25*stdHeightRatio);
            }
        }

        for(Node n : resultPane.getChildren()){
            ((Text) n).setFont(Font.font("Calibri", 50*stdHeightRatio));
        }

        resultPane.setMaxWidth(bounds.getWidth()/4*stdHeightRatio);
        resultPane.setMaxHeight(bounds.getWidth()/7*stdHeightRatio);
    }

    /**
     * This method defines interrupted end game scene
     * @author Pasquale Gioia, Alessandro Annechini
     */
    public void setupInterruptedEndGameScreen(){

        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        stdScreenRatio = bounds.getWidth()/bounds.getHeight();
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());

        endStackPane = new StackPane();
        endWallpaper = new ImageView(new Image(getClass().getResource("/Publisher material/Display_5.jpg").toString()));
        endWallpaper.setFitHeight(bounds.getHeight());
        endWallpaper.setFitWidth(bounds.getWidth());
        endWallpaper.setEffect(new GaussianBlur(20));
        StackPane.setAlignment(endWallpaper, Pos.CENTER);

        finalPointsBg = new ImageView(new Image(getClass().getResource("/misc/base_pagina2.jpg").toString()));
        finalPointsBg.setFitWidth(bounds.getWidth()*0.4);
        finalPointsBg.setFitHeight(bounds.getHeight()*0.35);
        Rectangle clip = new Rectangle( finalPointsBg.getFitWidth(), finalPointsBg.getFitHeight() );
        clip.setArcWidth(20);
        clip.setArcHeight(20);
        finalPointsBg.setClip(clip);
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        WritableImage image = finalPointsBg.snapshot(parameters, null);
        finalPointsBg.setClip(null);
        finalPointsBg.setEffect(new DropShadow(20, Color.BLACK));
        finalPointsBg.setImage(image);
        StackPane.setAlignment(finalPointsBg, Pos.CENTER);

        endStackPane.getChildren().add(endWallpaper);
        endStackPane.getChildren().add(finalPointsBg);

        resultPane = new GridPane();

        resultPane.setHgap(25);
        resultPane.setVgap(10);

        winner = new Text("You won!");
        winner.setFont(Font.font("Calibri", FontWeight.EXTRA_BOLD, 100));
        winner.setFill(Color.BLUE);
        winner.setY(bounds.getHeight()*0.2);
        winner.setX(bounds.getWidth()*0.5 - winner.getLayoutBounds().getWidth()*0.5);
        winner.setStroke(Color.BLACK);
        winner.setFill(Color.WHITE);
        Pane winnerPane = new Pane(winner);
        endStackPane.getChildren().add(winnerPane);

        List<String> playerEntryNames = new ArrayList<>();
        playerEntryNames.add(playerNickname);
        for(int i=0; i<state.getNumPlayers(); i++){
            if(i!=playerTurnId) playerEntryNames.add(state.getNicknames().get(i));
        }
        for(int i=0; i<state.getNumPlayers(); i++) {
            Text playerEntry = new Text(playerEntryNames.get(i));
            playerEntry.setTextAlignment(TextAlignment.CENTER);

            if(i==0){
                //FIRST PLAYER
                playerEntry.setFont(Font.font("Calibri", FontWeight.EXTRA_BOLD, 50));
                playerEntry.setFill(Color.GREEN);

            } else {
                //Other players
                playerEntry.setFont(Font.font("Calibri", 50));
                playerEntry.setFill(Color.RED);
            }
            resultPane.add(playerEntry, 0, i);
        }
        StackPane.setAlignment(resultPane, Pos.CENTER);
        resultPane.setMaxWidth(bounds.getWidth()/4);
        resultPane.setMaxHeight(bounds.getWidth()/7);
        endStackPane.getChildren().add(resultPane);

        scene = new Scene(endStackPane, bounds.getWidth(), bounds.getHeight());
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
        stage.setTitle("Game has ended");

        stage.widthProperty().addListener( (x)->setInterruptedEndGameGraphicsProportions() );
        stage.heightProperty().addListener( (x)->setInterruptedEndGameGraphicsProportions() );
        setInterruptedEndGameGraphicsProportions();
    }

    /**
     * This method sets the positions of the Interrupted End Game
     * elements with respect to the window size
     * @author Pasquale Gioia,Alessandro Annechini
     */
    public void setInterruptedEndGameGraphicsProportions(){
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        double stdHeight = Math.min(stage.getWidth()/stdScreenRatio,stage.getHeight());
        double stdHeightRatio = Math.min(stage.getWidth()/stdScreenRatio,stage.getHeight())/bounds.getHeight();

        finalPointsBg.setFitWidth(stdHeight*stdScreenRatio*0.4);
        finalPointsBg.setFitHeight(finalPointsBg.getFitWidth()/(stdScreenRatio*0.4)*0.35);

        resultPane.setHgap(25*stdHeightRatio);
        resultPane.setVgap(10*stdHeightRatio);

        //player with the highest score
        winner.setFont(Font.font("Calibri", FontWeight.EXTRA_BOLD, 100*stdHeightRatio));
        winner.setY(stage.getHeight()*0.2);
        winner.setX(stage.getWidth()*0.5 - winner.getLayoutBounds().getWidth()*0.5);

        for(Node n : resultPane.getChildren()){
            ((Text) n).setFont(Font.font("Calibri", 50*stdHeightRatio));
        }

        resultPane.setMaxWidth(bounds.getWidth()/4*stdHeightRatio);
        resultPane.setMaxHeight(bounds.getWidth()/7*stdHeightRatio);
    }

    /**
     * Update the chat when someone texts
     * @author Fiorentini Riccardo
     */
    private void updateChat(){
        ChatBean chat = state.getChat();
        chatArea.setEditable(false);
        String text = "";
        for(int i=0; i<chat.getText().size(); i++){
            text = text + "<" + chat.getSender().get(i) + "> " + chat.getText().get(i) + "\n";
        }
        chatArea.setText(text);
    }

    /**
     * This method creates a chat Stage
     * @author Pasquale Gioia, Fiorentini Riccardo
     */
    private void openChatWindow() {
        chatStage = new Stage();

        //Make it possible to open only one chat window at a time
        chatStage.setOnCloseRequest((e)->{
            isChatOpen = false;
        });

        chatStage.setX(stage.getX());
        chatStage.setY(stage.getY()*1.4);
        chatStage.setWidth(stage.getWidth()/4.5);
        chatStage.setHeight(stage.getHeight()*0.95);

        ChatBean chat = state.getChat();
        chatArea.setEditable(false);
        String text = "";
        for(int i=0; i<chat.getText().size(); i++){
            text = text + "<" + chat.getSender().get(i) + "> " + chat.getText().get(i) + "\n";
        }
        chatArea.setText(text);

        TextField messageField = new TextField();
        messageField.setPromptText("Write a message...");
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.setItems(FXCollections.observableArrayList("Broadcast"));
        for(String s: state.getNicknames()){
            if(!s.equals(playerNickname)){
                choiceBox.getItems().add(s);
            }
        }
        choiceBox.getSelectionModel().selectFirst();

        Button sendButton = new Button("Send");
        sendButton.setOnAction((e)-> {
            String message = messageField.getText().trim();
            if (!message.isEmpty()) {
                if(choiceBox.getValue().equals("Broadcast")){
                    doSendMex(message, null);
                }else{
                    doSendMex(message, choiceBox.getValue());
                }

                chatArea.appendText("<" + playerNickname + "> " + message + "\n");
                //state.getChat().
                messageField.clear();
            }
        });

        HBox inputBox = new HBox(messageField, sendButton);
        VBox out = new VBox(choiceBox,inputBox);
        out.setAlignment(Pos.CENTER);
        choiceBox.setPrefWidth(chatStage.getWidth());
        HBox.setHgrow(messageField, Priority.ALWAYS);
        HBox.setHgrow(sendButton, Priority.ALWAYS);
        inputBox.setSpacing(10);
        inputBox.setPadding(new Insets(10));

        BorderPane chatPane = new BorderPane();
        chatPane.setCenter(chatArea);
        chatPane.setBottom(out);
        BorderPane.setMargin(chatArea, new Insets(10));

        chatScene = new Scene(chatPane, chatStage.getX(), chatStage.getY());
        chatStage.setScene(chatScene);
        chatStage.show();
    }

    /**
     * Animation for the waiting room
     * @author Fiorentini Riccardo
     **/
    public void waitingRoom(){
        stackPane = new StackPane(imageViewWallpaper);
        scene = new Scene(stackPane, bounds.getWidth(), bounds.getHeight());
        stackPane.getChildren().add(imageViewTitle);
        nicknameLabel.setText("Waiting for other players to join");
        nicknameLabel.setAlignment(Pos.CENTER);
        VBox tmp = new VBox(10.0, nicknameLabel);
        tmp.setAlignment(Pos.CENTER);
        StackPane.setAlignment(tmp, Pos.CENTER);
        stackPane.getChildren().add(tmp);

        Image tile1 = new Image(getClass().getResource("/item tiles/Gatti1.1.png").toString());
        ImageView viewTile1 = new ImageView(tile1);
        viewTile1.setFitHeight(bounds.getWidth()*0.05);
        viewTile1.setFitWidth(bounds.getWidth()*0.05);
        StackPane.setAlignment(viewTile1, Pos.CENTER);

        Image tile2 = new Image(getClass().getResource("/item tiles/Giochi1.1.png").toString());
        ImageView viewTile2 = new ImageView(tile2);
        viewTile2.setFitHeight(bounds.getWidth()*0.05);
        viewTile2.setFitWidth(bounds.getWidth()*0.05);
        StackPane.setAlignment(viewTile2, Pos.CENTER);

        Image tile3 = new Image(getClass().getResource("/item tiles/Libri1.1.png").toString());
        ImageView viewTile3 = new ImageView(tile3);
        viewTile3.setFitHeight(bounds.getWidth()*0.05);
        viewTile3.setFitWidth(bounds.getWidth()*0.05);
        StackPane.setAlignment(viewTile3, Pos.CENTER);

        Image tile4 = new Image(getClass().getResource("/item tiles/Cornici1.1.png").toString());
        ImageView viewTile4 = new ImageView(tile4);
        viewTile4.setFitHeight(bounds.getWidth()*0.05);
        viewTile4.setFitWidth(bounds.getWidth()*0.05);
        StackPane.setAlignment(viewTile4, Pos.CENTER);

        Image tile5 = new Image(getClass().getResource("/item tiles/Piante1.1.png").toString());
        ImageView viewTile5 = new ImageView(tile5);
        viewTile5.setFitHeight(bounds.getWidth()*0.05);
        viewTile5.setFitWidth(bounds.getWidth()*0.05);
        StackPane.setAlignment(viewTile5, Pos.CENTER);

        Image tile6 = new Image(getClass().getResource("/item tiles/Trofei1.1.png").toString());
        ImageView viewTile6 = new ImageView(tile6);
        viewTile6.setFitHeight(bounds.getWidth()*0.05);
        viewTile6.setFitWidth(bounds.getWidth()*0.05);

        hBoxWaiting.setPadding(new Insets(10));
        hBoxWaiting.setAlignment(Pos.CENTER);
        hBoxWaiting.setVisible(true);

        FadeTransition T1 = new FadeTransition(Duration.seconds(2), viewTile1);
        T1.setFromValue(0.0);
        T1.setToValue(1);
        T1.setAutoReverse(true);
        T1.setCycleCount(FadeTransition.INDEFINITE);

        FadeTransition T2 = new FadeTransition(Duration.seconds(2), viewTile2);
        T2.setFromValue(0.0);
        T2.setToValue(1);
        T2.setAutoReverse(true);
        T2.setCycleCount(FadeTransition.INDEFINITE);

        FadeTransition T3 = new FadeTransition(Duration.seconds(2), viewTile3);
        T3.setFromValue(0.0);
        T3.setToValue(1);
        T3.setAutoReverse(true);
        T3.setCycleCount(FadeTransition.INDEFINITE);

        FadeTransition T4 = new FadeTransition(Duration.seconds(2), viewTile4);
        T4.setFromValue(0.0);
        T4.setToValue(1);
        T4.setAutoReverse(true);
        T4.setCycleCount(FadeTransition.INDEFINITE);

        FadeTransition T5 = new FadeTransition(Duration.seconds(2), viewTile5);
        T5.setFromValue(0.0);
        T5.setToValue(1);
        T5.setAutoReverse(true);
        T5.setCycleCount(FadeTransition.INDEFINITE);

        FadeTransition T6 = new FadeTransition(Duration.seconds(2), viewTile6);
        T6.setFromValue(0.0);
        T6.setToValue(1);
        T6.setAutoReverse(true);
        T6.setCycleCount(FadeTransition.INDEFINITE);

        hBoxWaiting.getChildren().clear();
        hBoxWaiting.getChildren().addAll(viewTile1, viewTile2, viewTile3, viewTile4, viewTile5, viewTile6);
        StackPane.setAlignment(hBoxWaiting,Pos.CENTER);
        tmp.getChildren().add(hBoxWaiting);

        try {
            T1.play();
            TimeUnit.MILLISECONDS.sleep(200);
            T2.play();
            TimeUnit.MILLISECONDS.sleep(200);
            T3.play();
            TimeUnit.MILLISECONDS.sleep(200);
            T4.play();
            TimeUnit.MILLISECONDS.sleep(200);
            T5.play();
            TimeUnit.MILLISECONDS.sleep(200);
            T6.play();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method updates the name of each player according to turn and connection
     * @author Pasquale Gioia
     **/
    public void updateNameColor(){

        for(int i=0; i<state.getNumPlayers(); i++){

            if(state.getCurrPlayerId()==i)
                allNickTexts.get(i).setFill(Color.GREEN);
            else {
                if (state.isConnected(i)) {
                    allNickTexts.get(i).setFill(Color.WHITE);
                }
            }
        }
    }

    /**
     * Update board graphics
     * @author Alessandro Annechini
     */
    public void updateBoard(){
        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                ImageView tile = boardTiles[i][j];
                if(state.getBoard().getTiles()[i][j].isFree()){
                    tile.setVisible(false);
                    tile.setOnMouseClicked((e)->{});
                    tile.setOnMouseMoved((e)->{});
                }
                else{
                    tile.setVisible(true);
                    tile.setImage(new Image(getClass().getResource(state.getTileImagePath(state.getBoard().getTiles()[i][j])).toString()));
                    tile.setOpacity(1);
                    final int i2 = i, j2 = j;
                    switch(cState){
                        case SELECT_FIRST_TILE:
                            if(state.getPickableTiles()[i][j]){
                                tile.setOpacity(1);
                                tile.setOnMouseMoved((e)->{});
                                tile.setOnMouseClicked((e) -> {
                                    doSelectTile(i2,j2);
                                    cState = MATCH_IDLE;
                                    updateBoard();
                                });
                            }
                            else{
                                tile.setOpacity(0.8);
                                tile.setOnMouseClicked((e)->{});
                                tile.setOnMouseMoved((e)->{});
                            }
                        case SELECT_SECOND_TILE:
                            if(state.getPickableTiles()[i][j]){
                                tile.setOpacity(1);
                                tile.setOnMouseMoved((e)->{});
                                tile.setOnMouseClicked((e) -> {
                                    doSelectTile(i2,j2);
                                    cState = MATCH_IDLE;
                                    updateBoard();
                                });
                            }
                            else{
                                tile.setOpacity(0.8);
                                tile.setOnMouseClicked((e)->{});
                                tile.setOnMouseMoved((e)->{});
                            }
                        default:
                            if(state.getCurrPlayerId()==playerTurnId){
                                if(state.getPickableTiles()[i][j]){
                                    tile.setOpacity(1);
                                    tile.setOnMouseMoved((e)->{});
                                    tile.setOnMouseClicked((e) -> {});
                                }
                                else{
                                    tile.setOpacity(0.8);
                                    tile.setOnMouseClicked((e)->{});
                                    tile.setOnMouseMoved((e)->{});
                                }
                            }else{
                                tile.setOpacity(1);
                                tile.setOnMouseClicked((e)->{});
                                tile.setOnMouseMoved((e)->{});
                            }
                    }
                }
            }
        }
    }

    /**
     * Update shelf graphics
     * @author Alessandro Annechini
     * @param toUpdate The shelf to be updated
     */
    public void updateShelf(int toUpdate){
        ImageView[][] tiles = toUpdate == playerTurnId ? playerShelfTiles : otherPlayersTiles.get(toUpdate < playerTurnId ? toUpdate : toUpdate-1);
        ShelfBean newShelf = state.getShelves().get(toUpdate);
        for(int i=0;i<6;i++){
            for(int j=0;j<5;j++){
                if(newShelf.getTiles()[i][j].isFree()) tiles[i][j].setVisible(false);
                else{
                    tiles[i][j].setVisible(true);
                    tiles[i][j].setImage(new Image(getClass().getResource(state.getTileImagePath(newShelf.getTiles()[i][j])).toString()));
                }
            }
        }
        if(toUpdate==playerTurnId){
            if(cState==SELECT_COLUMN){
                columns.setVisible(true);
                for(int i=0; i<5; i++){
                    int finalI = i;
                    rectangles.get(i).setOpacity(0);
                    rectangles.get(i).setOnMouseEntered((e)->rectangles.get(finalI).setOpacity(1));
                    rectangles.get(i).setOnMouseExited((e)->rectangles.get(finalI).setOpacity(0));
                    rectangles.get(i).setOnMouseClicked((e)-> {
                        rectangles.get(finalI).setVisible(true);
                        cState=MATCH_IDLE;
                        doSelectColumn(finalI);
                        updateShelf(playerTurnId);
                    });
                }
            }else if(cState == MATCH_IDLE){
                if(state.getCurrPlayerId()!=playerTurnId){
                    columns.setVisible(false);
                }else {
                    for (int i = 0; i < 5; i++) {
                        int finalI = i;
                        rectangles.get(i).setOnMouseEntered((e) -> {
                        });
                        rectangles.get(i).setOnMouseExited((e) -> {
                        });
                        rectangles.get(i).setOnMouseClicked((e) -> {
                        });
                    }
                }

            }
        }
    }

    /**
     * Update buffer graphics
     * @author Alessandro Annechini
     */
    public void updateBuffer(){
        if(state.getCurrPlayerId()!=playerTurnId){
            hboxBuffer.setVisible(false);
            tile1Buffer.setOnMouseClicked((e) -> {});
            tile2Buffer.setOnMouseClicked((e) -> {});
            tile3Buffer.setOnMouseClicked((e) -> {});
        }else{
            hboxBuffer.setVisible(true);
            if(state.getBuffer()[0].isFree()){
                tile1Buffer.setVisible(false);
                tile1Buffer.setOnMouseClicked((e) -> {});
            }
            else{
                tile1Buffer.setVisible(true);
                tile1Buffer.setImage(new Image(getClass().getResource(state.getTileImagePath(state.getBuffer()[0])).toString()));
                tile1Buffer.setOnMouseClicked((e)->{
                    doPutInColumn(0);
                    cState = MATCH_IDLE;
                    tile1Buffer.setOnMouseClicked((ee) -> {});
                    tile2Buffer.setOnMouseClicked((ee) -> {});
                    tile3Buffer.setOnMouseClicked((ee) -> {});
                });
            }
            if(state.getBuffer()[1].isFree()){
                tile2Buffer.setVisible(false);
                tile2Buffer.setOnMouseClicked((e) -> {});
            }
            else{
                tile2Buffer.setVisible(true);
                tile2Buffer.setImage(new Image(getClass().getResource(state.getTileImagePath(state.getBuffer()[1])).toString()));
                tile2Buffer.setOnMouseClicked((e)->{
                    doPutInColumn(1);
                    cState = MATCH_IDLE;
                    tile1Buffer.setOnMouseClicked((ee) -> {});
                    tile2Buffer.setOnMouseClicked((ee) -> {});
                    tile3Buffer.setOnMouseClicked((ee) -> {});
                });
            }
            if(state.getBuffer()[2].isFree()){
                tile3Buffer.setVisible(false);
                tile3Buffer.setOnMouseClicked((e) -> {});
            }
            else{
                tile3Buffer.setVisible(true);
                tile3Buffer.setImage(new Image(getClass().getResource(state.getTileImagePath(state.getBuffer()[2])).toString()));
                tile3Buffer.setOnMouseClicked((e)->{
                    doPutInColumn(2);
                    cState = MATCH_IDLE;
                    tile1Buffer.setOnMouseClicked((ee) -> {});
                    tile2Buffer.setOnMouseClicked((ee) -> {});
                    tile3Buffer.setOnMouseClicked((ee) -> {});
                });
            }
        }
    }

    /**
     * Update chairs graphics
     * @author Alessandro Annechini
     */
    public void updateChairs(){
        if(state.getFirstPlayerId()==playerTurnId){
            playerChair.setVisible(true);
            for(ImageView i : othersChair) i.setVisible(false);
            return;
        }
        playerChair.setVisible(false);
        for(int i=0;i<state.getNumPlayers()-1;i++){
            if(i < playerTurnId){
                if(state.getFirstPlayerId()==i) othersChair.get(i).setVisible(true);
                else othersChair.get(i).setVisible(true);
            }else{
                if(state.getFirstPlayerId()==i+1) othersChair.get(i).setVisible(true);
                else othersChair.get(i).setVisible(true);
            }
        }
    }

    /**
     * Update common goals token graphics
     * @author Alessandro Annechini
     */
    public void updateCommonGoalsPoints(){
        if(state.getCommonGoalsRemainingPoint()[0] > 0){
            tokenCommonGoal1.setVisible(true);
            tokenCommonGoal1.setImage(new Image(getClass().getResource(state.getScoringTokenImagePath(state.getCommonGoalsRemainingPoint()[0])).toString()));
        } else tokenCommonGoal1.setVisible(false);

        if(state.getCommonGoalsRemainingPoint()[1] > 0){
            tokenCommonGoal2.setVisible(true);
            tokenCommonGoal2.setImage(new Image(getClass().getResource(state.getScoringTokenImagePath(state.getCommonGoalsRemainingPoint()[1])).toString()));
        } else tokenCommonGoal2.setVisible(false);
        for(int i=0;i<state.getNumPlayers();i++){
            ImageView[] tokens = i==playerTurnId ? playerPointsTokens : otherPlayersPointsTokens.get(i<playerTurnId ? i : i-1);
            if(state.getCommonGoalPoints1().get(i)==0){
                tokens[0].setVisible(false);
            }else{
                tokens[0].setVisible(true);
                tokens[0].setImage(new Image(getClass().getResource(state.getScoringTokenImagePath(state.getCommonGoalPoints1().get(i))).toString()));
            }
            if(state.getCommonGoalPoints2().get(i)==0){
                tokens[1].setVisible(false);
            }else{
                tokens[1].setVisible(true);
                tokens[1].setImage(new Image(getClass().getResource(state.getScoringTokenImagePath(state.getCommonGoalPoints2().get(i))).toString()));
            }
            if(state.getLastPlayerId()!=i){
                tokens[2].setVisible(false);
            }else{
                tokens[2].setVisible(true);
            }
        }
    }

}

