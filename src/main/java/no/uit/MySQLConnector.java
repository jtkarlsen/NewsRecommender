package no.uit;

import no.uit.Beans.ArticleList;
import no.uit.Beans.Group;
import no.uit.utils.Utils;
import org.elasticsearch.search.facet.InternalFacet;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * Created by JanTore on 26.02.2015.
 */
public class MySQLConnector {
    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    private String sqlUsername = null;
    private String sqlPassword = null;

    public MySQLConnector() throws Exception {
        Properties properties = new Properties();
        InputStream stream = this.getClass().getResourceAsStream("/credentials");
        properties.load(stream);
        sqlUsername = properties.getProperty("username");
        sqlPassword = properties.getProperty("password");
    }

    public int getSessionCountForGroup(int groupId) throws Exception {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager
                    .getConnection("jdbc:mysql://localhost/interest?"
                            + "user=" + sqlUsername + "&password=" + sqlPassword);
            preparedStatement = connect
                    .prepareStatement("SELECT COUNT(*) FROM (select * from session where session_group_id = ?) AS subquery;");
            preparedStatement.setInt(1, groupId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                return resultSet.getInt(1);
            }
            return 0;

        } catch (Exception e) {
            throw e;
        }
    }

    public List<Group> getGroupsForUser(String username) throws Exception {
        List<Group> groups = new ArrayList<Group>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager
                    .getConnection("jdbc:mysql://localhost/interest?"
                            + "user=" + sqlUsername + "&password=" + sqlPassword);
            preparedStatement = connect
                    .prepareStatement("select * from session_group where userid= ?;");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Group group = new Group();
                group.setGroupId(resultSet.getInt("id"));
                groups.add(group);
            }

        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }

        for (Group group : groups) {
            group.setSessionCount(getSessionCountForGroup(group.getGroupId()));
        }
        return groups;
    }

    public List<String> getInterestKeywordsForInterests(List<Integer> interestIds) throws Exception {
        List<String> keywords = new ArrayList<String>();
        try {
            if (interestIds.size() > 0) {
                String sql = "select * from interest_keyword where interestid in (";
                for (int interestId : interestIds) {
                    sql += "?,";
                }
                sql = Utils.removeCharsFromEndOfString(1, sql);
                sql += ");";

                Class.forName("com.mysql.jdbc.Driver");
                connect = DriverManager
                        .getConnection("jdbc:mysql://localhost/interest?"
                                + "user=" + sqlUsername + "&password=" + sqlPassword);
                preparedStatement = connect
                        .prepareStatement(sql);

                for (int i = 0; i < interestIds.size(); i++) {
                    preparedStatement.setInt(i+1, interestIds.get(i));
                }
                resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    keywords.add(resultSet.getString("keyword"));
                }
            }
            return keywords;

        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }
    }

    public List<Integer> getInterestIdsForSessionIds(List<String> sessionIds) throws Exception {
        List<Integer> interestIds = new ArrayList<Integer>();
        try {
            if (sessionIds.size() > 0) {
                String sql = "select * from interest where sessionid in (";
                for (String sessionId : sessionIds) {
                    sql += "?,";
                }
                sql = Utils.removeCharsFromEndOfString(1, sql);
                sql += ");";

                Class.forName("com.mysql.jdbc.Driver");
                connect = DriverManager
                        .getConnection("jdbc:mysql://localhost/interest?"
                                + "user=" + sqlUsername + "&password=" + sqlPassword);
                preparedStatement = connect.prepareStatement(sql);

                for (int i = 0; i < sessionIds.size(); i++) {
                    preparedStatement.setString(i+1, sessionIds.get(i));
                }

                resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    interestIds.add(resultSet.getInt("id"));
                }
            }


        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }
        return interestIds;
    }

    public List<String> getKeywordsForGroup(int groupId) throws Exception {
        List<String> sessionIds = new ArrayList<String>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager
                    .getConnection("jdbc:mysql://localhost/interest?"
                            + "user=" + sqlUsername + "&password=" + sqlPassword);

            String sql = "select * from session where session_group_id= (?);";
            preparedStatement = connect
                    .prepareStatement(sql);
            preparedStatement.setInt(1, groupId);
            resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                sessionIds.add(resultSet.getString("id"));
            }

        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }

        List<Integer> interestIds = getInterestIdsForSessionIds(sessionIds);
        List<String> keywords = getInterestKeywordsForInterests(interestIds);
        return keywords;
    }

    private void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (statement != null) {
                statement.close();
            }

            if (connect != null) {
                connect.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
