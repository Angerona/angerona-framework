maxPreis(40).
menge(5).
billigstesBestaetigtesAngebot(X) :- #min{ Y: akzeptiertesKaufangebot(Y)} = X.
billigstesBestaetigtesAngebot(X) :- startAngebot(X), not akzeptiertesKaufangebotTmp.
akzeptiertesKaufangebotTmp :- akzeptiertesKaufangebot(X).
volleLager :- liefertermin(1), not knappeWare.
volleLager :- -liefertermin(1), not liefertermin(2).
kaufangebot(X) :- volleLager, billigstesBestaetigtesAngebot(K), L=K/10, X=K-L.
billigeresKaufangebot(Y) :- kaufangebot(X), billigstesBestaetigtesAngebot(Y), X<Y.
kaufen(X) :- billigstesBestaetigtesAngebot(X), maxPreis(Y), X<Y, not billigeresKaufangebot(X).
kaufen_trigger :- kaufen(X), phase3.
kaufangebot_trigger :- kaufangebot(X), phase3.
sprechakteZaehler(X) :- #count{ C : mi_sact1(_,a_Kaeufer,_,C,_)} = X.
phase3 :- sprechakteZaehler(X), X>2.

mi_has_secret1(a_Verkaeufer, pa_3, 1).
