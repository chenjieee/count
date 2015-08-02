package com.sample.count.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sample.count.model.Result;

@Service
public class CountService {

    private static Logger log = LoggerFactory.getLogger(CountService.class);

    public static final int TOP_N = 10;

    public List<Result> execute(String inputPath, boolean caseInsensitive, int minLength, int maxLength) {
        String outputPath = "output-" + getSeed();

        // execute hadoop jar
        String executeCommand = String.format("/home/chenjie/workspace/count/execute.sh %s %s %s %d %d", inputPath, outputPath, caseInsensitive,
                minLength, maxLength);
        exec(executeCommand);

        // fetch output from hdfs
        String fetchCommand = String.format("/home/chenjie/workspace/count/fetch.sh %s", outputPath);
        List<String> lines = exec(fetchCommand);

        // parse results
        return parseResults(lines);
    }

    private List<String> exec(String command) {
        try {
            log.debug("---- executing command: {} ----", command);

            // execute command
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            // collect output
            List<String> lines = new ArrayList<>();
            String line = null;
            while ((line = reader.readLine()) != null) {
                log.debug(line);
                lines.add(line);
            }

            log.debug("---- completed successfully ----");
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

        Collections.sort(results);

        return results.subList(0, TOP_N);
    }

    private String getSeed() {
        return String.valueOf(System.currentTimeMillis()).substring(8);
    }

}
