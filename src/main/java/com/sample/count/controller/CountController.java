package com.sample.count.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sample.count.model.Book;

@RestController
@RequestMapping("/api")
public class CountController {

    @RequestMapping(method = GET, value = "/now")
    public Book now() {
        return new Book(1, "Tom and Jerry");
    }

}
