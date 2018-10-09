package com.company;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
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
        this.pool = Executors.newCachedThreadPool();
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

    public ArrayList<nodeReturn> ToSolvedArray()throws InterruptedException, ExecutionException{
        ArrayList<nodeReturn> Buffer = new ArrayList<nodeReturn>();
        TreeBufferPack(Buffer, head.evaluate().get());
        return Buffer;
    }

    private void TreeBufferPack(ArrayList<nodeReturn> Buffer, nodeReturn n) throws InterruptedException, ExecutionException{
        nodeReturn L = n.getNodes()[0].evaluate().get();
        nodeReturn R = n.getNodes()[1].evaluate().get();
        if(L != null && R != null) {
            Buffer.add(L);
            Buffer.add(R);
            TreeBufferPack(Buffer, L);
            TreeBufferPack(Buffer, R);
        }
    }

    public class nodeReturn {
        private node[] nodes;
        private int value;
        private Patern[] p;

        nodeReturn(node[] nodes, int value) {
            nodes = nodes;
            value = value;

        }
        nodeReturn(node[] nodes, int value, Patern p) {
            nodes = nodes;
            value = value;
            p = p;

        }

        public Patern[] getPaterns() {
            return p;
        }

        public int getValue() {
            return value;
        }

        public node[] getNodes() {
            return nodes;
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
                    maxPair = new nodeReturn(null, Best.getVal());
                } else {
                    maxPair = new nodeReturn(null, 0);
                }

            } else {
                int maxPairValue = 0;
                for (int i = 0; i < children.size(); i++) {
                    node[] nextNodes = children.get(i);
                    try {
                        nodeReturn value0, value1;
                        if (evaluationMemo[nextNodes[0].getData().getWidth()][nextNodes[0].getData().getHeight()] == null) {
                            FutureTask<nodeReturn> f0 = nextNodes[0].evaluate();
                            evaluationMemo[nextNodes[0].getData().getWidth()][nextNodes[0].getData().getHeight()] = f0;
                            value0 = f0.get();
                        } else {
                            value0 = (nodeReturn) evaluationMemo[nextNodes[0].getData().getWidth()][nextNodes[0].getData().getHeight()].get();
                        }
                        if (evaluationMemo[nextNodes[1].getData().getWidth()][nextNodes[1].getData().getHeight()] == null) {
                            FutureTask<nodeReturn> f1 = nextNodes[1].evaluate();
                            evaluationMemo[nextNodes[1].getData().getWidth()][nextNodes[1].getData().getHeight()] = f1;
                            value1 = f1.get();
                        } else {
                            value1 = (nodeReturn) evaluationMemo[nextNodes[1].getData().getWidth()][nextNodes[1].getData().getHeight()].get();
                        }
                        int value = value0.getValue() + value1.getValue();
                        if (maxPairValue < value) {
                            maxPair = new nodeReturn(nextNodes, value);
                            maxPairValue = value;
                        }
                    } catch (Exception e) {
                        System.err.println(e);
                        maxPair = new nodeReturn(null, -1);
                    }

                }
            }
            return maxPair;
        }
    }

    public class node {
        private Cloth data;
        private ArrayList<node> parents;
        private ArrayList<node[]> children;

        node(node parent, Cloth data) {
            this.data = data;
            this.parents = new ArrayList<node>();
            this.parents.add(parent);
            this.children = new ArrayList<node[]>();
        }

        node(Cloth head) {
            this.data = head;
            this.parents = null;
            this.children = new ArrayList<node[]>();
        }

        public Cloth getData() {
            return data;
        }

        public void addChild(node[] data) {
            this.children.add(data);
        }

        public void addParent(node parent) {
            this.parents.add(parent);
        }

        public FutureTask<nodeReturn> evaluate() {
            if(evaluationMemo[this.data.getWidth()][this.data.getHeight()] == null) {
                valueCallable r = new valueCallable(this.children, this.data);
                FutureTask<nodeReturn> f = new FutureTask<>(r);
                ExecutorService pool = SearchTree.getThreadPool();
                pool.execute(f);
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