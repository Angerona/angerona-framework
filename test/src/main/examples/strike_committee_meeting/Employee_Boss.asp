blacklist :- not excused, -attend_work.
blacklist :- attend_scm.
excused :- attend_scm.
excused :- medical_appointment.
attend_scm :- not medical_appointment, ask_for_excuse.
medical_appointment :- not attend_scm, ask_for_excuse.
-attend_work :- excused.
attend_work :- not -attend_work.