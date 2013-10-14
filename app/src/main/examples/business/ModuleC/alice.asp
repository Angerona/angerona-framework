possible_offer(12) :- many_open_receiveables, good_order_situation.
possible_offer(11) :- -many_open_receiveables, good_order_situation.
possible_offer(10) :- many_open_receiveables, -good_order_situation.
possible_offer(8) :- -many_open_receiveables, -good_order_situation.

offer(X) :- #min{A : possible_offer(A)} = X.