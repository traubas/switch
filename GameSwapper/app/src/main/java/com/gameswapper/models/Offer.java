package com.gameswapper.models;

/**
 * Created by ofir on 17/02/18.
 */

public class Offer {
    String user_name;
    String game_name;
    String platform;
    String location;
    public Offer() {

    }
    public Offer(String name, String game, String plat) {
        user_name = name;
        game_name = game;
        platform = plat;
    }
    public Offer(String name, String game, String plat, String loc) {
        user_name = name;
        game_name = game;
        platform = plat;
        location = loc;
    }
    public void setGame(String game) {
        game_name = game;
    }
    public void setName(String name) {
        user_name = name;
    }
    public void setPlatform(String plat) {
        platform = plat;
    }
    public void setLocation(String loc) {
        location = loc;
    }
    public String getName() {
        return user_name;
    }
    public String getGame() {
        return game_name;
    }
    public String getPlatform() {
        return platform;
    }
    public String getLocation() {
        return location;
    }
}
