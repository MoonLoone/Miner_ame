package com.example.minergame.miner.minerUsage;

public class Plate {

    private int xCoord;
    private int yCoord;
    private boolean isMine;
    private boolean isChecked;
    private boolean isFlag;
    private int mineCount;

    public Plate(int xCoord, int yCoord) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        isChecked = false;
        isFlag = false;
        mineCount = 0;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public void setFlag(boolean flag) {
        isFlag = flag;
    }

    public int getxCoord() {
        return xCoord;
    }

    public int getyCoord() {
        return yCoord;
    }

    public boolean isMine() {
        return isMine;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public boolean isFlag() {
        return isFlag;
    }

    public int getMineCount() {
        return mineCount;
    }

    public void setMineCount(int mineCount) {
        this.mineCount = mineCount;
    }
}
