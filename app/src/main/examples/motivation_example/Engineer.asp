tautology.

at_hq.
full.
clouds.

shelter :- at_hq.
shelter :- in_cave.

danger :- tempest.
danger :- pre_tempest.

-fill_battery :- full.
-find_shelter :- shelter.
-secure_site :- secured.

-at_hq :- at_site.
-on_the_way :- at_site.
-in_cave :- at_site.

-at_site :- at_hq.
-on_the_way :- at_hq.
-in_cave :- at_hq.

-at_site :- on_the_way.
-at_hq :- on_the_way.
-in_cave :- on_the_way.

-at_site :- in_cave.
-at_hq :- in_cave.
-on_the_way :- in_cave.

-partial :- full.
-low :- full.

-full :- partial.
-low :- partial.

-full :- low.
-partial :- low.

-sun :- clouds.
-storm_or_rain :- clouds.
-tempest :- clouds.

-clouds :- sun.
-storm_or_rain :- sun.
-tempest :- sun.

-clouds :- storm_or_rain. 
-sun :- storm_or_rain.
-tempest :- storm_or_rain.

-clouds :- tempest.
-sun :- tempest.
-storm_or_rain :- tempest.

-pre_sun :- pre_clouds.
-pre_storm_or_rain :- pre_clouds.
-pre_tempest :- pre_clouds.

-pre_clouds :- pre_sun.
-pre_storm_or_rain :- pre_sun.
-pre_tempest :- pre_sun.

-pre_clouds :- pre_storm_or_rain. 
-pre_sun :- pre_storm_or_rain.
-pre_tempest :- pre_storm_or_rain.

-pre_clouds :- pre_tempest.
-pre_sun :- pre_tempest.
-pre_storm_or_rain :- pre_tempest.