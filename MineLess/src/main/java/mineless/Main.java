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
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.player.PlayerInteractEntityEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.level.Position;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;

public class Main extends PluginBase implements Listener {
	static DataBase db = null;

	@Override
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(this, this);
		db = new DataBase(this);
		this.getLogger().info(DataBase.message("플러그인이 정상적으로 실행되었습니다"));
	}
	@Override
	public void onDisable(){
		DataBase.save();
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

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		if (event.getPlayer().isOp()) {
			event.getBlock().getLevel().setBlock(event.getBlock(), DataBase.mineing(event.getBlock()));
			
		}
		return;
	} 
	
	@EventHandler
	public void onClick(PlayerInteractEvent event){
		if (event.getItem().getCustomName().equals("mine")) {
			DataBase.addMine(event.getBlock());
			event.getPlayer().sendMessage(DataBase.message(DataBase.toString(event.getBlock())+"위치에 광산이 만들어졌습니다"));
			
		}
	}
}
