package de.lundy.ildatabase.utils;

import de.lundy.ildatabase.dto.RunDTO;
import de.lundy.ildatabase.dto.RunnerDTO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class MySQLUtils {

    private Connection connection;
    private Properties properties;

    private static final String RUNNERS_TABLE = "create table if not exists runners " +
            "(discord_id bigint(20), " +
            "speedrun_username text, " +
            "speedrun_id text," +
            "rank_overall int, " +
            "rank_inbounds int, " +
            "rank_oob int, " +
            "rank_glitchless int, " +
            "points_overall float, " +
            "points_inbounds float, " +
            "points_oob float, " +
            "points_glitchless float)";

    private static final String RUN_TABLE = "create table if not exists runs " +
            "(category text, " +
            "speedrun_username text, " +
            "speedrun_id text, " +
            "level text, " +
            "weblink text, " +
            "video text, " +
            "demos text, " +
            "place int, " +
            "points float, " +
            "time int, " +
            "date bigint(20))";

    private static final String DISCORD_TABLE = "create table if not exists r_discord " +
            "(discord_id bigint(20), " +
            "speedrun_username text)";

    private static final String[] TABLES = {RUNNERS_TABLE, RUN_TABLE, DISCORD_TABLE};

    private Properties getProperties() {

        if (properties == null) {
            properties = new Properties();
            properties.setProperty("user", "root");
            properties.setProperty("password", "");
            properties.setProperty("MaxPooledStatements", "250");
        }

        return properties;

    }

    public Connection getConnection() {

        if (connection != null) {
            return connection;
        }

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/portal_ils", getProperties());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;

    }

    public void createTables() throws SQLException {

        var statement = getConnection().createStatement();

        for (var table : TABLES) {
            statement.execute(table);
        }

    }

    public void putRunIntoDatabase(RunDTO run) throws SQLException {

        var statement = getConnection().createStatement();
        statement.execute(String.format("insert into runs values (\"%s\", \"%s\", \"%s\", \"%s\", \"%s\", \"%s\", \"%s\", %d, %f, %d, %d)",
                run.getCategory(),
                run.getSpeedrunUsername(),
                run.getSpeedrunId(),
                run.getChamber(),
                run.getWeblink(),
                run.getVideo(),
                run.getDemos(),
                run.getPlace(),
                run.getPoints(),
                run.getTime(),
                run.getDate()));

    }

    public RunnerDTO getRunnerNoRanks(String username) throws SQLException {

        var statement = getConnection().createStatement();
        var results = statement.executeQuery("select * from runs where speedrun_username=\"" + username + "\"");

        var runner = new RunnerDTO();
        while (results.next()) {
            runner.setSpeedrunUsername(username);
            runner.setSpeedrunId(results.getString("speedrun_id"));
            runner.setInboundsPoints(getCategoryPoints(runner, "Inbounds"));
            runner.setOobPoints(getCategoryPoints(runner, "Out_of_Bounds"));
            runner.setGlitchlessPoints(getCategoryPoints(runner, "Glitchless"));
            runner.setPoints(getOverallPoints(runner));
        }

        return runner;

    }

    public void putRunnerIntoDatabase(RunnerDTO runner) throws SQLException {

        var statement = getConnection().createStatement();
        statement.execute(String.format("insert into runners values (%d, \"%s\", \"%s\", null, null, null, null, %f, %f, %f, %f)",
                runner.getDiscordId(),

                runner.getSpeedrunUsername(),
                runner.getSpeedrunId(),

                runner.getPoints(),
                runner.getInboundsPoints(),
                runner.getOobPoints(),
                runner.getGlitchlessPoints()));

    }

    public void addRanks() throws SQLException {

        var statement = getConnection().createStatement();
        var results = statement.executeQuery("select speedrun_username, " +
                "rank() over (order by points_overall desc) as 'orank', " +
                "rank() over (order by points_inbounds desc) as 'irank', " +
                "rank() over (order by points_glitchless desc) as 'grank', " +
                "rank() over (order by points_oob desc) as 'crank' " +
                "from runners");

        while (results.next()) {

            var overall = results.getInt("orank");
            var inbounds = results.getInt("irank");
            var glitchless = results.getInt("grank");
            var oob = results.getInt("crank");
            var r = results.getString("speedrun_username");
            var statement2 = getConnection().createStatement();
            statement2.execute("update runners set rank_overall=" + overall + " where speedrun_username=\"" + r + "\"");
            statement2.execute("update runners set rank_inbounds=" + inbounds + " where speedrun_username=\"" + r + "\"");
            statement2.execute("update runners set rank_oob=" + oob + " where speedrun_username=\"" + r + "\"");
            statement2.execute("update runners set rank_glitchless=" + glitchless + " where speedrun_username=\"" + r + "\"");

        }

    }

    public void backupDiscordId() throws SQLException {

        var statement = getConnection().createStatement();
        var results = statement.executeQuery("select discord_id, speedrun_username from runners");
        var ids = new HashMap<String, Long>();

        while (results.next()) {
            var discordId = results.getLong("discord_id");
            var username = results.getString("speedrun_username");
            if (discordId != 0) {
                ids.put(username, discordId);
            }
        }

        statement.execute("truncate r_discord");

        ids.forEach((d, u) -> {
            System.out.println(d + " -- " + u);
            try {
                statement.execute("insert into r_discord values (" + u + ", \"" + d + "\")");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

    }

    public void putDiscordIdFromBackup() throws SQLException {

        var statement = getConnection().createStatement();
        var results = statement.executeQuery("select * from r_discord");
        var ids = new HashMap<String, Long>();

        while (results.next()) {
            var discordId = results.getLong("discord_id");
            var username = results.getString("speedrun_username");
            if (discordId != 0) {
                ids.put(username, discordId);
            }
        }

        ids.forEach((d, u) -> {
            try {
                statement.execute("update runners set discord_id=" + u + " where speedrun_username=\"" + d + "\"");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

    }

    public float getCategoryPoints(RunnerDTO runner, String category) throws SQLException {

        var statement = getConnection().createStatement();
        var results = statement.executeQuery("select points from runs where speedrun_username=\"" + runner.getSpeedrunUsername() + "\" and category=\"" + category + "\"");
        var points = 0.0f;

        while (results.next()) {
            points += results.getFloat("points");
        }

        return points;

    }

    public HashSet<String> getAllRunners() throws SQLException {

        var statement = getConnection().createStatement();
        var results = statement.executeQuery("select speedrun_username from runs");
        var runners = new HashSet<String>();

        while (results.next()) {
            runners.add(results.getString("speedrun_username"));
        }

        return runners;

    }

    public float getOverallPoints(RunnerDTO runner) throws SQLException {

        var statement = getConnection().createStatement();
        ResultSet results = statement.executeQuery("select points from runs where speedrun_username=\"" + runner.getSpeedrunUsername() + "\"");
        var points = 0.0f;

        while (results.next()) {
            points += results.getFloat("points");
        }

        return points;

    }

    public void clearRuns() throws SQLException {
        getConnection().createStatement().execute("truncate runs");
    }

    public void clearRunners() throws SQLException {
        getConnection().createStatement().execute("truncate runners");
    }

}
