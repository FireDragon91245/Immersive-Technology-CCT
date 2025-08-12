package mctmods.immersivetechnology.common.util.compat.cct;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import mctmods.immersivetechnology.common.blocks.metal.tileentities.TileEntityFluidPumpAlternative;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class FluidPumpPeripheralProvider implements IPeripheralProvider {
    @Nullable
    @Override
    public IPeripheral getPeripheral(@Nonnull World world, @Nonnull BlockPos blockPos, @Nonnull EnumFacing enumFacing) {
        TileEntity entity = world.getTileEntity(blockPos);

        if (entity instanceof TileEntityFluidPumpAlternative) {
            return new FluidPumpPeripheralProvider.FluidPumpPeripheral(world, blockPos, TileEntityFluidPumpAlternative.class);
        }

        return null;
    }

    private static class FluidPumpPeripheral extends ITPeripheral<TileEntityFluidPumpAlternative> {

        protected FluidPumpPeripheral(World w, BlockPos pos, Class<? extends TileEntityFluidPumpAlternative> myClass) {
            super(w, pos, myClass);
        }

        @Nonnull
        @Override
        public String getType() {
            return "it_fluidPump";
        }

        @Nonnull
        @Override
        public String[] getMethodNames() {
            return new String[]{
                    "setComputerControl",
                    "getComputerControl",
                    "setEnabled",
                    "getEnabled",
                    "getPower",
                    "getTanks"
            };
        }

        @Nullable
        @Override
        public Object[] callMethod(@Nonnull IComputerAccess iComputerAccess, @Nonnull ILuaContext iLuaContext, int i, @Nonnull Object[] objects) throws LuaException, InterruptedException {
            TileEntityFluidPumpAlternative entity = getTileEntity();
            if (entity == null) {
                throw new LuaException("TileEntity not found at " + pos);
            }

            switch (i) {
                case 0: // setComputerControl
                    return enableComputerControl(objects);
                case 1: // getComputerControl
                    return new Object[]{entity.computerOn.isPresent()};
                case 2: // setEnabled
                    return setEnabled(objects);
                case 3: // getEnabled
                    return new Object[]{entity.computerOn.orElse(false)};
                case 4: // getPower
                    return new Object[]{CCTJavaConvert.EnergyToLuaTable(entity.energyStorage)};
                case 5: // getTanks
                    return new Object[]{CCTJavaConvert.FluidTankToLuaTable(entity.tank)};
                default:
                    throw new LuaException("Unknown method index: " + i);
            }
        }

        protected Object[] enableComputerControl(Object[] args) throws LuaException {
            boolean allow = CheckArgsBool(args);
            if (allow) {
                this.getTileEntity().computerOn = Optional.of(true);
            } else {
                this.getTileEntity().computerOn = Optional.empty();
            }

            return null;
        }

        protected Object[] setEnabled(Object[] args) throws LuaException {
            boolean enabled = CheckArgsBool(args);
            TileEntityFluidPumpAlternative pump = this.getTileEntity();
            if (!pump.computerOn.isPresent()) {
                throw new LuaException("Computer control must be enabled to enable or disable the machine");
            } else {
                pump.computerOn = Optional.of(enabled);
                return null;
            }
        }

        private boolean CheckArgsBool(Object[] args) throws LuaException {
            if (args.length == 1) {
                if (Boolean.class.isAssignableFrom(args[0].getClass())) {
                    return (Boolean) args[0];
                } else {
                    throw new LuaException("Expected boolean");
                }
            } else {
                throw new LuaException("Expected 1 argument");
            }
        }

        @Override
        public boolean equals(@Nullable IPeripheral iPeripheral) {
            if (iPeripheral instanceof FluidPumpPeripheral) {
                FluidPumpPeripheral other = (FluidPumpPeripheral) iPeripheral;
                return other.getTileEntity().getPos().equals(getTileEntity().getPos());
            }
            return false;
        }
    }
}
