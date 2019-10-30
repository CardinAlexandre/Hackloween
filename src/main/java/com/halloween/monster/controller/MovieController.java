package com.halloween.monster.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.halloween.monster.model.Movie;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.halloween.monster.repository.MoviesRepository.registerMouviesList;

@Controller
public class MovieController {

    private static final String MOVIE_URL = "https://hackathon-wild-hackoween.herokuapp.com/";
    ObjectMapper objectMapper = new ObjectMapper();
    Movie movieObject = null;
    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/movie")
    public String planet(Model model, @RequestParam Long id) {

        WebClient webClient = WebClient.create(MOVIE_URL);
        Mono<String> call = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/movies/{id}/")
                        .build(id))
                .retrieve()
                .bodyToMono(String.class);

        String response = call.block();
        ObjectMapper objectMapper = new ObjectMapper();
        // TODO : call the API and retrieve the planet

        try {
            movieObject = objectMapper.readValue(response, Movie.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        model.addAttribute("movieInfos", movieObject);

        return "movie";
    }

    @GetMapping("/save")
    public void save(){
        registerMouviesList(movieObject.getId(), movieObject.getTitle(), movieObject.getDirector(),movieObject.getYear(),movieObject.getCountry(),
                            movieObject.getCreatedAt(),movieObject.getUpdatedAt());
    }
}
