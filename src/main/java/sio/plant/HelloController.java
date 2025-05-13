package sio.plant;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import java.net.URL;
import java.util.ResourceBundle;


public class HelloController implements Initializable {

    @FXML
    public AnchorPane idAccueil;
    @FXML
    public AnchorPane idFerme;
    @FXML
    public ImageView idHoue;
    @FXML
    public AnchorPane idFin;
    @FXML
    public ImageView idArrosoir;
    @FXML
    private Label moneyLabel;
    @FXML
    private GridPane farmGrid;
    @FXML
    public Label idGraine;
    @FXML
    public Label idBle;

    private ImageView activeHoeCursor;
    private boolean isHoeSelected = false;

    private ImageView activeArroseur;
    private boolean isArrosoirSelected = false;

    private static final int TILE_SIZE = 64;
    private static final int GRID_SIZE = 5;
    private int money = 30;
    private int graine = 3;
    private int ble = 0;
    private FarmTile[][] tiles = new FarmTile[GRID_SIZE][GRID_SIZE];


    private void setupFarmGrid() {
        for (int x = 0; x < GRID_SIZE; x++) {
            for (int y = 0; y < GRID_SIZE; y++) {
                ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/images/terrain/terre.png")));
                imageView.setFitWidth(TILE_SIZE);
                imageView.setFitHeight(TILE_SIZE);

                FarmTile tile = new FarmTile(imageView);
                tiles[x][y] = tile;

                int finalX = x;
                int finalY = y;
                imageView.setOnMouseClicked(event -> handleTileClick(tile, finalX, finalY));

                farmGrid.add(imageView, x, y);
            }
        }
    }

    private void handleTileClick(FarmTile tile, int x, int y) {
        if (activeHoeCursor.isVisible()){
            if (tile.isPlanted()) {
                if (tile.isReadyToHarvest()) {
                    ble += tile.harvest();
                    updateBleDisplay();
                }
            } else {
                if (graine >= 1) {
                    graine -= 1;
                    updateGraineDisplay();
                    tile.creuser();
                }
            }
        }
    if (activeArroseur.isVisible() && tile.isArroser()){
        tile.plant();
    }
    }

    private void updateMoneyDisplay() {
        moneyLabel.setText(String.valueOf(money));
    }

    private void updateGraineDisplay() {
        idGraine.setText(String.valueOf(graine));
    }

    private void updateBleDisplay() {
        idBle.setText(String.valueOf(ble));
    }

    private void startGameLoop() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                for (int x = 0; x < GRID_SIZE; x++) {
                    for (int y = 0; y < GRID_SIZE; y++) {
                        tiles[x][y].update();

                    }
                }
            }
        }.start();
    }

    @FXML
    public void clickStart(MouseEvent mouseEvent) {
        idAccueil.setVisible(false);
        idFerme.setVisible(true);
        idFin.setVisible(false);
        moneyLabel.setText(String.valueOf(money));
        idGraine.setText(String.valueOf(graine));
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idAccueil.setVisible(true);
        idFerme.setVisible(false);
        idFin.setVisible(false);
        setupFarmGrid();
        startGameLoop();
        setupHoeCursor();
        setupButton();
        setupArrosoirCursor();
    }

    @FXML
    public void clickGraine(MouseEvent mouseEvent) {
        if (money >= 30) {
            money -= 30;
            graine += 1;
            updateMoneyDisplay();
            updateGraineDisplay();
        }
    }

    @FXML
    public void clickMoney(MouseEvent mouseEvent) {
        if (ble >= 20) {
            ble -= 20;
            money += 50;
            System.out.println("+50");
            updateBleDisplay();
            updateMoneyDisplay();
        }
        if (money>=1000){
            idFerme.setVisible(false);
            idFin.setVisible(true);
        }
    }

    private void setupHoeCursor() {
        // Création du curseur-houe
        activeHoeCursor = new ImageView(new Image(getClass().getResourceAsStream("/images/interface/menu/Houe_0.png")));
        activeHoeCursor.setFitWidth(50);
        activeHoeCursor.setFitHeight(50);
        activeHoeCursor.setPreserveRatio(true);
        activeHoeCursor.setVisible(false);
        activeHoeCursor.setMouseTransparent(true);

        // Ajout au conteneur principal
        AnchorPane rootPane = (AnchorPane) farmGrid.getParent();
        rootPane.getChildren().add(activeHoeCursor);
    }
    private void setupButton() {
        // Configuration du bouton
        idHoue.setOnMouseClicked(e -> {
            isHoeSelected = !isHoeSelected;

            if (isHoeSelected) {
                activeHoeCursor.setVisible(true);
                activeArroseur.setVisible(false);
                setupHoeTracking();
            } else {
                activeHoeCursor.setVisible(false);
                removeHoeTracking();
            }
        });
        idArrosoir.setOnMouseClicked(e -> {
            isArrosoirSelected = !isArrosoirSelected;

            if (isArrosoirSelected) {
                activeArroseur.setVisible(true);
                activeHoeCursor.setVisible(false);
                setupArrosoirTracking();
            } else {
                activeArroseur.setVisible(false);
                removeArrosoirTracking();
            }
        });
    }
    private void setupHoeTracking() {
        // Suivi de la souris
        farmGrid.setOnMouseMoved(e -> {
            activeHoeCursor.setX(e.getSceneX() - 170); // Centrage
            activeHoeCursor.setY(e.getSceneY() - 30);
        });
    }
    private void removeHoeTracking() {
        farmGrid.setOnMouseMoved(null);
        farmGrid.setOnMouseClicked(null);
    }


    private void setupArrosoirCursor() {
        // Création du curseur-houe
        activeArroseur = new ImageView(new Image(getClass().getResourceAsStream("/images/interface/menu/arrosoir.png")));
        activeArroseur.setFitWidth(50);
        activeArroseur.setFitHeight(50);
        activeArroseur.setPreserveRatio(true);
        activeArroseur.setVisible(false);
        activeArroseur.setMouseTransparent(true);

        // Ajout au conteneur principal
        AnchorPane rootPane = (AnchorPane) farmGrid.getParent();
        rootPane.getChildren().add(activeArroseur);
    }
    private void setupArrosoirTracking() {
        // Suivi de la souris
        farmGrid.setOnMouseMoved(e -> {
            activeArroseur.setX(e.getSceneX() - 170); // Centrage
            activeArroseur.setY(e.getSceneY() - 30);
        });
    }
    private void removeArrosoirTracking() {
        farmGrid.setOnMouseMoved(null);
        farmGrid.setOnMouseClicked(null);
    }

}