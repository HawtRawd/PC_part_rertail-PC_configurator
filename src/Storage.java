import enums.ComponentType;
import enums.StorageForm;
import enums.StorageInterface;

public class Storage extends Product{
    private int capacity;
    private StorageForm storageForm;
    private StorageInterface storageInterface;

    public Storage(int id, String name, String manufacturer, double price, int stock, ComponentType category, int capacity, StorageForm storageForm, StorageInterface storageInterface) {
        super(id, name, manufacturer, price, stock, category);
        this.capacity = capacity;
        this.storageForm = storageForm;
        this.storageInterface = storageInterface;
    }

    @Override
    public String getTechSpecsSummary() {
        return "Capacity:" + getCapacity() + "Storage format: " + getStorageForm() + "Interface: "  + getStorageInterface();
    }

    public int getCapacity() {
        return capacity;
    }

    public StorageForm getStorageForm() {
        return storageForm;
    }

    public StorageInterface getStorageInterface() {
        return storageInterface;
    }
}
