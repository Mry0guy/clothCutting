package com.company;
import java.util.ArrayList;

public class SearchTree {
    node head;
    SearchTree(Cloth head) {
        this.head = new node(head);
    }





    private class node {
        private Cloth data;
        private ArrayList parents;
        private ArrayList children;
        node(node parent, Cloth data) {
            this.data = data;
            this.parents = new ArrayList<node>();
            this.parents.add(parent);
            this.children = new ArrayList<node>();
        }

        node(Cloth head) {
            this.data = head;
            this.parents = null;
            this.children = new ArrayList<node>();
        }

        public Cloth getData() {
            return data;
        }

        public void addChild(Cloth data) {
            this.children.add(new node(this, data));
        }

        public void addParent(node parent) {
            this.parents.add(parent);
        }

        public ArrayList getChildren() {
            return children;
        }
    }
}
