package com.sample.count.service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
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

    public static final String OUTPUT_FILE = "part-r-00000";
    public static final int TOP_N = 10;

    public List<Result> execute(String inputPath, boolean caseInsensitive, int minLength, int maxLength) {
        String outputPath = "output-" + getSeed();

        // execute hadoop and fetch output
        String executeCommand = String.format("./execute.sh %s %s %s %d %d", inputPath, outputPath, caseInsensitive, minLength, maxLength);
        exec(executeCommand);

        // parse results
        return parseResults(outputPath);
    }

    private void exec(String command) {
        try {
            log.debug("---- executing command: {} ----", command);

            // execute command
            Process process = Runtime.getRuntime().exec(command);

            // collect output
            collectOutput(process.getErrorStream());
            collectOutput(process.getInputStream());

            // wait for process to complete
            process.waitFor();
            process.destroy();

            log.debug("---- completed successfully ----");
        } catch (Exception e) {
            log.error("unable to execute command", e);
        }
    }

    private void collectOutput(final InputStream inputStream) {
        new Thread() {
            @Override
            public void run() {
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    List<String> lines = new ArrayList<>();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        log.debug(line);
                        lines.add(line);
                    }
                    reader.close();
                } catch (Exception e) {
                    log.error("unable to collect output");
                }
            };
        }.start();
    }

    private List<Result> parseResults(String outputPath) {
        List<Result> results = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(outputPath + "/" + OUTPUT_FILE)));
            String line = null;
            while ((line = reader.readLine()) != null) {
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
            reader.close();
        } catch (Exception e) {
            log.error("unable to parse results");
        }

        Collections.sort(results);

        if (results.size() > TOP_N) {
            results = results.subList(0, TOP_N);
        }

        return results;
    }

    private String getSeed() {
        return String.valueOf(System.currentTimeMillis()).substring(8);
    }

}
