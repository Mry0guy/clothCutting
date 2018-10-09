package com.company;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

public class Patern {
    int width;
    int height;
    int value;
    char symbol;
    static Patern smallestPatern;
    static ArrayList<Patern> paterns = new ArrayList<Patern>();

    Patern() {
        this.width = 0;
        this.height = 0;
        this.value = -1;
    }

    Patern(int width, int height, int value, char symbol) {
        this.width = width;
        this.height = height;
        this.value = value;
        this.symbol = symbol;
        this.paterns.add(this);
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

    public static int getSmallestSide() {
        return Math.min(smallestPatern.getHeight(), smallestPatern.getWidth());
    }

    public static Patern getSmallestPatern() {
        if(smallestPatern == null) {
            int smallestSide = -1;
            Iterator<Patern> p = paterns.listIterator();
            while (p.hasNext()) {
                Patern test = p.next();
                if (smallestSide == -1 || smallestSide > Math.min(test.getHeight(), test.getWidth())) {
                    smallestSide = Math.min(test.getHeight(), test.getWidth());
                    smallestPatern = test;
                }
            }
            System.out.printf("patern %d by %d is the smallest patern\n", smallestPatern.width, smallestPatern.height);
        }
        return smallestPatern;
    }

    public static Iterator<Patern> getSizeIterator() {
        paterns.sort(new sizeCompare());
        Iterator<Patern> p = paterns.listIterator();
        return p;
    }

    public static Iterator<Patern> getValueIterator() {
        paterns.sort(new valueCompare());
        Iterator<Patern> p = paterns.listIterator();
        return p;
    }

    public boolean fits(Cloth c) {
        if((this.getWidth() <= c.getHeight() && this.getHeight() <= c.getWidth()) || (this.getWidth() <= c.getWidth() && this.getHeight() <= c.getHeight())) {
            return true;
        } else {
            return false;
        }
    }
}
 class sizeCompare implements Comparator<Patern> {
    public int compare(Patern a, Patern b) {
        return Math.min(a.getWidth(), a.getHeight()) - Math.min(b.getWidth(), b.getHeight());
    }
}

class valueCompare implements Comparator<Patern> {
    public int compare(Patern a, Patern b){
        return a.getVal() - b.getVal();
    }
}
