package com.company;

public class Patern {
    int width;
    int height;
    int value;
    char symbol;
    Patern(int width, int h, int value, char symbol) {
        this.width = width;
        this.height = height;
        this.value = value;
        this.symbol = symbol;
    }

    public int getW() {
        return this.width;
    }
    public int getH() {
        return this.height;
    }
    public int getVal() {
        return this.value;
    }
    public char getSym() {
        return this.symbol;
    }

}
