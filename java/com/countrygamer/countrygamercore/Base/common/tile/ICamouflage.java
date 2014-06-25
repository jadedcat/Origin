package com.countrygamer.countrygamercore.base.common.tile;

import com.countrygamer.countrygamercore.common.lib.ItemMeta;

public interface ICamouflage {

	public boolean isCamouflaged();
	
	public ItemMeta getCamouflage();
	
	public void setCamouflage(ItemMeta itemMeta);
	
}
