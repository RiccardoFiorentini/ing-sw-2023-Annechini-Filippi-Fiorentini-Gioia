package main.java.it.polimi.ingsw.Client;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class GUIGameApplication extends Application {
    private ImageView board;
    private ImageView playerShelf;
    private Stage stage;
    private Scene scene;
    private double stdScreenRatio;
    private double boardTileRatio = 0.1008;
    private double shelfTileRatio = 0.127;
    private ImageView[][] boardTiles;
    private ImageView[][] playerShelfTiles;
    int numPlayers=4;
    private List<ImageView> otherPlayerShelves;
    private List<ImageView[][]> otherPlayersTiles;

    public static void main(String[] args){
        launch(args);
    }

    public void start(Stage stageParam) throws Exception {
        this.stage = stageParam;
        setupGameScreen();
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
}
