package com.company;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

public class Patern {
    int width;
    int height;
    int value;
    char symbol;
    static ArrayList<Patern> paterns;
    static Patern smallestPatern;
    Patern(int width, int height, int value, char symbol) {
        this.width = width;
        this.height = height;
        this.value = value;
        this.symbol = symbol;
        paterns.add(this);
    }

    public int getWidth() {
        return this.width;
    }
    public int getHeight() {
        return this.height;
    }
    public int getVal() {
        return this.value;
    }
    public char getSym() {
        return this.symbol;
    }

    static Patern getSmallestPatern() {
        if(smallestPatern == null) {
            int minSide = 0;
            Patern ret = null;
            Iterator<Patern> p = paterns.listIterator();
            while (p.hasNext()) {
                Patern test = p.next();
                if (minSide > Math.min(test.getHeight(), test.getWidth())) {
                    minSide = Math.min(test.getHeight(), test.getWidth());
                    ret = test;
                }
            }
            smallestPatern = ret;
            return ret;
        } else {
            return smallestPatern;
        }
    }

}
