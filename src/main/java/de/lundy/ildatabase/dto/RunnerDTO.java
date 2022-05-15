package de.lundy.ildatabase.dto;

public class RunnerDTO {

    private long discordId;
    private String speedrunUsername;
    private String speedrunId;
    private float inboundsPoints;
    private float oobPoints;
    private float glitchlessPoints;
    private float points;

    public long getDiscordId() {
        return discordId;
    }

    public void setDiscordId(long discordId) {
        this.discordId = discordId;
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

    public float getInboundsPoints() {
        return inboundsPoints;
    }

    public void setInboundsPoints(float inboundsPoints) {
        this.inboundsPoints = inboundsPoints;
    }

    public float getOobPoints() {
        return oobPoints;
    }

    public void setOobPoints(float oobPoints) {
        this.oobPoints = oobPoints;
    }

    public float getGlitchlessPoints() {
        return glitchlessPoints;
    }

    public void setGlitchlessPoints(float glitchlessPoints) {
        this.glitchlessPoints = glitchlessPoints;
    }

    public float getPoints() {
        return points;
    }

    public void setPoints(float points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "RunnerDTO{" + "discordId=" + discordId + ", speedrunUsername='" + speedrunUsername + '\'' + ", speedrunId='" + speedrunId + '\'' + ", inboundsPoints=" + inboundsPoints + ", oobPoints=" + oobPoints + ", glitchlessPoints=" + glitchlessPoints + ", points=" + points + '}';
    }
}
