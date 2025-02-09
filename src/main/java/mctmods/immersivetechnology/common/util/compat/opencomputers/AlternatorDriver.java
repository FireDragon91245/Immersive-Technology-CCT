package mctmods.immersivetechnology.common.util.compat.opencomputers;

import blusunrize.immersiveengineering.common.util.compat.opencomputers.ManagedEnvironmentIE;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverSidedTileEntity;
import mctmods.immersivetechnology.common.blocks.metal.tileentities.TileEntityAlternatorMaster;
import mctmods.immersivetechnology.common.blocks.metal.tileentities.TileEntityAlternatorSlave;
import mctmods.immersivetechnology.common.util.compat.cct.CCTJavaConvert;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;

public class AlternatorDriver extends DriverSidedTileEntity {
    @Override
    public Class<?> getTileEntityClass() {
        return TileEntityAlternatorSlave.class;
    }

    @Override
    public ManagedEnvironment createEnvironment(World world, BlockPos blockPos, EnumFacing enumFacing) {
        TileEntity tileEntity = world.getTileEntity(blockPos);

        if (tileEntity instanceof TileEntityAlternatorSlave) {
            TileEntityAlternatorSlave te = (TileEntityAlternatorSlave) tileEntity;
            TileEntityAlternatorMaster tem = te.master();

            if (tem != null) {
                return new AlternatorEnvironment(world, tem.getPos());
            }
        }

        return null;
    }

    public static class AlternatorEnvironment extends ManagedEnvironmentIE<TileEntityAlternatorMaster> {
        public AlternatorEnvironment(World world, BlockPos pos) {
            super(world, pos, TileEntityAlternatorMaster.class);
        }

        @SuppressWarnings("unused")
        @Callback(doc = "function():int -- gets how fast the alternator is spinning")
        public Object[] getSpeed(Context context, Arguments args) {
            return new Object[]{getTileEntity().speed};
        }

        @SuppressWarnings("unused")
        @Callback(doc = "function():table -- gets energy stored, energy capacity and energy produced")
        public Object[] getEnergy(Context context, Arguments args) {
            TileEntityAlternatorMaster entity = getTileEntity();
            HashMap<String, Object> result = CCTJavaConvert.EnergyToLuaTable(entity.energyStorage);
            result.put("energyGenerated", entity.energyGenerated());

            return new Object[]{
                    result
            };
        }

        @SuppressWarnings("unused")
        @Callback(doc = "function():int -- gets how much energy is generated per tick")
        public Object[] getEnergyProduced(Context context, Arguments args) {
            return new Object[]{getTileEntity().energyGenerated()};
        }

        @Override
        public String preferredName() {
            return "it_alternator";
        }

        @Override
        public int priority() {
            return 1000;
        }
    }
}
