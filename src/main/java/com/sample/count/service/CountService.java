package com.sample.count.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sample.count.model.Result;

@Service
public class CountService {

    private static Logger log = LoggerFactory.getLogger(CountService.class);

    public List<Result> execute() {
        System.out.println("start executing...");
        List<String> lines = exec("hdfs dfs -cat output/*");
        return parseResults(lines);
    }

    private List<String> exec(String command) {
        try {
            log.debug("---- executing command: {}", command);
            List<String> lines = new ArrayList<>();
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                log.debug(line);
                lines.add(line);
            }
            log.debug("---- complete successfully");
            return lines;
        } catch (Exception e) {
            log.error("failed to execute", e);
        }
        return new ArrayList<>();
    }

    private List<Result> parseResults(List<String> lines) {
        List<Result> results = new ArrayList<>();
        for (String line : lines) {
            try {
                String[] parts = line.split("\\s+");
                if (parts.length == 2) {
                    Result result = new Result(parts[0], Integer.parseInt(parts[1]));
                    results.add(result);
                }
            } catch (Exception e) {
                log.warn("unable to parse: {}", line);
            }
        }
        return results;
    }

}
