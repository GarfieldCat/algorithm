package com.garfield.garfield;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * http://www.codebytes.in/2014/10/red-black-tree-java-implementation.html
 * https://www.jianshu.com/p/84416644c080
 */
public class RedBlackTreeTest {

    @Test
    public void Test() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        Integer[] array = {22, 33, 4, 5, 6, 7, 8, 55, 3, 2, 1, 6, 4};

        System.out.println("The test begin!-----------------------");

        for (Integer i : array) {
            tree.insert(i);
        }
        tree.printTree();

        System.out.println("The test begin!-----------------------");

        tree.delete(2);

        tree.printTree();


    }
}