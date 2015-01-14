lieferTermin(1,a) :- menge(X,a), X>0,X<=10.
lieferTermin(2,a) :- menge(X,a), X>10,X<=20.
lieferTermin(3,a) :- menge(X,a), X>20.
firma(a).
lieferKosten(a).
-herstellung(X) :- kleinerAlsLagerKap(X).
herstellung(X) :- -kleinerAlsLagerKap(X).
volleLager(a).
kleinerAlsLagerKap(a) :- menge(X,a), X<=20.
-kleinerAlsLagerKap(a) :- menge(X,a), X>20.
lieferTerminPasstZu(a) :- lieferTermin(X,a), kleinerAlsLagerKap(a), X<=2.
-lieferTerminPasstZu(a) :- lieferTermin(X,a), kleinerAlsLagerKap(a), X>2.
firma(a) :- -herstellung(a), lieferTerminPasstZu(a), lieferKosten(a).
-firma(a) :- kleinerAlsLagerKap(a), herstellung(a).
geringerPreis(X) :- volleLager(X), firma(X).
