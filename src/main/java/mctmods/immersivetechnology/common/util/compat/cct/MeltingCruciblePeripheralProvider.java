package mctmods.immersivetechnology.common.util.compat.cct;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import mctmods.immersivetechnology.ImmersiveTechnology;
import mctmods.immersivetechnology.common.blocks.metal.tileentities.TileEntityMeltingCrucibleMaster;
import mctmods.immersivetechnology.common.blocks.metal.tileentities.TileEntityMeltingCrucibleMaster;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;

public class MeltingCruciblePeripheralProvider implements IPeripheralProvider {

    @Nullable
    @Override
    public IPeripheral getPeripheral(@Nonnull World world, @Nonnull BlockPos blockPos, @Nonnull EnumFacing enumFacing) {
        TileEntity entity = world.getTileEntity(blockPos);

        if (entity instanceof TileEntityMeltingCrucibleMaster) {
            return new DistillerPeripheral(world, blockPos, TileEntityMeltingCrucibleMaster.class);
        }

        return null;
    }

    private static class DistillerPeripheral extends ITPeripheral.ITPeripheralMultiblock<TileEntityMeltingCrucibleMaster> {

        protected DistillerPeripheral(World w, BlockPos pos, Class<? extends TileEntityMeltingCrucibleMaster> myClass) {
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
                    "getTanks",
                    "getComputerControl",
                    "setComputerControl",
                    "setEnabled",
                    "getEnabled",
                    "getInputItem"
            };
        }

        @Nullable
        @Override
        public Object[] callMethod(@Nonnull IComputerAccess iComputerAccess, @Nonnull ILuaContext iLuaContext, int i, @Nonnull Object[] objects) throws LuaException, InterruptedException {
            TileEntityMeltingCrucibleMaster entity = getTileEntity();

            if (entity == null) {
                return null;
            }

            switch (i) {
                case 0: { // getTanks
                    return new Object[]{
                            new HashMap<String, Object>(1) {{
                                put("output", CCTJavaConvert.FluidTankToLuaTable(entity.tanks[0]));
                            }}
                    };
                }
                case 1: { // getComputerControl
                    return new Object[]{
                            entity.computerOn.isPresent()
                    };
                }
                case 2: { // setComputerControl
                    return super.enableComputerControl(objects);
                }
                case 3: { // setEnabled
                    return super.setEnabled(objects);
                }
                case 4: { // getEnabled
                    return new Object[]{
                            entity.computerOn.orElse(false)
                    };
                }
                case 5: { // getInputItem
                    return new Object[]{
                            CCTJavaConvert.ItemStackToLuaTable(getTileEntity().inventory.get(0))
                    };
                }
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
