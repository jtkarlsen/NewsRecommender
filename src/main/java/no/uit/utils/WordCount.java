package no.uit.utils;

/**
 * Created by JanTore on 27.02.2015.
 */
public class WordCount {

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void addCount() {
        this.count++;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public WordCount(String word) {
        this.word = word;
        this.count = 1;
    }

    int count;
    String word;
}
