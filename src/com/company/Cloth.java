package com.company;
import java.util.ArrayList;

public class Cloth {
    int width;
    int height;
    int x;
    int y;
    int value;

    Cloth(int width, int height, int x, int y) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.value = -1;
    }

    Cloth(int width, int height) {
        this.width = width;
        this.height = height;
        this.x = 0;
        this.y = 0;
        this.value = -1;
    }

    public ArrayList<Cloth> cut(int cut, boolean isWidthCut) {
        ArrayList<Cloth> ret = new ArrayList<Cloth>;
        if(isWidthCut) {
            ret.add(new Cloth(this.getWidth() - cut, this.getHeight(), cut, this.getY()));
            ret.add(new Cloth(cut, this.getHeight(), this.getX(), this.getY()));
        } else {
            ret.add(new Cloth(this.getWidth(), this.getHeight() - cut, cut, this.getX()));
            ret.add(new Cloth(this.getWidth(), cut, this.getX(), this.getY()));
        }
        return ret;

    }


    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
