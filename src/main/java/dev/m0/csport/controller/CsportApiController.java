package dev.m0.csport.controller;

import java.util.*;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(name = "Personal Sport TrackerZ")
@RestController
public class CsportApiController {

    private final List<String> quotes = List.of(
      "The Greatest Trick the Devil Ever Pulled Was Convincing the World He Didn’t Exist. - The Usual Suspects",
      "All creative people want to do the unexpected. - Hedy Lamarr",
      "Im going to make him an offer he cant refuse. - The Godfather",
      "The knowledge of anything, since all things have causes, is not acquired or complete unless it is known by its causes. -Ibn Sina",
      "May the Force be with you. - Star Wars",
      "Just when thoght I was out, They pull me back in. - The Godfather",
      "Keep it simple, stupid. - Kelly johnson",
      "Say “hello” to my little friend! - Scarface",
      "The saddest aspect of life right now is that gathers knowledge faster than society gathers wisdom. ― Isaac Asimov",
      "You can't handle the truth! - A Few God Men",
      "I’ll be back. - Terminator 2",
      "Before anything else, preparation is the key to success. - Alexander Graham Bell",
      "Houston, we have a problem. - Apollo 13",
      "I have no special talents, I am only passionately curious. - Albert Einstein",
      "For a successful technology, reality must take precedence over public relations, for nature cannot be fooled. - Richard Feynman"
    );

    @GetMapping("/")
    @Operation(
      summary = "Random Quotes")
    @ResponseBody
    public String getRandomQuote() {
        Random random = new Random();
        int randomIndex = random.nextInt(quotes.size());
        return quotes.get(randomIndex);
    }
}