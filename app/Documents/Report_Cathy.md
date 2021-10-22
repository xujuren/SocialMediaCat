# ezclap - Report [Cathy]

## Table of Contents

1. [Team Members and Roles](#team-members-and-roles)
2. [Conflict Resolution Protocol](#conflict-resolution-protocol)
2. [Application Description](#application-description)
3. [Application UML](#application-uml)
3. [Application Design and Decisions](#application-design-and-decisions)
4. [Summary of Known Errors and Bugs](#summary-of-known-errors-and-bugs)
5. [Testing Summary](#testing-summary)
6. [Implemented Features](#implemented-features)
7. [Team Meetings](#team-meetings)


## Team Members and Roles

| UID | Name | Role |
| :--- | :----: | ---: |
| **u7323583** | Cathy Cheung | Developer |
| **u7274552** | Kevin Wang | Developer |
| **u7324787** | Xiangyu Song | Developer |
| **u7149851** | Juren Xu | Developer |


### Conflict Resolution Protocol
As a four-member team, we need to jointly develop a medium-sized Android application, and conflict resolution has become a very critical problem. Therefore, we formulated a set of conflict resolution protocols at the early stage of project development to solve various conflicts that may occur during the development process.

First, determine the types of conflict -- relational, task-based, and process-based. (Classification from Professor Neal at Stanford)

| Conflict Types | Definition |
| :--- | :----: |
| Relational conflict | Conflict caused by differences between people, including personality differences, antagonism, and personal antipathy. | 
| Task-based conflict | Conflicts caused by differences in team members' perceptions of team tasks.        | 
| Flow type conflict  | Including differences of opinion on the way things are done and the allocation of resources.        | 

**Solution:** <br>
Task-based conflict is the least harmful to the team, and in most cases can even have a positive impact on the final outcome of the project. Different perspectives and solutions are encouraged and discussed in frequently scheduled team meetings to determine the best solution, which helps us to adopt more efficient means to achieve the project objectives.

Process conflicts are very likely to occur, and the team will take different perspectives and opinions into consideration, leading the parties to reach consensus and abide by it. We hope to adopt a compromise solution. The team will gather the viewpoints and opinions of various parties in the meeting and come up with a conflict resolution solution accepted and committed by most people, which may require the compromise of some team members.

Relational conflict is the most unacceptable type of conflict, and teams should do their best to prevent the first two from becoming relational conflict. We ensure effective communication among team members through various channels, and enhance trust and cooperation among team members in regular team meetings to effectively avoid possible relationship conflicts and ensure that team efficiency can be maintained at a high level.

Kevin acts as the lead and is responsible for the overall coordination of conflict issues between team members.


## Application Description          

The Android application developed by our team is an Insta-like social application, through which users can publish their own posts (including pictures), view and like others' posts, and also search all posts according to specific conditions to get the posts they are interested in. All user data is reasonably stored in a remote database, which means that once users have registered, they can log in and view posts from anywhere in the world.

**Canberra** - The name of our App, "Canberra" means "meeting place" in the Indigenous language Ngunnawal.
The App is intended to be a simple, user-friendly platform for people to share photos and words.

**Quotes**: shares quotes to users and non-users on the main page (`MainActivity`).
* Source: extracted via an API (https://quote-garden.herokuapp.com/api/v3/quotes/random)
* Purpose: share ideas to users, encouraging them to do the same.

**Example**：<br>
The initial page of the program is the login page, the user needs to enter the correct account and password to log in their account.<br>
If the user does not already have an account, click the Create Account button to create one.<br>
The email address provided for registration must be in the correct format. The password entered twice must be the same and the length of the password must be at least six characters. Otherwise, an error message will be displayed and the registration fails.<br>
Correct registration will be provided with a successful information and return to the login page.<br>
If a user enters an incorrect account or password, the user cannot log in and an error message is returned.<br>
When the user enters the correct account and password, the login screen will be displayed and the login is successful.<br>
The Log Out button provides the logout function. The current user will be logged out and allowed to Log in to other accounts.<br>
TIMELINE will show the existing posts. Search and return functions are provided.<br>
Users can search by criteria, and some incorrect input has already been processed.<br>
The search results are displayed on the current page.<br>
Users can also click on posts to display their details and like them.<br>
The same user is forbidden to perform "like" operation for many times (the user can only select Unlike when he/she enters the "like" state again).<br>
Clicking back returns you to the main screen.<br>
MANAGE PROFILE allows you to modify personal information.<br>
After the nickname is changed, the welcome sign on the main screen is automatically changed.<br>
CREAT POST provides the ability to create new posts. Includes GPS information and photo selection.<br>
MY POST shows the posts belonging to the current user, and you can see the posts we just created.<br>
Users can click on a specific post to like or delete it.<br>
This is the complete usage process of our social application, and I hope you will be satisfied with our work.

## Application Design and Decisions

### Data Model

#### 1) Data Instances - Sources
Thousands of Facebook posts have been scraped using the python package `facebook_scraper`.
* (Source: https://pypi.org/project/facebook-scraper/)
* Raw Post data were cleaned, and additional actions (like, delete posts) were generated based on the loaded/created Posts' data.
* Multiple data files, for posts and actions, have been produced and stored in `assets`.
* (Note: To better demonstrate and test our Application, about 50% of the actions are "create post" while only 47% and 3% were "like" and "delete" respectively.)

#### 2) Data flow in the Demonstration App
* handled by the `AssetHandler` class
* A total of 30 posts (stored in `Assets/posts.*`) will be pre-loaded as "existing posts", and available for User's view.
* A simulation User Data will be released every 10 seconds (create posts, like posts and delete posts) from 2223 data instances.

#### 3. CSV files were mainly relied on for the simulation.
* Location: `Assets/userActions.csv` , `Assets/posts.csv`
* Objective: Store the UserActivity data used to model the action data.
* Reasons of using CSV file:
    * the classes are of simple, non-nested structure, could be clearly modelled using CSVs
    * Reading, parsing and storing CSV (or Bespoken files) are more efficient than using JSON (or XML) files.

#### 4. Multiple Data Instances
For extensibility and flexibility, Posts could be loaded from different files formats (i.e., CSV, bespoken, Json).
* Location: all under `Assets` folder 
* data in the `Assets` files include 6 fields (userId, tags, postId, content, photoId, likeCounts)
* data in the `userActions` files include 5 fields (action, userId, tags, content and photoId) for 'create post',
  2 fields (userId, postId) for 'likes' both 'delete post' actions.
    
### Data Persistence
All the data and updates from local instances and users were also persisted using Firebase.

**Advantages:** </br>
* Security: delegates much of the security concerns to the specialized platform, especially passwords and Personally Identifiable Information.
* efficiency and synchronization: the Realtime Database offers the possibility of synchronization, which is important in social media apps.
* compatibility: compatible with Android studio and Gradle.

**Limitations in our implementation**
* Due to limited time where proper integration and testing is not possible, real-time synchronization has not been implemented with the new features.
* A real-life use case shall involve both read and write of data via Firebase, in replacement of the local data instances.

### Data Structure
#### Red-Black Tree
 * **Objective：** In our application, we used two layers of Red-Black tree to store our all Posts instance. As the diagram 1 shown below, there is one tree for each tag, and the tag itself is also store as a tree structure (Tree in another tree). For example, in the diagram 1 below shows that there are three posts: post_1, post_2 and post_3. They are all characteristic by Tag_5 and they are all stored in the tree which under the node Tag_5. Please note that if none of a post is characteristic by a tag, then the corresponding tag node will be deleted from outer layer tree (Tag Tree). For example, we can see from the diagram 1 below, there isn’t a post characteristic by Tag_1, Tag_2, Tag_3, Tag_4. Therefore, all these nodes will be deleted from outer layer tree and the original tree will be maintained as tree in diagram 2.

 * **Locations:** RBTree.java, RBTreeNode.java, Color.java in Tree folder.

 * **Reasons:** When we decide our data structure, we firstly compared tree and list and we choose tree instead of list because the tree usually has better performance and more efficient in search and deletion which matches the requirement of our application (a social application requires user can search from all posts or delete their own posts).
    * Then we compared self-balance tree and ordinary tree. And we choose self-balance tree instead of ordinary tree because a self-balance tree and its average search, insertion and deletion time is better than ordinary tree.
    * Finally, in the self-balance tree, we choose the Red-black tree instead of the AVL tree. Because we consider AVL to be a strong self-balance tree and it requires the height difference between the two sides of the tree should be less than one which will require more rotations when doing self-balancing. When we need to insert or delete frequently, the resources used to maintain self-balancing are likely to exceed the time we save due to self-balancing. Therefore, under the trade-off, we finally chose the red-black tree as our data structure.


## Tokenizer, Parser and Search

### Query Grammar

A valid query consists of at least one attribute. Different attributes could be connected with ";".
 * Example: *#comp2400;@postID1*

Query Attributes:
1. #tagname (e.g. #comp2400) , this attribute is to find all posts with #comp2400 as their tag
2. @postID( e.g. @LP1), the postID is a unique identifier to each post, which means different post has its corresponding different unique post ID. When this attribute is put into the search query, there should be only one Post as search result


### Implementation

**Object Token:** An object Token is constructed by a string and its enum type. The string represents the content of the token(not included the # or @ ). The enum Type includes TAG, AND, POSTID

**Search Tokenizer:** The searchTokenizer is used to tokenize a query string (#comp2400;@postID1) when processing the query.
 * method next() extracts a Token of current position and points to the next one  if method hasNext(). returns true.
 * method current() returns current token

**Abstract Exp class: ** Create an abstract Exp class to store all query terms, PostIDExp, TagExp and AndExp to extends from the abstract class 
 * Exp includes a Token type and a string
    * getToken()
   * getValue() return the string
 * example : query : *#comp2400;@postID1*
    * output: TagExp(Token.Type.TAG,"comp2400") AndExp(Token.Type.AND,";") PostIDExp(Token.Type.POSTID,"postID1") 

### Parser class

- Exp getTag()  method (Exp is the return type)
1. step: use the tokenizer 
2. Since Tag is our first attribute in the grammar, we check if the current token's type is TAG. If yes, we return a TagExp as output
3. If no, we return null

- Exp getPostID() method (Exp is the return type)
1. step: use the tokenizer 
2. Situation 1: PostID is our third Token in the grammar if there is a Tag Token as first attribute, And Token as second attribute.  
    - We use tokenizer.next() for 2 times to move to the third position and check if the current token's type is PostID.
    1. If yes, return a PostIDExp as output
    2. If no, return null
    
    Situation 2: PostID is our first and only attribute in the grammar.
    
    - We check if the first position of token's type is PostID
    1. If yes, return a PostIDExp as output
    2. If no, return null


### Design Patterns**

#### 1) Template method and 2) factory method
used together to handle data of different formats
* the abstract class `AssetHandler` defines the requirements in reading and parsing of files.
* the implementation of some of the tasks (e.g. `actionsFromDataInstances()`) were delegated to its subclasses, each corresponds to a file format.
* The creation of the subclasses was performed by `AssetHandlerFactory`.

* **Advantages**:
    * 'pluggable' for multiple sources/formats of data instances
    * helped encapsulating the implementation of the standard workflow.

#### 3) Data Access Object (DAO) Pattern:
* Most of the data access and persistence process in relation to UserActivity are defined by `IUserActivityDao` and realised in `UserActivityDao`.

* **Advantages**:
    * helped decoupling the domain/business logic (i.e. users' activities) with data access with Firebase. 
    * not to expose data storage details on the interface.
    * more flexible composition of objects.
    * Clearer overall structure of codes.

#### 4) Singleton pattern
* We used the singleton pattern to ensure that all actions by the current user are performed by the same user object.
* Business logic dictates that each application process should allow only one user to log in, so we set up singletons to ensure that multiple users do not appear in the same process. In code logic, the singleton ensures that all operations have access to "user" resources.
* that is, all user operations are performed by the current user.




## Testing Summary                   [TODO]     *[What features have you tested? What is your testing coverage?]*
*Please provide some screenshots of your testing summary, showing the achieved testing coverage. Feel free to provide further details on your tests.*
*Here is an example:*
*Number of test cases: ...*
*Code coverage: ...*
*Types of tests created: ...*





## Implemented Features         [TODO]

**Part I: Basic App**
1. User must be able to login (not necessarily sign up):
 * we used Firebase Authentication to handle create account, login and logout. 
2. User must be able to load (from files or Firebase) and view posts (e.g. on a timeline activity): 
* Location: shown in a timeline after user clicks the `to Timeline` or `My posts` button, and also as a single post after clicking a post in the timeline.
* posts are loaded/created using local data instances (as described in *Data Instances* section), and viewable via the timeline.
3. Feed app with a data file with at least 1,000 valid data instances
* Please find description in the [`Data Instances` section].

**Firebase Integration**
1. Use Firebase to implement user Authentication/Authorisation. (easy): (as in Part I: Basic App, Item 1)
2. Use Firebase to persist all data used in your app. (medium)
* Please find description in the [`Data Persistence` section].

**Greater Data Usage, Handling and Sophistication**
1. Read data instances from multiple local files in different formats (JSON, CSV, and Bespoken). (easy)
 * Please find description in the [`Data model` section].
3. Use GPS information. (easy):
 * Location: `AssetHandlerFactory`.
 * Description: the user can opt to share his/her current GPS location by a button. If permission to read location is not available, the same will be reequested.
   After that, the location will be read and shown on screen, and added at to the content of the post when the user click "Create Post".


## Summary of Known Errors and Bugs

1. *Bug 1:*
 * CreatePost: If the user clicks "Create Post" too fast after clicking "share location" (from 'Not shared' to 'Shared'), the GPS may not be available with the Post.

2. *Bug 2:*
 * The linkage between pages messes up when the User click the default Android return buttons instead of the added "RETURN" button in 'Current Post'.


*Assumptions*
- The local data instances represents valid inputs.
- In case there is a collision between loaded information and/or actions, the LATER action may be superceding.
  (which should be duely avoided under real cases).
  - Invalid/Ignored Inputs: empty inputs ("") will be ignored in the Manage Profile function.
  - Invalid/Ignored Inputs: new line characters (\n) and double-quotation marks within post content will be removed.





## Team Meetings
- *[Team Meeting 1](./Meeting1_Minutes.md)*      // TODO
- *[Team Meeting 2](./Meeting2_Minutes.md)*
- *[Team Meeting 3](./Meeting3_Minutes.md)*
