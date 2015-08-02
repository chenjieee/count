package com.sample.count.model;

public class Result implements Comparable<Result> {

    private String word;
    private int count;

    public Result() {
    }

    public Result(String word, int count) {
        this.word = word;
        this.count = count;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public int compareTo(Result that) {
        if (this.count < that.count) {
            return 1;
        } else if (this.count == that.count) {
            return 0;
        } else {
            return -1;
        }
    }

}
