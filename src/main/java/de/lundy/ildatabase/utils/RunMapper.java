package de.lundy.ildatabase.utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import de.lundy.ildatabase.dto.RunDTO;
import de.lundy.ildatabase.hook.LeaderboardHook;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RunMapper {

    private RunMapper() {}

    protected static final String[] CATEGORIES = {"xwdmg4dq", "02qoxl7k", "xw20jzkn"};
    protected static final String[] CHAMBERS = {"00-01", "02-03", "04-05", "06-07", "08", "09", "10", "11-12", "13",
                                             "14", "15", "16", "17", "18", "19", "e00", "e01", "e02", "Adv_13",
                                             "Adv_14", "Adv_15", "Adv_16", "Adv_17", "Adv_18"};

    private static class LeaderboardData {
        LeaderboardHook data;
    }

    private static LeaderboardHook forCategoryAndLevel(String category, String level) throws IOException {

        var g = new Gson();
        var u = new URL("https://www.speedrun.com/api/v1/leaderboards/4pd0n31e/level/" + level + "/" + category);
        var conn = (HttpURLConnection)u.openConnection();
        conn.setRequestProperty("User-Agent", "lundylizard-IlDatabase-Speedrun4J/2.1");
        var r = new InputStreamReader(conn.getInputStream());
        var l = g.fromJson(r, LeaderboardData.class);
        r.close();
        return l.data;

    }

    public static List<RunDTO> mapRuns() throws IOException, JsonSyntaxException {

        var runDTOList = new ArrayList<RunDTO>();

        for (var cat : CATEGORIES) {
            for (var chamber : CHAMBERS) {

                Arrays.stream(RunMapper.forCategoryAndLevel(cat, chamber).getRuns()).toList().forEach(r -> {

                    var run = new RunDTO();

                    run.setChamber(chamber);

                    try {
                        run.setSpeedrunUsername(r.getRun().getPlayers()[0].getName());
                        run.setSpeedrunId(r.getRun().getPlayers()[0].getId());
                    } catch (IllegalStateException | JsonSyntaxException | IOException e) {
                        e.printStackTrace();
                    }

                    run.setTime((int) (r.getRun().getTimes().getPrimaryT() * 1000));
                    run.setPlace(r.getPlace());
                    run.setPoints(calculatePoints(r.getPlace()));

                    try {
                        run.setDate(r.getRun().getStatus().getVerifyDate().getTime());
                    } catch (ParseException | NullPointerException e) {
                        run.setDate(-1);
                    }

                    try {
                        run.setCategory(r.getRun().getCategory().getName().replace(" ", "_"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    var demos = "null";
                    if (r.getRun().getComment() != null) {

                        demos = r.getRun().getComment().replace("\n", " ").replace("\r", " ").trim();
                        var demosSplit = demos.split(" ");

                        for (String s : demosSplit) {
                            if (s.toLowerCase().startsWith("https://") || s.toLowerCase().startsWith("http://")) {
                                demos = s;
                            } else {
                                demos = "null";
                            }
                        }
                    }

                    run.setDemos(demos);
                    run.setVideo(r.getRun().getVideos() != null ? r.getRun().getVideos().getLinks()[0].getUri() : "null");
                    run.setWeblink(r.getRun().getWeblink());

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    runDTOList.add(run);
                    System.out.println(run);

                });
            }
        }

        return runDTOList;

    }

    private static float calculatePoints(int place) {
        if (place > 50) return 0.0f;
        var pointsExp = ((Math.pow(50.0f - (place - 1.0f), 2.0f)) / 50.0f);
        return (float) (((pointsExp < 1 ? 1 : pointsExp) + (51 - place)) / 2.0f);
    }

}
