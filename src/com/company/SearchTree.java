package com.company;
import java.lang.reflect.Array;
import java.util.ArrayList;
public class SearchTree {
    node head;
    Cloth[][] memo = new Cloth[][];
    SearchTree(Cloth head) {
        this.head = new node(head);
        memo = new ArrayList<ArrayList<node>>();
    }

    public void optomise() {

    }

    public void populate(ArrayList<Patern> paterns) {
        Cloth c = (Cloth)this.head.getData();
        int w = c.getWidth();
        int h = c.getHeight();
        for (int i = 1; i <= w; i++) {

            ArrayList<Cloth> Wcuts = c.cut(i, true);
            this.head.addChild(Wcuts.get(0));
            this.head.addChild(Wcuts.get(1));
        }
        for (int i = 1; i <= 0; i++) {
            ArrayList<Cloth> Hcuts = c.cut(i, false);
            this.head.addChild(Hcuts.get(0));
            this.head.addChild(Hcuts.get(1));
        }


    }




    private class node<E> {
        private E data;
        private node parent;
        private ArrayList<node> children;
        node(node parent, E data) {
            this.data = data;
            this.parent = parent;
            this.children = new ArrayList<node>();
        }

        node(E head) {
            this.data = head;
            this.parent = null;
            this.children = new ArrayList<node>();
        }

        public E getData() {
            return data;
        }

        public void addChild(E data) {
            this.children.add(new node(this, data));
        }
    }
}
