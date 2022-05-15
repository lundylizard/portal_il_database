package de.lundy.ildatabase.hook;

import com.tsunderebug.speedrun4j.data.Link;
import com.tsunderebug.speedrun4j.data.Videos;
import com.tsunderebug.speedrun4j.game.Category;
import com.tsunderebug.speedrun4j.game.run.Status;
import com.tsunderebug.speedrun4j.game.run.Timeset;
import com.tsunderebug.speedrun4j.platform.System;

import java.io.IOException;
import java.util.Map;

public class RunHook {

    private String id;
    private String weblink;
    private String game;
    private String level;
    private String category;
    private Videos videos;
    private String comment;
    private Status status;
    private PlayerHook[] players;
    private String date;
    private String submitted;
    private Timeset times;
    private System system;
    private Link splits;
    private Map<String, String> values;
    private Link[] links;

    public String getId() {
        return this.id;
    }

    public String getWeblink() {
        return this.weblink;
    }

    public Videos getVideos() {
        return this.videos;
    }

    public String getComment() {
        return this.comment;
    }

    public Status getStatus() {
        return this.status;
    }

    public PlayerHook[] getPlayers() {
        return this.players;
    }

    public String getDate() {
        return this.date;
    }

    public String getSubmitted() {
        return this.submitted;
    }

    public Timeset getTimes() {
        return this.times;
    }

    public System getSystem() {
        return this.system;
    }

    public Link getSplits() {
        return this.splits;
    }

    public Link[] getLinks() {
        return this.links;
    }

    public Category getCategory() throws IOException {
        return Category.fromID(this.category);
    }

    public String getGame() {
        return game;
    }

    public String getLevel() {
        return level;
    }
}
