package no.jckf.nerf;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Iterator;

public class Nerf extends JavaPlugin implements Listener {
	private int radius;
	private int limit;

	public void onEnable() {
		radius = getConfig().getInt("radius",128);
		limit = getConfig().getInt("limit",512);

		getServer().getPluginManager().registerEvents(this,this);
	}

	public void onDisable() {

	}

	@EventHandler
	public void onExplode(EntityExplodeEvent event) {
		event.setYield(0);

		int n = event.getEntity().getNearbyEntities(radius,radius,radius).size();

		Iterator<Block> i = event.blockList().iterator();
		while (i.hasNext()) {
			Block b = i.next();

			if (b.getType() == Material.TNT && n > limit) {
				i.remove();
			}
		}
	}

	public boolean onCommand(CommandSender sender,Command command,String label,String[] args) {
		if (!sender.isOp()) {
			return true;
		}

		if (args.length < 2) {
			sender.sendMessage("Current radius and limit is " + radius + " and " + limit + ".");
			return false;
		}

		radius = Integer.parseInt(args[0]);
		limit = Integer.parseInt(args[1]);

		getConfig().set("radius",radius);
		getConfig().set("limit",limit);
		saveConfig();

		sender.sendMessage("Radius and limit changed.");
		return true;
	}
}
