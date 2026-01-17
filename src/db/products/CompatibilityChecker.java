package db.products;

import components.*;

import java.util.ArrayList;
import java.util.List;

public class CompatibilityChecker {

    public List<String> checkBuild(CPU cpu, Motherboard mobo, List<GPU> gpus, PSU psu, List<RAM> ramList, Case casse, List<Storage> storageList, Cooler cooler) {
        List<String> warnings = new ArrayList<>();

        if (cpu != null && mobo != null) {
            if (!cpu.getSocket().equalsIgnoreCase(mobo.getSocket())) {
                warnings.add("Critical: CPU Socket (" + cpu.getSocket() +
                        ") does not match Motherboard Socket (" + mobo.getSocket() + ")");
            }
        }
        if (psu != null) {
            int totalWattage = 0;
            if (cpu != null) totalWattage += cpu.getTdp();
            if (!gpus.isEmpty()) {
                for (GPU gpu : gpus)
                    totalWattage += gpu.getWattage();
            }
            totalWattage += 150;

            if (psu.getWattage() < totalWattage) {
                warnings.add("Warning: PSU Wattage (" + psu.getWattage() + "W) is too low. " +
                        "Estimated usage is " + (totalWattage) + "W.");
            }
        }
        if (mobo != null && !ramList.isEmpty()) {
            int sticks = 0;
            for(RAM kit : ramList){
                sticks = sticks + kit.getModules();
            }
            if (sticks > mobo.getMaxRamSlots()) {
                warnings.add("Physical Error: You have selected " + sticks +
                        " RAM sticks, but this motherboard only has " + mobo.getMaxRamSlots() + " slots.");
            }

            for (RAM ram : ramList) {
                if (!ram.getType().equalsIgnoreCase(mobo.getRamTypeSupported())) {
                    warnings.add("Incompatible RAM: Stick is " + ram.getType() +
                            " but Motherboard requires " + mobo.getRamTypeSupported());
                }
            }
        }
        if (casse != null && mobo != null){
            int caseRank = getSizeRank(casse.getFormFactor());
            int moboRank = getSizeRank(mobo.getFormFactor());

            if(caseRank < moboRank){
                warnings.add("Physical Error: The '" + casse.getName() + "' case (Max: " + casse.getFormFactor() +
                    ") is too small for the '" + mobo.getName() + "' motherboard (" + mobo.getFormFactor() + ").");
            }
        }

        if(!gpus.isEmpty() && casse != null){
            for(GPU gpu : gpus) {
                if (gpu.getLengthMm() > casse.getGPULengthMM())
                    warnings.add("Physical Error: The '" + casse.getName() + "' case (Max: " + casse.getGPULengthMM() +
                            ") might not fit the '" + gpu.getName() + " GPU (" + gpu.getLengthMm() + ").");
            }
        }

        if(!gpus.isEmpty() && mobo != null){
            if(gpus.size() > mobo.getPcieSlots())
                warnings.add("Error: Not enough PCIE X16 slots on the motherboard for the selected GPUs!");
            for(GPU gpu: gpus){
                if(!gpu.getPcieTech().equals(mobo.getPcieTech()))
                    warnings.add("Warning: PCIE tech mismatch! GPUs will run, but at slower speeds. Consider changing one of these components: "
                    + "GPU: " + gpu.getPcieTech() + ". Mobo: " + mobo.getPcieTech());
            }
            System.out.println("DEBUG: GPU Count = " + gpus.size() + ", Mobo Slots = " + mobo.getPcieSlots());
        }

        if(!storageList.isEmpty() && mobo != null){
            int nvmes = 0;
            int satas = 0;
            for(Storage storage : storageList){
                if(storage.getConnectivity().equalsIgnoreCase("nvme"))
                    nvmes ++;
                else satas++;
            }
            if(nvmes > mobo.getNvmeSlots())
                warnings.add("Error: Too many NVMe storage options added for the motherboard selected!");
            if(satas > mobo.getSataSlots())
                warnings.add("Error: Too many SATA storage options added for the motherboard selected!");
        }
        if (cpu != null && cooler != null) {
            if (!cpu.getSocket().equalsIgnoreCase(cooler.getSocket())) {
                warnings.add("Warning: CPU Socket (" + cpu.getSocket() +
                        ") does not match Motherboard Socket (" + cooler.getSocket() + ")");
            }
        }
        return warnings;
    }
    private int getSizeRank(String formFactor){
        if(formFactor == null) return -1;
        String clean = formFactor.toLowerCase().replace("-", "").replace(" ", "");
        if(clean.contains("eatx")) return 3;
        else if (clean.contains("atx") && !clean.contains("micro")) return  2;
        else if (clean.contains("micro") || clean.contains("matx")) return 1;
        else if (clean.contains("mini") || clean.contains("itx")) return 0;

        return -1;
    }
}