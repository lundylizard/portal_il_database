package de.lundy.ildatabase.dto;

public class RunDTO {

    private String category;
    private String speedrunUsername;
    private String speedrunId;
    private String chamber;
    private String weblink;
    private String video;
    private String demos;
    private int place;
    private float points;
    private int time;
    private long date;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSpeedrunUsername() {
        return speedrunUsername;
    }

    public void setSpeedrunUsername(String speedrunUsername) {
        this.speedrunUsername = speedrunUsername;
    }

    public String getSpeedrunId() {
        return speedrunId;
    }

    public void setSpeedrunId(String speedrunId) {
        this.speedrunId = speedrunId;
    }

    public String getChamber() {
        return chamber;
    }

    public void setChamber(String chamber) {
        this.chamber = chamber;
    }

    public String getWeblink() {
        return weblink;
    }

    public void setWeblink(String weblink) {
        this.weblink = weblink;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getDemos() {
        return demos;
    }

    public void setDemos(String demos) {
        this.demos = demos;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public float getPoints() {
        return points;
    }

    public void setPoints(float points) {
        this.points = points;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "RunDTO{" + "category='" + category + '\'' + ", speedrunUsername='" + speedrunUsername + '\'' + ", speedrunId='" + speedrunId + '\'' + ", chamber='" + chamber + '\'' + ", weblink='" + weblink + '\'' + ", video='" + video + '\'' + ", demos='" + demos + '\'' + ", place=" + place + ", points=" + points + ", time=" + time + ", date=" + date + '}';
    }
}
