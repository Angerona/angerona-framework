world {
	blacklist :- -excused, -attend_work.
	blacklist :- attend_scm.
	excused :- attend_scm.
	excused :- attend_burial.
	attend_scm :- excused, not attend_burial.
	attend_burial :- excused, not attend_scm.
	-attend_work :- excused.    
} view->Employee {
}
