maxPreis(40).
menge(5).
billigstesBestaetigtesAngebot(X) :- #min{ Y: akzeptiertesKaufangebot(Y)} = X.
billigstesBestaetigtesAngebot(X) :- startAngebot(X), not akzeptiertesKaufangebotT.
akzeptiertesKaufangebotT :- akzeptiertesKaufangebot(X).
volleLager :- liefertermin(1), not knappeWare.
volleLager :- -liefertermin(1), not liefertermin(2).
kaufangebot(X) :- volleLager, billigstesBestaetigtesAngebot(K), L=K/10, X=K-L, not kaufangebotGemacht.
billigeresKaufangebot(Y) :- kaufangebot(X), billigstesBestaetigtesAngebot(Y), X<Y.
kaufen(X) :- billigstesBestaetigtesAngebot(X), maxPreis(Y), X<Y, not billigeresKaufangebot(X).
kaufen_trigger :- kaufen(X), waitForKaeufer.
kaufangebot_trigger :- kaufangebot(X), waitForKaeufer.
waitForKaeuferTmp(X) :- #count{ C : mi_sact1(_,a_Kaeufer,_,C,_)} = X.
waitForKaeufer :- waitForKaeuferTmp(X), X>2.
kaufangebotGemacht :- mi_sact1(t_Inform, a_Verkaeufer, pa_12,_,_).

mi_has_secret(a_Verkaeufer, pa_3).
