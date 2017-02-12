package mineless;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.item.Item;
import cn.nukkit.level.Position;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;

public class DataBase {
	private static Main plugin;

	private static DataBase instance = null;
	private static LinkedHashMap<Block, Integer> mine = new LinkedHashMap<>();
	private static LinkedHashMap<Block, Integer> nether = new LinkedHashMap<>();

	static List<String> mines = new ArrayList<>();
	static List<String> nethers = new ArrayList<>();

	@SuppressWarnings({ "deprecation", "serial", "unchecked" })
	public DataBase(Main plugin) {
		plugin.getDataFolder().mkdirs();
		Config mine = new Config(plugin.getDataFolder() + "/mine.json", Config.JSON,
				new ConfigSection(new LinkedHashMap<String, Object>() {
					{
						LinkedHashMap<String, Object> map1 = new LinkedHashMap<>();
						map1.put(DataBase.toString(Block.get(Block.DIAMOND_ORE)), 5);
						map1.put(DataBase.toString(Block.get(Block.EMERALD_ORE)), 5);
						map1.put(DataBase.toString(Block.get(Block.GOLD_ORE)), 50);
						map1.put(DataBase.toString(Block.get(Block.IRON_ORE)), 175);
						map1.put(DataBase.toString(Block.get(Block.COAL_ORE)), 375);
						put("MINE", map1);

						LinkedHashMap<String, Object> map2 = new LinkedHashMap<>();
						map2.put(DataBase.toString(Block.get(Block.OBSIDIAN)), 100);
						map2.put(DataBase.toString(Block.get(Block.GLOWSTONE_BLOCK)), 150);
						map2.put(DataBase.toString(Block.get(Block.QUARTZ_ORE)), 200);
						put("NETHER", map2);
					}

				}));
		Config positions = new Config(new File(plugin.getDataFolder(), "position.json"), Config.JSON,
				new ConfigSection(new LinkedHashMap<String, Object>() {

					{
						put("MINE", new ArrayList<String>());
						put("NETHER", new ArrayList<String>());
					}
				}));

		DataBase.mine = this.toMine((Map<String, Object>) mine.get("MINE"));
		DataBase.nether = this.toMine((Map<String, Object>) mine.get("NETHER"));

		DataBase.mines = positions.getList("MINE");
		DataBase.nethers = positions.getList("NETHER");
		DataBase.plugin = plugin;
	}

	public static Block mineing(Position pos) {
		if (mines.contains(toString(pos))) {
			int ran = new Random().nextInt(1000);
			for (Block b : mine.keySet()) {
				if (ran < mine.get(b)) {
					return b;
				}
				continue;
			}
			return Block.get(Block.COBBLESTONE);
		} else if (nethers.contains(toString(pos))) {
			int ran = new Random().nextInt(1000);
			for (Block b : nether.keySet()) {
				if (ran < nether.get(b)) {
					return b;
				}
				continue;
			}
			return Block.get(Block.NETHERRACK);
		}
		return null;
	}

	public static void addMine(Position pos) {
		DataBase.mines.add(toString(pos));
		return;
	}

	public static void addNetherMine(Position pos) {
		DataBase.nethers.add(toString(pos));
		return;
	}


	private LinkedHashMap<Block, Integer> toMine(Map<String, Object> m) {
		LinkedHashMap<Block, Integer> map = new LinkedHashMap<>();
		ArrayList<String> slist = new ArrayList<>();
		slist.addAll(m.keySet());
		Collections.sort(slist, new Comparator<String>() {
			@Override
			public int compare(String s1, String s2) {
				int i = (int) m.get(s1);
				int i2 = (int) m.get(s2);

				if (i - i2 > 0) {
					return 1;
				} else if (i - i2 == 0) {
					return 0;
				} else {
					return -1;
				}
			}
		});

		for (String s : slist) {
			map.put(toBlock(s), (Integer) m.get(s));
		}

		return map;
	}

	public static int toInteger(String str) {
		int i = 0;
		try {
			i = Integer.parseInt(str);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return i;
	}

	public static String toString(Block block) {
		if (block.getDamage() == 0) {
			return block.getId() + "";
		}
		return block.getId() + ":" + block.getDamage();
	}

	public static Block toBlock(String str) {
		if (str.contains(":")) {
			String[] strs = str.split(":");
			return Block.get(toInteger(strs[0]), toInteger(strs[1]));
		}
		return Block.get(toInteger(str));
	}

	public static String toString(Item item) {
		StringBuilder str = new StringBuilder();
		str.append(item.getId());

		if (item.getDamage() != 0) {
			str.append(":" + item.getDamage());
		}
		if (item.getCount() == 1) {
			str.append(":" + item.getCount());
		}
		return str.toString();

	}

	public static Item toItem(String str) {
		if (str.contains(":")) {
			String[] strs = str.split(":");

			switch (strs.length - 1) {
			case 1:
				return Item.get(toInteger(strs[0]), toInteger(strs[1]));
			case 2:
				return Item.get(toInteger(strs[0]), toInteger(strs[1]), toInteger(strs[2]));
			}
		}
		return Item.get(toInteger(str));

	}

	public static String toString(Position pos) {
		StringBuilder str = new StringBuilder();
		str.append(pos.getFloorX()).append(":").append(pos.getFloorY()).append(":").append(pos.getFloorZ()).append(":")
				.append(pos.getLevel().getFolderName());
		return str.toString();

	}

	public static Position toPosition(String s) {
		String[] str = s.split(":");
		return new Position(Integer.parseInt(str[0]), Integer.parseInt(str[1]), Integer.parseInt(str[2]),
				Server.getInstance().getLevelByName(str[3]));
	}

	public static String message(String message) {
		return "§a§l[알림] §r§7" + message;
	}

	public static String alert(String message) {
		return "§c§l[알림] §r§7" + message;
	}

	public static void save(){
		LinkedHashMap<String, Object> map1 = new LinkedHashMap<>();
		for(Block block : DataBase.mine.keySet()){
		map1.put(toString(block), mine.get(block));
		}
		Config config = new Config(new File(plugin.getDataFolder(),"mine.json"),Config.JSON);
		config.set("MINE", map1);
		
		for(Block block1 : DataBase.nether.keySet()){
			map1.clear();
			map1.put(toString(block1), nether.get(block1));
		}
		config.set("NETHER", map1);
		config.save();
		Config config1 = new Config(new File(plugin.getDataFolder(),"positions.json"),Config.JSON);
		config1.set("MINE", mines);
		config1.set("NETHER", nethers);
		config1.save();
	}
}
