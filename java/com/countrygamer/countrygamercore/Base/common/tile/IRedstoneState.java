package com.countrygamer.countrygamercore.Base.common.tile;

import com.countrygamer.countrygamercore.lib.RedstoneState;

/**
 * A base class for TileEntityBase which is used to discern the tile entity's {@link RedstoneState}
 * 
 * @author Country_Gamer
 * 
 */
public interface IRedstoneState {
	
	public void setRedstoneState(RedstoneState state);
	
	public RedstoneState getRedstoneState();
	
}
