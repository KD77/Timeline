package org.example;

public class TimeLine {

    private int timelineID;
    private String title;
    private int userID;
    private String keywords;
    private int rating;
    private int start;
    private int end;


    public TimeLine(){}

    public TimeLine(String title, int user , String keywords){

        setTitle(title);
        setUserID(user);
        setKeywords(keywords);

    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getTimelineID() {
        return timelineID;
    }

    public void setTimelineID(int timelineID) {
        this.timelineID = timelineID;
    }

    public String getTitle() {
        return title;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }


    public int getRating() {
        return rating;
    }



    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "timelineID='" + timelineID + "'" +
        ", title='" + title + "'" +
        ", userID='" + userID + "'" +
                ", keywords='" + keywords + "'" +
        ", rating=" + rating;
    }

}
