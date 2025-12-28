package components;

public class RAM extends Product {
    private String type;
    private int frequency;
    private int capacityGb;
    private int modules;

    public RAM(){
        super();
    }

    public RAM(int id, String name, double price, int stock, String imageUrl, int manufacturerId, String manufacturerName, int categoryId, String categoryName, String type, int frequency, int capacityGb, int modules) {
        super(id, name, price, stock, imageUrl, manufacturerId, manufacturerName, categoryId, categoryName);
        this.type = type;
        this.frequency = frequency;
        this.capacityGb = capacityGb;
        this.modules = modules;
    }

    public String getType() {
        return type;
    }

    public int getFrequency() {
        return frequency;
    }

    public int getCapacityGb() {
        return capacityGb;
    }

    public int getModules() {
        return modules;
    }
}
