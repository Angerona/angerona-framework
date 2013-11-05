asked_count(X) :- #max{S : asked(S, _)} = X.
business(Z) :- asked(Z, many_open_receivables).
business(Z) :- asked(Z, good_order_situation).

:- business(0).
:- business(X), business(Y), X=Y+1.