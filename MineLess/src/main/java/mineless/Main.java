package mineless;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.player.PlayerInteractEntityEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Position;
import cn.nukkit.network.protocol.UpdateBlockPacket;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import task.BlockPlaceTask;

public class Main extends PluginBase implements Listener {
	DataBase db = null;

	@Override
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(this, this);
		db = new DataBase(this);
		this.getLogger().info(db.message("플러그인이 정상적으로 실행되었습니다"));
	}

	@Override
	public void onDisable() {
		db.save();
	}

	public String toString(Position pos) {
		StringBuilder str = new StringBuilder();
		str.append(pos.getFloorX()).append(":").append(pos.getFloorY()).append(":").append(pos.getFloorZ()).append(":")
				.append(pos.getLevel().getFolderName());
		return str.toString();

	}

	public Position toPosition(String s) {
		String[] str = s.split(":");
		return new Position(Integer.parseInt(str[0]), Integer.parseInt(str[1]), Integer.parseInt(str[2]),
				getServer().getLevelByName(str[3]));
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onBreak(BlockBreakEvent event) {
		if (db.isMine(event.getBlock())) {

			Block block = db.mineing(event.getBlock());
			block.setLevel(event.getBlock().getLevel());
			block.position(event.getBlock());
			//event.getPlayer().sendMessage(block.getId() + "");
			/*
			 * block.setLevel(event.getBlock().getLevel());
			 * block.position(event.getBlock()); UpdateBlockPacket pk = new
			 * UpdateBlockPacket(); pk.blockId = block.getId(); pk.blockData =
			 * block.getDamage(); pk.x = block.getFloorX(); pk.y =
			 * block.getFloorY(); pk.x = block.getFloorZ(); pk.flags =
			 * UpdateBlockPacket.FLAG_NONE;
			 * event.getPlayer().directDataPacket(pk);
			 * 
			 * event.getBlock().getLevel().setBlock(event.getBlock(), block,
			 * true, true);
			 * 
			 * BlockPlaceEvent ev = new BlockPlaceEvent(event.getPlayer(),
			 * block, block, block, new Item(1));
			 * this.getServer().getPluginManager().callEvent(ev);
			 * 
			 * event.getBlock().getLevel().setBlock(event.getBlock(), block);
			 */
			this.getServer().getScheduler().scheduleDelayedTask(
					new BlockPlaceTask(this, Block.get(block.getId(), block.getDamage(), block)), 5);
		}
	}

	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		if (event.getItem().getId() == Item.STICK) {
			db.addMine(event.getBlock());
			event.getPlayer().sendMessage(db.message(db.toString((Position) event.getBlock()) + "위치에 광산이 만들어졌습니다"));

		}
	}
}
