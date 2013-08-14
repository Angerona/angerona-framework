affair_with_claire :- bob_lies.
affair_with_claire :- not claire_is_business_partner.

claire_is_business_partner :- -bob_lies.
claire_is_business_partner :- not affair_with_claire.


bob_lies :- at(bob, X), X!=restaurant.
bob_lies :- -meet(bob, claire).
-bob_lies :- at(bob, restaurant), meet(bob, claire).

at(claire, restaurant).