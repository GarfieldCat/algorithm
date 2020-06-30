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

    private enum Color {
        Red, Black;
    }

    private Node<T> root;
    private final Node<T> nilNode;

    public RedBlackTree() {
        this.root = null;
        this.nilNode = new Node<>(null);
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

    private Node<T> find(T value, Node<T> node) {
        while (true) {
            if (node == null) return null;
            int compareResult = node.value.compareTo(value);

            if (compareResult == 0) return node;
            if (compareResult > 0) {
                node = node.left;
            } else {
                node = node.right;
            }
        }
    }

    public T min() {
        Node<T> result = min(root);
        return result == null ? null : result.value;
    }

    private Node<T> min(Node<T> node) {
        if (node == null) return null;

        while (true) {
            if (node.left == null) {
                return node;
            } else {
                node = node.left;
            }
        }
    }

    public T max() {
        Node<T> result = max(root);
        return result == null ? null : result.value;
    }

    private Node<T> max(Node<T> node) {
        if (node == null) return null;

        while (true) {
            if (node.right == null) {
                return node;
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

    public void clearTree() {
        this.root = null;
    }

    public boolean delete(T value) {
        Node<T> deleteNode = find(value, root);
        if (deleteNode == null) return false;

        Node<T> targetNode = deleteNode;
        Node<T> fixNode;

        if (deleteNode.left != null && deleteNode.right != null) {
            targetNode = this.min(deleteNode.right);
        }
        if (targetNode.left != null) {
            fixNode = targetNode.left;
        } else {
            fixNode = targetNode.right;
            if(fixNode == null){
                fixNode = nilNode;
                fixNode.parent = targetNode;
                targetNode.right = fixNode;
            }
        }
        transplant(targetNode, fixNode);

        if (deleteNode != targetNode) {
            deleteNode.value = targetNode.value;
            targetNode.value = null;
        }

        if (targetNode.color == Color.Black) {
            deleteFixTree(fixNode);
        }

        if(fixNode == null){
            transplant(nilNode, null);
        }

        return true;
    }

    /**
     * remove the target node
     */
    private void transplant(Node<T> target, Node<T> with) {
        if (target.parent == null) {
            root = with;
        } else if (target == target.parent.left) {
            target.parent.left = with;
        } else {
            target.parent.right = with;
        }
        if (with != null) {
            with.parent = target.parent;
        }
    }

    private void deleteFixTree(Node<T> fixNode) {
        Node<T> brotherNode;

        while (fixNode != root && fixNode.color == Color.Black) {
            if (fixNode == fixNode.parent.left) {
                brotherNode = fixNode.parent.right;

                if (brotherNode.color == Color.Red) {
                    brotherNode.color = Color.Black;
                    fixNode.parent.color = Color.Red;
                    rotateAnticlockwise(fixNode.parent);
                    brotherNode = fixNode.parent.right;
                }
                if ((brotherNode.left == null || brotherNode.left.color == Color.Black)
                        && (brotherNode.right == null || brotherNode.right.color == Color.Black)) {
                    brotherNode.color = Color.Red;
                    fixNode = fixNode.parent;
                } else {
                    if (brotherNode.right == null || brotherNode.right.color == Color.Black) {
                        brotherNode.color = Color.Red;
                        brotherNode.left.color = Color.Black;
                        rotateClockwise(brotherNode);
                        brotherNode = fixNode.parent.right;
                    }
                    brotherNode.color = fixNode.parent.color;
                    fixNode.parent.color = Color.Black;
                    brotherNode.right.color = Color.Black;
                    rotateAnticlockwise(fixNode.parent);
                    fixNode = root;
                }
            } else {
                brotherNode = fixNode.parent.left;

                if (brotherNode.color == Color.Red) {
                    brotherNode.color = Color.Black;
                    fixNode.parent.color = Color.Red;
                    rotateClockwise(fixNode.parent);
                    brotherNode = fixNode.parent.left;
                }
                if ((brotherNode.left == null || brotherNode.left.color == Color.Black)
                        && (brotherNode.right == null || brotherNode.right.color == Color.Black)) {
                    brotherNode.color = Color.Red;
                    fixNode = fixNode.parent;
                } else {
                    if (brotherNode.left == null || brotherNode.left.color == Color.Black) {
                        brotherNode.color = Color.Red;
                        brotherNode.right.color = Color.Black;
                        rotateClockwise(brotherNode);
                        brotherNode = fixNode.parent.left;
                    }
                    brotherNode.color = fixNode.parent.color;
                    fixNode.parent.color = Color.Black;
                    brotherNode.left.color = Color.Black;
                    rotateClockwise(fixNode.parent);
                    fixNode = root;
                }
            }
        }

        fixNode.color = Color.Black;
    }
}
