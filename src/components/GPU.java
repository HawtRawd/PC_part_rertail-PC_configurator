package components;
public class GPU extends Product {
    private String pcieTech;
    private double baseClock;
    private int vram;
    private int wattageDraw;
    private int lengthMm;

    public GPU(){
        super();
    }

    public GPU(int id, String name, double price, int stock, String imageUrl,
               int manufacturerId, String manufacturerName,
               int categoryId, String categoryName, String pcieTech, double baseClock, int vram, int wattageDraw, int lengthMm) {

        super(id, name, price, stock, imageUrl, manufacturerId, manufacturerName, categoryId, categoryName);

        this.pcieTech = pcieTech;
        this.baseClock = baseClock;
        this.vram = vram;
        this.wattageDraw = wattageDraw;
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
        return wattageDraw;
    }

    public int getLengthMm() {
        return lengthMm;
    }
}