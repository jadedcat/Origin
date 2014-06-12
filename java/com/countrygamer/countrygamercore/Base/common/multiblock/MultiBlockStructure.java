package com.countrygamer.countrygamercore.Base.common.multiblock;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;

import com.countrygamer.countrygamercore.Base.common.tile.TileEntityMultiBlockComponent;
import com.countrygamer.countrygamercore.lib.ItemMeta;

/**
 * 
 * TODO;
 * 	Block classes for Master and Component blocks
 * 	Passive gen
 * 	gui support
 * @author Country_Gamer
 *
 */
public class MultiBlockStructure {
	
	public final String structureKey;
	public final boolean shouldDestroyMasterTile;
	public final Map<int[], ItemMeta> blocks = new HashMap<int[], ItemMeta>();
	public final Map<int[], Class<? extends TileEntityMultiBlockComponent>> tiles = new HashMap<int[], Class<? extends TileEntityMultiBlockComponent>>();
	
	public MultiBlockStructure(String structureKey, boolean shouldDestroyMaster) {
		this.structureKey = structureKey;
		this.shouldDestroyMasterTile = shouldDestroyMaster;
		
	}
	
	public void addBlock(int x, int y, int z, Block block, int meta) {
		this.addBlock(x, y, z, block, meta, null);
	}
	
	public void addBlock(int x, int y, int z, Block block, int meta,
			Class<? extends TileEntityMultiBlockComponent> tileEntity) {
		this.blocks.put(new int[] {
				x, y, z
		}, new ItemMeta(block, meta));
		
		if (tileEntity != null) {
			this.tiles.put(new int[] {
					x, y, z
			}, tileEntity);
		}
	}
	
}
