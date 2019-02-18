package org.abondar.experimental.seachestimator.test;

import org.abondar.industrial.searchestimator.estimator.ScoreEstimator;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Score estimator tests
 */
public class ScoreEstimatorTest {

    /**
     * Unit test for regular expression which finds occurrences
     */
    @Test
    public void regexTest(){
        String keyword = "test";
        String regex = "(.* )*("+keyword+")( .*)*";

        assertTrue("test".matches(regex));
        assertFalse("test1".matches(regex));
        assertTrue("test 2".matches(regex));
        assertTrue("big test".matches(regex));
        assertTrue("big test 1".matches(regex));

    }

    /**
     * Unit test for finding occurrences if keyword is a single word
     */
    @Test
    public void occurrenceSingleKeywordTest(){
        String keyword = "test";
        ScoreEstimator sc = new ScoreEstimator(keyword);

        List<String> reqResults = Arrays.asList("test", "test1","test 2","big test","big test 1");
        sc.calcOccurrence(reqResults);

        int occ = sc.getScores().get(0);
        assertEquals(4L,occ);
    }


    /**
     * Unit test for finding occurrences if keyword has several words
     */
    @Test
    public void occurrenceMultiKeywordTest(){
        String keyword = "big test";
        ScoreEstimator sc = new ScoreEstimator(keyword);

        List<String> reqResults = Arrays.asList("big test1", "bigtest","test really big","big test", "really test big","more big test");
        sc.calcOccurrence(reqResults);

        int occ = sc.getScores().get(0);
        assertEquals(2L,occ);
    }


    /**
     * Unit test for score estimation
     */
    @Test
    public void scoreTest(){
        String keyword = "test";
        ScoreEstimator sc = new ScoreEstimator(keyword);

        List<String> reqResults = Arrays.asList("test", "test1","test 2","big test","big test 1");
        sc.calcOccurrence(reqResults);

        reqResults = Arrays.asList("test", "test1","test2","big test","big test 1");
        sc.calcOccurrence(reqResults);

        reqResults = Arrays.asList("test", "test 1","test 2","big test","big test 1");
        sc.calcOccurrence(reqResults);

        int score = sc.estimate();
        assertEquals(40L,score);
    }

}
