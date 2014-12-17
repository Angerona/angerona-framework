lager(9,100).
minPreis(30).
anzahlBegruendungenFuerPreisnachlass(1).
liefertermin(1) :- menge(X), lager(Y,_), X<Y.
liefertermin(2) :- menge(X), lager(Y,_), Y<X.
volleLager :- lager(K,L), M=L-K, N=M*100, O=N/L, O<10.
knappeWare :- lager(K,L), M=L-K, N=M*100, O=N/L, O>90.
akzeptiertesKaufangebot(X) :- volleLager, kaufangebot(X), minPreis(Y), X>Y.
startAngebot(X) :- minPreis(K), menge(_), anzahlBegruendungenFuerPreisnachlass(L), M=K/10, N=L*M, O=N+1, X=K+O.
akzeptiertesKaufangebot_trigger :- akzeptiertesKaufangebot(X), waitForKaeufer.
waitForKaeuferTmp(X) :- #count{ C : mi_sact1(_,a_Kaeufer,_,C,_)} = X.
waitForKaeufer :- waitForKaeuferTmp(X), X>3.

knappeWare.
