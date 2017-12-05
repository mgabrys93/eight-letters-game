package com.example.model;

import com.example.enums.GameStatusEnum;

import java.util.Map;

public class GameStatus {
    private String username;
    private String word;
    private Map<String, Integer> playersPoints;
    private boolean success;
    private GameStatusEnum gameStatusEnum;

    public GameStatus() {

    }

    public GameStatus(String word, boolean success, GameStatusEnum gameStatusEnum, String username) {
        this.word = word;
        this.success = success;
        this.gameStatusEnum = gameStatusEnum;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public GameStatusEnum getGameStatusEnum() {
        return gameStatusEnum;
    }

    public void setGameStatusEnum(GameStatusEnum gameStatusEnum) {
        this.gameStatusEnum = gameStatusEnum;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Map<String, Integer> getPlayersPoints() {
        return playersPoints;
    }

    public void setPlayersPoints(Map<String, Integer> playersPoints) {
        this.playersPoints = playersPoints;
    }
}

