package com.countrygamer.countrygamercore.Base.common.tile;

import net.minecraft.nbt.NBTTagCompound;

import com.countrygamer.countrygamercore.Base.common.multiblock.MultiBlockHandler;

public class TileEntityMultiBlockMaster extends TileEntityTankBase {
	
	private String handlerKey;
	
	private boolean hasFullStructure;
	
	public TileEntityMultiBlockMaster(String name, String multiBlockHandlerKey, int tankSize,
			int inventorySize, int maxStackSize) {
		super(name, tankSize, inventorySize, maxStackSize);
		this.handlerKey = multiBlockHandlerKey;
		this.hasFullStructure = false;
		
	}
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		
		MultiBlockHandler handler = MultiBlockHandler.getHandler(this.handlerKey);
		
		if (this.isFullyFormed()) {
			if (!handler.isValidStructure(this.getWorldObj(), this.xCoord, this.yCoord, this.zCoord,
					true)) {
				System.out.println("Remove the structure");
				handler.removeStructure(this.getWorldObj(), this.xCoord, this.yCoord, this.zCoord);
			}
		}
		else {
			if (handler.isValidStructure(this.getWorldObj(), this.xCoord, this.yCoord, this.zCoord,
					true)) {
				System.out.println("Make the structure");
				handler.makeStructure(this.getWorldObj(), this.xCoord, this.yCoord, this.zCoord);
			}
		}
		
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCom) {
		super.writeToNBT(tagCom);
		
		tagCom.setString("handlerKey", this.handlerKey);
		tagCom.setBoolean("fullyFormed", this.hasFullStructure);
		
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCom) {
		super.readFromNBT(tagCom);
		
		this.handlerKey = tagCom.getString("handlerKey");
		this.hasFullStructure = tagCom.getBoolean("fullyFormed");
		
	}
	
	public String getHandlerKey() {
		return this.handlerKey;
	}
	
	public boolean isFullyFormed() {
		return this.hasFullStructure;
	}
	
	public void hasFormedStructure(boolean formed) {
		this.hasFullStructure = formed;
	}
	
}
