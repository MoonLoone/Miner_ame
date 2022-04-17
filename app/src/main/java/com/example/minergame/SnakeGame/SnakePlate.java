package com.example.minergame.SnakeGame;

public class SnakePlate {

    private int existTime;
    private int xCoord;
    private int yCoord;
    private boolean isCherry;

    public SnakePlate(int xCoord, int yCoord,int existTime) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.existTime = existTime;
    }

    public int getExistTime() {
        return existTime;
    }

    public int getxCoord() {
        return xCoord;
    }

    public int getyCoord() {
        return yCoord;
    }

    public void setExistTime(int existTime) {
        this.existTime = existTime;
    }

    public void setxCoord(int xCoord) {
        this.xCoord = xCoord;
    }

    public void setyCoord(int yCoord) {
        this.yCoord = yCoord;
    }

    public void setIsCherry(boolean isCherry) {
        this.isCherry = isCherry;
    }

    public boolean getIsCherry() {
        return isCherry;
    }
}
