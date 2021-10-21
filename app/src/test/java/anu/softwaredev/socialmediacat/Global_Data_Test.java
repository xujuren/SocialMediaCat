package anu.softwaredev.socialmediacat;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import Tree.Global_Data;
import anu.softwaredev.socialmediacat.Classes.Post;

public class Global_Data_Test {
    List<Post> expected = new LinkedList<>();
    @Before
    public void setUp(){
        for (int i = 0; i < 10; i++) {
            Post post = new Post( "uId_" + i, "tag_" + i, "postId_" + i, "content_" + i, i);
            Global_Data.getInstance().insert(post);
            expected.add(post);
        }
    }

    @After
    public void cleanUp(){
        for (int i = 0; i < 10; i++) {
            Post post = new Post( "uId_" + i, "tag_" + i, "postId_" + i, "content_" + i, i);
            Global_Data.getInstance().delete(post);
            expected.remove(post);
        }
    }


    @Test
    public void insertTest(){
        assertEquals(expected.toString(), Global_Data.getInstance().toList().toString());
    }

    @Test
    public void deleteTest() {

        Post toDelete  = new Post("uId_" + 2, "tag_" + 2, "postId_" + 2, "content_" + 2, 2);
        Global_Data.getInstance().delete(toDelete);
        expected.remove(2);
        assertEquals(expected.toString(), Global_Data.getInstance().toList().toString());


    }


    @Test
    public void testSearch(){
        Post toSearch  = new Post("uId_" + 3, "tag_" + 3, "postId_" + 3, "content_" + 3, 3);
        List<Post> byTag = new LinkedList<>();
        byTag.add(toSearch);
        assertEquals(toSearch.toString(), Global_Data.getInstance().searchById(toSearch.getPostId()).toString());
        assertEquals(toSearch.toString(),Global_Data.getInstance().search(toSearch.getTag(),toSearch.getPostId()).toString());
        assertEquals(byTag.toString(), Global_Data.getInstance().searchByTag(toSearch.getTag()).toString());
    }

    @Test
    public void testLike() {
        Post post = new Post( "uId_" + 1, "tag_" + 1, "postId_" + 1, "content_" + 1, 1);
        Global_Data.getInstance().likePost(post);
        assertEquals(1, Global_Data.getInstance().searchById(post.getPostId()).getLikeCount());

        Global_Data.getInstance().unlikePost(post);
        assertEquals(0, Global_Data.getInstance().searchById(post.getPostId()).getLikeCount());
    }

    @Test
    public void testSearchByUser() {
        List<Post> user1 = Global_Data.getInstance().searchByUser("uId_1");
        List<Post> expected = new LinkedList<>();
        expected.add(new Post( "uId_" + 1, "tag_" + 1, "postId_" + 1, "content_" + 1, 1));
        assertEquals(expected.toString(), user1.toString());
    }
}
