package com.gameswapper.models;

import android.media.Image;

/**
 * Created by user on 14/02/2018.
 */

public class Game {
    private String game_name, description;
    int game_image;

    public Game() {

    }

    public Game(String name, String desc, int imageId) {
        game_name = name;
        description = desc;
        game_image = imageId;
    }

    public void setName(String name) {
        game_name = name;
    }

    public void setDesc(String desc) {
        description = desc;
    }

    public void setImage(int imageId) {
        game_image = imageId;
    }

    public String getName() {
        return game_name;
    }
    public String getDesc() {
        return description;
    }

    public int getImage() {
        return game_image;
    }
}
