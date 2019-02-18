package org.abondar.industrial.searchestimator.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * POJO class for generating JSON response
 */
@XmlRootElement
public class Estimation {


    private String keyword;

    private int score;

    public Estimation(String keyword, int score) {
        this.keyword = keyword;
        this.score = score;
    }

    public Estimation(){

    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Estimation{" +
                "keyword='" + keyword + '\'' +
                ", score=" + score +
                '}';
    }
}
