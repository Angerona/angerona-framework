world {
	fired :- -excused, -attend_work.
	fired :- attend_scm.
	excused :- attend_scm.
	excused :- attend_burial.
	-attend_work :- excused.
    
    attend_work.
} view->Employee {
	fired :- -excused, -attend_work.
	fired :- attend_scm.
	excused :- attend_scm.
	excused :- attend_burial.
	-attend_work :- excused.
}
