package Tree;

import java.util.LinkedList;
import java.util.List;

import anu.softwaredev.socialmediacat.Classes.Post;

public class Global_Data {
    RBTree<String> data;

    public static Global_Data instance = null;

    private Global_Data(){
        data = new RBTree<>();
    }

    public static Global_Data getInstance() {
        if (instance == null) {
            instance = new Global_Data();
        }
        return instance;
    }


    public void insert(String tag, Post post) {
        data.insert(tag, post);
    }

    public void delete(Post post) {
        data.delete(post.getTag(), post);
    }

    public void inorderPrint() {
        data.inorderPrint(data.root);
    }

    public RBTree<String> getData(){
        return data;
    }

    public void setData(RBTree<String> data) {
        this.data = data;
    }

    public List<Post> toList() {
        List<RBTreeNode<String>> allTags = data.findAll(data.root);
        List<Post> allPosts = new LinkedList<Post>();
        for (RBTreeNode<String> tagSet:allTags) {
            allPosts.addAll(tagSet.postsTree.treeToListInorder(tagSet.postsTree.root));
        }
        return allPosts;
    }

    public Post searchById(String postId) {
        List<RBTreeNode<String>> allTags = data.findAll(data.root);
        for (RBTreeNode<String> node:allTags) {
            RBTreeNode<Post> result = node.getPostsTree().findById(postId);
            if (result != null)
                return result.getKey();
        }
        return null;
    }

    public Post search(String tag, String PostId) {
        RBTreeNode<String> node = data.find(tag);
        if (node == null)
            return null;
        return node.getPostsTree().findById(PostId).getKey();
    }

    public void likePost(Post pt) {
        Post post = search(pt.getTag(), pt.getPostId());
        if (post != null)
            post.likePost();
    }
}
