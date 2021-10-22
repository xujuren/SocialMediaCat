package anu.softwaredev.socialmediacat;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import Tree.Color;
import Tree.RBTree;
import Tree.RBTreeNode;
import anu.softwaredev.socialmediacat.Classes.Post;

public class TreeTest {
    RBTree<Integer> testTree = new RBTree<>();
    @Test
    public void insertTest(){
        for (int i = 1; i <= 11; i++) {
            testTree.insert(i);
        }

        assertEquals(Color.BLACK,testTree.find(1).getColor());
        assertEquals(Color.BLACK,testTree.find(2).getColor());
        assertEquals(Color.BLACK,testTree.find(3).getColor());
        assertEquals(Color.BLACK,testTree.find(4).getColor());
        assertEquals(Color.BLACK,testTree.find(5).getColor());
        assertEquals(Color.BLACK,testTree.find(6).getColor());
        assertEquals(Color.BLACK,testTree.find(7).getColor());
        assertEquals(Color.RED,testTree.find(8).getColor());
        assertEquals(Color.RED,testTree.find(9).getColor());
        assertEquals(Color.BLACK,testTree.find(10).getColor());
        assertEquals(Color.RED,testTree.find(11).getColor());
    }

    @Test
    public void deleteTest(){
        for (int i = 1; i <= 11; i++) {
            testTree.insert(i);
        }

        for (int i = 1; i <= 5 ; i++) {
            testTree.delete(i);
        }

        assertEquals(Color.RED,testTree.find(11).getColor());

        for (int i = 6; i <= 10 ; i++) {
            testTree.delete(i);
        }

        testTree.inorderPrint(testTree.root);
        assertEquals(Color.BLACK,testTree.find(11).getColor());
        testTree.delete(11);
        testTree.inorderPrint(testTree.root);

    }

    @Test
    public void toListTest() {
        List<Integer> expected = new LinkedList<>();

        for (int i = 1; i <= 11; i++) {
            expected.add(i);
        }


        for (int i = 1; i <= 11; i++) {
            testTree.insert(i);
        }

        assertArrayEquals(expected.toArray(), testTree.treeToListInorder(testTree.root).toArray());

    }


    @Test
    public void testSecondLayer() {
        RBTree<String> data = new RBTree<>();
        List<Post> expected = new LinkedList<>();
        for (int i = 1; i <= 11; i++) {
            Post post = new Post( "uId_" + i, "tag_" + i, "postId_" + i, "content_" + i, i);
            data.insert(post.getTag(),post);
        }
        Post post = new Post( "uId_" + 0, "tag_" + 1, "postId_" + 0, "content_" + 0, 0);
        expected.add(post);

        expected.add(new Post( "uId_" + 1, "tag_" + 1, "postId_" + 1, "content_" + 1, 1));



        data.insert(post.getTag(), post);

        // Find node with tag_1
        RBTreeNode<String> tag1= data.find(post.getTag());
        assertEquals(expected.toString(), tag1.getPostsTree().treeToListInorder(tag1.getPostsTree().root).toString());
    }

}
