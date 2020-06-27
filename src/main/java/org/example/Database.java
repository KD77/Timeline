package org.example;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;

public class Database {

    public static Connection con = null;

    /* Get connection to database */
    public static void getConnection() {
        try {
            String url = "jdbc:mysql://localhost/timeline?useSSL=false&serverTimezone=UTC";
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url,
                    "VP", "VP12345678!");
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println("No connection");
        }

    }

    /* Get Timeline form Database */
    public static TimeLine getTimeLine(int timeLineID) throws SQLException {
        PreparedStatement statement = con.prepareStatement("SELECT * from timeline WHERE timelineID=" + timeLineID);
        ResultSet result = statement.executeQuery();
        if (result.next()) {
            String title = result.getString("title");
            int userID = result.getInt("userID");
            String keywords = result.getString("keywords");

            TimeLine timeline = new TimeLine(title, userID, keywords);
            timeline.setStart(result.getInt("StartPoint"));
            timeline.setEnd(result.getInt("EndPoint"));
            return timeline;
        }
        return new TimeLine();

    }

    /* created a new timeline in database */
    public static void crateTimeLine(TimeLine tl) {
        String tlTitle = tl.getTitle();
        int userID = tl.getUserID();
        String tlKeywords = tl.getKeywords();
        int start = tl.getStart();
        int end = tl.getEnd();

        try {
            PreparedStatement posted = con.prepareStatement("INSERT INTO timeline (userID, title, keywords, StartPoint, EndPoint) VALUES ('" + userID + "','" + tlTitle + "','" + tlKeywords + "'," + start + "," + end + ")");
            posted.executeUpdate(); // execute the insert
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    /* save Timeline and the events in database */
    public static void saveTimeLine(int userID, int timelineID, String newTitle, String newKeywords, ArrayList<Events> newEvents) {

        try {
            // update timeline name
            PreparedStatement statement = con.prepareStatement("update timeline set  title='" + newTitle + "', keywords='" + newKeywords + "' where timelineID=" + timelineID);
            statement.executeUpdate();

            // delete and insert new events in timelininfo
            deleteEvent(timelineID);
            saveEvents(newEvents, timelineID);


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }


    /* gets the timeline ID */
    public static int getTimeLineID(String tlName) {
        try {
            PreparedStatement statement = con.prepareStatement("SELECT * from timeline WHERE title='" + tlName + "'");
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                if (result.getString("title").equals(tlName)) {
                    return result.getInt("timelineID");
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return 0;
    }

    /* save events database */
    public static void saveEvents(ArrayList<Events> arr, int tlID) {
        try {
            for (Events event : arr) {
                PreparedStatement statement = con.prepareStatement("INSERT INTO timelineinfo (timelineID,nodeName,description,point,isImage) VALUES (" + tlID + ",'" + event.getNode() + "','" + event.getDescription() + "'," + event.getPoint() + "," + event.getTrueOrFalse() + ")");
                statement.executeUpdate();
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    /* Delete timeline form database */
    public static void deleteTimeLine(int timelineID) throws SQLException {

        PreparedStatement deleteTL = con.prepareStatement("delete from timeline.timeline where timelineID=" + timelineID + "");
        PreparedStatement deleteTLEvents = con.prepareStatement("delete from timeline.timelineinfo where timelineID=" + timelineID + "");
        deleteTLEvents.executeUpdate();
        deleteTL.executeUpdate();
    }

    /* Delete events form database */
    public static void deleteEvent(int timelineID) {
        try {

            PreparedStatement deleteTLEvent = con.prepareStatement("delete from timeline.timelineinfo where timelineID=" + timelineID);
            deleteTLEvent.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /* gives a rating to a timeline */
    public static void giveRating(int rating, int userID, int timelineID) throws SQLException {
        //Checks if user already rated this timeline if so update rating else give rating
        PreparedStatement statement = con.prepareStatement("SELECT UserID, TimelineID from tmrating WHERE ( userID=" + userID + " and TimelineID=" + timelineID + ")");
        ResultSet result = statement.executeQuery();
        if (!result.next()) {
            statement = con.prepareStatement("Insert into tmrating(UserID, TimelineID, Rating) values (" + userID + "," + timelineID + "," + rating + ")");
        } else {
            statement = con.prepareStatement("UPDATE tmrating set UserID=" + userID + ",TimelineID=" + timelineID + ",Rating=" + rating +
                    " where UserID=" + userID + " and TimelineID=" + timelineID);
        }
        statement.executeUpdate();

        //calculate the new total rating for this timeline
        PreparedStatement statement2 = con.prepareStatement("SELECT TimelineID, Rating from tmrating Where TimelineID= " + timelineID);
        ResultSet ratings = statement2.executeQuery();

        int totRating = 0;
        int size = 0;
        while (ratings.next()) {
            totRating += ratings.getInt("Rating");
            size++;

        }
        int middleValue = totRating / size;
        statement2 = con.prepareStatement("UPDATE timeline set rating=" + middleValue + " where timelineID=" + timelineID);
        statement2.executeUpdate();

    }


    /* Get events from a timeline */
    public static ArrayList<Events> getEvents(int timeLineID) throws SQLException {
        ArrayList<Events> events = new ArrayList<>();
        PreparedStatement statement = con.prepareStatement("SELECT * from timelineinfo WHERE timelineID=" + timeLineID + "");
        ResultSet rs = statement.executeQuery();
        int count = 0;
        while (rs.next()) {
            if (count < 4)

            count++;
            Events event = new Events();
            event.setNode(rs.getString("nodeName"));
            event.setDescription(rs.getString("description"));
            event.setPoint(rs.getInt("point"));
            events.add(event);

        }

        return events;
    }
    /* saves images to the database */
    public static void saveImagesDos(int timelineID, ArrayList<String> file) {
        try {
            for (String s : file) {
                File imageFile = new File(String.valueOf(s));
                FileInputStream fin = new FileInputStream(imageFile);
                PreparedStatement pre = con.prepareStatement("INSERT INTO nodepic (TimelineID,img) VALUES (?,?)");
                pre.setInt(1, timelineID);
                pre.setBinaryStream(2, fin, (int) imageFile.length());
                pre.executeUpdate();

            }

        } catch (Exception e1) {
            System.out.println(e1.getMessage());
        }
    }

    /*  Logs in user to the program  */
    public static void userLogIn(String e, String p) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ? and password = sha1(?)";
        PreparedStatement preparedStatement = con.prepareStatement(sql);
        preparedStatement.setString(1, e);
        preparedStatement.setString(2, p);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (!resultSet.next()) {
            throw new SQLException("Wrong username or password");
        } else {
            //set the logged in user to the user that logged in
            App.loggedInUser = new User();
            App.loggedInUser.setUserID(resultSet.getInt("userID"));
            App.loggedInUser.setUsername(resultSet.getString("username"));
            App.loggedInUser.setAdmin(resultSet.getInt("admin"));
            App.loggedInUser.loggedIn = true;

        }
    }
}

