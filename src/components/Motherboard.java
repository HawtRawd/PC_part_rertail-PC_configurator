package components;

public class Motherboard extends Product {
    // Matches 'mobo_specs'
    private String socket;
    private String chipset;
    private String formFactor;
    private int maxRamSlots;
    private int maxRamCapacity;
    private String pcieTech;
    private int pcieSlots;
    private int nvmeSlots;
    private int sataSlots;
    private String ramTypeSupported;

    public Motherboard(){
        super();
    }

    public Motherboard(int id, String name, double price, int stock, String imageUrl, int manufacturerId, String manufacturerName, int categoryId, String categoryName, String socket, String chipset, String formFactor, int maxRamSlots, int maxRamCapacity, String pcieTech, int pcieSlots, int nvmeSlots, int sataSlots, String ramTypeSupported) {
        super(id, name, price, stock, imageUrl, manufacturerId, manufacturerName, categoryId, categoryName);
        this.socket = socket;
        this.chipset = chipset;
        this.formFactor = formFactor;
        this.maxRamSlots = maxRamSlots;
        this.maxRamCapacity = maxRamCapacity;
        this.pcieTech = pcieTech;
        this.pcieSlots = pcieSlots;
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

    public String getPcieTech() {
        return pcieTech;
    }

    public int getPcieSlots() {
        return pcieSlots;
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
