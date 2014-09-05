low :- -energy_8, -energy_4.

-fill_battery :- energy_8, energy_4.
-secure_site :- secured.

:- at_site, at_hq.
:- at_site, on_the_way.
:- at_site, at_cave.
:- at_hq, on_the_way.
:- at_hq, at_cave.
:- on_the_way, at_cave.

:- sun, clouds.
:- sun, storm_or_rain.
:- sun, thunderstorm.
:- clouds, storm_or_rain.
:- clouds, thunderstorm.
:- storm_or_rain, thunderstorm.

:- pre_sun, pre_clouds.
:- pre_sun, pre_storm_or_rain.
:- pre_sun, pre_thunderstorm.
:- pre_clouds, pre_storm_or_rain.
:- pre_clouds, pre_thunderstorm.
:- pre_storm_or_rain, pre_thunderstorm.