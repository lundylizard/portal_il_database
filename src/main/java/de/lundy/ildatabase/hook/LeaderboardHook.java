package de.lundy.ildatabase.hook;

import com.google.gson.annotations.SerializedName;
import com.tsunderebug.speedrun4j.data.Link;

import java.util.Map;

public class LeaderboardHook {

    private String weblink;
    private String game;
    private String category;
    private String level;
    private String platform;
    private String region;
    private boolean emulators;
    @SerializedName("video-only")
    private boolean videoOnly;
    private String timing;
    private Map<String, String> values;
    private PlacedRunHook[] runs;
    private Link[] links;

    public String getLevel() {
        return this.level;
    }

    public boolean isEmulators() {
        return this.emulators;
    }

    public boolean isVideoOnly() {
        return this.videoOnly;
    }

    public String getTiming() {
        return this.timing;
    }

    public PlacedRunHook[] getRuns() {
        return this.runs;
    }

    public Link[] getLinks() {
        return this.links;
    }

    public String getWeblink() {
        return weblink;
    }

    public String getGame() {
        return game;
    }

    public String getCategory() {
        return category;
    }

    public String getPlatform() {
        return platform;
    }

    public String getRegion() {
        return region;
    }

    public Map<String, String> getValues() {
        return values;
    }
}
