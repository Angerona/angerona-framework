possible_offer(20).
possible_offer(15) :- cai, os.
possible_offer(15) :- cai, rap.
possible_offer(10) :- cai, os, rap.
offer(X) :- #min{A : possible_offer(A)} = X.

asked_count(X) :- #max{S : asked(S, _)} = X.

-se.
-wd(nokia).
-wd(apple).
-wd(samsung).
