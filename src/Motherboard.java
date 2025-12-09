import enums.*;

public class Motherboard extends Product{
    private SocketType socket;
    private FormFactor formFactor;
    private RamType ramTypeSupported;
    private int maxRamCapacity;
    private int ramSlots;
    private StorageSlot[] storageSlots;
    private PCIE[] pciePorts;

    public Motherboard(int id, String name, String manufacturer, double price, int stock, ComponentType category, SocketType socket, FormFactor formFactor, RamType ramTypeSupported, int maxRamCapacity, StorageSlot storageSlot, PCIE[] pciePorts) {
        super(id, name, manufacturer, price, stock, category);
        this.socket = socket;
        this.formFactor = formFactor;
        this.ramTypeSupported = ramTypeSupported;
        this.maxRamCapacity = maxRamCapacity;
        this.pciePorts = pciePorts;
        this.storageSlots = storageSlots;
    }

    @Override
    public String getTechSpecsSummary(){
        return "Socket: " + socket + " | Size: " + formFactor + " | RAM: " + ramTypeSupported;
    }

    public SocketType getSocket() {
        return socket;
    }
    public FormFactor getFormFactor() {
        return formFactor;
    }
    public RamType getRamTypeSupported() {
        return ramTypeSupported;
    }
    public int getMaxRamCapacity() {
        return maxRamCapacity;
    }
    public int getMaxRamSlots(){
        return ramSlots;
    }
    public int getNVMESlots(){
        int count = 0;
        for(int i = 0; i < storageSlots.length; i++){
            if (storageSlots[i].getType() == StorageInterface.NVME && storageSlots[i].getFormFactor() == StorageForm.NM2) {
                count++;
            }
        }
        return count;
    }
    public int getSATAM2Slots(){
        int count = 0;
        for (StorageSlot slot : storageSlots) {
            if (slot.getType() == StorageInterface.SATA && slot.getFormFactor() == StorageForm.NM2) {
                count++;
            }
        }
        return count;
    }
    public int getStandardSATASlots(){
        int count = 0;
        for (StorageSlot slot : storageSlots) {
            if(slot.getType() == StorageInterface.NVME && (slot.getFormFactor() == StorageForm.S25 || slot.getFormFactor() == StorageForm.S35)){
                count++;
            }
        }
        return count;
    }

    public int getPcieX16Slots() {
        int count = 0;
        for(int i = 0; i < pciePorts.length; i++){
            if(pciePorts[i] == PCIE.X16)
                count++;
        }
        return count;
    }

    public int getPcieX8Slots() {
        int count = 0;
        for(int i = 0; i < pciePorts.length; i++){
            if(pciePorts[i] == PCIE.X8)
                count++;
        }
        return count;
    }

    public int getPcieX4Slots() {
        int count = 0;
        for(int i = 0; i < pciePorts.length; i++){
            if(pciePorts[i] == PCIE.X4)
                count++;
        }
        return count;
    }
}
