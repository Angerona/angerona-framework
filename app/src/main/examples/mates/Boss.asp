% Scenario Description
-attend_work 		:- excused.
attend_scm 			:- not medical_appointment, ask_for_excuse.
attend_work 		:- not -attend_work.
blacklist 			:- attend_scm.
blacklist			:- not excused, -attend_work.
excused				:- medical_appointment.
medical_appointment	:- not attend_scm, ask_for_excuse.