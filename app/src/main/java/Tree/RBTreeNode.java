package Tree;

import java.util.LinkedList;
import java.util.List;

import anu.softwaredev.socialmediacat.Classes.Post;

public class RBTreeNode<E extends Post>{

    boolean color;

    /**节点关键字*/
    E key;

    /**父节点*/
    RBTreeNode<E> parent;

    /**左子节点*/
    RBTreeNode<E> left;

    /**右子节点*/
    RBTreeNode<E> right;

    /**帖子*/
    RBTreeNode<Post> tree;


    public RBTreeNode(E key, boolean color, RBTreeNode<E> parent, RBTreeNode<E> left, RBTreeNode<E> right) {
        this.key = key;
        this.color = color;
        this.parent = parent;
        this.left = left;
        this.right = right;
//        this.tree = new RBTree<Posts>()

    }

    public RBTreeNode() {
        key = null;

    }

    public RBTreeNode(E element) {
        key = element;
        left = new EmptyRBTreeNode<>();

        right = new EmptyRBTreeNode<>();
    }

    @Override
    public String toString() {
        return "RBTNode{" +
                "color=" + (color ? "red":"black") +
                ", key=" + key +
                '}';
    }


    /**
     * List the elements of the tree with in-order
     */
    public List<E> inOrder() {
        return this.treeToListInOrder(this);
    }

    /**
     * Converts tree to list in-order. Helper method of inOrder.
     * @param tree to convert to list.
     * @return in-order list of tree values.
     */
    private List<E> treeToListInOrder(RBTreeNode<E> tree) {
        List<E> list = new LinkedList<>();

        if(tree != null){
            // Recurse through left subtree.
            if (tree.left.key != null) {
                list.addAll(treeToListInOrder(tree.left));
            }

            // Add current node's value
            list.add(tree.key);

            // Recurse through left subtree.
            if (tree.right.key != null) {
                list.addAll(treeToListInOrder(tree.right));
            }

            return list;
        }
        return list;
    }


    public static class EmptyRBTreeNode<E extends Post> extends EmptyRBTreeNode<E> {
        @Override
        public RBTreeNode<E> insert(E element) {
            return new RBTreeNode<E>(element);
        }
    }
}
