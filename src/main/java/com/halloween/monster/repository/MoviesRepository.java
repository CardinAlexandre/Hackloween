package com.halloween.monster.repository;

import com.halloween.monster.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MoviesRepository {
    private static List<Movie> mouviesList = new ArrayList<Movie>() {
        {
        }};

    public static void registerMouviesList(int id, String title, String director, int year, String country, String createdAt, String updatedAt) {
        id = mouviesList.size() + 1;
        mouviesList.add(new Movie(id, title, director, year, country, createdAt, updatedAt));
    }
}
