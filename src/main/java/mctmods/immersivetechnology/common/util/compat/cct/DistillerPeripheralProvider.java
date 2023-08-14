package mctmods.immersivetechnology.common.util.compat.cct;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import mctmods.immersivetechnology.ImmersiveTechnology;
import mctmods.immersivetechnology.common.blocks.metal.tileentities.TileEntityDistillerMaster;
import mctmods.immersivetechnology.common.blocks.metal.tileentities.TileEntityDistillerSlave;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;

public class DistillerPeripheralProvider implements IPeripheralProvider {

    @Nullable
    @Override
    public IPeripheral getPeripheral(@Nonnull World world, @Nonnull BlockPos blockPos, @Nonnull EnumFacing enumFacing) {
        TileEntity entity = world.getTileEntity(blockPos);

        if(entity instanceof TileEntityDistillerSlave) {
            TileEntityDistillerSlave te = (TileEntityDistillerSlave) entity;
            TileEntityDistillerMaster tem = te.master();
            if(tem != null && te.isRedstonePos()) {
                return new DistillerPeripheral(world, tem.getPos(), TileEntityDistillerMaster.class);
            }
        }

        return null;
    }

    private static class DistillerPeripheral extends ITPeripheral.ITPeripheralMultiblock<TileEntityDistillerMaster> {

        protected DistillerPeripheral(World w, BlockPos pos, Class<? extends TileEntityDistillerMaster> myClass) {
            super(w, pos, myClass);
        }

        @Nonnull
        @Override
        public String getType() {
            return String.format("%s_distiller", ImmersiveTechnology.MODID);
        }

        @Nonnull
        @Override
        public String[] getMethodNames() {
            return new String[]{
                    "getEnergy",
                    "getTanks",
                    "getEmptyCanisters",
                    "getFullCanisters",
                    "getComputerControl",
                    "setComputerControl",
                    "setEnabled",
                    "getEnabled"
            };
        }

        @Nullable
        @Override
        public Object[] callMethod(@Nonnull IComputerAccess iComputerAccess, @Nonnull ILuaContext iLuaContext, int i, @Nonnull Object[] objects) throws LuaException {
            TileEntityDistillerMaster entity = getTileEntity();

            if (entity == null) {
                return null;
            }

            switch (i) {
                case 0: { // getEnergy
                    return new Object[]{
                            CCTJavaConvert.EnergyToLuaTable(entity.energyStorage)
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
                case 2: { // getEmptyCanisters
                    return new Object[]{
                            new HashMap<String, Object>(2) {{
                                put("input", CCTJavaConvert.ItemStackToLuaTable(entity.inventory.get(0)));
                                put("output", CCTJavaConvert.ItemStackToLuaTable(entity.inventory.get(2)));
                            }}
                    };
                }
                case 3: { // getFullCanisters
                    return new Object[]{
                            new HashMap<String, Object>(2) {{
                                put("input", CCTJavaConvert.ItemStackToLuaTable(entity.inventory.get(1)));
                                put("output", CCTJavaConvert.ItemStackToLuaTable(entity.inventory.get(3)));
                            }}
                    };
                }
                case 4: { // isComputerControl
                    return new Object[]{entity.computerOn.isPresent()};
                }
                case 5: { // setComputerControl
                    return super.enableComputerControl(objects);
                }
                case 6: { // setEnabled
                    return super.setEnabled(objects);
                }
                case 7: { // getEnabled
                    return new Object[]{entity.computerOn.orElse(false)};
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
