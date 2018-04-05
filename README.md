# Dynmap-Structures [![Build Status](https://travis-ci.org/KovuTheHusky/dynmap-structures.svg?branch=master)](https://travis-ci.org/KovuTheHusky/dynmap-structures)

A Bukkit plugin that shows your world's structures (such as Villages, Strongholds, and Temples) on Dynmap.

![Dynmap-Structures](https://kovuthehusky.com/assets/dynmapstructures2.png)

## Features

* Adds markers to [Dynmap](https://dev.bukkit.org/projects/dynmap) where all structures are located.
* Currently supports the following structures in the following dimensions:
    * Overworld: [Igloo](https://minecraft.gamepedia.com/Igloo), [Woodland Mansion](https://minecraft.gamepedia.com/Woodland_mansion), [Abandoned Mineshaft](https://minecraft.gamepedia.com/Abandoned_mineshaft), [Ocean Monument](https://minecraft.gamepedia.com/Ocean_monument), [Stronghold](https://minecraft.gamepedia.com/Stronghold), [Desert Temple](https://minecraft.gamepedia.com/Desert_temple), [Jungle Temple](https://minecraft.gamepedia.com/Jungle_temple), [Witch Hut](https://minecraft.gamepedia.com/Generated_structures#Witch_hut), [Village](https://minecraft.gamepedia.com/Village).
    * Nether: [Nether Fortress](https://minecraft.gamepedia.com/Nether_fortress).
    * End: [End City](https://minecraft.gamepedia.com/End_city).
* Configure which types of structures you would like to be shown.
* Compatible with [CraftBukkit/Spigot](https://www.spigotmc.org), as well as any other [Forge](http://www.minecraftforge.net) based Bukkit-compatible server with [DynmapForge](https://minecraft.curseforge.com/projects/dynmapforge) and [DynmapCBBridge](https://minecraft.curseforge.com/projects/dynmapcbbridge) installed.
* Multi-world compatibility with plugins such as [Multiverse](https://dev.bukkit.org/projects/multiverse-core) installed.
* Includes compatibility with [Biomes O' Plenty](https://minecraft.curseforge.com/projects/biomes-o-plenty).

## Configuration

The **structures** node supports boolean values for the following keys:

**endcity**

    If true, displays End Cities on your map.

**fortress**

    If true, displays Nether Fortresses on your map.

**igloo**

    If true, displays Igloos on your map.

**mansion**

    If true, displays Woodland Mansions on your map.

**mineshaft**

    If true, displays Abandoned Mineshafts on your map. Default value is false.

**monument**

    If true, displays Ocean Monuments on your map.

**stronghold**

    If true, displays Strongholds on your map.

**temple**

    If true, displays Desert Temples and Jungle Temples on your map.

**witch**

    If true, displays Witch Huts on your map.

**village**

    If true, displays Villages on your map.

The **layer** node supports the following key-value pairs:

**name**

    A string that is used for the name of the layer. It is shown in the layer control UI element.

**hidebydefault**

    If true, the structures layer will be hidden by default.

**layerprio**

    An integer representing the layer priority in Dynmap.

**nolabels**

    If true, no labels will be shown for structures on the map.

**minzoom**

    The minimum zoom level where structures will be shown on the map.

**inc-coord**

    If true, coordinates will be included in the labels for structures.

You can also place a hash in front of any of the nodes to comment it out and disable it.

## Links

* Website: <https://kovuthehusky.com/projects#dynmapstructures>
* Example: <https://kovuthehusky.com/examples/dynmap-structures>
* Issues: <https://github.com/KovuTheHusky/dynmap-structures/issues>
* Source: <https://github.com/KovuTheHusky/dynmap-structures>
* Builds: <https://travis-ci.org/KovuTheHusky/dynmap-structures>
* Bukkit: <https://dev.bukkit.org/projects/dynmap-structures>
* Spigot: <https://www.spigotmc.org/resources/dynmap-structures.39534>
* Metrics: <https://bstats.org/plugin/bukkit/dynmap-structures>
