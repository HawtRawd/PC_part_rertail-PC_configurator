import enums.StorageForm;
import enums.StorageInterface;

import java.util.ArrayList;
import java.util.List;

public class PCBuild {
    private List<Product> parts = new ArrayList<>();
    private CPU selectedCPU;
    private Motherboard selectedMotherboard;
    private PSU selectedPSU;
    private Case selectedCase;

    private List<Storage> selectedStorages = new ArrayList<>();
    private List<GPU> selectedGPUs = new ArrayList<>();
    private List<RAM> selectedRAMs = new ArrayList<>();

    public int calculateTotalWattage() {
        int total = 0;
        if (selectedCPU != null) total += selectedCPU.getTdp();

        // Sum all GPUs
        for (GPU gpu : selectedGPUs) {
            total += gpu.getPowerDraw();
        }

        // Sum all Storage (approx 5-10W each, simplified)
        // total += storageDevices.size() * 10;

        return total;
    }

    public int calculateRAMSize(){
        int size = 0;
        for(int i = 0; i < selectedRAMs.size(); i++){
            size = size + selectedRAMs.get(i).getStorage();
        }
        return size;
    }
    public int calculateStorageSize(){
        int size = 0;
        for(int i = 0; i < selectedStorages.size(); i++){
            size = size + selectedStorages.get(i).getCapacity();
        }
        return size;
    }
    public int getRAMModules(){
        int size = 0;
        for(int i = 0; i < selectedRAMs.size(); i++){
            size = size + selectedRAMs.get(i).getModules();
        }
        return size;
    }

    public void addRam(RAM newRam){
        if(selectedMotherboard.getRamTypeSupported() != newRam.getRamType())
            throw new IllegalStateException("Ram type mismatch!");
        if(selectedMotherboard.getMaxRamSlots() < getRAMModules() + newRam.getModules()){
            throw new IllegalStateException("Too many ram modules for motherboard!");
        }
        else if(selectedMotherboard.getMaxRamCapacity() < calculateRAMSize() + newRam.getStorage()){
            throw new IllegalStateException("Ram size too much for motherboard!");
        }
        if(getRAMModules() + newRam.getModules() % 2 != 0)
            System.out.println("Warning! Dual Channeling won't work correctly! Please make sure you have an even number of RAM modules! RAM added nevertheless.");
        selectedRAMs.add(newRam);
        parts.add(newRam);
    }

    public int getNVMEStorages(){
        int count = 0;
        for(int i = 0; i < selectedStorages.size(); i++){
            if(selectedStorages.get(i).getStorageInterface() == StorageInterface.NVME){
                count++;
            }
        }
        return count;
    }

    public int getSATAM2Storages(){
        int count = 0;
        for(int i = 0; i < selectedStorages.size(); i++){
            if(selectedStorages.get(i).getStorageInterface() == StorageInterface.SATA && selectedStorages.get(i).getStorageForm() == StorageForm.NM2){
                count++;
            }
        }
        return count;
    }

    public int getStandardSATAStorages(){
        int count = 0;
        for(int i = 0; i < selectedStorages.size(); i++){
            if(selectedStorages.get(i).getStorageInterface() == StorageInterface.SATA && selectedStorages.get(i).getStorageForm() == StorageForm.S35 || selectedStorages.get(i).getStorageForm() == StorageForm.S25){
                count++;
            }
        }
        return count;
    }
    public void addStorage(Storage newStorage) {
        if (newStorage.getStorageForm() == StorageForm.NM2 && newStorage.getStorageInterface() == StorageInterface.NVME) {
            if (1 + getNVMEStorages() > selectedMotherboard.getNVMESlots()) {
                throw new IllegalStateException("Too many NVME drives for motherboard!");
            }
        }
        else if (newStorage.getStorageInterface() == StorageInterface.SATA && newStorage.getStorageForm() == StorageForm.S35 || newStorage.getStorageForm() == StorageForm.S25) {
            if (1 + getStandardSATAStorages() > selectedMotherboard.getStandardSATASlots()){
                throw new IllegalStateException("Too many standard SATA drives for motherboard!");
            }
        }
        else if (newStorage.getStorageInterface() == StorageInterface.SATA && newStorage.getStorageForm() == StorageForm.NM2) {
            if (1 + getSATAM2Storages() > selectedMotherboard.getSATAM2Slots()) {
                throw new IllegalStateException("Too many M.2 SATA drives for motherboard!");
            }
        }
            selectedStorages.add(newStorage);
            parts.add(newStorage);
    }

    public void addGPU(GPU newGpu) {
        // 1. Dependency Check: We need a motherboard to know how many slots we have
        if (selectedMotherboard == null) {
            throw new IllegalStateException("Please select a Motherboard before adding a GPU.");
        }

        // 2. PCIe Slot Validation
        // Assuming Motherboard has a field 'pcieX16Slots' (usually 1, 2, or 3)
        if (selectedGPUs.size() >= selectedMotherboard.getPcieX16Slots()) {
            throw new IllegalStateException("No available PCIe x16 slots on this motherboard!");
        }

        // 3. Physical Clearance Validation (Case)
        // We only check this if a case has ALREADY been selected.
        if (selectedCase != null) {
            // GPU length is usually in mm (e.g., 320mm)
            // Case max length is in mm (e.g., 300mm)
            if (newGpu.getLengthMM() > selectedCase.getGpuLength()) {
                throw new IllegalStateException("This GPU is too long for the selected Case! " +
                        "(GPU: " + newGpu.getLengthMM() + "mm, Case Limit: " + selectedCase.getGpuLength() + "mm)");
            }
        }

        // 4. Power Warning (Optional but helpful)
        if (selectedPSU != null) {
            int currentWattage = calculateTotalWattage(); // You need to implement this helper
            int newTotal = currentWattage + newGpu.getPowerDraw();

            if (newTotal > selectedPSU.getWattage()) {
                System.out.println("WARNING: Adding this GPU exceeds your current PSU wattage rating!");
                // We usually allow the add, but warn the user they need a better PSU.
            }
        }

        // 5. Add to lists
        selectedGPUs.add(newGpu);
        parts.add(newGpu);
    }

    public void addCase(Case newCase) {
        // Check if existing GPUs fit into this NEW case
        for (GPU existingGpu : selectedGPUs) {
            if (existingGpu.getLengthMM() > newCase.getGpuLength()) {
                throw new IllegalStateException("Cannot select this Case: Your current GPU (" +
                        existingGpu.getName() + ") is too long!");
            }
        }

        this.selectedCase = newCase;
        parts.add(newCase);
    }



}
