affair_with_claire :- bob_lies.
affair_with_claire :- not claire_is_business_partner.

claire_is_business_partner :- -bob_lies.
claire_is_business_partner :- not affair_with_claire.

bob_lies :- at(claire, X), X!=restaurant.
bob_lies :- at(bob, X), X!=restaurant.
-bob_lies :- at(bob, restaurant).
-bob_lies :- not bob_lies, not meet(bob, claire).

meet(bob, claire) :- at(bob, X), at(claire, X).

%meet(bob, claire).
%at(bob, restaurant).


