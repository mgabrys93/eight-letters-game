package com.example.util;

import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

@Component
public class WordChecker implements ApplicationListener<ContextRefreshedEvent>{

    private Set<String> englishWords;
    private List<String> eightCharacterEnglishWords;
    private static final int WORD_LENGTH = 8;

    @Bean
    private Random getRandom(){
        return new Random();
    }

    public boolean isValid(String word){
        return englishWords.contains(word);
    }

    public String getCharacterSet(){
        String word = findRandomEightLetterWord();
        return shuffle(word);
    }

    private String shuffle(String word){
        List<Character> characters = new ArrayList<>();
        for(char c: word.toCharArray()){
            characters.add(c);
        }
        StringBuilder output = new StringBuilder(word.length());
        while(characters.size() != 0){
            int randPicker = getRandom().nextInt(characters.size());
            output.append(characters.remove(randPicker));
        }
        return output.toString();
    }

    private String findRandomEightLetterWord(){
        int randomValue = getRandom().nextInt(eightCharacterEnglishWords.size());
        return eightCharacterEnglishWords.get(randomValue);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        englishWords = loadEnglishWords();
    }

    private Set<String> loadEnglishWords(){
        try(BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(
                        new ClassPathResource("words_alpha.txt").getInputStream()))) {
            englishWords = new HashSet<>();
            eightCharacterEnglishWords = new ArrayList<>();
            String line;
            while ((line = bufferedReader.readLine()) != null){
                if(line.length() == WORD_LENGTH) eightCharacterEnglishWords.add(line);
                englishWords.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return englishWords;
    }
}
