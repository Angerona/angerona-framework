world {
	attend_scm.
	meet_with(andre).
} view->Boss {
	fired :- -excused, -attend_work.
	fired :- attend_scm.
	excused :- attend_scm.
	excused :- attend_burial.
	attend_scm :- excused, not attend_burial.
	attend_burial :- excused, not attend_scm.
	-attend_work :- excused.    
    -attend_work.
}