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

    public ClothGraphics(ArrayList<SearchTree.nodeReturn> n){
        data = n;
        SearchTree.node[] top = n.get(0).getNodes();
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
        Iterator<SearchTree.nodeReturn> paintIterator = data.listIterator();
        while(paintIterator.hasNext()){
            SearchTree.nodeReturn paint = paintIterator.next();
            if(paint.getNodes() == null){
                paternPaint(paint, g);
            } else {
                cutPaint(paint, g);
            }
        }
    }

    public void paternPaint(SearchTree.nodeReturn nodes, Graphics g){
        Patern p[] = nodes.getPaterns();
        SearchTree.node drawLocation[] = nodes.getNodes();
        if(p[0] != null){
            g.setColor(p[0].getColor());
            g.drawRect(
                    drawLocation[0].getData().getX(),
                    drawLocation[0].getData().getY(),
                    drawLocation[0].getData().getWidth(),
                    drawLocation[0].getData().getHeight()
            );
        }
        if(p[1] != null){
            g.setColor(p[1].getColor());
            g.drawRect(
                    drawLocation[1].getData().getX(),
                    drawLocation[1].getData().getY(),
                    drawLocation[1].getData().getWidth(),
                    drawLocation[1].getData().getHeight()
            );
        }
    }

    public void cutPaint(SearchTree.nodeReturn nodes, Graphics g){
        SearchTree.node[] searchNode = nodes.getNodes();
        if(searchNode[0].getData().getWidth() == searchNode[1].getData().getWidth()) {
            g.setColor(CUT_COLOR);
            g.drawLine(
                    searchNode[0].getData().getX(),
                    searchNode[0].getData().getHeight(),
                    searchNode[0].getData().getX() + searchNode[0].getData().getWidth(),
                    searchNode[0].getData().getHeight()
            );
        } else if (searchNode[0].getData().getHeight() == searchNode[1].getData().getHeight()){
            g.setColor(CUT_COLOR);
            g.drawLine(
                    searchNode[0].getData().getWidth(),
                    searchNode[0].getData().getY(),
                    searchNode[0].getData().getWidth(),
                    searchNode[0].getData().getY() + searchNode[0].getData().getHeight()
            );
        } else {
            System.err.println("wrong shape output");
        }
    }
}
