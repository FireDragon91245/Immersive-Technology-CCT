package mctmods.immersivetechnology.common.util.compat.opencomputers;

import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.lib.manual.ManualPages;
import mctmods.immersivetechnology.common.Config;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

// Credit to Blusunrize, this thing is basically the same as the implementation in IE

public class ComputerManualHelper {
    private static boolean added = false;

    public static void addManualContent() {
        if (added) {
            return;
        }
        added = true;

        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
            if (Config.ITConfig.Machines.Multiblock.enable_steamTurbine)
                ManualHelper.getManual().addEntry("computer.steamTurbine", "computers",
                        new ManualPages.Text(ManualHelper.getManual(), "computer.steamTurbine0")
                );
            if (Config.ITConfig.Machines.Multiblock.enable_boiler)
                ManualHelper.getManual().addEntry("computer.boiler", "computers",
                        new ManualPages.Text(ManualHelper.getManual(), "computer.boiler0"),
                        new ManualPages.Text(ManualHelper.getManual(), "computer.boiler1")
                );
            if (Config.ITConfig.Machines.Multiblock.enable_distiller)
                ManualHelper.getManual().addEntry("computer.distiller", "computers",
                        new ManualPages.Text(ManualHelper.getManual(), "computer.distiller0"),
                        new ManualPages.Text(ManualHelper.getManual(), "computer.distiller1")
                );
            if (Config.ITConfig.Machines.Multiblock.enable_solarTower)
                ManualHelper.getManual().addEntry("computer.solarTower", "computers",
                        new ManualPages.Text(ManualHelper.getManual(), "computer.solarTower0"),
                        new ManualPages.Text(ManualHelper.getManual(), "computer.solarTower1")
                );
            if (Config.ITConfig.Machines.Multiblock.enable_gasTurbine)
                ManualHelper.getManual().addEntry("computer.gasTurbine", "computers",
                        new ManualPages.Text(ManualHelper.getManual(), "computer.gasTurbine0"),
                        new ManualPages.Text(ManualHelper.getManual(), "computer.gasTurbine1")
                );
            if (Config.ITConfig.Machines.Multiblock.enable_heatExchanger)
                ManualHelper.getManual().addEntry("computer.heatExchanger", "computers",
                        new ManualPages.Text(ManualHelper.getManual(), "computer.heatExchanger0"),
                        new ManualPages.Text(ManualHelper.getManual(), "computer.heatExchanger1")
                );
            if (Config.ITConfig.Machines.Multiblock.enable_electrolyticCrucibleBattery)
                ManualHelper.getManual().addEntry("computer.electrolyticCrucibleBattery", "computers",
                        new ManualPages.Text(ManualHelper.getManual(), "computer.electrolyticCrucibleBattery0"),
                        new ManualPages.Text(ManualHelper.getManual(), "computer.electrolyticCrucibleBattery1")
                );
            if (Config.ITConfig.Machines.Multiblock.enable_meltingCrucible)
                ManualHelper.getManual().addEntry("computer.meltingCrucible", "computers",
                        new ManualPages.Text(ManualHelper.getManual(), "computer.meltingCrucible0")
                );
            if (Config.ITConfig.Machines.Multiblock.enable_solarMelter)
                ManualHelper.getManual().addEntry("computer.solarMelter", "computers",
                        new ManualPages.Text(ManualHelper.getManual(), "computer.solarMelter0"),
                        new ManualPages.Text(ManualHelper.getManual(), "computer.solarMelter1")
                );

            ManualHelper.getManual().addEntry("computer.alternator", "computers",
                    new ManualPages.Text(ManualHelper.getManual(), "computer.alternator0")
            );
            ManualHelper.getManual().addEntry("computer.fluidValve", "computers",
                    new ManualPages.Text(ManualHelper.getManual(), "computer.fluidValve0")
            );
            ManualHelper.getManual().addEntry("computer.fluidPump", "computers",
                    new ManualPages.Text(ManualHelper.getManual(), "computer.fluidPump0")
            );
            ManualHelper.getManual().addEntry("computer.steelSheetMetalTank", "computers",
                    new ManualPages.Text(ManualHelper.getManual(), "computer.steelSheetMetalTank0")
            );
        }
    }
}
