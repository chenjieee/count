package com.sample.count.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sample.count.model.Book;
import com.sample.count.model.Result;

@RestController
@RequestMapping("/api")
public class CountController {

    @RequestMapping(method = GET, value = "/now")
    public Book now() {
        return new Book(1, "Tom and Jerry");
    }

    @RequestMapping(method = POST, value = "/execute")
    public Result[] execute() {
        return new Result[] { new Result("hello", 1234), new Result("world", 999), new Result("welcome", 800), new Result("game", 777),
                new Result("controller", 345), new Result("play", 333), new Result("springframework", 300), new Result("main", 200), new Result("bower", 100),
                new Result("css", 10) };
    }

}
