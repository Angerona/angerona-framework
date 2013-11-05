asked_count(X) :- #max{S : asked(S, _)} = X.
business(Z) :- asked(Z, many_open_receiveables).
business(Z) :- asked(Z, good_order_situation).

:- business(0).
:- business(X), business(Y), X=Y+1.

many_open_receiveables.
-good_order_situation.
had_a_nice_travel.
-did_sport_last_week.

offer(10).