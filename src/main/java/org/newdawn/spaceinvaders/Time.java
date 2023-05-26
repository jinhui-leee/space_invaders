package org.newdawn.spaceinvaders;

public class Time {

    private static Time time = new Time();

    private String timeString = "";
    private Integer bestTimeInt = 0;

    private String totalClearTime ="";
    private Integer totalClearTimeInt = 0;

    public static Time get() {
        return time;
    }

    public void resetTime() {
        totalClearTimeInt = convertTimeStringToInt(totalClearTime);
    }

    public void calculateTime(long elapsedTime) {
        int minutes=(int)(elapsedTime/1000)/60;
        int seconds=(int)(elapsedTime/1000)%60;
        int millis=(int)(elapsedTime%1000);
        timeString = String.format("%02d:%02d:%02d", minutes, seconds, millis/10);

    }

    public int convertTimeStringToInt(String timeStr) {
        String[] tokens = timeStr.split(":");
        int minutes = Integer.parseInt(tokens[0]);
        int seconds = Integer.parseInt(tokens[1]);
        int millis = Integer.parseInt(tokens[2]);
        int timeInt = minutes * 60 * 1000 + seconds * 1000 + millis;
        return timeInt;
    }

    // Int 형으로 된 시간을 String 형으로 변환
    public String convertTimeIntToString(int timeInt) {
        int minutes = timeInt / 1000 / 60;
        int seconds = (timeInt / 1000) % 60;
        int millis = timeInt % 1000;
        String timeStr = String.format("%02d:%02d:%02d", minutes, seconds, millis/10);
        return timeStr;
    }

    public String calculateTotalTime(String timeStr) {
        String[] tokens = timeStr.split(":");
        int minutes = Integer.parseInt(tokens[0]);
        int seconds = Integer.parseInt(tokens[1]);
        int millis = Integer.parseInt(tokens[2]);
        int timeInt = minutes * 60 * 1000 + seconds * 1000 + millis;
        totalClearTimeInt += timeInt;

        minutes = timeInt / 1000 / 60;
        seconds = (timeInt / 1000) % 60;
        millis = timeInt % 1000;
        timeStr = String.format("%02d:%02d:%02d", minutes, seconds, millis/10);
        totalClearTime = timeStr;
        return totalClearTime;
    }


    public String getTimeString() {
        return timeString;
    }

    public void setTimeString(String timeString) {
        this.timeString = timeString;
    }

    public Integer getBestTimeInt() {
        return bestTimeInt;
    }

    public void setBestTimeInt(Integer bestTimeInt) {
        this.bestTimeInt = bestTimeInt;
    }

    public String getTotalClearTime() {
        return totalClearTime;
    }

    public void setTotalClearTime(String totalClearTime) {
        this.totalClearTime = totalClearTime;
    }

    public Integer getTotalClearTimeInt() {
        return totalClearTimeInt;
    }

    public void setTotalClearTimeInt(Integer totalClearTimeInt) {
        this.totalClearTimeInt = totalClearTimeInt;
    }
}
