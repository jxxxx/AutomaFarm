
package _jx.AutomaFarm;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import _jx.AutomaFarm.client.gui.inventory.ContainerVector;
import _jx.AutomaFarm.client.gui.inventory.GuiVector;
import _jx.AutomaFarm.tileentity.TileEntityVector;
import cpw.mods.fml.common.network.IGuiHandler;

public class AtGuiHandler implements IGuiHandler
{
	public static int guiVector;

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		if(id == guiVector)
		{
			TileEntity tile = world.getBlockTileEntity(x, y, z);

			if(!(tile instanceof TileEntityVector)) return null;

			return new ContainerVector(player.inventory, (TileEntityVector)tile);
		}

		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		if(id == guiVector)
		{
			TileEntity tile = world.getBlockTileEntity(x, y, z);

			if(!(tile instanceof TileEntityVector)) return null;

			return new GuiVector(player.inventory, (TileEntityVector)tile);
		}

		return null;
	}

}
