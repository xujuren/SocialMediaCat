package Tree;

import java.util.LinkedList;
import java.util.List;

import anu.softwaredev.socialmediacat.Classes.Post;

public class RBTree <E extends Post>{

    /**根节点*/
    public RBTreeNode<E> root;

    /**
     * 颜色常量 false表示红色，true表示黑色
     */
    private static final boolean RED = false;
    private static final boolean BLACK = true;


    public void insert(E key) {
        System.out.println("插入[" + key + "]:");
        RBTreeNode<E> node=new RBTreeNode<E>(key, BLACK,null,null,null);
        // 如果新建结点失败，则返回。
        if (node != null)
            insert(node);
    }


    private void insert(RBTreeNode<E> node) {
        int cmp;
        RBTreeNode<E> y = null;
        RBTreeNode<E> x = this.root;

        // 1. 将红黑树当作一颗二叉查找树，将节点添加到二叉查找树中。
        while (x != null) {
            y = x;
            cmp = node.key.compareTo(x.key);
            if (cmp < 0)
                x = x.left;
            else
                x = x.right;
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

        // 2. 设置节点的颜色为红色
        node.color = RED;

        // 3. 将它重新修正为一颗二叉查找树
        insertFixUp(node);
    }


    private void insertFixUp(RBTreeNode<E> node) {
        RBTreeNode<E> parent, gparent;

        // 若“父节点存在，并且父节点的颜色是红色”
        while (((parent = getParent(node))!=null) && isRed(parent)) {
            gparent = getParent(parent);

            //若“父节点”是“祖父节点的左孩子”
            if (parent == gparent.left) {
                // Case 1条件：叔叔节点是红色
                RBTreeNode<E> uncle = gparent.right;
                if ((uncle!=null) && isRed(uncle)) {
                    setBlack(uncle);
                    setBlack(parent);
                    setRed(gparent);
                    node = gparent;
                    continue;
                }

                // Case 3条件：叔叔是黑色，且当前节点是右孩子
                if (parent.right == node) {
                    RBTreeNode<E> tmp;
                    leftRotate(parent);
                    tmp = parent;
                    parent = node;
                    node = tmp;
                }

                // Case 2条件：叔叔是黑色，且当前节点是左孩子。
                setBlack(parent);
                setRed(gparent);
                rightRotate(gparent);
            } else {    //若“z的父节点”是“z的祖父节点的右孩子”
                // Case 1条件：叔叔节点是红色
                RBTreeNode<E> uncle = gparent.left;
                if ((uncle!=null) && isRed(uncle)) {
                    setBlack(uncle);
                    setBlack(parent);
                    setRed(gparent);
                    node = gparent;
                    continue;
                }

                // Case 2条件：叔叔是黑色，且当前节点是左孩子
                if (parent.left == node) {
                    RBTreeNode<E> tmp;
                    rightRotate(parent);
                    tmp = parent;
                    parent = node;
                    node = tmp;
                }

                // Case 3条件：叔叔是黑色，且当前节点是右孩子。
                setBlack(parent);
                setRed(gparent);
                leftRotate(gparent);
            }
        }

        // 将根节点设为黑色
        setBlack(this.root);
    }


    public void remove(E key) {
        RBTreeNode<E> node;

        if ((node = search(root, key)) != null)
            remove(node);
    }


    private void remove(RBTreeNode<E> node) {
        RBTreeNode<E> child, parent;
        boolean color;


        if ( (node.left!=null) && (node.right!=null) ) {

            RBTreeNode<E> replace = node;


            replace = replace.right;
            while (replace.left != null)
                replace = replace.left;


            if (getParent(node)!=null) {
                if (getParent(node).left == node)
                    getParent(node).left = replace;
                else
                    getParent(node).right = replace;
            } else {

                this.root = replace;
            }


            child = replace.right;
            parent = getParent(replace);

            color = getColor(replace);


            if (parent == node) {
                parent = replace;
            } else {
                // child不为空
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

            if (color == BLACK)
                removeFixUp(child, parent);

            node = null;
            return ;
        }

        if (node.left !=null) {
            child = node.left;
        } else {
            child = node.right;
        }

        parent = node.parent;
        color = node.color;

        if (child!=null)
            child.parent = parent;

        if (parent!=null) {
            if (parent.left == node)
                parent.left = child;
            else
                parent.right = child;
        } else {
            this.root = child;
        }

        if (color == BLACK)
            removeFixUp(child, parent);
        node = null;
    }


    private void removeFixUp(RBTreeNode<E> node, RBTreeNode<E> parent) {
        RBTreeNode<E> other;

        while ((node==null || isBlack(node)) && (node != this.root)) {
            if (parent.left == node) {
                other = parent.right;
                if (isRed(other)) {
                    // Case 1: x的兄弟w是红色的
                    setBlack(other);
                    setRed(parent);
                    leftRotate(parent);
                    other = parent.right;
                }

                if ((other.left==null || isBlack(other.left)) &&
                        (other.right==null || isBlack(other.right))) {
                    // Case 2: x的兄弟w是黑色，且w的俩个孩子也都是黑色的
                    setRed(other);
                    node = parent;
                    parent = getParent(node);
                } else {

                    if (other.right==null || isBlack(other.right)) {
                        // Case 4: x的兄弟w是黑色的，并且w的左孩子是红色，右孩子为黑色。
                        setBlack(other.left);
                        setRed(other);
                        rightRotate(other);
                        other = parent.right;
                    }
                    // Case 3: x的兄弟w是黑色的；并且w的右孩子是红色的，左孩子任意颜色。
                    setColor(other, getColor(parent));
                    setBlack(parent);
                    setBlack(other.right);
                    leftRotate(parent);
                    node = this.root;
                    break;
                }
            } else {

                other = parent.left;
                if (isRed(other)) {
                    // Case 1: x的兄弟w是红色的
                    setBlack(other);
                    setRed(parent);
                    rightRotate(parent);
                    other = parent.left;
                }

                if ((other.left==null || isBlack(other.left)) &&
                        (other.right==null || isBlack(other.right))) {
                    // Case 2: x的兄弟w是黑色，且w的俩个孩子也都是黑色的
                    setRed(other);
                    node = parent;
                    parent = getParent(node);
                } else {

                    if (other.left==null || isBlack(other.left)) {
                        // Case 4: x的兄弟w是黑色的，并且w的左孩子是红色，右孩子为黑色。
                        setBlack(other.right);
                        setRed(other);
                        leftRotate(other);
                        other = parent.left;
                    }

                    // Case 3: x的兄弟w是黑色的；并且w的右孩子是红色的，左孩子任意颜色。
                    setColor(other, getColor(parent));
                    setBlack(parent);
                    setBlack(other.left);
                    rightRotate(parent);
                    node = this.root;
                    break;
                }
            }
        }

        if (node!=null)
            setBlack(node);
    }


    /**
     * 查询节点
     * @param key
     * @return
     */
    public RBTreeNode<E> search(E key) {
        return search(root, key);
    }

    public RBTreeNode<E> search(int key) {
        return search(root, key);
    }

    private RBTreeNode<E> search(RBTreeNode<E> x, int key) {
        if (x==null)
            return x;



        int cmp = key - x.key.getPid();
        if (cmp < 0)
            return search(x.left, key);
        else if (cmp > 0)
            return search(x.right, key);
        else
            return x;
    }

    private RBTreeNode<E> search(RBTreeNode<E> x, E key) {
        if (x==null)
            return x;

        int cmp = key.compareTo(x.key);
        if (cmp < 0)
            return search(x.left, key);
        else if (cmp > 0)
            return search(x.right, key);
        else
            return x;
    }

    /**
     * 中序遍历
     * @param node
     */


    /**
     * 中序遍历
     * @param node
     */
    public List<E> middleTreeIterator(RBTreeNode<E> node){
//        List<E> tree = new ArrayList<E>();
        List<E> list = new LinkedList<>();
        if(node != null){
            list.addAll(middleTreeIterator(node.left));//遍历当前节点左子树
            list.add(node.key);
//            System.out.println("key:" + node.key);
            list.addAll(middleTreeIterator(node.right));
        }
        return list;
    }




    private RBTreeNode<E> getParent(RBTreeNode<E> node) {
        return node!=null ? node.parent : null;
    }
    private boolean getColor(RBTreeNode<E> node) {
        return node!=null ? node.color : BLACK;
    }
    private boolean isRed(RBTreeNode<E> node) {
        return ((node!=null)&&(node.color==RED)) ? true : false;
    }
    private boolean isBlack(RBTreeNode<E> node) {
        return !isRed(node);
    }
    private void setBlack(RBTreeNode<E> node) {
        if (node!=null)
            node.color = BLACK;
    }
    private void setRed(RBTreeNode<E> node) {
        if (node!=null)
            node.color = RED;
    }
    private void setParent(RBTreeNode<E> node, RBTreeNode<E> parent) {
        if (node!=null)
            node.parent = parent;
    }
    private void setColor(RBTreeNode<E> node, boolean color) {
        if (node!=null)
            node.color = color;
    }



    private void leftRotate(RBTreeNode<E> x) {
        // 设置x的右孩子为y
        RBTreeNode<E> y = x.right;

        // 将 “y的左孩子” 设为 “x的右孩子”；
        // 如果y的左孩子非空，将 “x” 设为 “y的左孩子的父亲”
        x.right = y.left;
        if (y.left != null)
            y.left.parent = x;

        // 将 “x的父亲” 设为 “y的父亲”
        y.parent = x.parent;

        if (x.parent == null) {
            this.root = y;            // 如果 “x的父亲” 是空节点，则将y设为根节点
        } else {
            if (x.parent.left == x)
                x.parent.left = y;    // 如果 x是它父节点的左孩子，则将y设为“x的父节点的左孩子”
            else
                x.parent.right = y;    // 如果 x是它父节点的左孩子，则将y设为“x的父节点的左孩子”
        }

        // 将 “x” 设为 “y的左孩子”
        y.left = x;
        // 将 “x的父节点” 设为 “y”
        x.parent = y;
    }


    private void rightRotate(RBTreeNode<E> y) {
        // 设置x是当前节点的左孩子。
        RBTreeNode<E> x = y.left;

        // 将 “x的右孩子” 设为 “y的左孩子”；
        // 如果"x的右孩子"不为空的话，将 “y” 设为 “x的右孩子的父亲”
        y.left = x.right;
        if (x.right != null)
            x.right.parent = y;

        // 将 “y的父亲” 设为 “x的父亲”
        x.parent = y.parent;

        if (y.parent == null) {
            this.root = x;            // 如果 “y的父亲” 是空节点，则将x设为根节点
        } else {
            if (y == y.parent.right)
                y.parent.right = x;    // 如果 y是它父节点的右孩子，则将x设为“y的父节点的右孩子”
            else
                y.parent.left = x;    // (y是它父节点的左孩子) 将x设为“x的父节点的左孩子”
        }

        // 将 “y” 设为 “x的右孩子”
        x.right = y;

        // 将 “y的父节点” 设为 “x”
        y.parent = x;
    }


    /**
     * List the elements of the tree with in-order
     */



}
