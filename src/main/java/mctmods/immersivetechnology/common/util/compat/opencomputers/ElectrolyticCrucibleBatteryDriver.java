package mctmods.immersivetechnology.common.util.compat.opencomputers;

import blusunrize.immersiveengineering.common.util.compat.opencomputers.ManagedEnvironmentIE;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverSidedTileEntity;
import mctmods.immersivetechnology.common.blocks.metal.tileentities.TileEntityElectrolyticCrucibleBatteryMaster;
import mctmods.immersivetechnology.common.blocks.metal.tileentities.TileEntityElectrolyticCrucibleBatterySlave;
import mctmods.immersivetechnology.common.util.compat.cct.CCTJavaConvert;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;

public class ElectrolyticCrucibleBatteryDriver extends DriverSidedTileEntity {
    @Override
    public Class<?> getTileEntityClass() {
        return TileEntityElectrolyticCrucibleBatterySlave.class;
    }

    @Override
    public ManagedEnvironment createEnvironment(World world, BlockPos blockPos, EnumFacing enumFacing) {
        TileEntity tileEntity = world.getTileEntity(blockPos);

        if (tileEntity instanceof TileEntityElectrolyticCrucibleBatterySlave) {
            TileEntityElectrolyticCrucibleBatterySlave te = (TileEntityElectrolyticCrucibleBatterySlave) tileEntity;
            TileEntityElectrolyticCrucibleBatteryMaster tem = te.master();
            if (tem != null && te.isRedstonePos()) {
                return new ElectrolyticCrucibleBatteryEnvironment(world, tem.getPos());
            }
        }

        return null;
    }

    public static class ElectrolyticCrucibleBatteryEnvironment extends ManagedEnvironmentIE.ManagedEnvMultiblock<TileEntityElectrolyticCrucibleBatteryMaster> {
        public ElectrolyticCrucibleBatteryEnvironment(World world, BlockPos pos) {
            super(world, pos, TileEntityElectrolyticCrucibleBatteryMaster.class);
        }

        @SuppressWarnings("unused")
        @Callback(doc = "function():table -- gets energy storage information")
        public Object[] getEnergy(Context context, Arguments args) {
            return new Object[]{getTileEntity().energyStorage};
        }

        @SuppressWarnings("unused")
        @Callback(doc = "function():table -- gets information about the fluid tanks")
        public Object[] getTanks(Context context, Arguments args) {
            TileEntityElectrolyticCrucibleBatteryMaster entity = getTileEntity();
            return new Object[]{
                    new HashMap<String, Object>(4) {{
                        put("input", CCTJavaConvert.FluidTankToLuaTable(entity.tanks[0]));
                        put("output_0", CCTJavaConvert.FluidTankToLuaTable(entity.tanks[1]));
                        put("output_1", CCTJavaConvert.FluidTankToLuaTable(entity.tanks[2]));
                        put("output_2", CCTJavaConvert.FluidTankToLuaTable(entity.tanks[3]));
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

        @Override
        public String preferredName() {
            return "it_electrolytic_crucible_battery";
        }

        @Override
        public int priority() {
            return 1000;
        }
    }
}
