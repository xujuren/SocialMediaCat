package anu.softwaredev.socialmediacat.dao.decorator;

import java.util.List;

import anu.softwaredev.socialmediacat.Classes.Post;

public interface IUserActivityDao {

    /** 5 Methods */

    public UserActivity createPost(String uId, String tags, String content, int photoId);

    public UserActivity likePost(String username, Integer idPost);

    public List<Post> findAllPosts();       // change to get Posts

    public String getFilePath();

    public void deleteAll();


}
