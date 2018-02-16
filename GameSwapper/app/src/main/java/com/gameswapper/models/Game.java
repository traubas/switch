package com.gameswapper.models;

import android.media.Image;

import com.gameswapper.R;

/**
 * Created by user on 14/02/2018.
 */

public class Game {
    private String game_name, description, platform;
    int game_image;

    public Game() {

    }
    public Game(String name) {
        game_name =name;
    }
    public Game(String name, String desc) {
        game_name = name;
        description = desc;
    }
    public Game(String name, String desc, String plat) {
        game_name = name;
        description = desc;
        platform = plat;
    }
    public Game(String name, String desc, int imageId) {
        game_name = name;
        description = desc;
        game_image = imageId;
    }
    public Game(String name, String desc, int imageId, String plat) {
        game_name = name;
        description = desc;
        game_image = imageId;
        platform = plat;
    }
    public void setPlatform(String plat) { platform = plat; }
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
    public String getPlatform() { return platform; }

    public int getImage() {
        return game_image;
    }

    public void addImage() {
        switch (game_name) {
            case "PlayerUnknown's: Battlegrounds":
                game_image = R.drawable.pubg;
                break;
            case "Fifa 18":
                game_image = R.drawable.fifa18;
                break;
            case "Assassin's Creed: Origins":
                game_image = R.drawable.assassinsorigins;
                break;
            case "NBA 2K18":
                game_image = R.drawable.nba2k18;
                break;
            case "Uncharted 4: A Thief's End":
                game_image = R.drawable.uncharted4;
                break;
        }
    }
}
