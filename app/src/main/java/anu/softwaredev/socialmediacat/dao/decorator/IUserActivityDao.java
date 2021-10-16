package anu.softwaredev.socialmediacat.dao.decorator;

import java.util.List;

import anu.softwaredev.socialmediacat.Classes.Post;

public interface IUserActivityDao {

    /** 5 Methods */

    public void createPost(String uId, String tags, String content, int photoId);

    public void likePost(Integer idPost);

    // TODO [@Kyle] see here for likePost
    UserActivity likePost(String username, String postId);

    public List<Post> findAllPosts();       // change to get Posts

    public String getFilePath();

    public void deleteAll();


}
