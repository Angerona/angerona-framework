low :- -energy_8, -energy_4.

slow :- storm_or_rain.
slow :- thunderstorm.
-slow :- not slow.

-at_hq :- at_site.
-on_the_way :- at_site.
-at_cave:- at_site.
-at_site :- at_hq.
-on_the_way :- at_hq.
-at_cave :- at_hq.
-at_site :- on_the_way.
-at_hq :- on_the_way.
-at_cave:- on_the_way.
-at_site:- at_cave.
-at_hq :- at_cave.
-on_the_way :- at_cave.

-sun :- clouds.
-storm_or_rain :- clouds.
-thunderstorm:- clouds.
-clouds :- sun.
-storm_or_rain :- sun.
-thunderstorm :- sun.
-clouds :- storm_or_rain.
-sun :- storm_or_rain.
-thunderstorm:- storm_or_rain.
-clouds:- thunderstorm.
-sun :- thunderstorm.
-storm_or_rain :- thunderstorm.

-pre_sun :- pre_clouds.
-pre_storm_or_rain :- pre_clouds.
-pre_thunderstorm:- pre_clouds.
-pre_clouds :- pre_sun.
-pre_storm_or_rain :- pre_sun.
-pre_thunderstorm :- pre_sun.
-pre_clouds :- pre_storm_or_rain.
-pre_sun :- pre_storm_or_rain.
-pre_thunderstorm:- pre_storm_or_rain.
-pre_clouds:- pre_thunderstorm.
-pre_sun :- pre_thunderstorm.
-pre_storm_or_rain :- pre_thunderstorm.