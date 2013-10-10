os.
rap.
cai.

asked_out :- se.
asked_out :- -se.

asked_count(X) :- #max{S : asked(S, _)} = X.

want_acquire :- asked(_, os), asked(_, cai).
want_acquire :- asked(_, rap), asked(_, cai).
