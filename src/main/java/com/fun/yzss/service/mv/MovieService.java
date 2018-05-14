package com.fun.yzss.service.mv;

import com.fun.yzss.model.movie.Movie;

import java.util.List;

/**
 * Created by fanqq on 2016/10/10.
 */
public interface MovieService {
    void addMovie(Movie mv) throws Exception;

    void addMovies(List<Movie> mvs) throws Exception;

    void listMovie() throws Exception;
}
