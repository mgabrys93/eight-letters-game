package com.example.service.impl;

import com.example.model.Game;
import com.example.repository.GameRepository;
import com.example.service.GameService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@RunWith(MockitoJUnitRunner.class)
public class GameServiceImplTest {

    @Mock
    GameRepository gameRepository;

    @InjectMocks
    GameService gameService = new GameServiceImpl();

    private Game activeGame, inactiveGame, fullActiveGame;


    @Before
    public void setUp() throws Exception {
        activeGame = new Game("test", 5, 100, true);
        inactiveGame = new Game("test", 5, 100, false);
        fullActiveGame = new Game("test", 3, 100, true);

        Set<String> users = new HashSet<>(Arrays.asList("user", "user1", "user2"));

        activeGame.setUsers(users);
        inactiveGame.setUsers(users);
        fullActiveGame.setUsers(users);

    }

    @Test
    public void canJoinToGame() throws Exception {
        Mockito.when(gameRepository.findOne(0L)).thenReturn(activeGame);
        Mockito.when(gameRepository.findOne(1L)).thenReturn(inactiveGame);
        Mockito.when(gameRepository.findOne(2L)).thenReturn(fullActiveGame);

        Assert.assertTrue(gameService.canJoinToGame(0L));
        Assert.assertFalse(gameService.canJoinToGame(1L));
        Assert.assertFalse(gameService.canJoinToGame(2L));

    }

}