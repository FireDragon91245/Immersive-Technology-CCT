package mctmods.immersivetechnology.common.util.compat.opencomputers;

import blusunrize.immersiveengineering.common.util.compat.opencomputers.ManagedEnvironmentIE;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverSidedTileEntity;
import mctmods.immersivetechnology.common.blocks.metal.tileentities.TileEntityMeltingCrucibleMaster;
import mctmods.immersivetechnology.common.blocks.metal.tileentities.TileEntityMeltingCrucibleSlave;
import mctmods.immersivetechnology.common.util.compat.cct.CCTJavaConvert;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;

public class MeltingCrucibleDriver extends DriverSidedTileEntity {
    @Override
    public Class<?> getTileEntityClass() {
        return TileEntityMeltingCrucibleSlave.class;
    }

    @Override
    public ManagedEnvironment createEnvironment(World world, BlockPos blockPos, EnumFacing enumFacing) {
        TileEntity tileEntity = world.getTileEntity(blockPos);

        if (tileEntity instanceof TileEntityMeltingCrucibleSlave) {
            TileEntityMeltingCrucibleSlave te = (TileEntityMeltingCrucibleSlave) tileEntity;
            TileEntityMeltingCrucibleMaster tem = te.master();
            if (tem != null && te.isRedstonePos()) {
                return new MeltingCrucibleEnvironment(world, tem.getPos());
            }
        }

        return null;
    }

    public static class MeltingCrucibleEnvironment extends ManagedEnvironmentIE.ManagedEnvMultiblock<TileEntityMeltingCrucibleMaster> {
        public MeltingCrucibleEnvironment(World world, BlockPos pos) {
            super(world, pos, TileEntityMeltingCrucibleMaster.class);
        }

        @SuppressWarnings("unused")
        @Callback(doc = "function():table -- gets information about the fluid tanks")
        public Object[] getTanks(Context context, Arguments args) {
            return new Object[]{
                    new HashMap<String, Object>(1) {{
                        put("output", CCTJavaConvert.FluidTankToLuaTable(getTileEntity().tanks[0]));
                    }}
            };
        }

        @SuppressWarnings("unused")
        @Callback(doc = "function():boolean -- checks if computer control is enabled")
        public Object[] getComputerControl(Context context, Arguments args) {
            return new Object[]{getTileEntity().computerOn.isPresent()};
        }

        @SuppressWarnings("unused")
        @Callback(doc = "function(enabled:boolean):nil -- enables or disables computer control")
        public Object[] setComputerControl(Context context, Arguments args) {
            return super.enableComputerControl(context, args);
        }

        @SuppressWarnings("unused")
        @Callback(doc = "function(enabled:boolean):nil -- enables or disables the machine")
        public Object[] setEnabled(Context context, Arguments args) {
            return super.setEnabled(context, args);
        }

        @SuppressWarnings("unused")
        @Callback(doc = "function():boolean -- checks if the machine is enabled")
        public Object[] getEnabled(Context context, Arguments args) {
            return new Object[]{getTileEntity().computerOn.orElse(false)};
        }

        @SuppressWarnings("unused")
        @Callback(doc = "function():table -- gets information about the input item")
        public Object[] getInputItem(Context context, Arguments args) {
            return new Object[]{CCTJavaConvert.ItemStackToLuaTable(getTileEntity().inventory.get(0))};
        }

        @Override
        public String preferredName() {
            return "it_melting_crucible";
        }

        @Override
        public int priority() {
            return 1000;
        }
    }
}
