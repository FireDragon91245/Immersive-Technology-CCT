package mctmods.immersivetechnology.common.util.compat.cct;

import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;

public  abstract class ITPeripheral<T extends TileEntityIEBase> implements IPeripheral {

    World w;
    BlockPos pos;
    Class<? extends TileEntityIEBase> myClass;

    protected ITPeripheral(World w, BlockPos pos, Class<? extends TileEntityIEBase> myClass) {
        this.w = w;
        this.pos = pos;
        this.myClass = myClass;
    }

    protected T getTileEntity() {
        TileEntity te = this.w.getTileEntity(this.pos);
        return te != null && this.myClass.isAssignableFrom(te.getClass()) ? (T) te : null;
    }

    public static abstract class ITPeripheralMultiblock<T2 extends TileEntityMultiblockMetal<?, ?>> extends ITPeripheral<T2> {

        protected ITPeripheralMultiblock(World w, BlockPos pos, Class<? extends T2> myClass) {
            super(w, pos, myClass);
        }

        protected Object[] enableComputerControl(Object[] args) throws LuaException {
            boolean allow = CheckArgsBool(args);
            if (allow) {
                this.getTileEntity().computerOn = Optional.of(true);
            } else {
                this.getTileEntity().computerOn = Optional.empty();
            }

            return null;
        }

        protected Object[] setEnabled(Object[] args) throws LuaException {
            boolean enabled = CheckArgsBool(args);
            TileEntityMultiblockMetal<?, ?> te = this.getTileEntity();
            if (!te.computerOn.isPresent()) {
                throw new LuaException("Computer control must be enabled to enable or disable the machine");
            } else {
                te.computerOn = Optional.of(enabled);
                return null;
            }
        }

        private boolean CheckArgsBool(Object[] args) throws LuaException {
            if (args.length == 1) {
                if (Boolean.class.isAssignableFrom(args[0].getClass())) {
                    return  (Boolean) args[0];
                } else {
                    throw new LuaException("Expected boolean");
                }
            } else {
                throw new LuaException("Expected 1 argument");
            }
        }
    }
}
