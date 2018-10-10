package com.company;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.*;


public class SearchTree {
    node head;
    node[][] memo;
    FutureTask[][] evaluationMemo;
    static ExecutorService pool;

    SearchTree(Cloth head, ArrayList<Patern> paterns) {
        this.head = new node(head);
        this.memo = new node[head.getWidth()+1][head.getHeight()+1];
        this.evaluationMemo = new FutureTask[head.getWidth()+1][head.getHeight()+1];
        pool = Executors.newCachedThreadPool();
    }

    public void Search(ArrayList<Patern> paterns) {
        this.head.populate(paterns);
        try {
            nodeReturn value = this.head.evaluate().get();
            System.out.printf("the value of the best choice was %d", value.getValue());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public ArrayList<nodeReturn> ToSolvedArray(){
        ArrayList<nodeReturn> Buffer = new ArrayList<>();
        try{
            nodeReturn solved = head.evaluate().get();
            TreeBufferPack(Buffer,solved, new int[]{0,0});
        } catch(Exception e){
            System.err.println("the evaluation thread has crashed within the solution");
        }

        return Buffer;
    }

    private void TreeBufferPack(ArrayList<nodeReturn> Buffer, nodeReturn n, int[] cords) {
        try{
            nodeReturn L = n.getNodes()[0].evaluate().get();
            nodeReturn R = n.getNodes()[1].evaluate().get();
            if(n.getOrientaion()){
                L.addCord(cords);
                R.addCord(new int[]{cords[0], cords[1] + n.getNodes()[0].getData().getHeight()});
                Buffer.add(L);
                Buffer.add(R);
                TreeBufferPack(Buffer, L, cords);
                TreeBufferPack(Buffer, R, new int[]{cords[0], cords[1] + n.getNodes()[0].getData().getHeight()});
            } else {
                L.addCord(cords);
                R.addCord(new int[]{cords[0] + n.getNodes()[0].getData().getWidth(), cords[1]});
                Buffer.add(L);
                Buffer.add(R);
                TreeBufferPack(Buffer, L, cords);
                TreeBufferPack(Buffer, R, new int[]{cords[0] + n.getNodes()[0].getData().getWidth(), cords[1]});
            }
            return;
        } catch (NullPointerException e){
            return;
        }catch (InterruptedException e){
            return;
        }catch (ExecutionException e) {
            return;
        }
    }

    public class nodeReturn {
        private node[] nodes;
        private int value;
        private Patern p;
        private ArrayList<int[]> cords;
        private boolean vertical;

        nodeReturn(node[] nodes, int value) {
            this.nodes = nodes;
            this.value = value;
            this.cords = new ArrayList<>();

        }
        nodeReturn(node[] nodes, int value, Patern p) {
            this.nodes = nodes;
            this.value = value;
            this.p = p;
            this.cords = new ArrayList<>();
        }

        public void  setOrientation(boolean b){
            vertical = b;
        }

        public boolean getOrientaion(){
            return vertical;
        }

        public Patern getPaterns() {
            return p;
        }

        public int getValue() {
            return value;
        }

        public node[] getNodes() {
            return nodes;
        }

        public ArrayList<int[]> getCords(){
            return cords;
        }

        public void addCord(int[] cord) {
            this.cords.add(cord);
        }
    }

    public static ExecutorService getThreadPool() {
        return pool;
    }


    private class valueCallable implements Callable<nodeReturn> {
        private ArrayList<node[]> children;
        private Cloth cloth;

        valueCallable(ArrayList<node[]> children, Cloth c) {
            this.children = children;
            this.cloth = c;
        }

        public nodeReturn call() {
            nodeReturn maxPair = null;
            //Base Case: The node is only large enough for one pattern
            if (children.size() == 0) {
                Iterator<Patern> p = Patern.getValueIterator();
                Patern Best = null;
                while (p.hasNext() && Best == null) {
                    Patern test = p.next();
                    if (test.fits(this.cloth)) {
                        Best = test;
                    }
                }
                if(Best != null) {
                    maxPair = new nodeReturn(null, Best.getValue(), Best);
                    if(Best.getWidth() <= this.cloth.getWidth()){
                        maxPair.setOrientation(true);
                    } else {
                        maxPair.setOrientation(false);
                    }
                } else {
                    maxPair = new nodeReturn(null, 0);
                }

            } else { // Recursive Case: the node has possible children
                int maxPairValue = -1;
                for (int i = 0; i < children.size(); i++) {
                    node[] nextNodes = children.get(i);
                    boolean vertical = true;
                    if(nextNodes[0].getData().getX() == nextNodes[1].getData().getX()){
                        vertical = false;
                    }
                    try {
                        nodeReturn value0, value1;
                        value0 = nextNodes[0].evaluate().get();
                        value1 = nextNodes[1].evaluate().get();
                        if(value0 == null){
                            System.out.printf("W:%d H:%d\n",this.cloth.getWidth(), this.cloth.getHeight());
                        }
                        int value = value0.getValue() + value1.getValue();
                        if (maxPairValue < value) {
                            maxPair = new nodeReturn(nextNodes, value);
                            maxPair.setOrientation(vertical);
                            maxPairValue = value;
                        }
                    } catch (Exception e) {
                        System.err.println(e);
                        maxPair = new nodeReturn(null, 0);
                    }

                }
            }
            System.out.printf("resolved: w: %d h: %d v:%d\n", this.cloth.getWidth(), this.cloth.getHeight(), maxPair.getValue());
            return maxPair;
        }
    }

    public class node {
        private Cloth data;
        private ArrayList<int[]> locations;
        private ArrayList<node[]> children;

        node(node parent, Cloth data) {
            this.data = data;
            this.locations = new ArrayList<>();
            this.children = new ArrayList<node[]>();
        }

        node(Cloth head) {
            this.data = head;
            this.children = new ArrayList<node[]>();
        }

        public Cloth getData() {
            return data;
        }

        public void addChild(node[] data) {
            this.children.add(data);
        }

        public FutureTask<nodeReturn> evaluate() {
            if(evaluationMemo[this.data.getWidth()][this.data.getHeight()] == null) {
                valueCallable r = new valueCallable(this.children, this.data);
                FutureTask<nodeReturn> f = new FutureTask<>(r);
                ExecutorService pool = SearchTree.getThreadPool();
                pool.execute(f);
                evaluationMemo[this.getData().getWidth()][this.getData().getHeight()] = f;
                return f;
            } else {
                return evaluationMemo[this.data.getWidth()][this.data.getHeight()];
            }
        }


        public void populate(ArrayList<Patern> paterns) {
            ArrayList<Cloth[]> possible = this.data.PosibleCuts(paterns);
            if (possible.size() != 0) {
                Iterator<Cloth[]> i = possible.listIterator();
                while (i.hasNext()) {
                    Cloth[] c = i.next();
                    node[] n = new node[2];
                    boolean memo0 = false;
                    boolean memo1 = false;
                    if (memo[c[0].getWidth()][c[0].getHeight()] != null) {
                        n[0] = memo[c[0].getWidth()][c[0].getHeight()];
                        memo0 = true;
                    } else {
                        node ref = new node(this, c[0]);
                        memo[c[0].getWidth()][c[0].getHeight()] = ref;
                        n[0] = ref;
                    }
                    if (memo[c[1].getWidth()][c[1].getHeight()] != null) {
                        n[1] = memo[c[1].getWidth()][c[1].getHeight()];
                        memo1 = true;
                    } else {
                        node ref = new node(this, c[1]);
                        memo[c[1].getWidth()][c[1].getHeight()] = ref;
                        n[1] = ref;
                    }
                    this.addChild(n);
                    if(!memo0) {
                        n[0].populate(paterns);
                    }
                    if(!memo1) {
                        n[1].populate(paterns);
                    }
                }
            }
        }
    }
}