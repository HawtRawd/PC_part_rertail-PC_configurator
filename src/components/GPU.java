package components;
public class GPU extends Product {
    private String pcieTech;
    private double baseClock;
    private int vram;
    private int wattage;
    private int lengthMm;

    public GPU(int id, String name, double price, int stock, String imageUrl,
               int manufacturerId, String manufacturerName,
               int categoryId, String categoryName, String pcieTech, double baseClock, int vram, int wattage, int lengthMm) {

        super(id, name, price, stock, imageUrl, manufacturerId, manufacturerName, categoryId, categoryName);

        this.pcieTech = pcieTech;
        this.baseClock = baseClock;
        this.vram = vram;
        this.wattage = wattage;
        this.lengthMm = lengthMm;
    }

    public String getPcieTech() {
        return pcieTech;
    }

    public double getBaseClock() {
        return baseClock;
    }

    public int getVram() {
        return vram;
    }

    public int getWattage() {
        return wattage;
    }

    public int getLengthMm() {
        return lengthMm;
    }
}