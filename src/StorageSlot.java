import enums.StorageForm;
import enums.StorageInterface;

public class StorageSlot {
    private StorageInterface type;
    private StorageForm formFactor;

    public StorageSlot(StorageInterface type, StorageForm formFactor) {
        this.type = type;
        this.formFactor = formFactor;
    }

    public StorageInterface getType() { return type; }
    public StorageForm getFormFactor() { return formFactor; }
}