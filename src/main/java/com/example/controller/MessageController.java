package com.example.controller;

import com.example.enums.GameStatusEnum;
import com.example.model.Game;
import com.example.model.GameStatus;
import com.example.model.Message;
import com.example.service.GameService;
import com.example.util.WordChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@RestController
public class MessageController {

    @Autowired
    private GameService gameService;

    @Autowired
    private WordChecker wordChecker;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/message")
    @SendTo("/topic/message")
    public Message getMessage(Message message){
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        message.setMessageDateTime(localDateTime.format(dtf));
        return message;
    }

    @MessageMapping("/game/{gameId}/status")
    public void updateGameStatus(@DestinationVariable Long gameId, GameStatus gameStatus){
        Optional<Game> game = gameService.findOne(gameId);
        if(game.isPresent()){
            simpMessagingTemplate.convertAndSend("/topic/game/" + gameId + "/status",
                    checkWord(gameStatus, game.get().getCharacterSet()));
        }
    }

    private GameStatus checkWord(GameStatus gameStatus, String letterSet){
        if(!correctLetters(gameStatus.getWord(), letterSet)) {
            gameStatus.setGameStatusEnum(GameStatusEnum.INVALID_LETTER);
        }else if(wordChecker.isValid(gameStatus.getWord())){
            int newPoints = 5 * gameStatus.getWord().length();
            gameStatus.getPlayersPoints().put(gameStatus.getUsername(), newPoints + gameStatus.getPlayersPoints()
                    .getOrDefault(gameStatus.getUsername(), 0));
            gameStatus.setGameStatusEnum(GameStatusEnum.SUCCESS);
        }else {
            gameStatus.setGameStatusEnum(GameStatusEnum.INVALID_WORD);
        }
        return gameStatus;
    }

    private boolean correctLetters(String word, String letterSet){
        StringBuilder stringBuilder = new StringBuilder(letterSet);
        for(int i=0; i<word.length(); i++){
            char c = word.charAt(i);
            int ind = stringBuilder.indexOf(String.valueOf(c));
            if(ind == -1) return false;
            stringBuilder.deleteCharAt(ind);
        }
        return true;
    }

}
