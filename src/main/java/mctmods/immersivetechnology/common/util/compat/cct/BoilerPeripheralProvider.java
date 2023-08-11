package mctmods.immersivetechnology.common.util.compat.cct;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import mctmods.immersivetechnology.ImmersiveTechnology;
import mctmods.immersivetechnology.common.blocks.metal.tileentities.TileEntityBoilerMaster;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;

public class BoilerPeripheralProvider implements IPeripheralProvider {
    @Nullable
    @Override
    public IPeripheral getPeripheral(@Nonnull World world, @Nonnull BlockPos blockPos, @Nonnull EnumFacing enumFacing) {
        TileEntity entity = world.getTileEntity(blockPos);

        if (entity instanceof TileEntityBoilerMaster) {
            return new BoilerPeripheral(world, blockPos, TileEntityBoilerMaster.class);
        }

        return null;
    }

    private static class BoilerPeripheral extends ITPeripheral.ITPeripheralMultiblock<TileEntityBoilerMaster> {

        protected BoilerPeripheral(World w, BlockPos pos, Class<? extends TileEntityBoilerMaster> myClass) {
            super(w, pos, myClass);
        }

        @Nonnull
        @Override
        public String getType() {
            return String.format("%s_boiler", ImmersiveTechnology.MODID);
        }

        @Nonnull
        @Override
        public String[] getMethodNames() {
            return new String[]{
                    "getHeat",
                    "getTanks",
                    "getComputerControl",
                    "setComputerControl",
                    "setEnabled",
                    "getEmptyCanisters",
                    "getFullCanisters",
                    "getHeatCelsius",
                    "getEnabled"
            };
        }

        @Nullable
        @Override
        public Object[] callMethod(@Nonnull IComputerAccess iComputerAccess, @Nonnull ILuaContext iLuaContext, int i, @Nonnull Object[] objects) throws LuaException {
            TileEntityBoilerMaster entity = this.getTileEntity();

            if (entity == null) {
                return null;
            }

            switch (i) {
                case 0: {// getHeats
                    return new Object[]{entity.heatLevel};
                }
                case 1: { // getTanks
                    return new Object[]{
                            new HashMap<String, Object>(3) {{
                                put("fuel", CCTJavaConvert.FluidTankToLuaTable(entity.tanks[0]));
                                put("input", CCTJavaConvert.FluidTankToLuaTable(entity.tanks[1]));
                                put("output", CCTJavaConvert.FluidTankToLuaTable(entity.tanks[2]));
                            }}
                    };
                }
                case 2: { // isComputerControl
                    return new Object[]{
                            entity.computerOn.isPresent()
                    };
                }
                case 3: { // enableComputerControl
                    return super.enableComputerControl(objects);
                }
                case 4: { // setStatus
                    return super.setEnabled(objects);
                }
                case 5: { // getEmptyCanisters
                    return new Object[]{
                            new HashMap<String, Object>(3) {{
                                put("fuel", CCTJavaConvert.ItemStackToLuaTable(getTileEntity().inventory.get(0)));
                                put("input", CCTJavaConvert.ItemStackToLuaTable(getTileEntity().inventory.get(2)));
                                put("output", CCTJavaConvert.ItemStackToLuaTable(getTileEntity().inventory.get(5)));
                            }}
                    };
                }
                case 6: { // getFullCanisters
                    return new Object[]{
                            new HashMap<String, Object>(3) {{
                                put("fuel", CCTJavaConvert.ItemStackToLuaTable(getTileEntity().inventory.get(1)));
                                put("input", CCTJavaConvert.ItemStackToLuaTable(getTileEntity().inventory.get(3)));
                                put("output", CCTJavaConvert.ItemStackToLuaTable(getTileEntity().inventory.get(4)));
                            }}
                    };
                }
                case 7: {
                    return new Object[]{(entity.heatLevel / 20) + 30};
                }
                case 8: {
                    return new Object[]{entity.computerOn.orElse(false)};
                }
                default:
                    return null;
            }
        }

        @Override
        public boolean equals(@Nullable IPeripheral iPeripheral) {
            if (iPeripheral instanceof BoilerPeripheral) {
                BoilerPeripheral other = (BoilerPeripheral) iPeripheral;
                return other.getTileEntity().getPos().equals(getTileEntity().getPos());
            }
            return false;
        }
    }
}
