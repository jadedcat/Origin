package com.countrygamer.countrygamercore.Base.common.multiblock;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.countrygamer.countrygamercore.Base.common.tile.TileEntityBase;
import com.countrygamer.countrygamercore.Base.common.tile.TileEntityMultiBlockComponent;
import com.countrygamer.countrygamercore.Base.common.tile.TileEntityMultiBlockMaster;
import com.countrygamer.countrygamercore.lib.ItemMeta;
import com.countrygamer.countrygamercore.lib.UtilCrash;

public class MultiBlockHandler {
	
	public static final Map<String, MultiBlockHandler> handlers = new HashMap<String, MultiBlockHandler>();
	
	public static void registerMultiBlock(MultiBlockHandler handler) {
		if (MultiBlockHandler.handlers.containsKey(handler.structure.structureKey)) {
			UtilCrash.throwCrashReport("Overwriting MultiBlockHandler.",
					"Cannot over-write existing MultiBlockHandler with key "
							+ handler.structure.structureKey);
		}
		MultiBlockHandler.handlers.put(handler.structure.structureKey, handler);
	}
	
	public static MultiBlockHandler getHandler(String key) {
		return MultiBlockHandler.handlers.get(key);
	}
	
	public final MultiBlockStructure structure;
	
	public MultiBlockHandler(MultiBlockStructure structure) {
		this.structure = structure;
		
	}
	
	public boolean isValidStructure(World world, int mainX, int mainY, int mainZ, boolean checkTiles) {
		for (int[] blockCoords : this.structure.blocks.keySet()) {
			ItemMeta im = this.structure.blocks.get(blockCoords);
			Block block = im.getBlock();
			int meta = im.getMetadata();
			
			int x = mainX + blockCoords[0];
			int y = mainY + blockCoords[1];
			int z = mainZ + blockCoords[2];
			
			if (world.getBlock(x, y, z) != block && world.getBlockMetadata(x, y, z) != meta) {
				return false;
			}
		}
		if (checkTiles) {
			for (int[] blockCoords : this.structure.tiles.keySet()) {
				Class<? extends TileEntityBase> requiredTileEntity = this.structure.tiles
						.get(blockCoords);
				int x = mainX + blockCoords[0];
				int y = mainY + blockCoords[1];
				int z = mainZ + blockCoords[2];
				TileEntity tileEntity = world.getTileEntity(x, y, z);
				if (tileEntity == null
						|| !tileEntity.getClass().isAssignableFrom(requiredTileEntity)) {
					return false;
				}
			}
		}
		return true;
	}
	
	public void makeStructure(World world, int mainX, int mainY, int mainZ) {
		System.out.println("Making structure with key: " + this.structure.structureKey);
		
		TileEntityMultiBlockMaster masterTE = (TileEntityMultiBlockMaster) world.getTileEntity(
				mainX, mainY, mainZ);
		
		for (int[] tileCoords : this.structure.tiles.keySet()) {
			Class<? extends TileEntityMultiBlockComponent> tileClass = this.structure.tiles
					.get(tileCoords);
			
			int x = mainX + tileCoords[0];
			int y = mainY + tileCoords[1];
			int z = mainZ + tileCoords[2];
			
			TileEntity previousTE = world.getTileEntity(x, y, z);
			if (previousTE == null) {
				TileEntityMultiBlockComponent tileEntity = null;
				
				try {
					tileEntity = tileClass.newInstance();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				
				if (tileEntity != null) {
					tileEntity.setMasterTileEntity(masterTE);
					world.setTileEntity(x, y, z, tileEntity);
				}
			}
			else {
				if (!previousTE.getClass().isAssignableFrom(tileClass)) {
					return;
				}
			}
			
		}
		
		masterTE.hasFormedStructure(true);
		
	}
	
	public void removeStructure(World world, int mainX, int mainY, int mainZ) {
		((TileEntityMultiBlockMaster) world.getTileEntity(mainX, mainY, mainZ))
				.hasFormedStructure(false);
		
		for (int[] tileCoords : this.structure.tiles.keySet()) {
			int x = mainX + tileCoords[0];
			int y = mainY + tileCoords[1];
			int z = mainZ + tileCoords[2];
			
			world.removeTileEntity(x, y, z);
			
		}
	}
	
}
