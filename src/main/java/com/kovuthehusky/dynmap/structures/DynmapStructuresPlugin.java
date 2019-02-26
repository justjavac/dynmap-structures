package com.kovuthehusky.dynmap.structures;

import java.io.*;
import java.util.*;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.StructureType;
import org.bukkit.block.Biome;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.dynmap.DynmapCommonAPI;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerSet;

import static org.bukkit.StructureType.*;
import static org.bukkit.block.Biome.*;

@SuppressWarnings("unused")
public class DynmapStructuresPlugin extends JavaPlugin implements Listener {
    private static final StructureType[][] BIOMES = new StructureType[Biome.values().length][];
    private static final Map<StructureType, String> LABELS = new HashMap<StructureType, String>() {{
        put(BURIED_TREASURE, "\u57cb\u85cf\u7684\u5b9d\u85cf");
        put(DESERT_PYRAMID, "\u6c99\u6f20\u795e\u6bbf");
        put(END_CITY, "\u672b\u5730\u57ce");
        put(NETHER_FORTRESS, "\u4e0b\u754c\u8981\u585e");
        put(IGLOO, "\u96ea\u5c4b");
        put(JUNGLE_PYRAMID, "\u4e1b\u6797\u795e\u5e99");
        put(WOODLAND_MANSION, "\u6797\u5730\u5e9c\u90b8");
        put(MINESHAFT, " \u5e9f\u5f03\u77ff\u4e95");
        put(OCEAN_MONUMENT, "\u6d77\u5e95\u9057\u8ff9");
        put(OCEAN_RUIN, "\u6c34\u4e0b\u9057\u8ff9");
        put(SHIPWRECK, "\u6c89\u8239");
        put(STRONGHOLD, "\u8981\u585e");
        put(SWAMP_HUT, "\u6cbc\u6cfd\u5c0f\u5c4b");
        put(VILLAGE, "\u6751\u5e84");
    }};
    private static final Map<StructureType, Boolean> STRUCTURES = new HashMap<>();

    private MarkerAPI api;
    private MarkerSet set;
    private boolean noLabels;
    private boolean includeCoordinates;

    @Override
    public void onEnable() {
        // Set up the metrics
        new Metrics(this);
        // Set up the configuration
        this.saveDefaultConfig();
        FileConfiguration configuration = this.getConfig();
        configuration.options().copyDefaults(true);
        this.saveConfig();
        // Fill in biome data structure
        BIOMES[OCEAN.ordinal()] = new StructureType[] {BURIED_TREASURE, MINESHAFT, OCEAN_RUIN, SHIPWRECK, STRONGHOLD};
        BIOMES[PLAINS.ordinal()] =  new StructureType[] {MINESHAFT, STRONGHOLD, VILLAGE};
        BIOMES[DESERT.ordinal()] = new StructureType[] {DESERT_PYRAMID, MINESHAFT, STRONGHOLD, VILLAGE};
        BIOMES[MOUNTAINS.ordinal()] = new StructureType[] {MINESHAFT, STRONGHOLD};
        BIOMES[FOREST.ordinal()] = new StructureType[] {MINESHAFT, STRONGHOLD};
        BIOMES[TAIGA.ordinal()] = new StructureType[] {MINESHAFT, STRONGHOLD, VILLAGE};
        BIOMES[SWAMP.ordinal()] = new StructureType[] {MINESHAFT, STRONGHOLD, SWAMP_HUT};
        BIOMES[RIVER.ordinal()] = new StructureType[] {MINESHAFT, STRONGHOLD};
        BIOMES[NETHER.ordinal()] = new StructureType[] {NETHER_FORTRESS};
        BIOMES[THE_END.ordinal()] = new StructureType[] {END_CITY};
        BIOMES[FROZEN_OCEAN.ordinal()] = new StructureType[] {BURIED_TREASURE, MINESHAFT, OCEAN_RUIN, SHIPWRECK, STRONGHOLD};
        BIOMES[FROZEN_RIVER.ordinal()] = new StructureType[] {MINESHAFT, STRONGHOLD};
        BIOMES[SNOWY_TUNDRA.ordinal()] = new StructureType[] {IGLOO, MINESHAFT, STRONGHOLD, VILLAGE};
        BIOMES[SNOWY_MOUNTAINS.ordinal()] = new StructureType[] {MINESHAFT, STRONGHOLD, VILLAGE};
        BIOMES[MUSHROOM_FIELDS.ordinal()] = new StructureType[] {MINESHAFT, STRONGHOLD};
        BIOMES[MUSHROOM_FIELD_SHORE.ordinal()] = new StructureType[] {MINESHAFT, STRONGHOLD};
        BIOMES[BEACH.ordinal()] = new StructureType[] {BURIED_TREASURE, MINESHAFT, OCEAN_RUIN, SHIPWRECK, STRONGHOLD};
        BIOMES[DESERT_HILLS.ordinal()] = new StructureType[] {DESERT_PYRAMID, MINESHAFT, STRONGHOLD, VILLAGE};
        BIOMES[WOODED_HILLS.ordinal()] = new StructureType[] {MINESHAFT, STRONGHOLD};
        BIOMES[TAIGA_HILLS.ordinal()] = new StructureType[] {MINESHAFT, STRONGHOLD, VILLAGE};
        BIOMES[MOUNTAIN_EDGE.ordinal()] = new StructureType[] {MINESHAFT, STRONGHOLD};
        BIOMES[JUNGLE.ordinal()] = new StructureType[] {JUNGLE_PYRAMID, MINESHAFT, STRONGHOLD};
        BIOMES[JUNGLE_HILLS.ordinal()] = new StructureType[] {JUNGLE_PYRAMID, MINESHAFT, STRONGHOLD};
        BIOMES[JUNGLE_EDGE.ordinal()] = new StructureType[] {JUNGLE_PYRAMID, MINESHAFT, STRONGHOLD};
        BIOMES[DEEP_OCEAN.ordinal()] = new StructureType[] {BURIED_TREASURE, MINESHAFT, OCEAN_MONUMENT, OCEAN_RUIN, SHIPWRECK, STRONGHOLD};
        BIOMES[STONE_SHORE.ordinal()] = new StructureType[] {BURIED_TREASURE, MINESHAFT, OCEAN_RUIN, SHIPWRECK, STRONGHOLD};
        BIOMES[SNOWY_BEACH.ordinal()] = new StructureType[] {BURIED_TREASURE, MINESHAFT, OCEAN_RUIN, SHIPWRECK, STRONGHOLD};
        BIOMES[BIRCH_FOREST.ordinal()] = new StructureType[] {MINESHAFT, STRONGHOLD};
        BIOMES[BIRCH_FOREST_HILLS.ordinal()] = new StructureType[] {MINESHAFT, STRONGHOLD};
        BIOMES[DARK_FOREST.ordinal()] = new StructureType[] {MINESHAFT, STRONGHOLD, WOODLAND_MANSION};
        BIOMES[SNOWY_TAIGA.ordinal()] = new StructureType[] {IGLOO, MINESHAFT, STRONGHOLD, VILLAGE};
        BIOMES[SNOWY_TAIGA_HILLS.ordinal()] = new StructureType[] {MINESHAFT, STRONGHOLD, VILLAGE};
        BIOMES[GIANT_TREE_TAIGA.ordinal()] = new StructureType[] {MINESHAFT, STRONGHOLD};
        BIOMES[GIANT_TREE_TAIGA_HILLS.ordinal()] = new StructureType[] {MINESHAFT, STRONGHOLD};
        BIOMES[WOODED_MOUNTAINS.ordinal()] = new StructureType[] {MINESHAFT, STRONGHOLD};
        BIOMES[SAVANNA.ordinal()] = new StructureType[] {MINESHAFT, STRONGHOLD, VILLAGE};
        BIOMES[SAVANNA_PLATEAU.ordinal()] = new StructureType[] {MINESHAFT, STRONGHOLD, VILLAGE};
        BIOMES[BADLANDS.ordinal()] = new StructureType[] {MINESHAFT, STRONGHOLD};
        BIOMES[WOODED_BADLANDS_PLATEAU.ordinal()] = new StructureType[] {MINESHAFT, STRONGHOLD};
        BIOMES[BADLANDS_PLATEAU.ordinal()] = new StructureType[] {MINESHAFT, STRONGHOLD};
        BIOMES[SMALL_END_ISLANDS.ordinal()] = new StructureType[] {END_CITY};
        BIOMES[END_MIDLANDS.ordinal()] = new StructureType[] {END_CITY};
        BIOMES[END_HIGHLANDS.ordinal()] = new StructureType[] {END_CITY};
        BIOMES[END_BARRENS.ordinal()] = new StructureType[] {END_CITY};
        BIOMES[WARM_OCEAN.ordinal()] = new StructureType[] {BURIED_TREASURE, MINESHAFT, OCEAN_RUIN, SHIPWRECK, STRONGHOLD};
        BIOMES[LUKEWARM_OCEAN.ordinal()] = new StructureType[] {BURIED_TREASURE, MINESHAFT, OCEAN_RUIN, SHIPWRECK, STRONGHOLD};
        BIOMES[COLD_OCEAN.ordinal()] = new StructureType[] {BURIED_TREASURE, MINESHAFT, OCEAN_RUIN, SHIPWRECK, STRONGHOLD};
        BIOMES[DEEP_WARM_OCEAN.ordinal()] = new StructureType[] {BURIED_TREASURE, MINESHAFT, OCEAN_MONUMENT, OCEAN_RUIN, SHIPWRECK, STRONGHOLD};
        BIOMES[DEEP_LUKEWARM_OCEAN.ordinal()] = new StructureType[] {BURIED_TREASURE, MINESHAFT, OCEAN_MONUMENT, OCEAN_RUIN, SHIPWRECK, STRONGHOLD};
        BIOMES[DEEP_COLD_OCEAN.ordinal()] = new StructureType[] {BURIED_TREASURE, MINESHAFT, OCEAN_MONUMENT, OCEAN_RUIN, SHIPWRECK, STRONGHOLD};
        BIOMES[DEEP_FROZEN_OCEAN.ordinal()] = new StructureType[] {BURIED_TREASURE, MINESHAFT, OCEAN_MONUMENT, OCEAN_RUIN, SHIPWRECK, STRONGHOLD};
        BIOMES[THE_VOID.ordinal()] = new StructureType[] {};
        BIOMES[SUNFLOWER_PLAINS.ordinal()] = new StructureType[] {MINESHAFT, STRONGHOLD, VILLAGE};
        BIOMES[DESERT_LAKES.ordinal()] = new StructureType[] {DESERT_PYRAMID, MINESHAFT, STRONGHOLD, VILLAGE};
        BIOMES[GRAVELLY_MOUNTAINS.ordinal()] = new StructureType[] {MINESHAFT, STRONGHOLD};
        BIOMES[FLOWER_FOREST.ordinal()] = new StructureType[] {MINESHAFT, STRONGHOLD};
        BIOMES[TAIGA_MOUNTAINS.ordinal()] = new StructureType[] {MINESHAFT, STRONGHOLD, VILLAGE};
        BIOMES[SWAMP_HILLS.ordinal()] = new StructureType[] {MINESHAFT, STRONGHOLD, SWAMP_HUT};
        BIOMES[ICE_SPIKES.ordinal()] = new StructureType[] {MINESHAFT, STRONGHOLD, VILLAGE};
        BIOMES[MODIFIED_JUNGLE.ordinal()] = new StructureType[] {JUNGLE_PYRAMID, MINESHAFT, STRONGHOLD};
        BIOMES[MODIFIED_JUNGLE_EDGE.ordinal()] = new StructureType[] {JUNGLE_PYRAMID, MINESHAFT, STRONGHOLD};
        BIOMES[TALL_BIRCH_FOREST.ordinal()] = new StructureType[] {MINESHAFT, STRONGHOLD};
        BIOMES[TALL_BIRCH_HILLS.ordinal()] = new StructureType[] {MINESHAFT, STRONGHOLD};
        BIOMES[DARK_FOREST_HILLS.ordinal()] = new StructureType[] {MINESHAFT, STRONGHOLD, WOODLAND_MANSION};
        BIOMES[SNOWY_TAIGA_MOUNTAINS.ordinal()] = new StructureType[] {MINESHAFT, STRONGHOLD, VILLAGE};
        BIOMES[GIANT_SPRUCE_TAIGA.ordinal()] = new StructureType[] {MINESHAFT, STRONGHOLD};
        BIOMES[GIANT_SPRUCE_TAIGA_HILLS.ordinal()] = new StructureType[] {MINESHAFT, STRONGHOLD};
        BIOMES[MODIFIED_GRAVELLY_MOUNTAINS.ordinal()] = new StructureType[] {MINESHAFT, STRONGHOLD};
        BIOMES[SHATTERED_SAVANNA.ordinal()] = new StructureType[] {MINESHAFT, STRONGHOLD, VILLAGE};
        BIOMES[SHATTERED_SAVANNA_PLATEAU.ordinal()] = new StructureType[] {MINESHAFT, STRONGHOLD, VILLAGE};
        BIOMES[ERODED_BADLANDS.ordinal()] = new StructureType[] {MINESHAFT, STRONGHOLD};
        BIOMES[MODIFIED_WOODED_BADLANDS_PLATEAU.ordinal()] = new StructureType[] {MINESHAFT, STRONGHOLD};
        BIOMES[MODIFIED_BADLANDS_PLATEAU.ordinal()] = new StructureType[] {MINESHAFT, STRONGHOLD};
        // Fill in id and label data structures
        for (StructureType type : StructureType.getStructureTypes().values()) {
            String id = type.getName().toLowerCase(Locale.ROOT).replace("_", "");
            STRUCTURES.put(type, configuration.getBoolean("structures." + id));
            String label = configuration.getString("labels." + id);
            if (label != null) {
                LABELS.put(type, label);
            }
        }
        // Register for events
        this.getServer().getPluginManager().registerEvents(this, this);
        // Check if Dynmap is even enabled
        if (Bukkit.getPluginManager().isPluginEnabled("dynmap")) {
            // Set up our Dynmap layer
            api = ((DynmapCommonAPI) Bukkit.getPluginManager().getPlugin("dynmap")).getMarkerAPI();
            String layer = configuration.getString("layer.name");
            set = api.getMarkerSet("structures");
            if (set == null) {
                set = api.createMarkerSet("structures", layer, null, true);
            }
            set.setHideByDefault(configuration.getBoolean("layer.hidebydefault"));
            set.setLayerPriority(configuration.getInt("layer.layerprio"));
            noLabels = configuration.getBoolean("layer.noLabels");
            int minZoom = configuration.getInt("layer.minzoom");
            if (minZoom > 0) {
                set.setMinZoom(minZoom);
            }
            includeCoordinates = configuration.getBoolean("layer.inc-coord");
            // Create the marker icons
            for (StructureType type : StructureType.getStructureTypes().values()) {
                String str = type.getName().toLowerCase(Locale.ROOT).replaceAll("_", "");
                InputStream in = this.getClass().getResourceAsStream("/" + str + ".png");
                if (in != null) {
                    if (api.getMarkerIcon("structures." + str) == null) {
                        api.createMarkerIcon("structures." + str, str, in);
                    } else {
                        api.getMarkerIcon("structures." + str).setMarkerIconImage(in);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        Location location = new Location(event.getChunk().getWorld(), event.getChunk().getX() << 4, 64, event.getChunk().getZ() << 4);
        Biome biome = location.getWorld().getBiome(location.getBlockX(), location.getBlockZ());
        for (StructureType type : BIOMES[biome.ordinal()]) {
            if (STRUCTURES.get(type)) {
                Location structure = location.getWorld().locateNearestStructure(location, type, 1, false);
                if (structure != null) {
                    String id = type.getName().toLowerCase(Locale.ROOT).replace("_", "");
                    String world = structure.getWorld().getName();
                    int x = structure.getBlockX();
                    int z = structure.getBlockZ();
                    String label = "";
                    if (!noLabels) {
                        label = LABELS.get(type);
                        if (includeCoordinates) {
                            label = label + " [" + x * 16 + "," + z * 16 + "]";
                        }
                    }
                    set.createMarker(id + "," + x + "," + z, label, world, x, 64, z, api.getMarkerIcon("structures." + id), true);
                }
            }
        }
    }
}
