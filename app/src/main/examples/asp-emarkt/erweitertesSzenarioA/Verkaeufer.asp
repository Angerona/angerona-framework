lager(91,100).
minPreis(30).
anzahlBegruendungenFuerPreisnachlass(1).
liefertermin(1) :- menge(X), lager(Y,_), X<Y.
liefertermin(2) :- menge(X), lager(Y,_), Y<X.
volleLager :- lager(K,L), K>90.
knappeWare :- lager(K,L), K<10.
akzeptiertesKaufangebot(X) :- volleLager, kaufangebot(X), minPreis(Y), X>Y.
startAngebot(X) :- minPreis(K), menge(_), anzahlBegruendungenFuerPreisnachlass(L), M=K/10, N=L*M, O=N+1, X=K+O.
akzeptiertesKaufangebot_trigger :- akzeptiertesKaufangebot(X), phase3.
sprechakteZaehler(X) :- #count{ C : mi_sact1(_,a_Kaeufer,_,C,_)} = X.
phase3 :- sprechakteZaehler(X), X>3.

knappeWare_trigger :- mi_sact1(t_Query,a_Kaeufer,pa_6,_,1), liefertermin(1).
