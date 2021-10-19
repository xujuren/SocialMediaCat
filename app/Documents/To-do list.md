** To-Do List **
Pls Add/update items and Bugs
Add your **Name** to the items (except =your original tasks) to avoid crash     (and also remove/update status)

- If something cannot be fixed or don't want to fix it, can add to "list of known bugs" in report on Thu/Fri


_**URGENT + Fundamental**_

**MEDIUM FEATURE (or Hard/Very Hard also ok) **                              @[  ]
* Feature: Choose & implement the feature

**Bugs / incompleted new functions**
* currentPost UI: scroll up/down to see posts will also get directed into current Post @[*****]
    * Like UI: didn't change  count change at view (only +once but implemented multiple times)
    * delete UI: (1) didnt leave @Current Post, can press again and "Post deleted" (2) use default "last page" at android  
    
* delete: didn't check USER
* both: >> implement the RBTree
* search > tok par > show result

**Overall workflow(UI-to-process)**
* Local Data structure: change everything into using RBTree !

**Minor &  2nd Urgent**

* **data instances** for delete & like ......

**Revise UI (minor revisions) ** (write down which part/parts/all that you are changing)
* ProfileActivity: REMOVE "proPicUrl", replace into e.g. Location (*cuz not enough time implement profile picture), and change User class
    * now BUG here .... REMOVE if n/a by Thursday morning       [@Cathy]
* AppActivity: design it so that things could be placed into fragments?
* logIn > main screen, with Create Account button, remove forgetPassword activity & layout       [@Cathy]
* Current Post: size out of bound, and layout can finetune  a bit
* timeline: (long postId)     
    
**Checkings**
* Check UI input's validation & warning messages, and bugs
* layouts (different emulator sizes): e.g. currentpost out of boundary Pixel API 30  
* testing
   
    

** Not Urgent and Optional **

* 1000 data instances > from real data source
* Screen 'tag' input after User Input, before Storing 
* load fb



====================================================================================================
RECENTly done and pushed
    - Set up the whole RBTree  @Juren
        - tested search, set up with kev
    - pushed Tokenizer, Parser  @kev
    - Firebase Like & delete @Cathy     (and tested 10/19)
        - revised layout 
    - Like, +some UI views @Kyle
    
