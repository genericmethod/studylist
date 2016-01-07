# Studylist

A little side project help people learn lists.

Example:

Say we want to learn **the 10 basic rules of a pragmatic programmer**:

* Do not repeat yourself
* Fix broken windows
* Crash early
* Use tracer bullets
* Write shy code
* Configure, don’t integrate
* Refactor Early, refactor often
* Design to test
* If you’ve found a bug, write a test
* Know when to stop

A user subscribed to this list will receive a couple of emails through out the day with the whole list, except for an item or two.
Repeating the list and trying to recall the missing item on the list should help learn the list.

# Implementation

Current implementation is fairly simple.
The application is a scheduled task (deployed on Heroku as a worker) that performs the following tasks.
 - Randomly selects a list to be learnt from a json document
 - Sends the list with a missing entry via an email together with a link to the answer.

 # Status
 Very early prototype



