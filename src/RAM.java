import enums.ComponentType;
import enums.RamFF;
import enums.RamType;

public class RAM extends Product{
    private int storage;
    private RamType ramType;
    private RamFF ramFF;
    private int modules;
    private int frequency;

    public RAM(int id, String name, String manufacturer, double price, int stock, ComponentType category, int storage, RamType ramType, RamFF ramFF, int modulus, int frequency) {
        super(id, name, manufacturer, price, stock, category);
        this.storage = storage;
        this.ramType = ramType;
        this.ramFF = ramFF;
        this.modules = modulus;
        this.frequency = frequency;
    }

    @Override
    public String getTechSpecsSummary() {
        return "Storage: " + getStorage() + "Type: " + getRamType() + "Form Factor: " + getRamFF() + "Modules: " + getModules() + "Frequency: " + getFrequency();
    }

    public int getStorage() {
        return storage;
    }

    public RamType getRamType() {
        return ramType;
    }

    public RamFF getRamFF() {
        return ramFF;
    }

    public int getModules() {
        return modules;
    }

    public int getFrequency() {
        return frequency;
    }
}
