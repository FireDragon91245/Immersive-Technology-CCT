package mctmods.immersivetechnology.common.util.compat.cct;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import mctmods.immersivetechnology.ImmersiveTechnology;
import mctmods.immersivetechnology.common.blocks.metal.tileentities.TileEntityGasTurbineMaster;
import mctmods.immersivetechnology.common.blocks.metal.tileentities.TileEntityGasTurbineSlave;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;

public class GasTurbinePeripheralProvider implements IPeripheralProvider {

    @Nullable
    @Override
    public IPeripheral getPeripheral(@Nonnull World world, @Nonnull BlockPos blockPos, @Nonnull EnumFacing enumFacing) {
        TileEntity entity = world.getTileEntity(blockPos);

        if(entity instanceof TileEntityGasTurbineSlave) {
            TileEntityGasTurbineSlave te = (TileEntityGasTurbineSlave) entity;
            TileEntityGasTurbineMaster tem = te.master();
            if(tem != null && te.isRedstonePos()) {
                return new GasTurbinePeripheralPeripheral(world, tem.getPos(), TileEntityGasTurbineMaster.class);
            }
        }

        return null;
    }

    private static class GasTurbinePeripheralPeripheral extends ITPeripheral.ITPeripheralMultiblock<TileEntityGasTurbineMaster> {

        protected GasTurbinePeripheralPeripheral(World w, BlockPos pos, Class<? extends TileEntityGasTurbineMaster> myClass) {
            super(w, pos, myClass);
        }

        @Nonnull
        @Override
        public String getType() {
            return String.format("%s_gasTurbine", ImmersiveTechnology.MODID);
        }

        @Nonnull
        @Override
        public String[] getMethodNames() {
            return new String[]{
                    "getSpeed",
                    "getTanks",
                    "getComputerControl",
                    "setComputerControl",
                    "setEnabled",
                    "getEnabled"
            };
        }

        @Nullable
        @Override
        public Object[] callMethod(@Nonnull IComputerAccess iComputerAccess, @Nonnull ILuaContext iLuaContext, int i, @Nonnull Object[] objects) throws LuaException {
            TileEntityGasTurbineMaster entity = getTileEntity();

            if (entity == null) {
                return null;
            }

            switch (i) {
                case 0: { // getSpeed
                    return new Object[]{
                            entity.speed
                    };
                }
                case 1: { // getTanks
                    return new Object[]{
                            new HashMap<String, Object>(2) {{
                                put("input", CCTJavaConvert.FluidTankToLuaTable(entity.tanks[0]));
                                put("output", CCTJavaConvert.FluidTankToLuaTable(entity.tanks[1]));
                            }}
                    };
                }
                case 2: { // getComputerControl
                    return new Object[]{
                            entity.computerOn.isPresent()
                    };
                }
                case 3: { // setComputerControl
                    return super.enableComputerControl(objects);
                }
                case 4: { // setEnabled
                    return super.setEnabled(objects);
                }
                case 5: { // getEnabled
                    return new Object[]{
                            entity.computerOn.orElse(false)
                    };
                }
                default:
                    return null;
            }
        }

        @Override
        public boolean equals(@Nullable IPeripheral iPeripheral) {
            if (iPeripheral instanceof GasTurbinePeripheralPeripheral) {
                GasTurbinePeripheralPeripheral other = (GasTurbinePeripheralPeripheral) iPeripheral;
                return other.getTileEntity().getPos().equals(getTileEntity().getPos());
            }
            return false;
        }
    }

}
