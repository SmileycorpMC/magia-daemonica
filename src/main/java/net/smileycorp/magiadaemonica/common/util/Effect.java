package net.smileycorp.magiadaemonica.common.util;

public class Effect {

    private int level;
    private int duration;

    public Effect(int level, int duration) {
        this.level = level;
        this.duration = duration;
    }

    public int getLevel() {
        return level;
    }

    public int getDuration() {
        return duration;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

}
