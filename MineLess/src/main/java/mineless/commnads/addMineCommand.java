package mineless.commnads;

import java.util.LinkedHashMap;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandExecutor;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.permission.Permission;
import mineless.DataBase;
import mineless.Main;

public class addMineCommand extends Command {
	Main plugin = null;

	public addMineCommand(Main plugin) {
		super("광산", "서버내 광산을 관리합니다", DataBase.command("/광산 <추가모드|제거모드|일반모드|확률>"));
		this.plugin = plugin;

		// CommandParameter[] parameters = { new CommandParameter("추가모드",
		// CommandParameter.ARG_TYPE_STRING, true),
		// new CommandParameter("제거모드", CommandParameter.ARG_TYPE_STRING, true),
		// new CommandParameter("일반모드", CommandParameter.ARG_TYPE_STRING, true),
		// new CommandParameter("확률", CommandParameter.ARG_TYPE_STRING, true) };
		// CommandParameter[] optaion = { new CommandParameter("목록",
		// CommandParameter.ARG_TYPE_STRING, true),
		// new CommandParameter("추가", CommandParameter.ARG_TYPE_BLOCK_POS,
		// true),
		// new CommandParameter("수정", CommandParameter.ARG_TYPE_STRING, true) };
		//
		LinkedHashMap<String, CommandParameter[]> parameter = new LinkedHashMap<>();
		// parameter.put("광산", new CommandParameter[] { new
		// CommandParameter("<추가모드|제거모드|일반모드|확류설정>", true) });
		parameter.put("추가모드", new CommandParameter[] { new CommandParameter("추가모드", false) });
		parameter.put("제거모드", new CommandParameter[] { new CommandParameter("제거모드", false) });
		parameter.put("일반모드", new CommandParameter[] { new CommandParameter("일반모드", false) });
		// parameter.put("확률설정", optaion);
		// parameter.put("광산", parameters);
		this.setCommandParameters(parameter);
		this.setPermission(Permission.DEFAULT_OP);
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(this.getUsage());
			return true;

		}
		if (sender.isOp()) {
			switch (args[0]) {
			case "추가모드":
				if (Main.isMinner(sender)) {
					sender.sendMessage(DataBase.command("당신은 이미 광산 추가모드 상태입니다"));
					return true;
				}
				if (Main.isDeller(sender)) {
					sender.sendMessage(DataBase.command("당신은 광산 삭제모드입니다 일반모드로 변경 후 다시 시도해주세요"));
					return true;
				}
				sender.sendMessage(DataBase.command("\"광산추가\"모드가 되었습니다 광산을 만들고 싶은 장소를 터치하여 주세요"));
				Main.addMinner(sender);
				return true;

			case "제거모드":
				if (Main.isDeller(sender)) {
					sender.sendMessage(DataBase.command("당신은 이미 광산 삭제모드 상태입니다"));
					return true;
				}
				if (Main.isMinner(sender)) {
					sender.sendMessage(DataBase.command("당신은 광산 추가모드입니다 일반모드로 변경 후 다시 시도해주세요"));
					return true;
				}
				sender.sendMessage(DataBase.command("\"광산제거\"모드가 되었습니다 광산을 제거하고 싶은 장소를 터치하여 주세요"));
				Main.addDeller(sender);
				return true;
			case "일반모드":
				if (Main.isDeller(sender)) {
					Main.delDeller(sender);
				}
				if (Main.isMinner(sender)) {
					Main.delMinner(sender);
				}
				sender.sendMessage(DataBase.command("정상적으로 처리되었습니다"));
				return true;
			case "확률":
				sender.sendMessage(this.getUsage());
				return true;

			default:
				sender.sendMessage(this.getUsage());
				return true;
			}

		}
		sender.sendMessage(this.getUsage());
		return true;
	}

}
