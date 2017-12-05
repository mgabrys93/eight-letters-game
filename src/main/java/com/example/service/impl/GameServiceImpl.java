package com.example.service.impl;

import com.example.model.Game;
import com.example.repository.GameRepository;
import com.example.service.GameService;
import com.example.util.WordChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class GameServiceImpl implements GameService {


    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private WordChecker wordChecker;

    @Override
    public Game save(Game game) {
        return gameRepository.save(updateNewGame(game));
    }

    @Override
    public Game update(Game game) {
        return gameRepository.save(game);
    }

    @Override
    public void delete(Game game) {
        gameRepository.delete(game);
    }

    @Override
    public List<Game> findAll() {
        return gameRepository.findAll();
    }

    @Override
    public Optional<Game> findOne(long id) {
        return Optional.ofNullable(gameRepository.findOne(id));
    }

    @Override
    public boolean canJoinToGame(long id) {
        Optional<Game> game = findOne(id);
        return game.isPresent()
                && (game.get().isActive() && game.get().getUsers().size() < game.get().getMaxPlayers());
    }

    @Override
    public List<Game> getActiveGames() {
        return gameRepository.findAllByIsActiveTrue();
    }

    private Game updateNewGame(Game game){
        game.setActive(true);
        game.setCharacterSet(wordChecker.getCharacterSet());
        game.setUsers(new HashSet<>());
        return game;
    }
}
