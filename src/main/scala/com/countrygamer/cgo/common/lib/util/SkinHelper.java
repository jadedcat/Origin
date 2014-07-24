package com.countrygamer.cgo.common.lib.util;

import com.countrygamer.cgo.common.Origin;
import com.countrygamer.cgo.common.lib.LogHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;

/**
 * @author maxpowa (permissions given)
 */
public class SkinHelper {

	private BufferedImage bufferedimage;
	private String username;
	private ResourceLocation resourceLocation;
	private DynamicTexture skin;

	public SkinHelper(String username) {
		this.username = username;
		this.resourceLocation = new ResourceLocation("skins/" + username);
	}

	public void prepareSkin() {
		this.skin = (DynamicTexture) Minecraft.getMinecraft().getTextureManager()
				.getTexture(this.resourceLocation);

		try {
			URL url = new URL(String.format("http://skins.minecraft.net/MinecraftSkins/%s.png",
					this.username));
			this.bufferedimage = ImageIO.read(url);
		} catch (Exception exception) {
			LogHelper.error(Origin.pluginName(), "Invalid " + this.username);
			this.bufferedimage = null;
			return;
		}

		if (this.skin == null) {
			this.skin = new DynamicTexture(bufferedimage.getWidth(), bufferedimage.getHeight());
			Minecraft.getMinecraft().getTextureManager()
					.loadTexture(this.resourceLocation, this.skin);
		}

		bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(),
				this.skin.getTextureData(), 0, bufferedimage.getWidth());
		this.skin.updateDynamicTexture();
	}

	public ResourceLocation getSkin() {
		return this.resourceLocation;
	}

	public float getSkinWidth() {
		return this.bufferedimage.getWidth();
	}

	public float getSkinHeight() {
		return this.bufferedimage.getHeight();
	}

	public static enum Part {
		HEAD, BODY, LEFTARM, RIGHTARM, LEFTLEG, RIGHTLEG
	}

	/**
	 * Renders a given part of a player using the given skin
	 *
	 * @param x         x coord on the screen
	 * @param y         y coord on the screen
	 * @param scale     the scale of the rendered image
	 * @param part      the part you want to render
	 * @param direction the side you want to render of the part:
	 *                  Down is Down |
	 *                  Up is Up |
	 *                  North is forward |
	 *                  South is backward |
	 *                  West is left |
	 *                  East is right |
	 * @param isArmor   whether the layer you want to render is the armor layer of the player's tex
	 * @param skin      the SkinHelper object that you are going to render (holds the player's skin)
	 */
	@SuppressWarnings("unused")
	public static void renderPart(int x, int y, float scale, Part part, ForgeDirection direction,
			boolean isArmor, SkinHelper skin) {
		// get the parts coords and size
		int[] partUVWH = SkinHelper.getPartUVWH(part, isArmor);
		// get the side the direction is for
		int side = direction.ordinal();
		int[] sideUVWH = SkinHelper.getSidedUVWH(part, side);

		int u = partUVWH[0] + sideUVWH[0];
		int v = partUVWH[1] + sideUVWH[1];
		int w = partUVWH[2] + sideUVWH[2];
		int h = partUVWH[3] + sideUVWH[3];

		GL11.glPushMatrix();
		GL11.glScalef(scale, scale, 1.0f);
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		GL11.glTranslatef(x / scale, y / scale, 1.0f);
		UtilRender.bindResource(skin.getSkin());
		SkinHelper.drawPart(x, y, u, v, w, h, skin.getSkinWidth(), skin.getSkinHeight());
		GL11.glPopMatrix();

	}

	private static int[] getPartUVWH(Part part, boolean isArmor) {
		int u, v;

		switch (part) {
			case HEAD:
				u = 0;
				v = 0;
				if (isArmor) {
					u += 32;
				}
				break;
			case BODY:
				u = 16;
				v = 16;
				if (isArmor) {
					v += 16;
				}
				break;
			case LEFTARM:
				u = 32;
				v = 48;
				if (isArmor) {
					u += 16;
				}
				break;
			case RIGHTARM:
				u = 40;
				v = 16;
				if (isArmor) {
					v += 16;
				}
				break;
			case LEFTLEG:
				u = 16;
				v = 48;
				if (isArmor) {
					u -= 16;
				}
				break;
			case RIGHTLEG:
				u = 0;
				v = 16;
				if (isArmor) {
					v += 16;
				}
				break;
			default:
				u = 0;
				v = 0;
				break;
		}

		return new int[] { u, v };
	}

	private static int[] getSidedUVWH(Part part, int side) {
		int[] uv, wh;
		if (part == Part.HEAD) {

		}
		switch (part) {
			case HEAD:
				uv = SkinHelper.getPartUVBySide(8, 8, side);
				wh = SkinHelper.getPartWHBySide(8, 8, 8, side);
				break;
			case BODY:
				uv = SkinHelper.getPartUVBySide(4, 8, side);
				wh = SkinHelper.getPartWHBySide(8, 4, 12, side);
				break;
			default:
				uv = SkinHelper.getPartUVBySide(4, 4, side);
				wh = SkinHelper.getPartWHBySide(4, 4, 12, side);
				break;
		}

		int[] uvwh = new int[4];
		uvwh[0] = uv[0];
		uvwh[1] = uv[1];
		uvwh[2] = wh[0];
		uvwh[3] = wh[1];
		return uvwh;
	}

	private static int[] getPartUVBySide(int unitSideU, int unitSideV, int forgeDirectionSide) {
		int u, v;

		switch (forgeDirectionSide) {
			case 0:
				u = unitSideU * 2;
				v = unitSideV * 0;
				break;
			case 1:
				u = unitSideU * 1;
				v = unitSideV * 0;
				break;
			case 2:
				u = unitSideU * 1;
				v = unitSideV * 1;
				break;
			case 3:
				u = unitSideU * 2;
				v = unitSideV * 1;
				break;
			case 4:
				u = unitSideU * 3;
				v = unitSideV * 1;
				break;
			case 5:
				u = unitSideU * 0;
				v = unitSideV * 1;
				break;
			default:
				u = 0;
				v = 0;
				break;
		}

		return new int[] { u, v };
	}

	private static int[] getPartWHBySide(int width, int length, int height,
			int forgeDirectionSide) {
		int w, h;

		if (forgeDirectionSide == 0 || forgeDirectionSide == 1) {
			w = width;
			h = length;
		}
		else if (forgeDirectionSide == 2 || forgeDirectionSide == 3 || forgeDirectionSide == 4
				|| forgeDirectionSide == 5) {
			w = width;
			h = height;
		}
		else {
			w = 0;
			h = 0;
		}

		return new int[] { w, h };
	}

	private static void drawPart(int x, int y, float u, float v, int width, int height,
			float imageWidth,
			float imageHeight) {
		Gui.func_146110_a(x, y, u, v, width, height, imageWidth, imageHeight);
	}

}
