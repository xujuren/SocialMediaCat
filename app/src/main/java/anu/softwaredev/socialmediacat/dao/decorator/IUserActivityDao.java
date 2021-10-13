package anu.softwaredev.socialmediacat.dao.decorator;

import java.util.List;

public interface IUserActivityDao {

    /** 5 Methods */

    public UserActivity createPost(String category, String postId, String content, String uId);

    public UserActivity likePost(String username, Integer idPost);

    public List<UserActivity> findAllPosts();

    public String getFilePath();

    public void deleteAll();


}
