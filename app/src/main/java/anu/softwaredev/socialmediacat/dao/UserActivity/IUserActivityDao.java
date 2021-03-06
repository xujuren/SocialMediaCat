package anu.softwaredev.socialmediacat.dao.UserActivity;

import android.content.Context;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import anu.softwaredev.socialmediacat.Classes.Post;

/** Defines the data access and persistence tasks in relation to UserActivity */
public interface IUserActivityDao {
    void createPost(String uId, String tags, String content, int photoId);
    void likePost(Post post, String Uid);
    void unlikePost(Post post, String Uid);
    void deletePost(String postId);
    void loadPosts(List<Post> posts);
    void findUserProfileUid(String userId, TextView uIdTv, TextView captionTv);
    void findUserProfile(FirebaseUser user, TextInputLayout userNameLayout, TextInputLayout interestsLayout, TextInputLayout captionLayout);
    void updateProfile(Context ctx, FirebaseUser user, String newName, String newInterests, String newCaption);
}
