# ezclap - Report [Cathy]


## Team Members and Roles

| UID | Name | Role |
| :--- | :----: | ---: |
| **u7323583** | Cathy Cheung | Android, Data Model, Firebase |


## Design/Introduction
(additional info for Use Case & Design)

**Canberra** - The name of our App, "Canberra" means "meeting place" in the Indigenous language Ngunnawal.
The App is intended to be a simple, user-friendly platform for people to share photos and words.

**Quotes**: shares quotes to users and non-users on the main page (`MainActivity`).
* Source: extracted via an API (https://quote-garden.herokuapp.com/api/v3/quotes/random)
* Purpose: share ideas to users, encouraging them to do the same.

## Application Design and Decisions

### Data Model
To simulate real-life user data and interaction on a social media platform, Facebook post have been used to model the `Post` and `UserActivity` objects in the Application.

#### 1) Data Instances - Sources
Thousands of Facebook posts have been scraped using the python package `facebook_scraper`.
* (Source: https://pypi.org/project/facebook-scraper/)
* Raw Post data were cleaned, and additional actions (like, delete posts) were generated based on the loaded/created Posts' data.
* Multiple data files, for posts and actions, have been produced and stored in `assets`.

#### 2) Data flow in the Demonstration App
* A total of 30 posts will be pre-loaded as "existing posts" available to the User.
* A simulation User Data will be released every 10 seconds (create posts, like posts and delete posts) from 2223 data instances.
* (Note: To better demonstrate and test our Application, about 50% of the actions are "create post" while only 3% were to "delete posts".

#### 3. CSV files were mainly relied on for the simulation (`userActions.csv`)
* Objective: Store the UserActivity data used to model the action data.
* Reasons of using CSV file:
    * the classes are of simple, non-nested structure, could be clearly modelled using CSVs
    * Reading, parsing and storing CSV (or Bespoken files) are more efficient than using JSON (or XML) files.

#### 4. Multiple Data Instances
For extensibility and flexibility, Posts could be loaded from different files formats (i.e., CSV, bespoken, Json).
* data in the posts files include 6 fields (userId, tags, postId, content, photoId, likeCounts)
* data in the userActions files include 5 fields (action, userId, tags, content and photoId) for 'create post',
  2 fields (userId, postId) for 'likes', and 2 fields (userId, postId) for 'delete post'
    - this non-nested structure is the best for firebase integration.

### Data Persistence: All the data and updates from local instances and users were also persisted using Firebase.
**Advantages:**
* Security: delegates much of the security concerns to the specialized platform, especially passwords and Personally Identifiable Information.
* efficiency and synchronization: the Realtime Database offers the possibility of synchronization, which is important in social media apps.
* compatibility: compatible with Android studio and Gradle.

**Limitations in our implementation**
* Due to limited time where proper integration and testing is not possible, real-time synchronization has not been implemented with the new features.
* A real-life use case shall involve both read and write of data via Firebase, in replacement of the local data instances.



### Design Patterns**

A wide variety of design patterns have been used (1) in the code structure and also
(2) via implementation of available packages and methods. In particular, the following design patterns have been implemented:

#### Template method and factory method: used together to handle data of different formats
* the abstract class `AssetHandler` defines the requirements in reading and parsing of files.
* the implementation of some of the tasks (e.g. `actionsFromDataInstances()`) were delegated to its subclasses, each corresponds to a file format.
* The creation of the subclasses was performed by `AssetHandlerFactory`.

**Advantages**:
* 'pluggable' for multiple sources/formats of data instances
* helped encapsulating the implementation of the standard workflow.

#### Data Access Object (DAO) Pattern:
* Most of the data access and persistence process in relation to UserActivity are defined by `IUserActivityDao` and realised in `UserActivityDao`.

**Advantages**
* helped decoupling the domain/business logic (i.e. users' activities) with data access with Firebase.
* not to expose data storage details on the interface.
* more flexible composition of objects.
* Clearer overall structure of codes.




## Implemented Features

**Part I: Basic App**
 * 1. User must be able to login (not necessarily sign up)
 * User must be able to load (from files or Firebase) and view posts (e.g. on a timeline activity)
 * Feed app with a data file with at least 1,000 valid data instances

**Firebase Integration**
1. Use Firebase to implement user Authentication/Authorisation. (easy)
2. Use Firebase to persist all data used in your app. (medium)

**Greater Data Usage, Handling and Sophistication**
1. Read data instances from multiple local files in different formats (JSON, CSV, and Bespoken). (easy)
3. Use GPS information. (easy)


## Summary of Known Errors and Bugs

1. *Bug 1:*
- CreatePost: If the user clicks "Create Post" too fast after clicking "share location"
  (from 'Not shared' to 'Shared'), the GPS may not be available with the Post.

2. *Bug 2:*
    - The linkage between pages messes up when the User click the default Android return buttons instead of the added "RETURN" button in 'Current Post'.
    -

*Assumptions*
- The local data instances represents valid inputs.
- In case there is a collision between loaded information and/or actions, the LATER action may be superceding.
  (which should be duely avoided under real cases).
  - Invalid/Ignored Inputs: empty inputs ("") will be ignored in the Manage Profile function.
  - Invalid/Ignored Inputs: new line characters (\n) and double-quotation marks within post content will be removed.


