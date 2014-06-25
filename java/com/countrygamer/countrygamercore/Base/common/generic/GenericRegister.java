package com.countrygamer.countrygamercore.base.common.generic;

import net.minecraft.util.RegistryNamespaced;

public abstract class GenericRegister {
	
	//private final LinkedHashMap<String, Object> DATA = new LinkedHashMap<String, Object>();
	
	private final RegistryNamespaced DATA = new RegistryNamespaced();
	
	public abstract void registerObjects();
	
	public void registerObject(int id, String key, Object obj) {
		this.DATA.addObject(id, key, obj);
	}
	
	public Object getObject(String key) {
		return this.DATA.getObject(key);
	}
	
	public Object getObject(int id) {
		return this.DATA.getObjectById(id);
	}
	
	public RegistryNamespaced getRegister() {
		return this.DATA;
	}
	
}
