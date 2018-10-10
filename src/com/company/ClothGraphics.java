package com.company;



import javax.swing.*;
import java.awt.*;

import java.util.ArrayList;
import java.util.Iterator;

public class ClothGraphics extends JPanel {
    int width;
    int height;
    ArrayList<SearchTree.nodeReturn> data;
    public Color BACKGROUND = Color.GREEN;
    public Color CUT_COLOR = Color.BLACK;
    int scalar;

    public ClothGraphics(ArrayList<SearchTree.nodeReturn> n, int width, int height, int scalar){
        this.scalar = scalar;
        data = n;
        this.width = width;
        this.height = height;
        setPreferredSize(new Dimension(width * scalar, height * scalar));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(BACKGROUND);
        g.fillRect(0,0, width * scalar, height * scalar);
        Iterator<SearchTree.nodeReturn> paintIterator = data.listIterator();
        while(paintIterator.hasNext()){
            SearchTree.nodeReturn paint = paintIterator.next();
            if(paint.getNodes() == null){
                paternPaint(paint, g);
            }
        }
        paintIterator = data.listIterator();
        while (paintIterator.hasNext()){
            SearchTree.nodeReturn paint = paintIterator.next();
            if(paint.getNodes() != null){
                cutPaint(paint, g);
            }
        }
    }

    public void paternPaint(SearchTree.nodeReturn nodes, Graphics g){
        ArrayList<int[]> drawLocation = nodes.getCords();
        Patern p = nodes.getPaterns();
        g.setColor(p.getColor());
        Iterator<int[]> drawIter = drawLocation.listIterator();
        while (drawIter.hasNext()) {
            int[] d = drawIter.next();
            if(nodes.getOrientaion()) {
                g.fillRect(d[0] * scalar, d[1] * scalar, p.getWidth() * scalar, p.getHeight() * scalar);
                System.out.printf("PATTERN: x: %d y: %d w: %d h: %d\n", d[0], d[1], p.getWidth(), p.getHeight());
            } else {
                g.fillRect(d[0] * scalar, d[1] * scalar, p.getHeight() * scalar, p.getWidth() * scalar);
                System.out.printf("PATTERN: x: %d y: %d w: %d h: %d\n", d[0], d[1], p.getWidth(), p.getHeight());
            }
        }
    }

    public void cutPaint(SearchTree.nodeReturn nodes, Graphics g){
        SearchTree.node[] searchNode = nodes.getNodes();
        ArrayList<int[]> drawLocation = nodes.getCords();
        Iterator<int[]> drawIter = drawLocation.listIterator();
        while (drawIter.hasNext()){
            int[] d = drawIter.next();
            if(!nodes.getOrientaion()) {
                g.setColor(CUT_COLOR);
                g.drawLine(
                        d[0]* scalar,
                        (d[1] + searchNode[0].getData().getHeight())* scalar,
                        (d[0]+ searchNode[0].getData().getWidth()) * scalar,
                        (d[1] + searchNode[0].getData().getHeight()) * scalar
                );
                System.out.printf("CUT: x1: %d y1: %d x2: %d y2: %d\n", d[0], (d[1] + searchNode[0].getData().getHeight()), (d[0]+ searchNode[0].getData().getWidth()),(d[1] + searchNode[0].getData().getHeight()));
            } else if (searchNode[0].getData().getHeight() == searchNode[1].getData().getHeight()){
                g.setColor(CUT_COLOR);
                g.drawLine(
                        (d[0] + searchNode[0].getData().getWidth()) * scalar,
                        d[1] * scalar,
                        (d[0] +searchNode[0].getData().getWidth()) * scalar,
                        (d[1] + searchNode[0].getData().getHeight()) * scalar
                );
                System.out.printf("CUT: x1: %d y1: %d x2: %d y2: %d\n", (d[0] + searchNode[0].getData().getWidth()), d[1], (d[0] +searchNode[0].getData().getWidth()),(d[1] + searchNode[0].getData().getHeight()));
            } else {
                System.err.println("wrong shape output");
            }
        }
    }
}
