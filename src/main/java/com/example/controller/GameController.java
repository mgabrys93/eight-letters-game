package com.example.controller;

import com.example.model.Game;
import com.example.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
public class GameController {

    @Autowired
    private GameService gameService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/game/new/")
    public Game createNewGame(@RequestBody Game game){
        return gameService.save(game);
    }

    @GetMapping("/game/{gameId}")
    public ResponseEntity getGameById(@PathVariable Long gameId){
        Optional<Game> game = gameService.findOne(gameId);
        if(!gameService.canJoinToGame(gameId)) return new ResponseEntity(HttpStatus.NOT_FOUND);
        updateGameUsers(game.get());
        simpMessagingTemplate.convertAndSend("/topic/game/" + gameId + "/players", game.get().getUsers());
        return new ResponseEntity(game.get(), HttpStatus.OK);
    }

    @GetMapping("/game/list/active")
    public ResponseEntity getActiveGames(){
        return new ResponseEntity(gameService.getActiveGames(), HttpStatus.OK);
    }

    private void updateGameUsers(Game game){
        Set<String> users = game.getUsers();
        users.add(SecurityContextHolder.getContext().getAuthentication().getName());
        game.setUsers(users);
        updateGameVisibility(game);
        gameService.update(game);
    }

    private void updateGameVisibility(Game game){
        if(game.getUsers().size() == game.getMaxPlayers()) game.setActive(false);
    }

}
