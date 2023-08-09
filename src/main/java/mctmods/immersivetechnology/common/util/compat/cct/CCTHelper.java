package mctmods.immersivetechnology.common.util.compat.cct;

import dan200.computercraft.api.ComputerCraftAPI;
import mctmods.immersivetechnology.common.Config;
import mctmods.immersivetechnology.common.util.compat.ITCompatModule;

public class CCTHelper extends ITCompatModule {

    @Override
    public void preInit() {

    }

    @Override
    public void init() {
        if (Config.ITConfig.Machines.Multiblock.enable_boiler)
            ComputerCraftAPI.registerPeripheralProvider(new BoilerPeripheralProvider());
    }

    @Override
    public void postInit() {

    }

}
