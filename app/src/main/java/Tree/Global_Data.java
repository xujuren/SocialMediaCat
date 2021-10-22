package Tree;
import com.google.firebase.auth.FirebaseAuth;

import java.util.LinkedList;
import java.util.List;
import anu.softwaredev.socialmediacat.Classes.Post;

public class Global_Data {
    RBTree<String> data;
//    List<Post> myPosts;

    public static Global_Data instance = null;

    private Global_Data(){
        data = new RBTree<>();
//        myPosts = new ArrayList<>();        //        myPosts = new LinkedList<>();
    }

    public static Global_Data getInstance() {
        if (instance == null) {
            instance = new Global_Data();
        }
        return instance;
    }


    public void insert(Post post) {
        data.insert(post.getTag(), post);
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

    // TODO @Juren - change name here? getAllPost()?
    public List<Post> toList() {
        List<RBTreeNode<String>> allTags = data.findAll(data.root);
        List<Post> allPosts = new LinkedList<Post>();
        for (RBTreeNode<String> tagSet:allTags) {
            allPosts.addAll(tagSet.postsTree.treeToListInorder(tagSet.postsTree.root));
//            if (tagSet.getPostsTree() != null)
//                allPosts.addAll(tagSet.postsTree.treeToListInorder(tagSet.postsTree.root));
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

    public List<Post> searchByTag(String tag) {
        RBTreeNode<String> node = data.find(tag);
        if (node == null)
            return new LinkedList<Post>();
        return node.postsTree.treeToListInorder(node.postsTree.root);
    }

    public List<Post> searchByUser(String uid){
        List<Post> allPost = new LinkedList<>();
        List<RBTreeNode<String>> allTags = data.findAll(data.root);
        for (RBTreeNode<String> node:allTags) {
            allPost.addAll(node.postsTree.treeToListInorder(node.postsTree.root));
        }
        allPost.removeIf(post -> !post.getUId().equals(uid));

        return allPost;
    }

    public Post search(String tag, String PostId) {
        if (PostId==""){
            return null;
        }
        RBTreeNode<String> node = data.find(tag);
        if (node == null || node.getPostsTree() == null)
            return null;


        RBTreeNode<Post> result = node.getPostsTree().findById(PostId);

        return result == null ? null : result.getKey();
//        return node.getPostsTree().findById(PostId).getKey();
    }

    public boolean likePost(Post pt, String Uid) {
        Post post = search(pt.getTag(), pt.getPostId());
        if (post != null) {
            post.likePost(Uid);
            return true;
        }
        return false;
    }

    public boolean unlikePost(Post pt, String Uid) {
        Post post = search(pt.getTag(), pt.getPostId());
        if (post != null) {
            post.unlikePost(Uid);
            return true;
        }
        return false;
    }

}
