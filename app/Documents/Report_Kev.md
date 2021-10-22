# ezclap - Report

The following is a report template to help your team successfully provide all the details necessary for your report in a structured and organised manner. Please give a straightforward and concise report that best demonstrates your project. Note that a good report will give a better impression of your project to the reviewers.

*Here are some tips to write a good report:*

* *Try to summarise and list the `bullet points` of your project as much as possible rather than give long, tedious paragraphs that mix up everything together.*

* *Try to create `diagrams` instead of text descriptions, which are more straightforward and explanatory.*

* *Try to make your report `well structured`, which is easier for the reviewers to capture the necessary information.*

*We give instructions enclosed in square brackets [...] and examples for each sections to demonstrate what are expected for your project report.*

*Please remove the instructions or examples in `italic` in your final report.*

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

## Team Members and Roles              >> saw on Kyle's

| UID | Name | Role |
| :--- | :----: | ---: |
| [u7274552] | [(Kevin)Junhao Wang] | [Tokenizer and Parser, integrated with search function] |

## Conflict Resolution Protocol        >> saw on Kyle's
 
## Application Description          >> saw on Kyle's (maybe more specific will be better, but only we have the time later)

*[What is your application, what does it do? Include photos or diagrams if necessary]*
*e.g. PetBook is a social media application specifically targetting pet owners... it provides... certified practitioners, such as veterians are indicated by a label next to their profile...*
**Application Use Cases and or Examples**
*[Provide use cases and examples of people using your application. Who are the target users of your application? How do the users use your application?]*
   *e.g.: Molly wants to inquiry about her cat, McPurr's recent troublesome behaviour*
      1. *Molly notices that McPurr has been hostile since...*
      2. *She makes a post about... with the tag...*
      3. *Lachlan, a vet, writes a reply to Molly's post...*
      4. ...
      5. *Molly gives Lachlan's reply a 'tick' response*

   *e.g. (navigation application) Targets Users: Drivers*
      * *Users can use it to navigate in order to reach the destinations.*
      * *Users can learn the traffic conditions*
      * ...
   
   *Target Users: Those who want to find some good restaurants*
   * *Users can find nearby restaurants and the application can give recommendations*
   * ...

*List all the use cases in text descriptions or create use case diagrams. Please refer to https://www.visual-paradigm.com/guide/uml-unified-modeling-language/what-is-use-case-diagram/ for use case diagram.*

## Application UML         >> Kyle

## Application Design and Decisions
*Please give *clear and concise descriptions* for each subsections of this part. 
It would be better to *list all the concrete items* for each subsection and give no more than *`5` concise, crucial reasons* of your design. 
   Example (for subsection `Data Structures`):*
      *I used the following data structures in my project:*
         1. *LinkedList*
             * *Objective: It is used for storing xxxx for xxx feature.*
             * *Locations: line xxx in XXX.java, ..., etc.*
             * *Reasons:*
                 * *It is more efficient than Arraylist for insertion with a time complexity O(1)*
                 * *We don't need to access the item by index for this feature*


**Data Structures**  *[data structures utilised, Where and why?]*       *[TODO]*

**Design Patterns** *[design patterns utilised, Where and why?]*        *[TODO]*
public abstract class AssetHandler {
- AssetHandlerFactory - factory method 
  - Template method pattern    public static void createPosts(Context ctx) {

# Search: Tokenizer and Parser

### Query Grammar

A valid query consists of at least one attribute. Different attributes could be connected with ";".

Example:

#comp2400;@postID1

Query Attributes:

1. #tagname (e.g. #comp2400) , this attribute is to find all posts with #comp2400 as their tag
2. @postID( e.g. @LP1), the postID is a unique identifier to each post, which means different post has its corresponding different unique post ID. When this attribute is put into the search query, there should be only one Post as search result

## Implementation

### Object Token

An object Token is constructed by a string and its enum type. The string represents the content of the token(not included the # or @ ). The enum Type includes TAG, AND, POSTID

### Search Tokenizer

The searchTokenizer is used to tokenize a query string (#comp2400;@postID1) when processing the query.

- method next() extracts a Token of current position and points to the next one  if method hasNext(). returns true.
- method current() returns current token

### Abstract Exp class

Create an abstract Exp class to store all query terms, PostIDExp, TagExp and AndExp to extends from the abstract class 

Exp includes a Token type and a string 

- getToken()
- getValue() return the string

example : query :

 #comp2400;@postID1

output: TagExp(Token.Type.TAG,"comp2400") AndExp(Token.Type.AND,";") PostIDExp(Token.Type.POSTID,"postID1") 

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

### Partially invalid query handling (medium feature)

- Situation 1 :  e.g. (#comp2400;@#) The first part(tag) is valid, but the second part(postID) is invalid as @ should be followed with only numbers or characters, not #
    
     For this situation. Our search query could still process the first part (#comp2400) and show the search result.
    
- Situation 2 :  e.g. (#comp2400;#comp2100) The first part(tag) is valid, but the second part(tag) is invalid as there should be only one tag as search query input according to our grammar rules.
    
    For this situation Our search query could still process the first part (#comp2400) and show the search result.
    
- Situation 3: e.g. (@postID1;@postID2) The first part(postID) is valid, but the second part(postID) is invalid as there should be only one postID as search query input according to our grammar rules.
    
    For this situation Our search query could still process the first part (#comp2400) and show the search result.

**Surpise Item**                       >> N/A
*[If you implement the surprise item, explain how your solution addresses the surprise task. What decisions do your team make in addressing the problem?]*

**Other**
*[What other design decisions have you made which you feel are relevant? Feel free to separate these into their own subheadings.]*


## Summary of Known Errors and Bugs    [TODO]
*[known errors and bugs? consequences they might lead to?]* List all the known errors and bugs here

1. *Bug 1:*
- CreatePost: GPS may not re-ask permission even if re-run (unless Emulator?)
2. *Bug 2:*
    - *{sample} A space bar (' ') in the sign in email will crash the application.*




## Testing Summary                   [TODO]     *[What features have you tested? What is your testing coverage?]*
*Please provide some screenshots of your testing summary, showing the achieved testing coverage. Feel free to provide further details on your tests.*
   *Here is an example:*
      *Number of test cases: ...*
      *Code coverage: ...*
      *Types of tests created: ...*


## Implemented Features            [TODO]     *[What features have you implemented?]*

*Part I: Basic App*
1. *User must be able to login (not necessarily sign up) *
2. *User must be able to load (from files or Firebase >> FILES) and view posts (e.g. on a timeline activity) *
4. *Feed app with a data file with at least 1,000 valid data instances*                 *[Question: "A" data file?]*
- an item can be a post or an action (like, follow, ...)

*QUESTION* retrieve data from a local file = ?        local?

Points: *DATA*
- decide BEST FORMAT for my app  *Question: ONE local file???*
- incl script & confirm can use if download data


*Firebase Integration*
1. *Use Firebase to implement user Authentication/Authorisation. (easy)*
2. *Use Firebase to persist all data used in your app (medium)* // (this item replace the requirement to retrieve data from a local file)

*Greater Data Usage, Handling and Sophistication*
1. *Read data instances from multiple local files in different formats (JSON, CSV, and Bespoken). (easy)*
   
3. *Use GPS information. (easy)*

   
   *Here is an example:*
   
   *User Privacy*
   1. *Friendship. Users may send friend requests which are then accepted or denied. (easy)*
   2. *Privacy I: A user must approve a friend's request based on privacy settings. (easy)*
   3. *Privacy II: A user can only see a profile that is Public (consider that there are at least two types of profiles: public and private). (easy)*
   4. *Privacy III: A user can only follow someone who shares at least one mutual friend based on privacy settings. (Medium)*
   *Type 2*
      ...
   
## Team Meetings
- *[Team Meeting 1](./Meeting1_Minutes.md)*      // TODO 
- *[Team Meeting 2](./Meeting2_Minutes.md)*
- *[Team Meeting 3](./Meeting3_Minutes.md)*
