# To_Do_List
Managing basic to-do list stored in a XML file
Available commands:
**********************************************************************************
**list**: lists all the tasks sharing the same attribute.

Enter keyword <list> then the attribute <-s> and the status <task status>;
which is one of three: new, in_progress, done.
Enter keyword <list> with no attributes; lists all the tasks in the xml file.

_Example: list -s done; lists the completed tasks._
**********************************************************************************
**new**: creates a new task. The status is automatically set to (new).

Enter keyword <new> then, in a new line enter the following: 
<caption>,<description>,<priority>,<dead line>;

_Example: new \ Buy grocery, Go to the store to buy stuff, 100, 2020-03-13_
**********************************************************************************
**edit**: edits a task. Requires task's id.

Enter keyword <edit> and the task id <id> then, in a new line enter the following:
<caption>,<description>,<priority>,<dead line>;

_Example: edit 2 \ Buy grocery, Go to the store to buy stuff, 100, 2020-03-13_
**********************************************************************************
**complete**: marks a task as done.  

Enter keyword <complete> and the task id <id>.

_Example: complete 4_  
**********************************************************************************
**remove**: removes a task from the list. Requires task\'s id.

Enter keyword <remove> and the task id <id>;  

_Example: remove 2_
**********************************************************************************
**exit**: exits the program.  

Enter keyword <exit>;  

_Example: exit_
