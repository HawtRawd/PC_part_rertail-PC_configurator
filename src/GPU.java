public class GPU extends Product{
    private int vram;
    private PCIE pcie;
    private int powerDraw;
    private int pins;
    private int pcieTech;

    public GPU(int id, String name, String manufacturer, double price, int stock, ComponentType category, int vram, PCIE pcie, int powerDraw, int pins) {
        super(id, name, manufacturer, price, stock, category);
        this.vram = vram;
        this.pcie = pcie;
        this.powerDraw = powerDraw;
        this.pins = pins;
    }

    @Override
    public String getTechSpecsSummary() {
        return "VRAM: " + getVram() + "PCIE: " + getPcie() + "PowerDraw: " + getPowerDraw() + "Power Pins: " + getPins() + "PCIE Tech: " + getPCieTech();
    }

    public int getVram() {
        return vram;
    }
    public PCIE getPcie() {
        return pcie;
    }
    public int getPowerDraw() {
        return powerDraw;
    }
    public int getPins() {
        return pins;
    }
    public int getPCieTech() {
        return pcieTech;
    }
}
