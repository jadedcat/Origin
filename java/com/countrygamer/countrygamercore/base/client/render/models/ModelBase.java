package com.countrygamer.countrygamercore.base.client.render.models;

import java.util.ArrayList;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * A base class for new Models. Makes new models easier to create boxes.
 * 
 * @author Country_Gamer
 * 
 */
public class ModelBase extends net.minecraft.client.model.ModelBase implements IModelBase {
	
	public static final float f5 = 0.0625F;
	
	public ArrayList<ModelRenderer> modelList = new ArrayList<ModelRenderer>();
	
	public ModelBase(int textureWidth, int textureHeight) {
		this.textureWidth = textureWidth;
		this.textureHeight = textureHeight;
		
	}
	
	public void addModel(ModelRenderer model) {
		this.modelList.add(model);
	}
	
	/**
	 * Renders this model for an entity.
	 * 
	 * @see renderModel(float)
	 */
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		this.renderModel(f5);
	}
	
	/**
	 * Renders all boxes in this model
	 * 
	 * @param f5
	 */
	public void renderModel(float f5) {
		for (ModelRenderer model : this.modelList) {
			model.render(f5);
		}
	}
	
	/**
	 * Old rotation handler
	 * 
	 * @param f
	 * @param f1
	 * @param f2
	 * @param f3
	 * @param f4
	 * @param f5
	 */
	@Deprecated
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
	}
	
	/**
	 * New rotation handler. Rotates the specified model by the passed angles
	 * 
	 * @param model
	 * @param x
	 * @param y
	 * @param z
	 */
	@Override
	public void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
	
}
