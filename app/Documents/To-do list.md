** To-Do List **
Pls Add/update items and Bugs
Add your **Name** to the items (except =your original tasks) to avoid crash     (and also remove/update status)

- If something cannot be fixed or don't want to fix it, can add to "list of known bugs" in report on Thu/Fri

***Bugs ***
* delete post , doesnt work , post still in (mypost)
* (Oct 20, 8PM canberra time)
* Search: Input "#" in timeline search bar > error, Quit program 
* Search: Input "# entwines" (invalid) did not show error message 
* Search: Input "##" shows results if user inputted "#" (should not be tokenized?) when create post
* At "Current Post": click "back button" (back to timeline) -> click android's back button (Current post again) Stuck
* after searching something (blank input and click serach again), should show back the complete timeline?


_**URGENT + Fundamental**_
| Task | header | By |
| ------ | ------ | ------ |
| **MEDIUM FEATURE** <br /> (or Hard/Very Hard also ok) | + partially invalid search | **Kevin** |


**Bugs / incompleted new functions**

| Task | header | By |
| ------ | ------ | ------ |
| **delete** | adding a section for viewing the User's own posts (need modify) | **Juren** |
| Data Structure | change everything into using RBTree ! | **Juren** |
| Like | confirm & align latest way it works | **Kyle** |


**Checkings(all parts)**

| Task | header | By |
| ------ | ------ | ------ |
| Check UI input's validation & warning messages, and bugs  |  | **TBC** |
| **Layout (Issue in many parts)** | try to use a different emulator size, things get messed up in many places  <br /> design & change the whole thing (placed things into fragments if ok?) <br /> | **Kevin** |
| **testing** | all | Unit tests - **Juren** |



** Not mandatory - improvements **

| Task | header | By |
| ------ | ------ | ------ |
| **currentPostUI** | (1) scroll up/down will also get directed into current Post? <br /> (2) delete UI bug (see above) | **Kevin** |
| **ProfileActivity** | * import Photos, replace photoId (before Thurs) | **TBC** |
| **Profile Linkage** | * show user Info in timeline/current post (e.g. name, caption) | **TBC** |
| **Post Input** | * Screen 'tag' input after User Input, before Storing  | **TBC** |
| **Local Firebase** | *load Firebase *Juren*  | **TBC** |





====================================================================================================
RECENTly done and pushed
* Set up the whole RBTree  @Juren
    * tested search, set up with kev
* pushed Tokenizer, Parser  @kev
* Firebase Like & delete @Cathy     (and tested 10/19)
* Layout: logIn > main screen with Create Account button, remove forgetPassword activity, timeline (minor)
* **ProfileActivity** now BUG here .... solved & changed into Interest & Caption
* Like, +some UI views @Kyle
* 1000 data instances: >> facebook data (in our format) <br /> + delete & like - Cathy (also added User data)
* Search function: search > tok par > show result works in UI - Kyle
* Dislike implemented on firebase - *Cathy*
* (2) add other long info (content, postId, userId, ...) to Current Post instead of timeline - done
