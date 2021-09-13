package anu.softwaredev.socialmediacat.dao.decorator;

import java.util.List;

public interface iUserActivityDao {

    /* 5 Types */

    public UserActivity createPost(String username, String postContent);

    public UserActivity likePost(String username, String idPost);

    public List<UserActivity> findAllPosts();

    public String getFilePath();

    public void deleteAll();


}
