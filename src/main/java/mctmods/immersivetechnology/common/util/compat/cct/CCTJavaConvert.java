package mctmods.immersivetechnology.common.util.compat.cct;

import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorage;
import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;

import java.util.HashMap;

public class CCTJavaConvert {

    public static HashMap<String, Object> FluidTankToLuaTable(FluidTank t)
    {
        HashMap<String, Object> table = new HashMap<>(3);
        table.put("capacity", t.getCapacity());
        table.put("amount", t.getFluidAmount());
        table.put("fluid", FluidToLuaTable(t.getFluid()));
        return table;
    }

    private static HashMap<String, Object> FluidToLuaTable(FluidStack fluid) {
        if (fluid == null)
            return null;

        HashMap<String, Object> table = new HashMap<>(3);
        table.put("displayName", fluid.getLocalizedName());
        table.put("name", FluidRegistry.getFluidName(fluid));
        table.put("mod", FluidRegistry.getModId(fluid));
        return table;
    }

    public static HashMap<String, Object> ItemStackToLuaTable(ItemStack item)
    {
        HashMap<String, Object> table = new HashMap<>(5);
        if(item.equals(ItemStack.EMPTY))
            return table;
        table.put("displayName", item.getDisplayName());
        table.put("name", item.getItem().getRegistryName());
        table.put("count", item.getCount());
        table.put("maxCount", item.getMaxStackSize());
        if(FluidUtil.getFluidContained(item) != null)
        {
            table.put("fluid", FluidToLuaTable(FluidUtil.getFluidContained(item)));
        }
        return table;
    }

    public static HashMap<String, Object> EnergyToLuaTable(FluxStorage energyStorage) {
        HashMap<String, Object> table = new HashMap<>(2);
        table.put("capacity", energyStorage.getMaxEnergyStored());
        table.put("amount", energyStorage.getEnergyStored());
        return table;
    }
}
