package com.airplanegobrr.blockbreakerblockplacer.events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dispenser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.block.data.Directional;

import com.airplanegobrr.blockbreakerblockplacer.Blockbreakerblockplacer;

public class onBlockDispense implements Listener {
    private final Blockbreakerblockplacer main;

    public onBlockDispense(Blockbreakerblockplacer main) {
        this.main = main;
    }

    @EventHandler
    public void BlockDispenseEvent(BlockDispenseEvent event) {
        main.getLogger().info("Block dispensed!");
        Block block = event.getBlock();
        Location loc = block.getLocation();
        String xyzName = (loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ());
        ItemStack item = event.getItem();
        Material itemType = item.getType();
        Dispenser dispenser = (Dispenser) block.getState();

        // Config stuff
        main.getConfig().addDefault("debug", false);
        boolean debug = main.getConfig().getBoolean("debug");

        main.getConfig().addDefault("blockBreakers." + xyzName, false);
        boolean bbEnabled = main.getConfig().getBoolean("blockBreakers." + xyzName);

        main.getConfig().addDefault("blockPlacers." + xyzName, false);
        boolean bpEnabled = main.getConfig().getBoolean("blockPlacers." + xyzName);

        // Real stuff

        // blockbreaker
        if (bbEnabled) {
            if (debug) {
                main.getLogger().info(item.toString());
                main.getLogger().info(item.getType().toString());
                main.getLogger().info(item.getType().name());
                main.getLogger().info(xyzName);
            }

            if (item.getType().toString().contains("PICKAXE")) {
                main.getLogger().info("Pickaxe detected!");
                BlockFace bsFace = ((Directional) block.getBlockData()).getFacing();
                Block bsBlock = block.getRelative(bsFace);
                Material bsMaterial = bsBlock.getType();
                // if the material is not bedrock then break it
                if (bsMaterial != Material.BEDROCK) {
                    bsBlock.breakNaturally();
                    event.setCancelled(true);
                }
            }
        }

        // blockplacer
        if (bpEnabled) {
            if (debug) {
                main.getLogger().info(item.toString());
                main.getLogger().info(item.getType().toString());
                main.getLogger().info(item.getType().name());
                main.getLogger().info(xyzName);
            }

            if (itemType.isBlock()){
                BlockFace bsFace = ((Directional) block.getBlockData()).getFacing();
                Block bsBlock = block.getRelative(bsFace);
                Material bsMaterial = bsBlock.getType();
                
                if (bsMaterial == Material.AIR || bsMaterial == Material.WATER || bsMaterial == Material.LAVA) {
                    bsBlock.setType(itemType);
                    dispenser.getInventory().removeItem(item);
                    event.setCancelled(true);
                }
            }
        }

    }
}
