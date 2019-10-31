package com.halloween.monster.repository;

import com.halloween.monster.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MoviesRepository {
    private static List<Movie> moviesList = new ArrayList<Movie>() {
        {
        }};


    public static void add (Movie movie){
        moviesList.add(movie);
    }

    public static List<Movie> findAll() {
        return moviesList;
    }

    public static void addComentaire (Movie movie){
        moviesList.add(movie);
    }

    public static Movie findById(int id) {
        for (Movie movie : moviesList) {
            if (id==movie.getId()) {
                return movie;
            }
        }
        return null;
    }
}
