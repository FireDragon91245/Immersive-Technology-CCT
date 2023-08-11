package mctmods.immersivetechnology.common.util.compat.cct;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import mctmods.immersivetechnology.ImmersiveTechnology;
import mctmods.immersivetechnology.common.blocks.metal.tileentities.TileEntitySteelSheetmetalTankMaster;
import mctmods.immersivetechnology.common.blocks.metal.tileentities.TileEntitySteelSheetmetalTankSlave;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SteelSheetmetalTankPeripheralProvider implements IPeripheralProvider {

    @Nullable
    @Override
    public IPeripheral getPeripheral(@Nonnull World world, @Nonnull BlockPos blockPos, @Nonnull EnumFacing enumFacing) {
        TileEntity entity = world.getTileEntity(blockPos);

        if (entity instanceof TileEntitySteelSheetmetalTankSlave) {
            TileEntitySteelSheetmetalTankMaster masterPos = ((TileEntitySteelSheetmetalTankSlave) entity).master();

            if (masterPos != null) {
                return new SteelSheetmetalTankPeripheral(world, masterPos.getPos(), TileEntitySteelSheetmetalTankMaster.class);
            }
        }

        return null;
    }

    private static class SteelSheetmetalTankPeripheral extends ITPeripheral<TileEntitySteelSheetmetalTankMaster> {

        protected SteelSheetmetalTankPeripheral(World w, BlockPos pos, Class<? extends TileEntitySteelSheetmetalTankMaster> myClass) {
            super(w, pos, myClass);
        }

        @Nonnull
        @Override
        public String getType() {
            return String.format("%s_steelTank", ImmersiveTechnology.MODID);
        }

        @Nonnull
        @Override
        public String[] getMethodNames() {
            return new String[]{
                    "getTank"
            };
        }

        @Nullable
        @Override
        public Object[] callMethod(@Nonnull IComputerAccess iComputerAccess, @Nonnull ILuaContext iLuaContext, int i, @Nonnull Object[] objects) throws LuaException {
            TileEntitySteelSheetmetalTankMaster entity = getTileEntity();

            if (entity == null) {
                return null;
            }

            if (i == 0) { // getTank
                return new Object[]{
                        CCTJavaConvert.FluidTankToLuaTable(entity.tank)
                };
            }
            return null;
        }

        @Override
        public boolean equals(@Nullable IPeripheral iPeripheral) {
            if (iPeripheral instanceof SteelSheetmetalTankPeripheral) {
                SteelSheetmetalTankPeripheral other = (SteelSheetmetalTankPeripheral) iPeripheral;
                return other.getTileEntity().getPos().equals(getTileEntity().getPos());
            }
            return false;
        }
    }

}
