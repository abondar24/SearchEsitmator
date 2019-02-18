package org.abondar.industrial.searchestimator.estimator;

import java.util.ArrayList;
import java.util.List;

/**
 * The class for score estimation
 */
public class ScoreEstimator {

    //3 times ten occurrences
    private static final double MAX_SCORE = 30;

    private String keyword;

    private List<Integer> scores;

    public ScoreEstimator(String keyword) {
        this.keyword = keyword;
        scores = new ArrayList<>();

    }


    /**
     * Calculates the number of keyword occurrences in autocomplete results
     * @param reqResult - results of request to autocomplete API
     */
    public void calcOccurrence(List<String> reqResult) {
        String regex = "(.* )*(" + keyword + ")( .*)*";
        long numOcc = reqResult.stream()
                .filter(r -> r.matches(regex))
                .count();
        scores.add((int) numOcc);
    }

    /**
     * Estimate the score
     */
    public int estimate() {

        int sum = scores.stream().mapToInt(Integer::intValue).sum();

        return Double.valueOf((sum / MAX_SCORE) * 100).intValue();
    }

    public List<Integer> getScores() {
        return scores;
    }
}
