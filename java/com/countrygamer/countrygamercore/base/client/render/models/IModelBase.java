package com.countrygamer.countrygamercore.base.client.render.models;

import net.minecraft.client.model.ModelRenderer;

public interface IModelBase {
	
	public void addModel(ModelRenderer model);
	
	public void setRotation(ModelRenderer model, float x, float y, float z);
	
}
