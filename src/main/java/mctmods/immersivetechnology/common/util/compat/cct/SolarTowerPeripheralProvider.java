package mctmods.immersivetechnology.common.util.compat.cct;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import mctmods.immersivetechnology.ImmersiveTechnology;
import mctmods.immersivetechnology.common.blocks.metal.tileentities.TileEntitySolarTowerMaster;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;

public class SolarTowerPeripheralProvider implements IPeripheralProvider {

    @Nullable
    @Override
    public IPeripheral getPeripheral(@Nonnull World world, @Nonnull BlockPos blockPos, @Nonnull EnumFacing enumFacing) {
        TileEntity entity = world.getTileEntity(blockPos);

        if (entity instanceof TileEntitySolarTowerMaster) {
            return new SolarTowerPeripheral(world, blockPos, TileEntitySolarTowerMaster.class);
        }

        return null;
    }

    private static class SolarTowerPeripheral extends ITPeripheral.ITPeripheralMultiblock<TileEntitySolarTowerMaster> {

        protected SolarTowerPeripheral(World w, BlockPos pos, Class<? extends TileEntitySolarTowerMaster> myClass) {
            super(w, pos, myClass);
        }

        @Nonnull
        @Override
        public String getType() {
            return String.format("%s_solarTower", ImmersiveTechnology.MODID);
        }

        @Nonnull
        @Override
        public String[] getMethodNames() {
            return new String[]{
                    "getHeat",
                    "getTanks",
                    "getComputerControl",
                    "setComputerControl",
                    "setEnabled",
                    "getEnabled",
                    "getHeatCelsius",
                    "getSolarLevel",
                    "getMaxSolarLevel",
                    "getFullCanisters",
                    "getEmptyCanisters"
            };
        }

        @Nullable
        @Override
        public Object[] callMethod(@Nonnull IComputerAccess iComputerAccess, @Nonnull ILuaContext iLuaContext, int i, @Nonnull Object[] objects) throws LuaException {
            TileEntitySolarTowerMaster entity = getTileEntity();

            if (entity == null) {
                return null;
            }

            switch (i) {
                case 0: { // getHeat
                    return new Object[]{
                            entity.heatLevel
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
                case 6: { // getHeatCelsius
                    return new Object[]{
                            (entity.heatLevel / 20) + 30
                    };
                }
                case 7: { // getSolarLevel
                    return new Object[]{
                            entity.reflectorStrength
                    };
                }
                case 8: { // getMaxSolarLevel
                    return new Object[]{
                            227.5
                    };
                }
                case 9: { // getFullCanisters
                    return new Object[]{
                            new HashMap<String, Object>(2)
                            {{
                                put("input", CCTJavaConvert.ItemStackToLuaTable(getTileEntity().inventory.get(1)));
                                put("output", CCTJavaConvert.ItemStackToLuaTable(getTileEntity().inventory.get(3)));
                            }}
                    };
                }
                case 10: { // getEmptyCanisters
                    return new Object[]{
                            new HashMap<String, Object>(2)
                            {{
                                put("input", CCTJavaConvert.ItemStackToLuaTable(getTileEntity().inventory.get(0)));
                                put("output", CCTJavaConvert.ItemStackToLuaTable(getTileEntity().inventory.get(2)));
                            }}
                    };
                }
                default:
                    return null;
            }
        }

        @Override
        public boolean equals(@Nullable IPeripheral iPeripheral) {
            if (iPeripheral instanceof SolarTowerPeripheral) {
                SolarTowerPeripheral other = (SolarTowerPeripheral) iPeripheral;
                return other.getTileEntity().getPos().equals(getTileEntity().getPos());
            }
            return false;
        }
    }

}
