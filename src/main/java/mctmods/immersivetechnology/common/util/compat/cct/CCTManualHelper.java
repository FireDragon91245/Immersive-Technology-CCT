package mctmods.immersivetechnology.common.util.compat.cct;

import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.lib.manual.ManualPages;
import dan200.computercraft.api.ComputerCraftAPI;
import mctmods.immersivetechnology.common.Config;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public class CCTManualHelper {
    private static boolean added = false;

    public static void addCCTManual()
    {
        if(FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
        {
            if(added)
            {
                return;
            }
            added = true;

            ManualHelper.getManual().addEntry("cct.introduction", "cct",
                    new ManualPages.Text(ManualHelper.getManual(), "cct.intro0")
            );

            ManualHelper.getManual().addEntry("cct.disableredstonecontrol", "cct",
                    new ManualPages.Text(ManualHelper.getManual(), "cct.disableredstonecontrol0"),
                    new ManualPages.Text(ManualHelper.getManual(), "cct.disableredstonecontrol1")
            );

            if (Config.ITConfig.Machines.Multiblock.enable_boiler)
                ManualHelper.getManual().addEntry("cct.boiler", "cct",
                        new ManualPages.Text(ManualHelper.getManual(), "cct.boiler0")
                );
            //if (Config.ITConfig.Machines.Multiblock.enable_distiller)
            //    ComputerCraftAPI.registerPeripheralProvider(new DistillerPeripheralProvider());
            //if (Config.ITConfig.Machines.Multiblock.enable_gasTurbine)
            //    ComputerCraftAPI.registerPeripheralProvider(new GasTurbinePeripheralProvider());
            //if (Config.ITConfig.Machines.Multiblock.enable_heatExchanger)
            //    ComputerCraftAPI.registerPeripheralProvider(new HeatExchangerPeripheralProvider());
            //if (Config.ITConfig.Machines.Multiblock.enable_solarTower)
            //    ComputerCraftAPI.registerPeripheralProvider(new SolarTowerPeripheralProvider());
            //if (Config.ITConfig.Machines.Multiblock.enable_solarMelter)
            //    ComputerCraftAPI.registerPeripheralProvider(new SolarMelterPeripheralProvider());
            //if (Config.ITConfig.Machines.Multiblock.enable_steamTurbine)
            //    ComputerCraftAPI.registerPeripheralProvider(new SteamTurbinePeripheralProvider());
            //if (Config.ITConfig.Machines.Multiblock.enable_highPressureSteamTurbine)
            //    ComputerCraftAPI.registerPeripheralProvider(new HighPressureSteamTurbinePeripheralProvider());
            //if (Config.ITConfig.Machines.Multiblock.enable_meltingCrucible)
            //    ComputerCraftAPI.registerPeripheralProvider(new MeltingCruciblePeripheralProvider());
            //if (Config.ITConfig.Machines.Multiblock.enable_electrolyticCrucibleBattery)
            //    ComputerCraftAPI.registerPeripheralProvider(new ElectrolyticCrucibleBatteryPeripheralProvider());
            //ComputerCraftAPI.registerPeripheralProvider(new AlternatorPeripheralProvider());
            //ComputerCraftAPI.registerPeripheralProvider(new SteelSheetmetalTankPeripheralProvider());
        }
    }
}
