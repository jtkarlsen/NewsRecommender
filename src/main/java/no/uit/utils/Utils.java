package no.uit.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by JanTore on 26.02.2015.
 */
public class Utils {
    public static String removeCharsFromEndOfString(int num, String str) {
        if (str.length() >= num) {
            str = str.substring(0, str.length()-num);
        }
        return str;
    }

    public static List<WordCount> stringArrayCounter(List<String> words) {
        List<WordCount> wordCountList = new ArrayList<WordCount>();
        for (String word : words) {
            boolean match = false;
            for (int i = 0; i<wordCountList.size(); i++) {
                if (word.equals(wordCountList.get(i).getWord())) {
                    wordCountList.get(i).addCount();
                    match = true;
                }
                if (match) {
                    break;
                }
            }
            if (!match) {
                wordCountList.add(new WordCount(word));
            }
        }
        return wordCountList;
    }
}
