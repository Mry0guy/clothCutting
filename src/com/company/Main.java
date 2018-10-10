package com.company;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.TreeSet;

class clothCutting {
    public static Cloth top;
    public static ArrayList<Patern> paterns;
    public static void main(String[] args) {
        top = new Cloth(10, 10);
        paterns = new ArrayList<Patern>();
        paterns.add(new Patern(2,2,5, 'a'));
        paterns.add(new Patern(1,4,2,'b'));
        SearchTree search = new SearchTree(top, paterns);
        search.Search(paterns);
        JPanel j = new ClothGraphics(search.ToSolvedArray(),10,10, 20);
        JFrame f = new JFrame("Cloth Cutting");
        f.setSize(200, 200);
        f.add(j);
        f.setVisible(true);
        while(true);
    }
}