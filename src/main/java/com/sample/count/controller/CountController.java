package com.sample.count.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sample.count.model.Book;
import com.sample.count.model.Result;
import com.sample.count.service.CountService;

@RestController
@RequestMapping("/api")
public class CountController {

    @Autowired
    CountService countService;

    @RequestMapping(method = GET, value = "/now")
    public Book now() {
        return new Book(1, "Tom and Jerry");
    }

    @RequestMapping(method = POST, value = "/execute")
    public List<Result> execute() {
        return countService.execute();
    }

}
