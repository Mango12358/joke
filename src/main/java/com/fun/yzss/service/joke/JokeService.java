package com.fun.yzss.service.joke;

public interface JokeService {
    void fetchJokes(boolean increment);

    void pushJokes();
}
