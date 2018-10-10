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

    public ArrayList<Cloth[]> PosibleCuts(ArrayList<Patern> paterns){
        ArrayList<Cloth[]> possibleCuts = new ArrayList<Cloth[]>();
        int Wmax = this.getWidth();
        int Hmax = this.getHeight();
        Patern p = Patern.getSmallestPatern();
        for (int x = 1; x < Wmax; x++){
            Cloth a = new Cloth(Wmax - x, Hmax, x, 0);
            Cloth b = new Cloth(x, Hmax, 0, 0);
            if(p.fits(a) || p.fits(b)) {
                Cloth[] cuts = new Cloth[2];
                cuts[1] = a;
                cuts[0] = b;
                possibleCuts.add(cuts);
            }
        }
        for(int y = 1; y < Hmax; y++) {
            Cloth a = new Cloth(Wmax, Hmax - y, 0, y);
            Cloth b = new Cloth(Wmax, y, 0, 0);
            if(p.fits(a) || p.fits(b)) {
                Cloth[] cuts = new Cloth[2];
                cuts[1] = a;
                cuts[0] = b;
                possibleCuts.add(cuts);
            }
        }
        return possibleCuts;
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

    public void setValue(int val) {
        this.value = val;
    }

    public int getValue() {
        return value;
    }
}
