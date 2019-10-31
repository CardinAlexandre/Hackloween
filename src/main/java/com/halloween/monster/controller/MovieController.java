package com.halloween.monster.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.halloween.monster.model.Movie;
import com.halloween.monster.repository.MoviesRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Controller
public class MovieController {

    private static final String MOVIE_URL = "https://hackathon-wild-hackoween.herokuapp.com";
    ObjectMapper objectMapper = new ObjectMapper();
    Movie movieObject = null;
    @GetMapping("/")
    public String index() {
        return "index";
    }


    @GetMapping("/connexion")
    public String connexion(){
        return "/movie";
    }


    @GetMapping("/movie")
    public String planet(Model model, @RequestParam(defaultValue = "1", required = false) Long id,
                         @RequestParam(defaultValue = "-1", required = false) Long add) {

        if (add != -1) {
            WebClient webClient = WebClient.create(MOVIE_URL);
            Mono<String> call = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/movies/{id}")
                            .build(add))
                    .retrieve()
                    .bodyToMono(String.class);
            String response = call.block();
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                JsonNode root = objectMapper.readTree(response);

                Movie movieToAdd = objectMapper.convertValue(root.get("movie"), Movie.class);
                MoviesRepository.add(movieToAdd);
                return "redirect:/movie?id=" +id;

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        WebClient webClient = WebClient.create(MOVIE_URL);
        Mono<String> call = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/movies/{id}")
                        .build(id))
                .retrieve()
                .bodyToMono(String.class);
        String response = call.block();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode root = objectMapper.readTree(response);
            movieObject = objectMapper.convertValue(root.get("movie"), Movie.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        model.addAttribute("movieInfos", movieObject);

        if (id > 5) { return "redirect:/list"; }

        return "movies";
    }

    @GetMapping("/list")
    public String moviesList(Model out) {
        out.addAttribute( "movieList",MoviesRepository.findAll());
        return "movies_list";
    }

    @GetMapping("/monmovie")
    public String monmovie(Model out,@RequestParam(name = "id",defaultValue = "0")int id){
        out.addAttribute("monmovie", MoviesRepository.findById(id));
        return "monmovie";
    }
}
