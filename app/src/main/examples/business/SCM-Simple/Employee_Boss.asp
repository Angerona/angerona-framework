blacklist(X)	:- attend_scm(X).
blacklist(X)	:- -attend_work(X), not excused(X).
attend_scm(X)	:- lies(X).
-attend_work(X)	:- excused(X).
lies(X)			:- -sport_club_member(X), attend_sport_event(X).
sport_club(X)	:- attend_sport_event(X), not -sport_club_member(X).
excused(X)		:- attend_sport_event(X), not lies(X).
excused(X)		:- attend_burial(X), not lies(X).

attend_work(employee) :- not -attend_work(employee).
-sport_club_member(employee).