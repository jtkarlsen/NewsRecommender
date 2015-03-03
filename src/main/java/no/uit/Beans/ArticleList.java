package no.uit.Beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JanTore on 26.02.2015.
 */
public class ArticleList {

    String responseTime;
    long totalHits;
    int hitsShowing;
    List<Article> articles;

    public ArticleList() {

    }

    public ArticleList(String responseTime, long totalHits, int hitsShowing) {
        this.responseTime = responseTime;
        this.totalHits = totalHits;
        this.hitsShowing = hitsShowing;
        this.articles = new ArrayList<Article>();
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public long getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(long totalHits) {
        this.totalHits = totalHits;
    }

    public int getHitsShowing() {
        return hitsShowing;
    }

    public void setHitsShowing(int hitsShowing) {
        this.hitsShowing = hitsShowing;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public void addArticles(Article article) {
        this.articles.add(article);
    }

}
