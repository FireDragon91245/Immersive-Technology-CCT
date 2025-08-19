package mctmods.immersivetechnology.common.util.compat.opencomputers;

import blusunrize.immersiveengineering.common.util.compat.opencomputers.ManagedEnvironmentIE;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverSidedTileEntity;
import mctmods.immersivetechnology.common.blocks.metal.tileentities.TileEntityFluidPumpAlternative;
import mctmods.immersivetechnology.common.util.compat.cct.CCTJavaConvert;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;

public class FluidPumpDriver extends DriverSidedTileEntity {
    @Override
    public Class<?> getTileEntityClass() {
        return TileEntityFluidPumpAlternative.class;
    }

    @Override
    public ManagedEnvironment createEnvironment(World world, BlockPos blockPos, EnumFacing enumFacing) {
        TileEntity tileEntity = world.getTileEntity(blockPos);

        if (tileEntity instanceof TileEntityFluidPumpAlternative) {
            return new FluidPumpEnvironment(world, blockPos);
        }

        return null;
    }

    public static class FluidPumpEnvironment extends ManagedEnvironmentIE<TileEntityFluidPumpAlternative> {
        public FluidPumpEnvironment(World world, BlockPos pos) {
            super(world, pos, TileEntityFluidPumpAlternative.class);
        }

        @SuppressWarnings("unused")
        @Callback(doc = "function(enabled:boolean):nil -- enables or disables computer control")
        public Object[] setComputerControl(Context context, Arguments args) {
            boolean enabled = args.checkBoolean(0);
            TileEntityFluidPumpAlternative pump = getTileEntity();
            pump.computerOn = enabled ? Optional.of(true) : Optional.empty();
            return new Object[]{};
        }

        @SuppressWarnings("unused")
        @Callback(doc = "function():boolean -- checks if computer control is enabled")
        public Object[] getComputerControl(Context context, Arguments args) {
            return new Object[]{getTileEntity().computerOn.isPresent()};
        }

        @SuppressWarnings("unused")
        @Callback(doc = "function(enabled:boolean):nil -- enables or disables the pump")
        public Object[] setEnabled(Context context, Arguments args) {
            boolean enabled = args.checkBoolean(0);
            TileEntityFluidPumpAlternative pump = getTileEntity();
            if (!pump.computerOn.isPresent()) {
                throw new IllegalStateException("Computer control must be enabled to enable or disable the pump");
            }
            pump.computerOn = Optional.of(enabled);
            return new Object[]{};
        }

        @SuppressWarnings("unused")
        @Callback(doc = "function():boolean -- checks if the pump is enabled")
        public Object[] getEnabled(Context context, Arguments args) {
            return new Object[]{getTileEntity().computerOn.orElse(false)};
        }

        @SuppressWarnings("unused")
        @Callback(doc = "function():table -- gets energy storage information")
        public Object[] getPower(Context context, Arguments args) {
            return new Object[]{CCTJavaConvert.EnergyToLuaTable(getTileEntity().energyStorage)};
        }

        @SuppressWarnings("unused")
        @Callback(doc = "function():table -- gets information about the fluid tank")
        public Object[] getTankInfo(Context context, Arguments args) {
            return new Object[]{
                    getTileEntity().tank.getInfo()
            };
        }

        @Override
        public String preferredName() {
            return "it_fluid_pump";
        }

        @Override
        public int priority() {
            return 1000;
        }
    }
}
