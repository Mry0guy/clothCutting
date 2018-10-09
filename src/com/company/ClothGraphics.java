package com.company;



import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class ClothGraphics extends JPanel {
    int width;
    int height;
    ArrayList<int[]> cuts = new ArrayList<int[]>();
    SearchTree.nodeReturn root;
    public Color BACKGROUND = Color.GREEN;

    public Color CUT_COLOR = Color.BLACK;

    public ClothGraphics(SearchTree.nodeReturn n ){
        SearchTree.node[] top = n.getNodes();
        root = n;
        if(top[0].getData().getWidth() == top[1].getData().getWidth()) {
            this.width = top[0].getData().getWidth();
            this.height = top[0].getData().getHeight() + top[1].getData().getHeight();
        } else if (top[0].getData().getHeight() == top[1].getData().getHeight()){
            this.width = top[0].getData().getWidth() + top[1].getData().getWidth();
            this.height = top[0].getData().getHeight();
        } else {
            System.err.println("wrong shape output");
        }
        setPreferredSize(new Dimension(width, height));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(BACKGROUND);

    }


    public int[] cut(SearchTree.node[] nodes){
        int [] x;
        SearchTree.node[] searchNode = nodes;
        if(searchNode[0].getData().getWidth() == searchNode[1].getData().getWidth()) {
            x = new int[]{
                    searchNode[0].getData().getX(),
                    searchNode[0].getData().getHeight(),
                    searchNode[0].getData().getX() + searchNode[0].getData().getWidth(),
                    searchNode[0].getData().getHeight()
            };
            cuts.add(x);
        } else if (searchNode[0].getData().getHeight() == searchNode[1].getData().getHeight()){
            x = new int[]{
                    searchNode[0].getData().getWidth(),
                    searchNode[0].getData().getY(),
                    searchNode[0].getData().getWidth(),
                    searchNode[0].getData().getY() + searchNode[0].getData().getHeight()
            };
            cuts.add(x);
        } else {
            System.err.println("wrong shape output");
        }
    }
}
