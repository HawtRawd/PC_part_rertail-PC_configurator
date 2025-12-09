public class Motherboard extends Product{
    private SocketType socket;
    private FormFactor formFactor;
    private String ramTypeSupported;
    private int maxRamCapacity;
    private int ramSlots;
    private StorageInterface[] storageInterfaces;
    private StorageForm[]  storageForms;
    private PCIE[] pciePorts;

    public Motherboard(int id, String name, String manufacturer, double price, int stock, ComponentType category, SocketType socket, FormFactor formFactor, String ramTypeSupported, int maxRamCapacity, StorageInterface[] storageInterfaces, PCIE[] pciePorts, StorageForm[] storageForms) {
        super(id, name, manufacturer, price, stock, category);
        this.socket = socket;
        this.formFactor = formFactor;
        this.ramTypeSupported = ramTypeSupported;
        this.maxRamCapacity = maxRamCapacity;
        this.storageInterfaces = storageInterfaces;
        this.pciePorts = pciePorts;
        this.storageForms = storageForms;
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
    public String getRamTypeSupported() {
        return ramTypeSupported;
    }
    public int getMaxRamCapacity() {
        return maxRamCapacity;
    }
    public StorageInterface[] getStorageInterfaces() {
        return storageInterfaces;
    }
    public StorageForm[] getStorageForms() {
        return storageForms;
    }
    public PCIE[] getPciePorts() {
        return pciePorts;
    }
}
