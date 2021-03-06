package Tree;

import anu.softwaredev.socialmediacat.Classes.Post;

public class RBTreeNode<T extends Comparable<T>> {

    /**color of node*/
    Color color;

    /**search key*/
    T key;

    /**parent node*/
    RBTreeNode<T> parent;

    /**left child*/
    RBTreeNode<T> left;

    /**right child*/
    RBTreeNode<T> right;

    /**A tree in node, used to store post has same tag*/
    RBTree<Post> postsTree= new RBTree<>();

    /**
     * Constructor, used for second layer tree
     * @param key
     * @param color
     * @param parent
     * @param left
     * @param right
     */
    public RBTreeNode(T key, Color color, RBTreeNode<T> parent, RBTreeNode<T> left, RBTreeNode<T> right) {
        this.key = key;
        this.color = color;
        this.parent = parent;
        this.left = left;
        this.right = right;
    }

    public T getKey() {
        return key;
    }

    public RBTree<Post> getPostsTree() {
        return postsTree;
    }

    /**
     * Constructor, if tag is not found in first layer tree then create it and insert current post into tree of current node
     * @param key
     * @param color
     * @param parent
     * @param left
     * @param right
     * @param post
     */
    public RBTreeNode(T key, Color color, RBTreeNode<T> parent, RBTreeNode<T> left, RBTreeNode<T> right, Post post ) {
        this.key = key;
        this.color = color;
        this.parent = parent;
        this.left = left;
        this.right = right;
        postsTree.insert(post);
    }


    public Color getColor() {
        return color;
    }

    @Override
    public String toString() {
        return "RBTreeNode{" +
                "color=" + color +
                ", key=" + key +
                ", postsTree=" + postsTree +
                '}';
    }

}