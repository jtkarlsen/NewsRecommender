package no.uit.Beans;

/**
 * Created by JanTore on 26.02.2015.
 */
public class Article {

    String id;
    String score;
    String title;
    String description;
    String link;
    String author;
    String guid;
    String pubDate;

    public Article() {

    }

    public Article(String id, String score, String title, String description, String link, String author, String guid, String pubDate) {
        this.id = id;
        this.score = score;
        this.title = title;
        this.description = description;
        this.link = link;
        this.author = author;
        this.guid = guid;
        this.pubDate = pubDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }
}
