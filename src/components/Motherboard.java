package components;

public class Motherboard extends Product {
    // Matches 'mobo_specs'
    private String socket;
    private String chipset;
    private String formFactor;
    private int maxRamSlots;
    private int maxRamCapacity;
    private int pcieTech;
    private int pcieX16Slots;
    private int pcieX8Slots;
    private int pcieX4Slots;
    private int nvmeSlots;
    private int sataSlots;
    private String ramTypeSupported;

    public Motherboard(int id, String name, double price, int stock, String imageUrl, int manufacturerId, String manufacturerName, int categoryId, String categoryName, String socket, String chipset, String formFactor, int maxRamSlots, int maxRamCapacity, int pcieTech, int pcieX16Slots, int pcieX8Slots, int pcieX4Slots, int nvmeSlots, int sataSlots, String ramTypeSupported) {
        super(id, name, price, stock, imageUrl, manufacturerId, manufacturerName, categoryId, categoryName);
        this.socket = socket;
        this.chipset = chipset;
        this.formFactor = formFactor;
        this.maxRamSlots = maxRamSlots;
        this.maxRamCapacity = maxRamCapacity;
        this.pcieTech = pcieTech;
        this.pcieX16Slots = pcieX16Slots;
        this.pcieX8Slots = pcieX8Slots;
        this.pcieX4Slots = pcieX4Slots;
        this.nvmeSlots = nvmeSlots;
        this.sataSlots = sataSlots;
        this.ramTypeSupported = ramTypeSupported;
    }

    public String getSocket() {
        return socket;
    }

    public String getChipset() {
        return chipset;
    }

    public String getFormFactor() {
        return formFactor;
    }

    public int getMaxRamSlots() {
        return maxRamSlots;
    }

    public int getMaxRamCapacity() {
        return maxRamCapacity;
    }

    public int getPcieTech() {
        return pcieTech;
    }

    public int getPcieX16Slots() {
        return pcieX16Slots;
    }

    public int getPcieX8Slots() {
        return pcieX8Slots;
    }

    public int getPcieX4Slots() {
        return pcieX4Slots;
    }

    public int getNvmeSlots() {
        return nvmeSlots;
    }

    public int getSataSlots() {
        return sataSlots;
    }

    public String getRamTypeSupported() {
        return ramTypeSupported;
    }
}
