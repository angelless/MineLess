package task;

import cn.nukkit.block.Block;
import cn.nukkit.scheduler.PluginTask;
import mineless.Main;

public class BlockPlaceTask extends PluginTask<Main> {
	Block block = null;

	public BlockPlaceTask(Main owner, Block block) {
		// TODO Auto-generated constructor stub
		super(owner);
		this.block = block;
	}

	@Override
	public void onRun(int currentTick) {
		// TODO Auto-generated method stub
		block.getLevel().setBlock(block, block);
	}
}
