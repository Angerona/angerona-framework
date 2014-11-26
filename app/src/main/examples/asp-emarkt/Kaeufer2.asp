% Wissen Alle
kleinerAlsLagerKap(a) :- menge(X,a), X<=20.
-kleinerAlsLagerKap(a) :- menge(X,a), X>20.
-firma(a) :- kleinerAlsLagerKap(a), herstellung(a).
firma(a) :- -herstellung(a), lieferTerminPasstZu(a), lieferKosten(a).
%lieferTerminPasstZu(a) :- lieferTermin(X,a), kleinerAlsLagerKap(a), X<=2.
%-lieferTerminPasstZu(a) :- lieferTermin(X,a), kleinerAlsLagerKap(a), X>2.
-herstellung(a) :- kleinerAlsLagerKap(a).
volleLager(a).
geringerPreis(a) :- volleLager(a), firma(a).
-geringerPreis(a) :- -firma(a).
% Hmm folgende Regel führt in Angerona zur keiner Antwortmenge -> DLV unterstützt dies
%herstellung(a); -herstellung(a).
