package mctmods.immersivetechnology.common.util.compat.cct;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import it.unimi.dsi.fastutil.Hash;
import mctmods.immersivetechnology.ImmersiveTechnology;
import mctmods.immersivetechnology.common.tileentities.TileEntityFluidValve;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;

public class FluidValvaProvider implements IPeripheralProvider {
    @Nullable
    @Override
    public IPeripheral getPeripheral(@Nonnull World world, @Nonnull BlockPos blockPos, @Nonnull EnumFacing enumFacing) {
        TileEntity entity = world.getTileEntity(blockPos);

        if (entity instanceof TileEntityFluidValve) {
            return new FluidValvePeripheral(world, blockPos, TileEntityFluidValve.class);
        }

        return null;
    }

    private static class FluidValvePeripheral extends ITPeripheral<TileEntityFluidValve> {

        public FluidValvePeripheral(World world, BlockPos blockPos, Class<TileEntityFluidValve> tileEntityFluidValveClass) {
            super(world, blockPos, tileEntityFluidValveClass);
        }

        @Nonnull
        @Override
        public String getType() {
            return String.format("%s_fluidValve", ImmersiveTechnology.MODID);
        }

        @Nonnull
        @Override
        public String[] getMethodNames() {
            return new String[]
                    {
                            "getMaxPerPacket",
                            "getMaxPerSecond",
                            "getMaxTargetFluidTank",
                            "setMaxPerPacket",
                            "setMaxPerSecond",
                            "setMaxTargetFluidTank",
                            "getFlowRates"
                    };
        }

        private int getIntOrTrow(Object[] objects) throws LuaException {
            if (objects.length != 1)
            {
                throw new LuaException("Expected 1 argument");
            }
            if (Double.class.isAssignableFrom(objects[0].getClass()))
            {
                return ((Double) objects[0]).intValue();
            }
            throw new LuaException("Expected Integer");
        }

        @Nullable
        @Override
        public Object[] callMethod(@Nonnull IComputerAccess iComputerAccess, @Nonnull ILuaContext iLuaContext, int i, @Nonnull Object[] objects) throws LuaException, InterruptedException {
            switch (i)
            {
                case 0: // getMaxPerPacket
                    return new Object[]{
                            getTileEntity().packetLimit
                    };
                case 1: // getMaxPerSecond
                    return new Object[]{
                            getTileEntity().timeLimit
                    };
                case 2: // getMaxTargetFluidTank
                    return new Object[]{
                            getTileEntity().keepSize
                    };
                case 3: // setMaxPerPacket
                    getTileEntity().packetLimit = getIntOrTrow(objects);
                    return new Object[]{};
                case 4: // setMaxPerSecond
                    getTileEntity().timeLimit = getIntOrTrow(objects);
                    return new Object[]{};
                case 5: // setMaxTargetFluidTank
                    getTileEntity().keepSize = getIntOrTrow(objects);
                    return new Object[]{};
                case 6: // getFlowRates
                    TileEntityFluidValve ent = getTileEntity();
                    return new Object[]{
                      new HashMap<String, Object>()
                      {{
                          put("flow_last_second", ent.lastAcceptedAmount);
                          put("flow_last_average", ent.lastAverage);
                          put("flow_average", ent.average);
                      }}
                    };
                default:
                    return null;
            }
        }

        @Override
        public boolean equals(@Nullable IPeripheral iPeripheral) {
            if (iPeripheral instanceof FluidValvePeripheral) {
                FluidValvePeripheral other = (FluidValvePeripheral) iPeripheral;
                return other.getTileEntity().getPos().equals(getTileEntity().getPos());
            }
            return false;
        }
    }
}
