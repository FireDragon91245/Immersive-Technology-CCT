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
        if (Config.ITConfig.Machines.Multiblock.enable_distiller)
            ComputerCraftAPI.registerPeripheralProvider(new DistillerPeripheralProvider());
        if (Config.ITConfig.Machines.Multiblock.enable_gasTurbine)
            ComputerCraftAPI.registerPeripheralProvider(new GasTurbinePeripheralProvider());
        if (Config.ITConfig.Machines.Multiblock.enable_heatExchanger)
            ComputerCraftAPI.registerPeripheralProvider(new HeatExchangerPeripheralProvider());
        if (Config.ITConfig.Machines.Multiblock.enable_solarTower)
            ComputerCraftAPI.registerPeripheralProvider(new SolarTowerPeripheralProvider());
        if (Config.ITConfig.Machines.Multiblock.enable_solarMelter)
            ComputerCraftAPI.registerPeripheralProvider(new SolarMelterPeripheralProvider());
        if (Config.ITConfig.Machines.Multiblock.enable_steamTurbine)
            ComputerCraftAPI.registerPeripheralProvider(new SteamTurbinePeripheralProvider());
        if (Config.ITConfig.Machines.Multiblock.enable_highPressureSteamTurbine)
            ComputerCraftAPI.registerPeripheralProvider(new HighPressureSteamTurbinePeripheralProvider());
        if (Config.ITConfig.Machines.Multiblock.enable_meltingCrucible)
            ComputerCraftAPI.registerPeripheralProvider(new MeltingCruciblePeripheralProvider());
        if (Config.ITConfig.Machines.Multiblock.enable_electrolyticCrucibleBattery)
            ComputerCraftAPI.registerPeripheralProvider(new ElectrolyticCrucibleBatteryPeripheralProvider());
    }

    @Override
    public void postInit() {

    }

}
