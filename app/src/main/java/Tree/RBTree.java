package Tree;

import java.util.LinkedList;
import java.util.List;

import anu.softwaredev.socialmediacat.Classes.Post;

public class RBTree<T extends Comparable<T>> {

    /**
     * root node
     */

    public RBTreeNode<T> root;


    /**
     * red color is false and black is true
     */
    private static final boolean RED = false;
    private static final boolean BLACK = true;


    /**
     * insert a new node into RB tree
     * @param key
     */
    public void insert(T key) {
        // System.out.println("Insert[" + key + "]:");
        RBTreeNode<T> node=new RBTreeNode<T>(key, Color.BLACK,null,null,null);
        if (node != null)
            insert(node);
    }

    /**
     * An inserting method special design for our app
     * @param key
     */
    public void insert(T key, Post post) {
        // System.out.println("Insert[" + key + "]:");
        RBTreeNode<T> node=new RBTreeNode<T>(key, Color.BLACK,null,null,null, post);
        if (node != null)
            insert(node, post);
    }

    /**
     * insert node into RB tree
     * @param node
     */
    private void insert(RBTreeNode<T> node) {
        int cmp;
        RBTreeNode<T> y = null;
        RBTreeNode<T> x = this.root;

        // insert as a binary search tree, we will fix it later
        while (x != null) {
            y = x;
            cmp = node.key.compareTo(x.key);
            if (cmp != 0){
                if (cmp < 0)
                    x = x.left;
                else
                    x = x.right;
            }else {
                return;
            }

        }

        node.parent = y;
        if (y!=null) {
            cmp = node.key.compareTo(y.key);
            if (cmp < 0)
                y.left = node;
            else
                y.right = node;
        } else {
            this.root = node;
        }

        // set node's to red
        node.color = Color.RED;

        // fix the balance of RBtree
        fixBalance_insert(node);
    }

    /**
     * An inserting method special design for our app
     * @param node, Posts
     */
    private void insert(RBTreeNode<T> node, Post post) {
        int cmp;
        RBTreeNode<T> y = null;
        RBTreeNode<T> x = this.root;

        // insert as a binary search tree, we will fix it later
        while (x != null) {
            y = x;
            cmp = node.key.compareTo(x.key);
            if (cmp == 0){
                x.postsTree.insert(post);

                return;
            } else {
                if (cmp < 0)
                    x = x.left;
                else
                    x = x.right;
            }

        }

        node.parent = y;
        if (y!=null) {
            cmp = node.key.compareTo(y.key);
            if (cmp < 0)
                y.left = node;
            else
                y.right = node;
        } else {
            this.root = node;

        }
        // set node's to red
        node.color = Color.RED;

        // fix the balance of RBtree
        fixBalance_insert(node);
    }


    /**
     * Fix balance of our RBtree after inserting
     * @param node
     */
    private void fixBalance_insert(RBTreeNode<T> node) {
        RBTreeNode<T> parent, gparent;

        // if parent node is exist and its color is red
//        parent = getParent(node);
        while (((parent = getParent(node))!=null) && isRed(parent)) {
            gparent = getParent(parent);

            // if parent node is left child of grandparent
            if (parent == gparent.left) {
                // case uncle is red
                RBTreeNode<T> uncle = gparent.right;
                if ((uncle!=null) && isRed(uncle)) {
                    toBlack(uncle);
                    toBlack(parent);
                    toRed(gparent);
                    node = gparent;
                    continue;
                }

                // // if uncle is black and current node are right child of its parent
                if (parent.right == node) {
                    RBTreeNode<T> tmp;
                    leftRotate(parent);
                    tmp = parent;
                    parent = node;
                    node = tmp;
                }


                // if uncle is black and current node are left child of its parent
                toBlack(parent);
                toRed(gparent);
                rightRotate(gparent);
            } else {
                // case if uncle node is red
                RBTreeNode<T> uncle = gparent.left;
                if ((uncle!=null) && isRed(uncle)) {
                    toBlack(uncle);
                    toBlack(parent);
                    toRed(gparent);
                    node = gparent;
                    continue;
                }

                // case if uncle is black and current node is left child
                if (parent.left == node) {
                    RBTreeNode<T> tmp;
                    rightRotate(parent);
                    tmp = parent;
                    parent = node;
                    node = tmp;
                }

                // case uncle is black and current node is right child
                toBlack(parent);
                toRed(gparent);
                leftRotate(gparent);
            }
        }

        // set root node to black
        toBlack(this.root);
    }



    /**
     * Remove a node from tree
     * @param key
     */
    public void delete(T key) {
        RBTreeNode<T> node;

        if ((node = find(root, key)) != null)
            delete(node);
    }

    /**
     * A special design delete method for our app
     * The function will remove the node if the tree in this node is null, else it will remove post from the inner tree of this node
     * @param key,post
     */
    public void delete(T key, Post post) {
        RBTreeNode<T> node;

        if ((node = find(root, key)) != null)
            if (node.postsTree.root == null)
                delete(node);
            else{
                node.postsTree.delete(post);
                System.out.println(node.postsTree.root);
                if(node.postsTree.root == null)
                    delete(node);
            }

    }



    /**
     * Delete a node from tree
     * @param node
     */
    private void delete(RBTreeNode<T> node) {
        RBTreeNode<T> child, parent;
        Color color;

        // if both right and left child of deleted node are not null
        if ( (node.left!=null) && (node.right!=null) ) {
            // the replaced node will be right node of deleted node
            RBTreeNode<T> replace = node;
            replace = replace.right;
            while (replace.left != null)
                replace = replace.left;

            // if deleted node is not root node
            if (getParent(node)!=null) {
                if (getParent(node).left == node)
                    getParent(node).left = replace;
                else
                    getParent(node).right = replace;
            } else {
                // if deleted node is root node, then set replaced node as root node
                this.root = replace;
            }


            // if child is the right child of replaced node, then it is also need fix
            child = replace.right;
            parent = getParent(replace);
            // save the color of replaced node
            color = getColor(replace);

            // if deleted node is the parent node of its replaced node
            if (parent == node) {
                parent = replace;
            } else {
                // if child is not null
                if (child!=null)
                    setParent(child, parent);
                parent.left = child;

                replace.right = node.right;
                setParent(node.right, replace);
            }

            replace.parent = node.parent;
            replace.color = node.color;
            replace.left = node.left;
            node.left.parent = replace;

            if (color == Color.BLACK)
                fixBalance_delete(child, parent);

            node = null;
            return ;
        }

        if (node.left !=null) {
            child = node.left;
        } else {
            child = node.right;
        }

        parent = node.parent;
        // 保存"取代节点"的颜色
        //
        color = node.color;

        if (child!=null)
            child.parent = parent;

        // node is not root node
        if (parent!=null) {
            if (parent.left == node)
                parent.left = child;
            else
                parent.right = child;
        } else {
            this.root = child;
        }

        if (color == Color.BLACK)
            fixBalance_delete(child, parent);
        node = null;
    }


    /**
     * fix the balance of tree after deleted a node
     * @param node
     * @param parent
     */
    private void fixBalance_delete(RBTreeNode<T> node, RBTreeNode<T> parent) {
        RBTreeNode<T> other;

        while ((node==null || isBlack(node)) && (node != this.root)) {
            if (parent.left == node) {
                other = parent.right;
                if (isRed(other)) {
                    // case x's brother is red
                    toBlack(other);
                    toRed(parent);
                    leftRotate(parent);
                    other = parent.right;
                }

                if ((other.left==null || isBlack(other.left)) &&
                        (other.right==null || isBlack(other.right))) {
                    // case x's brother is black the both children of it are all black
                    toRed(other);
                    node = parent;
                    parent = getParent(node);
                } else {

                    if (other.right==null || isBlack(other.right)) {
                        // case x's brother is black and its left child is red, right child is black
                        toBlack(other.left);
                        toRed(other);
                        rightRotate(other);
                        other = parent.right;
                    }
                    // case x's brother is black and its right child is red (left child can be any color)
                    setColor(other, getColor(parent));
                    toBlack(parent);
                    toBlack(other.right);
                    leftRotate(parent);
                    node = this.root;
                    break;
                }
            } else {

                other = parent.left;
                if (isRed(other)) {
                    // case x's brother is red
                    toBlack(other);
                    toRed(parent);
                    rightRotate(parent);
                    other = parent.left;
                }

                if ((other.left==null || isBlack(other.left)) &&
                        (other.right==null || isBlack(other.right))) {
                    // case x's brother is black and both child of it are all black
                    toRed(other);
                    node = parent;
                    parent = getParent(node);
                } else {

                    if (other.left==null || isBlack(other.left)) {
                        // case x's brother is black and its left child is red, right child is black
                        toBlack(other.right);
                        toRed(other);
                        leftRotate(other);
                        other = parent.left;
                    }

                    // case x's brother is black and its right child is red (left child can be any color)
                    setColor(other, getColor(parent));
                    toBlack(parent);
                    toBlack(other.left);
                    rightRotate(parent);
                    node = this.root;
                    break;
                }
            }
        }

        if (node!=null)
            toBlack(node);
    }


    /**
     * 查询节点
     * @param key
     * @return
     */
    public RBTreeNode<T> find(T key) {
        return find(root, key);
    }

    public RBTreeNode<Post> findById(String key) {
        return find((RBTreeNode<Post>)root, key);
    }

    /**
     * find a key in RBtree
     * @param node
     * @param key
     * @return
     */
    private RBTreeNode<T> find(RBTreeNode<T> node, T key) {
        if (node==null)
            return null;

        int compare = key.compareTo(node.key);
        if (compare < 0)
            return find(node.left, key);
        else if (compare > 0)
            return find(node.right, key);
        else
            return node;
    }

    /**
     * A special version of find method designed for our app
     * It took an unique id of Post and find it in our tree
     * @param node
     * @param postId
     * @return
     */
    private RBTreeNode<Post> find(RBTreeNode<Post> node, String postId) {
        if (node==null)
            return null;

//        int cmp = key.compareTo(x.key);
        int cmp = postId.compareTo(node.key.getPostId());
        if (cmp < 0)
            return find(node.left, postId);
        else if (cmp > 0)
            return find(node.right, postId);
        else
            return node;
    }




    public void inorderPrint(RBTreeNode<T> node){
        if(node != null){
            inorderPrint(node.left);
            System.out.println("key:" + node.key);
            inorderPrint(node.right);
        }
    }
    /**
     * List the elements of the tree with in-order
     * @param node
     */
    public List<T> treeToListInorder(RBTreeNode<T> node){
        List<T> list = new LinkedList<>();
        if(node != null){
            list.addAll(treeToListInorder(node.left));
            list.add(node.key);
            list.addAll(treeToListInorder(node.right));
        }
        return list;
    }

    /**
     * find all node in our tree
     *
     * @param node
     * @return all nodes in the tree to a list
     */
    public List<RBTreeNode<T>> findAll(RBTreeNode<T> node){
        List<RBTreeNode<T>> list = new LinkedList<>();
        if(node != null){
            list.addAll(findAll(node.left));
            list.add(node);
            list.addAll(findAll(node.right));
        }
        return list;
    }

    /**
     * retrieve parent node of current node
     * @param node
     * @return parent node of param node
     */
    private RBTreeNode<T> getParent(RBTreeNode<T> node) {
        if (node != null)
            return node.parent;
        return null;
    }

    /**
     * get color of current node
     * @param node
     * @return color of node
     */
    private Color getColor(RBTreeNode<T> node) {
        if(node != null)
            return node.color;
        return Color.BLACK;

    }

    /**
     * check if current node is red
     * @param node
     * @return true if red, false if black
     */
    private boolean isRed(RBTreeNode<T> node) {
        if(node == null)
            return false;
        return node.color != Color.BLACK;
    }

    /**
     * check if current node is black
     * @param node
     * @return true is black, false if red
     */
    private boolean isBlack(RBTreeNode<T> node) {
        if(node ==null)
            return false;
        return node.color != Color.RED;
    }

    /**
     * set color to black
     * @param node
     */
    private void toBlack(RBTreeNode<T> node) {
        if (node!=null)
            node.color = Color.BLACK;
    }

    /**
     * set color to red
     * @param node
     */
    private void toRed(RBTreeNode<T> node) {
        if (node!=null)
            node.color = Color.RED;
    }

    /**
     * if node is not null, set parent
     * @param node
     * @param parent
     */
    private void setParent(RBTreeNode<T> node, RBTreeNode<T> parent) {
        if (node!=null)
            node.parent = parent;
    }

    /**
     * if node is not null, set color
     * @param node
     * @param color
     */
    private void setColor(RBTreeNode<T> node, Color color) {
        if (node!=null)
            node.color = color;
    }


    /**
     * Left rotation
     * @param x
     */
    private void leftRotate(RBTreeNode<T> x) {
        // set x as right child of y
        RBTreeNode<T> y = x.right;

        // set left child of y as right child of x
        // if left child of y is null, then set x as parent node of left child of y
        x.right = y.left;
        if (y.left != null)
            y.left.parent = x;


        // set x's parent as y's parent
        y.parent = x.parent;

        if (x.parent == null) {
            // if x's parent node is null, then set y as root node
            this.root = y;
        } else {
            if (x.parent.left == x)
                // if x is left child of its parent node, then set y as left child of x's parent node
                x.parent.left = y;
            else
                // if x is right child of its parent node, then set y as right child of x's parent node
                x.parent.right = y;
        }
        // set x as y's left child
        y.left = x;
        // set y as parent node of y
        x.parent = y;
    }


    /**
     * Right rotation
     * @param y
     */
    private void rightRotate(RBTreeNode<T> y) {
        // set x as left child of current node
        RBTreeNode<T> x = y.left;

        // set right child of x as left child of y
        // if right child of x is not null, then set y as parent node of right child of x
        y.left = x.right;
        if (x.right != null)
            x.right.parent = y;

        // set y's parent node to x's parent node
        x.parent = y.parent;

        if (y.parent == null) {
            // if the parent node of y is null, then set x as root node
            this.root = x;
        } else {
            if (y == y.parent.right)
                //if y is right child of its parent node, then set x as right child of y's parent node
                y.parent.right = x;
            else
                //if y is left child of his parent node, then set x as left child of its parent node
                y.parent.left = x;
        }

        // set "y" as "right child of x"
        x.right = y;

        // set "y's parent" as x
        y.parent = x;
    }


}