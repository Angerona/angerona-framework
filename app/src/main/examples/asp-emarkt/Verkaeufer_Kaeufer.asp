% Wissen Alle
kleinerAlsLagerKap(a) :- menge(x,a).
kleinerAlsLagerKap(a) :- menge(y,a).
-kleinerAlsLagerKap(a) :- menge(z,a).
-firma(a) :- kleinerAlsLagerKap(a), herstellung(a).
firma(a) :- -herstellung(a), lieferTerminPasstZu(a), lieferKosten(a).
lieferTerminPasstZu(a) :- lieferTermin(1,a), kleinerAlsLagerKap(a).
lieferTerminPasstZu(a) :- lieferTermin(2,a), kleinerAlsLagerKap(a).
-lieferTerminPasstZu(a) :- lieferTermin(3,a), kleinerAlsLagerKap(a).
-herstellung(a) :- kleinerAlsLagerKap(a).
volleLager(a).
geringerPreis(a) :- volleLager(a), firma(a).
-geringerPreis(a) :- -firma(a).
% Hmm folgende Regel führt in Angerona zur keiner Antwortmenge -> DLV unterstützt dies
herstellung(a); -herstellung(a).
