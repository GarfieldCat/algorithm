package com.garfield.garfield;

public class RedBlackTree<T extends Comparable<T>> {

    private static class Node<T extends Comparable<T>> {
        T value;
        Node<T> left;
        Node<T> right;
        Node<T> parent;
        Color color;

        private Node() {
        }

        Node(T element) {
            this();
            this.value = element;
            this.color = Color.Black;
        }
    }

    enum Color {
        Red, Black;
    }

    private Node<T> root;

    public RedBlackTree() {
        this.root = null;
    }

    public void printTree() {
        printTree(this.root);
    }

    private void printTree(Node<T> node) {
        if (node == null)
            return;

        printTree(node.left);
        System.out.println(String.format("Color:%s, Value:%s, Parent:%s",
                node.color, node.value, node.parent == null ? null : node.parent.value));
        printTree(node.right);
    }

    public boolean contains(T value) {
        return contains(value, this.root);
    }

    private boolean contains(T value, Node<T> node) {
        while (true) {
            if (node == null) return false;
            int compareResult = node.value.compareTo(value);

            if (compareResult == 0) return true;
            if (compareResult > 0) {
                node = node.left;
            } else {
                node = node.right;
            }
        }
    }

    public boolean insert(T value) {
        Node<T> newNode = new Node<>(value);

        if (this.root == null) {
            this.root = newNode;
            return true;
        }

        newNode.color = Color.Red;
        Node<T> pCurrent = this.root;
        while (true) {
            if (pCurrent.value.compareTo(value) > 0) {
                if (pCurrent.left == null) {
                    newNode.parent = pCurrent;
                    pCurrent.left = newNode;
                    break;
                } else {
                    pCurrent = pCurrent.left;
                }

            } else if (pCurrent.value.compareTo(value) < 0) {
                if (pCurrent.right == null) {
                    newNode.parent = pCurrent;
                    pCurrent.right = newNode;
                    break;
                } else {
                    pCurrent = pCurrent.right;
                }

            } else {
                // Insertion fails if already present
                return false;
            }
        }

        fixTree(newNode);
        return true;
    }

    /**
     * The new node's color is red. If it's parent's color is red, fix the tree.
     *
     * @param node current node
     */
    private void fixTree(Node<T> node) {
        while (node.parent != null && node.parent.color == Color.Red) {
            Node<T> grand = node.parent.parent;
            Node<T> parent = node.parent;
            Node<T> uncle;
            if (parent == grand.left) {
                uncle = grand.right;
                if (uncle != null && uncle.color == Color.Red) {
                    grand.color = Color.Red;
                    parent.color = Color.Black;
                    uncle.color = Color.Black;
                    node = grand;
                    continue;
                }
                if (node == parent.right) {
                    // Double rotation needed
                    node = parent;
                    rotateAnticlockwise(node);
                    grand = node.parent.parent;
                    parent = node.parent;
                }
                grand.color = Color.Red;
                parent.color = Color.Black;
                rotateClockwise(grand);
            } else {
                uncle = grand.left;
                if (uncle != null && uncle.color == Color.Red) {
                    grand.color = Color.Red;
                    parent.color = Color.Black;
                    uncle.color = Color.Black;
                    node = grand;
                    continue;
                }
                if (node == parent.left) {
                    // Double rotation needed
                    node = parent;
                    rotateClockwise(node);
                    grand = node.parent.parent;
                    parent = node.parent;
                }
                grand.color = Color.Red;
                parent.color = Color.Black;
                rotateAnticlockwise(grand);
            }
        }
        root.color = Color.Black;
    }

    /**
     * Rotate binary tree node with left child.
     *
     * @param node current node
     * @return current node
     */
    private Node<T> rotateClockwise(Node<T> node) {
        Node<T> leftNode = node.left;

        if (node.parent != null) {
            if (node == node.parent.left) {
                node.parent.left = leftNode;
            } else {
                node.parent.right = leftNode;
            }
        } else {
            // Need to change root
            root = leftNode;
        }

        leftNode.parent = node.parent;
        node.parent = leftNode;
        if (leftNode.right != null) {
            leftNode.right.parent = node;
        }

        node.left = leftNode.right;
        leftNode.right = node;

        return leftNode;
    }

    /**
     * Rotate binary tree node with right child.
     *
     * @param node current node
     * @return current node
     */
    private Node<T> rotateAnticlockwise(Node<T> node) {
        Node<T> rightNode = node.right;

        if (node.parent != null) {
            if (node == node.parent.left) {
                node.parent.left = rightNode;
            } else {
                node.parent.right = rightNode;
            }
        } else {
            // Need to change root
            root = rightNode;
        }

        rightNode.parent = node.parent;
        node.parent = rightNode;
        if (rightNode.left != null) {
            rightNode.left.parent = node;
        }

        node.right = rightNode.left;
        rightNode.left = node;

        return rightNode;
    }

}
