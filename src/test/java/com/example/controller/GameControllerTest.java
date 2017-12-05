package com.example.controller;

import com.example.model.Game;
import com.example.service.impl.GameServiceImpl;
import com.example.util.WordChecker;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class GameControllerTest {

    @InjectMocks
    private GameController gameController = new GameController();

    @Mock
    private GameServiceImpl gameService;

    @Mock
    private WordChecker wordChecker;

    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    private MockMvc mockMvc;
    private Game game, inactiveGame, nullGame;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders
                .standaloneSetup(gameController)
                .build();
        setupGames();
        setupMocks();
    }


    @Test
    public void getGameById() throws Exception {
        Mockito.doNothing().when(simpMessagingTemplate).convertAndSend(Mockito.any(String.class), Mockito.any(Object.class));

        mockMvc.perform(get("/game/" + game.getId()))
                .andExpect(status().isOk());
        mockMvc.perform(get("/game/" + inactiveGame.getId()))
                .andExpect(status().isNotFound());
        mockMvc.perform(get("/game/" + nullGame.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void create() throws Exception {
        String json = new ObjectMapper().writeValueAsString(new Game("test", 3, 100, true));

        Mockito.when(wordChecker.getCharacterSet()).thenReturn("test");

        MvcResult result = mockMvc.perform(post("/game/new/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk())
            .andReturn();

        assertEquals(new ObjectMapper().writeValueAsString(game), result.getResponse().getContentAsString());
    }

    private void setupMocks(){
        Mockito.when(gameService.canJoinToGame(0)).thenReturn(true);
        Mockito.when(gameService.canJoinToGame(1)).thenReturn(false);
        Mockito.when(gameService.canJoinToGame(2)).thenReturn(false);

        Mockito.when(gameService.findOne(0)).thenReturn(java.util.Optional.ofNullable(game));
        Mockito.when(gameService.findOne(1)).thenReturn(java.util.Optional.ofNullable(inactiveGame));
        Mockito.when(gameService.findOne(2)).thenReturn(Optional.empty());

        Mockito.when(gameService.save(Mockito.any(Game.class))).thenReturn(game);

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("testUser");

    }

    private void setupGames(){
        game = new Game("test", 5, 100, true);
        inactiveGame = new Game("test", 5, 100, true);
        nullGame = new Game("test", 5, 100, true);

        Set<String> users = new HashSet<>(Arrays.asList("user", "user1", "user2"));
        game.setUsers(users);
        game.setId(0);
        inactiveGame.setUsers(users);
        inactiveGame.setId(1);
        nullGame.setUsers(users);
        nullGame.setId(2);
    }
}
