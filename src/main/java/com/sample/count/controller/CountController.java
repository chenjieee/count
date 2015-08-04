package com.sample.count.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sample.count.model.Result;
import com.sample.count.service.CountService;

@RestController
@RequestMapping("/api")
public class CountController {

    @Autowired
    CountService countService;

    @RequestMapping(method = GET, value = "/execute")
    public List<Result> execute(
            @RequestParam("inputPath") String inputPath, 
            @RequestParam(value = "caseInsensitive", defaultValue = "true") boolean caseInsensitive, 
            @RequestParam(value = "minLength", defaultValue = "0") int minLength, 
            @RequestParam(value = "maxLength", defaultValue = "255") int maxLength) {
        return countService.execute(inputPath, caseInsensitive, minLength, maxLength);
    }

}
