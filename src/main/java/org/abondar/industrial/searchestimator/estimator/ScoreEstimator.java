package org.abondar.industrial.searchestimator.estimator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScoreEstimator {

    //3 times ten occurrences
    private static final double MAX_SCORE=30;

    private String keyword;

    private List<Integer> scores;

    public ScoreEstimator(String keyword){
        this.keyword = keyword;
        scores = new ArrayList<>();

    }


    public void calcOccurrence(List<String> reqResult){
        String regex = "(.* )*("+keyword+")( .*)*";
        long numOcc = reqResult.stream()
                .filter(r-> r.matches(regex))
                .count();
        scores.add((int) numOcc);
    }

    public int estimate(){

        int sum = scores.stream().mapToInt(Integer::intValue).sum();

        return Double.valueOf((sum/MAX_SCORE)*100).intValue();
    }

    public List<Integer> getScores() {
        return scores;
    }
}
