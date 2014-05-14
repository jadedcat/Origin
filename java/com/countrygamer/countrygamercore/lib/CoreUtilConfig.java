package com.countrygamer.countrygamercore.lib;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.google.gson.Gson;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class CoreUtilConfig {
	
	static boolean doLog = true;
	
	public static void configurateJSON(FMLPreInitializationEvent event) {
		File recipeFile = new File(event.getModConfigurationDirectory(),
				"CGCoreRecipes.json");
		if (!recipeFile.exists())
			return;
		String jsonStr = "";
		try {
			jsonStr = new Scanner(recipeFile).useDelimiter("\\A").next();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		// jsonStr = "[{\"outputName\":\"abc\"}]";
		if (jsonStr != null && !jsonStr.equals("")) {
			Recipe[] recipes = (new Gson()).fromJson(jsonStr, Recipe[].class);
			for (Recipe r : recipes) {
				Item output = null;
				
				if (doLog) System.out.println("outputName: " + r.outputName);
				output = CoreUtilConfig.getItemFromName(r.outputName, doLog);
				
				String[] compNames = r.getComponents();
				Item[] components = null;
				boolean full_recipe = true;
				if (compNames != null) {
					if (compNames.length != 9) full_recipe = false;
					
					components = new Item[9];
					for (int i = 0; i < 9; i++) {
						if (i < compNames.length) {
							if (doLog)
								System.out.println("componetName" + i + ": "
										+ compNames[i]);
							if (!compNames[i].equals(""))
								components[i] = CoreUtilConfig.getItemFromName(
										compNames[i], doLog);
							else
								components[i] = null;
						}
						else {
							components[i] = null;
						}
					}
				}
				
				if (output != null && components != null) {
					String[] lines = new String[] {
							"   ", "   ", "   "
					};
					
					for (int i = 0; i < components.length; i++) {
						int line = i / 3;
						int lineOffset = i % 3;
						// if (doLog) System.out.println("\t\tLine" + line + " Index" + lineOffset);
						
						if (components[i] != null) {
							char[] chars = lines[line].toCharArray();
							chars[lineOffset] = Character.forDigit(i, 10);
							lines[line] = String.valueOf(chars);
						}
					}
					if (doLog) for (int i = 0; i < lines.length; i++)
						System.out.println("line" + i + ": \"" + lines[i] + "\"");
					
					List<Object> crafter = new ArrayList<Object>();
					if (full_recipe) {
						crafter.add(lines[0]);
						crafter.add(lines[1]);
						crafter.add(lines[2]);
					}
					for (int i = 0; i < 9; i++) {
						if (components[i] != null) {
							if (full_recipe) crafter.add(Character.forDigit(i, 10));
							crafter.add(components[i]);
						}
					}
					
					if (full_recipe) {
						System.out.println("Full Recipe");
						GameRegistry.addRecipe(new ItemStack(output),
								crafter.toArray());
					}
					else {
						System.out.println("Shapeless Recipe");
						GameRegistry.addShapelessRecipe(new ItemStack(output),
								crafter.toArray());
					}
				}
			}
		}
		
	}
	
	private static Item getItemFromName(String name, boolean log) {
		if (Block.blockRegistry.containsKey(name)) {
			if (log) System.out.println("\tBlock Reg. has key for " + name);
			Block outputBlock = (Block) Block.blockRegistry.getObject(name);
			return Item.getItemFromBlock(outputBlock);
		}
		else if (Item.itemRegistry.containsKey(name)) {
			if (log) System.out.println("\tItem Reg. has key for " + name);
			return (Item) Item.itemRegistry.getObject(name);
		}
		else {
			if (log) System.out.println("\tNo valid object found.");
			return null;
		}
	}
	
	public class Recipe {
		private String outputName;
		private String[] components;
		
		public void setOutputName(String str) {
			this.outputName = str;
		}
		
		public String getOutputName() {
			return this.outputName;
		}
		
		public void setComponents(String[] names) {
			this.components = names;
		}
		
		public String[] getComponents() {
			return this.components;
		}
		
	}
	
}
