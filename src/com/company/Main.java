package com.company;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.TreeSet;

class clothCutting {
    public static Cloth top;
    public static ArrayList<Patern> paterns;
    public static void main(String[] args) {
        top = new Cloth(100, 100);
        paterns = new ArrayList<Patern>();
        paterns.add(new Patern(2,3,5, 'a'));
        paterns.add(new Patern(1,4,2,'b'));
        paterns.add(new Patern(6,10,14, 'c'));
        SearchTree search = new SearchTree(top, paterns);
        search.Search(paterns);
        JPanel j = new ClothGraphics(search.ToSolvedArray());
        JFrame f = new JFrame("Cloth Cutting");
        f.setSize(200, 200);
        f.add(j);
        f.setVisible(true);
    }
}