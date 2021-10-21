package anu.softwaredev.socialmediacat.Classes;
import static org.junit.Assert.*;
import org.junit.Test;

public class UserTest {
    User user1A = new User("u1", "u1@t.com");
    User user1B = new User("u1", "u1@t.com", "", "", "");
    User user2A = new User("u2", "u2@t.com");
    User user3A = new User("u2", "u2@t.com", "User2", "", "");

    @Test
    public void CheckConstructorsSetUp() {
        assertNotEquals("The users object should not be equal", user1A, user1B);
        assertEquals("The users' string representations should be equal!", user1A.toString(), user1B.toString());

        assertNotEquals("The users object should not be equal", user2A, user3A);
        assertEquals("The users' string representations should not be equal!", user2A.toString(), user3A.toString());

    }

    @Test
    public void getEmailAddress() {
    }

    @Test
    public void getUserName() {
    }

    @Test
    public void getCaption() {
    }

    @Test
    public void getInterests() {
    }

    @Test
    public void setUid() {
    }

    @Test
    public void setEmailAddress() {
    }

    @Test
    public void setUserName() {
    }

    @Test
    public void setCaption() {
    }

    @Test
    public void setInterests() {
    }

    @Test
    public void toMap() {
    }
}