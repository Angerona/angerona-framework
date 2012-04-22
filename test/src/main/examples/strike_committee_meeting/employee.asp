world {
	fired :- -excused, -attend_work.
	fired :- attend_scm.
	excused :- attend_scm.
	excused :- attend_burial.
	-attend_work :- excused.
} view->Boss {
	fired :- -excused, -attend_work.
	fired :- attend_scm.
	excused :- attend_scm.
	excused :- attend_burial.
	-attend_work :- excused.
}