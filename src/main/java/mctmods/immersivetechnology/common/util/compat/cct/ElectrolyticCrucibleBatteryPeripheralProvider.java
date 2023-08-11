package mctmods.immersivetechnology.common.util.compat.cct;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import mctmods.immersivetechnology.ImmersiveTechnology;
import mctmods.immersivetechnology.common.blocks.metal.tileentities.TileEntityElectrolyticCrucibleBatteryMaster;
import mctmods.immersivetechnology.common.blocks.metal.tileentities.TileEntityElectrolyticCrucibleBatteryMaster;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;

public class ElectrolyticCrucibleBatteryPeripheralProvider implements IPeripheralProvider {

    @Nullable
    @Override
    public IPeripheral getPeripheral(@Nonnull World world, @Nonnull BlockPos blockPos, @Nonnull EnumFacing enumFacing) {
        TileEntity entity = world.getTileEntity(blockPos);

        if (entity instanceof TileEntityElectrolyticCrucibleBatteryMaster) {
            return new DistillerPeripheral(world, blockPos, TileEntityElectrolyticCrucibleBatteryMaster.class);
        }

        return null;
    }

    private static class DistillerPeripheral extends ITPeripheral.ITPeripheralMultiblock<TileEntityElectrolyticCrucibleBatteryMaster> {

        protected DistillerPeripheral(World w, BlockPos pos, Class<? extends TileEntityElectrolyticCrucibleBatteryMaster> myClass) {
            super(w, pos, myClass);
        }

        @Nonnull
        @Override
        public String getType() {
            return String.format("%s_heatExchanger", ImmersiveTechnology.MODID);
        }

        @Nonnull
        @Override
        public String[] getMethodNames() {
            return new String[]{
                    "getEnergy",
                    "getTanks",
                    "getComputerControl",
                    "setComputerControl",
                    "setEnabled",
                    "getEnabled"
            };
        }

        @Nullable
        @Override
        public Object[] callMethod(@Nonnull IComputerAccess iComputerAccess, @Nonnull ILuaContext iLuaContext, int i, @Nonnull Object[] objects) throws LuaException, InterruptedException {
            TileEntityElectrolyticCrucibleBatteryMaster entity = getTileEntity();

            if (entity == null) {
                return null;
            }

            switch (i) {
                case 0:  // getEnergy
                    return new Object[]{
                            CCTJavaConvert.EnergyToLuaTable(entity.energyStorage)
                    };
                case 1:  // getTanks
                    return new Object[]{
                            new HashMap<String, Object>(4) {{
                                put("input", CCTJavaConvert.FluidTankToLuaTable(entity.tanks[0]));
                                put("output_0", CCTJavaConvert.FluidTankToLuaTable(entity.tanks[1]));
                                put("output_1", CCTJavaConvert.FluidTankToLuaTable(entity.tanks[2]));
                                put("output_2", CCTJavaConvert.FluidTankToLuaTable(entity.tanks[3]));
                            }}
                    };
                case 2:  // getComputerControl
                    return new Object[]{entity.computerOn.isPresent()};
                case 3:  // setComputerControl
                    return super.enableComputerControl(objects);
                case 4:  // setEnabled
                    return super.setEnabled(objects);
                case 5:  // getEnabled
                    return new Object[]{entity.computerOn.orElse(false)};
                default:
                    return null;
            }
        }

        @Override
        public boolean equals(@Nullable IPeripheral iPeripheral) {
            if (iPeripheral instanceof DistillerPeripheral) {
                DistillerPeripheral other = (DistillerPeripheral) iPeripheral;
                return other.getTileEntity().getPos().equals(getTileEntity().getPos());
            }
            return false;
        }
    }

}
