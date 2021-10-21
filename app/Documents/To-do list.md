** To-Do List **
Pls Add/update items and Bugs
Add your **Name** to the items (except =your original tasks) to avoid crash     (and also remove/update status)

- If something cannot be fixed or don't want to fix it, can add to "list of known bugs" in report on Fri <==== 

***Bugs ***
* Search: Input "#" in timeline search bar > error, Quit program  Kev is working on this  
* Search: Input "# entwines" (invalid) did not show error message Kev is working on this  
* Search: Input "##" shows results if user inputted "#" (should not be tokenized?) when create post Kev is working on this  


* At "Current Post": click "back button" (back to timeline) -> click android's back button (Current post again) Stuck
* after searching something (blank input and click serach again), should show back the complete timeline?
* **MyPosts UI**: click android's built in return button - Like/delete not updated (unfixed @Friday 1am, after Kev's update)
* timeline Search Bar: height not enough to show text   (same as above, as shown in wechat photos)
* can also delete the fragments text "Home" (same)
*


_**URGENT + Fundamental**_

**Partially invalid search (for information)**
1. #correct;@not correct, 
2. #wrong → crash ,should 
3. #correct@correct
4. #wrong;@correct 
5. #wrong;@wrong



** URGENT: Bugs / incompleted new functions**

| Task | header | By |
| ------ | ------ | ------ |
| **currentPostUI** | (1) scroll up/down will also get directed into current Post? | **Kevin** |
| Like feature | confirm & align latest way it works | **Kyle** |
| **AppActivity Layout** | .. | **[TBC]]** |

**Checkings(all parts)**

| Task | header | By |
| ------ | ------ | ------ |
| **Layout (Issue in many parts)** | try to use a different emulator size, things get messed up in many places  <br /> design & change the whole thing (placed things into fragments if ok?) <br /> | **Kevin** busy, TBC |
| **testing** | all | Unit tests with report - **Juren** (Tree), [TBC: Parser tokenizer]|



** Not mandatory - improvements (N/A) **

| Task | header | By |
| ------ | ------ | ------ |
| **Use Case** | * ing | **Cathy** ok will summarize on Fri |
| **Post Input** | * Screen 'tag' input after User Input, before Storing  | N/A |
| **ProfileActivity** | * import Photos, replace photoId (before Thurs) | timesout |
| **Local Firebase** |   | timesout |




====================================================================================================
RECENTly done and pushed
* Set up the whole RBTree  @Juren
    * tested search, set up with kev
* pushed Tokenizer, Parser  @kev
* Firebase Like & delete @Cathy     (and tested 10/19)
* Layout: logIn > main screen with Create Account button, remove forgetPassword activity, timeline (minor) - C
* **ProfileActivity** now BUG here .... solved & changed into Interest & Caption - Cat
* Like, +some UI views @Kyle
* 1000 data instances: >> facebook data (in our format) <br /> + delete & like - Cathy (also added User data)
* Search function: search > tok par > show result works in UI - Kyle, and setting up with timeline/Current Post
* Dislike implemented on firebase - *Cathy*
* (2) add other long info (content, postId, userId, ...) to Current Post instead of timeline - *Cathy*
* delete post , doesnt work , post still in (mypost) !! done Juren
* **Profile Linkage**: show user Info in timeline/current post (e.g. name, caption) Cathy
* CurrentPost layout big revision, timeline minor revision & ALL layout alignment  - Cathy
* Fix minor alignment bugs - kyle
* MyPostUI copy UI - kyle
* **MEDIUM FEATURE**: partially invalid search - **Kevin** done 
* **MyPostUI** , Delete - **Juren** 
* Data Structure** **Juren** (major)
