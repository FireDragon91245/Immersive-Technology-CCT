package mctmods.immersivetechnology.common.util.compat.opencomputers;

import blusunrize.immersiveengineering.common.util.compat.opencomputers.ManagedEnvironmentIE;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverSidedTileEntity;
import mctmods.immersivetechnology.common.tileentities.TileEntityFluidValve;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;

public class FluidValveDriver extends DriverSidedTileEntity {
    @Override
    public Class<?> getTileEntityClass() {
        return TileEntityFluidValve.class;
    }

    @Override
    public ManagedEnvironment createEnvironment(World world, BlockPos blockPos, EnumFacing enumFacing) {
        TileEntity tileEntity = world.getTileEntity(blockPos);

        if(tileEntity instanceof TileEntityFluidValve)
        {
            TileEntityFluidValve te = (TileEntityFluidValve)tileEntity;

            return new FluidValveEnvironment(world, te.getPos());
        }

        return null;
    }

    public static class FluidValveEnvironment extends ManagedEnvironmentIE<TileEntityFluidValve> {
        public FluidValveEnvironment(World world, BlockPos pos) {
            super(world, pos, TileEntityFluidValve.class);
        }

        @SuppressWarnings("unused")
        @Callback(doc = "function():int -- gets limit per packet in mb")
        public Object[] getMaxPerPacket(Context context, Arguments args)
        {
            return new Object[]{getTileEntity().packetLimit};
        }

        @SuppressWarnings("unused")
        @Callback(doc = "function():int -- gets limit per second in mb")
        public Object[] getMaxPerSecond(Context context, Arguments args)
        {
            return new Object[]{getTileEntity().timeLimit};
        }

        @SuppressWarnings("unused")
        @Callback(doc = "function():int -- get limit in target tank in mb")
        public Object[] getMaxTargetFluidTank(Context context, Arguments args)
        {
            return new Object[]{getTileEntity().keepSize};
        }

        @SuppressWarnings("unused")
        @Callback(doc = "function(int):nil -- sets limit per packet in mb")
        public Object[] setMaxPerPacket(Context context, Arguments args)
        {
            getTileEntity().packetLimit = args.checkInteger(0);
            return new Object[]{};
        }

        @SuppressWarnings("unused")
        @Callback(doc = "function(int):nil -- sets limit per second in mb")
        public Object[] setMaxPerSecond(Context context, Arguments args)
        {
            getTileEntity().timeLimit = args.checkInteger(0);
            return new Object[]{};
        }

        @SuppressWarnings("unused")
        @Callback(doc = "function(int):nil -- sets limit in target tank in mb")
        public Object[] setMaxTargetFluidTank(Context context, Arguments args)
        {
            getTileEntity().keepSize = args.checkInteger(0);
            return new Object[]{};
        }

        @SuppressWarnings("unused")
        @Callback(doc = "function():table -- gets average flow rates")
        public Object[] getFlowRates(Context context, Arguments args)
        {
            TileEntityFluidValve ent = getTileEntity();
            return new Object[]{
                    new HashMap<String, Object>()
                    {{
                        put("flow_last_second", ent.lastAcceptedAmount);
                        put("flow_last_average", ent.lastAverage);
                        put("flow_average", ent.average);
                    }}
            };
        }

        @Override
        public String preferredName() {
            return "it_fluid_valve";
        }

        @Override
        public int priority() {
            return 1000;
        }
    }
}
