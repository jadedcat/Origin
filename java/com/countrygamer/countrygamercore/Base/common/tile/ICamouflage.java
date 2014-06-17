package com.countrygamer.countrygamercore.Base.common.tile;

import com.countrygamer.countrygamercore.lib.ItemMeta;

public interface ICamouflage {

	public boolean isCamouflaged();
	
	public ItemMeta getCamouflage();
	
	public void setCamouflage(ItemMeta itemMeta);
	
}
