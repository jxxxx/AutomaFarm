
package _jx.AutomaFarm.client.gui.inventory;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import _jx.AutomaFarm.tileentity.TileEntityVector;
import _jx.jxLib.network.TilePacketHandler;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiVector extends GuiContainer
{
    private TileEntityVector tile;
    private final ResourceLocation loc = new ResourceLocation("boiledtofu:textures/gui/vector.png");

    public GuiVector(InventoryPlayer inv, TileEntityVector tile)
    {
	super(new ContainerVector(inv, tile));
	this.tile = tile;
	this.xSize = 230;
	this.ySize = 219;
    }

    @Override
    public void initGui()
    {
	super.initGui();
	this.buttonList.add(new GuiButton(0, 110, 58, 20, 20, I18n.func_135053_a("<")));
	this.buttonList.add(new GuiButton(1, 150, 58, 20, 20, I18n.func_135053_a(">")));
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
	//if (button.id == -2 || button.id == -1) this.mc.displayGuiScreen((GuiScreen)null);

	if (!button.enabled) return;

	switch(button.id)
	{
	case 0: tile.setTargetmeta(tile.getTargetmeta() - 1) ; break;
	case 1: tile.setTargetmeta(tile.getTargetmeta() + 1) ; break;
	}

	TilePacketHandler.sentPackets(tile);

	//this.mc.getNetHandler().addToSendQueue(TilePacketHandler.getPackets(tile));
	//this.mc.getNetHandler().addToSendQueue(tile.getDescriptionPacket());
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	this.mc.func_110434_K().func_110577_a(loc);
	int k = (this.width - this.xSize) / 2;
	int l = (this.height - this.ySize) / 2;
	this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
	this.drawCenteredString(this.fontRenderer, String.valueOf(this.tile.getTargetmeta()), 140, 63, 14737632);
    }
}
