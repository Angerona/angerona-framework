world {
	attend_scm.
} view->Boss {
	blacklist :- not excused, -attend_work.
	blacklist :- attend_scm.
	excused :- attend_scm.
	excused :- attend_burial.
	attend_scm :- not attend_burial, ask_for_excuse.
	attend_burial :- not attend_scm, ask_for_excuse.
	-attend_work :- excused.
	attend_work :- not -attend_work.
}