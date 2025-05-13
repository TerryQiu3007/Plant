package sio.plant;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class FarmTile {
    private ImageView imageView;
    private long plantedTime;
    private boolean planted = false;
    private boolean arroser = false;
    public ImageView idArrosoir;

    public FarmTile(ImageView imageView) {
        this.imageView = imageView;
    }

    public void plant() {
        arroser = true;
        planted = true;
        plantedTime = System.currentTimeMillis();
        updateImage("/images/terrain/plante.png");
    }
    public void creuser() {
        arroser = true;
        planted = false;
        updateImage("/images/terrain/fosse_creusee.png");
    }

    public int harvest() {
        planted = false;
        updateImage("/images/terrain/terre.png");
        return 20;
    }

    public boolean isReadyToHarvest() {
        return System.currentTimeMillis() - plantedTime > 15000; //15s
    }

    public boolean isPlanted() {
        return planted;
    }
    public boolean isArroser() {
        return arroser;
    }

    public void update() {
        if (planted && isReadyToHarvest()&& isArroser()) {
            updateImage("/images/terrain/ble.png");
        }
    }

    private void updateImage(String imagePath) {
        Image image = new Image(getClass().getResourceAsStream(imagePath));
        imageView.setImage(image);
    }
}

