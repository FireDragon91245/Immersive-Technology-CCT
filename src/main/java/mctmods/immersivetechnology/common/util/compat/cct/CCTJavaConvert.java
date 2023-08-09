package mctmods.immersivetechnology.common.util.compat.cct;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import org.squiddev.cobalt.LuaString;
import org.squiddev.cobalt.LuaTable;
import org.squiddev.cobalt.ValueFactory;

import java.lang.reflect.Field;
import java.util.HashMap;

public class CCTJavaConvert {

    public static HashMap<String, Object> FluidTankToLuaTable(FluidTank t)
    {
        HashMap<String, Object> table = new HashMap<>();
        table.put("capacity", t.getCapacity());
        table.put("amount", t.getFluidAmount());
        table.put("fluid", FluidToLuaTable(t.getFluid()));
        return table;
    }

    private static HashMap<String, Object> FluidToLuaTable(FluidStack fluid) {
        if (fluid == null)
            return null;

        HashMap<String, Object> table = new HashMap<>();
        table.put("displayName", fluid.getLocalizedName());
        table.put("name", fluid.getFluid().getName());
        table.put("color", fluid.getFluid().getColor());
        return table;
    }

    public static HashMap<String, Object> ItemStackToLuaTable(ItemStack item)
    {
        HashMap<String, Object> table = new HashMap<>();
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

}
