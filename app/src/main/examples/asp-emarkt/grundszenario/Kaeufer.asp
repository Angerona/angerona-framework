volleLager(a).
kleinerAlsLagerKap(a) :- menge(X,a), X<=20.
-kleinerAlsLagerKap(a) :- menge(X,a), X>20.
lieferTerminPasstZu(a) :- lieferTermin(X,a), kleinerAlsLagerKap(a), X<=2.
-lieferTerminPasstZu(a) :- lieferTermin(X,a), kleinerAlsLagerKap(a), X>2.
firma(a) :- -herstellung(a), lieferTerminPasstZu(a), lieferKosten(a).
-firma(a) :- kleinerAlsLagerKap(a), herstellung(a).
geringerPreis(X) :- volleLager(X), firma(X).
