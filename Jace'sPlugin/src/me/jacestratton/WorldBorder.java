package me.jacestratton;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.util.Vector;

public class WorldBorder implements Listener {

    public WorldBorder(JacesPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void playerRepel(PlayerMoveEvent event) {
        repel(event.getPlayer());
    }

    @EventHandler
    public void vehicleRepel(VehicleMoveEvent event) {
        repel(event.getVehicle());
    }
    
    @EventHandler
    public void blockStop(BlockPlaceEvent event) {
        Double x = Math.abs(event.getBlock().getLocation().getX());
        Double z = Math.abs(event.getBlock().getLocation().getZ());
        if (x >= 5000 || z >= 5000) {
            event.setCancelled(true);
        }
    }

    private void repel(Entity entity) {
        if (Math.abs(entity.getLocation().getX()) > 5000 || Math.abs(entity.getLocation().getZ()) > 5000) {
            if (entity.getLocation().getX() > 5000) {
                entity.setVelocity(entity.getVelocity().add(new Vector(-0.3, .1, 0)));
            }
            if (entity.getLocation().getX() < -5000) {
                entity.setVelocity(entity.getVelocity().add(new Vector(0.3, .1, 0)));
            }
            if (entity.getLocation().getZ() > 5000) {
                entity.setVelocity(entity.getVelocity().add(new Vector(0, .1, -0.3)));
            }
            if (entity.getLocation().getZ() < -5000) {
                entity.setVelocity(entity.getVelocity().add(new Vector(0, .1, 0.3)));
            }
        }
    }

}
