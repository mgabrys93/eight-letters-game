package com.example.service;

import com.example.model.Game;

import java.util.List;

public interface GameService extends DefaultService<Game> {
    boolean canJoinToGame(long id);
    List<Game> getActiveGames();
}
