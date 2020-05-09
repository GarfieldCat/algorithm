package com.garfield.garfield;

import org.junit.Test;

import static org.junit.Assert.*;

public class RedBlackTreeTest {

    @Test
    public void Test() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        Integer[] array = {22, 33, 4, 5, 6, 7, 8, 55, 3, 2, 1, 6, 4};

        System.out.println("The test begin!-----------------------");

        for (Integer i : array) {
            tree.insert(i);
            System.out.println(i + " has inserted");
        }
        tree.printTree();


    }
}