package com.company;
import java.util.ArrayList;

public class Cloth {
    static Cloth[][] memo;
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

    public ArrayList<Cloth> CreateCuts(ArrayList<Patern> paterns) {
        ArrayList<Cloth> ret = new ArrayList<Cloth>();
        for(int w = 1; w <= this.width; w++) {
            for(int h = 1; h<= this.height; h++) {
                if(memo[w][h] != null) {

                } else {
                    ret.add(memo[w][h]);
                }
            }
        }
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
    public int getValue() {
        return this.value;
    }
    public void setValue(int x) {
        value = x;
    }
}
