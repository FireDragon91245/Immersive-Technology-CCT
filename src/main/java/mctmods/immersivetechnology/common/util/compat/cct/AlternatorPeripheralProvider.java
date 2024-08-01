package mctmods.immersivetechnology.common.util.compat.cct;

import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import mctmods.immersivetechnology.ImmersiveTechnology;
import mctmods.immersivetechnology.common.blocks.metal.tileentities.TileEntityAlternatorMaster;
import mctmods.immersivetechnology.common.blocks.metal.tileentities.TileEntityAlternatorSlave;
import mctmods.immersivetechnology.common.blocks.metal.tileentities.TileEntitySteamTurbineMaster;
import mctmods.immersivetechnology.common.blocks.metal.tileentities.TileEntitySteamTurbineSlave;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;

public class AlternatorPeripheralProvider implements IPeripheralProvider {

    @Nullable
    @Override
    public IPeripheral getPeripheral(@Nonnull World world, @Nonnull BlockPos blockPos, @Nonnull EnumFacing enumFacing) {
        TileEntity entity = world.getTileEntity(blockPos);

        if(entity instanceof TileEntityAlternatorSlave) {
            TileEntityAlternatorSlave te = (TileEntityAlternatorSlave) entity;
            TileEntityAlternatorMaster tem = te.master();
            if(tem != null) {
                return new AlternatorPeripheral(world, tem.getPos(), TileEntityAlternatorMaster.class);
            }
        }

        return null;
    }

    private static class AlternatorPeripheral extends ITPeripheral<TileEntityAlternatorMaster> {

        protected AlternatorPeripheral(World w, BlockPos pos, Class<? extends TileEntityAlternatorMaster> myClass) {
            super(w, pos, myClass);
        }

        @Nonnull
        @Override
        public String getType() {
            return String.format("%s_alternator", ImmersiveTechnology.MODID);
        }

        @Nonnull
        @Override
        public String[] getMethodNames() {
            return new String[]{
                    "getSpeed",
                    "getEnergy",
                    "getEnergyProduced"
            };
        }

        @Nullable
        @Override
        public Object[] callMethod(@Nonnull IComputerAccess iComputerAccess, @Nonnull ILuaContext iLuaContext, int i, @Nonnull Object[] objects) throws LuaException {
            TileEntityAlternatorMaster entity = getTileEntity();

            if (entity == null) {
                return null;
            }

            switch (i) {
                case 0: { // getSpeed
                    return new Object[]{
                            entity.speed
                    };
                }
                case 1: { // getEnergy
                    return new Object[]{
                            CCTJavaConvert.EnergyToLuaTable(entity.energyStorage),
                            new HashMap<String, Object>() {
                                {
                                    put("energyGenerated", entity.energyGenerated());
                                }
                            }
                    };
                }
                case 2: // getEnergyProduced
                {
                    return new Object[]{
                            entity.energyGenerated()
                    };
                }
                default:
                    return null;
            }
        }

        @Override
        public boolean equals(@Nullable IPeripheral iPeripheral) {
            if (iPeripheral instanceof AlternatorPeripheral) {
                AlternatorPeripheral other = (AlternatorPeripheral) iPeripheral;
                return other.getTileEntity().getPos().equals(getTileEntity().getPos());
            }
            return false;
        }
    }

}
