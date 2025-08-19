package mctmods.immersivetechnology.common.util.compat.opencomputers;

import blusunrize.immersiveengineering.common.util.compat.opencomputers.ManagedEnvironmentIE;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverSidedTileEntity;
import mctmods.immersivetechnology.common.blocks.metal.tileentities.TileEntitySteelSheetmetalTankMaster;
import mctmods.immersivetechnology.common.blocks.metal.tileentities.TileEntitySteelSheetmetalTankSlave;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SteelSheetMetalTankDriver extends DriverSidedTileEntity {
    @Override
    public Class<?> getTileEntityClass() {
        return TileEntitySteelSheetmetalTankSlave.class;
    }

    @Override
    public ManagedEnvironment createEnvironment(World world, BlockPos blockPos, EnumFacing enumFacing) {
        TileEntity tileEntity = world.getTileEntity(blockPos);

        if (tileEntity instanceof TileEntitySteelSheetmetalTankSlave) {
            TileEntitySteelSheetmetalTankSlave te = (TileEntitySteelSheetmetalTankSlave) tileEntity;
            TileEntitySteelSheetmetalTankMaster tem = te.master();
            if (tem != null) {
                return new SteelSheetmetalTankEnvironment(world, tem.getPos());
            }
        }

        return null;
    }

    public static class SteelSheetmetalTankEnvironment extends ManagedEnvironmentIE<TileEntitySteelSheetmetalTankMaster> {
        public SteelSheetmetalTankEnvironment(World world, BlockPos pos) {
            super(world, pos, TileEntitySteelSheetmetalTankMaster.class);
        }

        @SuppressWarnings("unused")
        @Callback(doc = "function():table -- gets information about the tank")
        public Object[] getTankInfo(Context context, Arguments args) {
            return new Object[]{
                    getTileEntity().tank.getInfo()
            };
        }

        @Override
        public String preferredName() {
            return "it_steel_tank";
        }

        @Override
        public int priority() {
            return 1000;
        }
    }
}
