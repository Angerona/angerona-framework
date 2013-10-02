possible_offer(20).
possible_offer(10) :- cai, os.
possible_offer(10) :- cai, rap.
possible_offer(15) :- cai, os, rap.
offer(X) :- #min{A : possible_offer(A)} = X.

-se.
-wd(nokia).
-wd(apple).
-wd(samsung).
