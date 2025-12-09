public class CPU extends Product{
    private SocketType socket;
    private int coreCount;
    private double baseClock;
    private int tdp;

    public CPU(int id, String name, String manufacturer, double price, int stock,
               SocketType socket, int cores, int tdp, ComponentType category) {
        super(id, name, manufacturer, price, stock, ComponentType.CPU);
        this.socket = socket;
        this.coreCount = cores;
        this.baseClock = baseClock;
        this.tdp = tdp;
    }
    @Override
    public String getTechSpecsSummary() {
        return "Socket: " + socket + " | " + coreCount + " Cores | " + tdp + "W";
    }

    public SocketType getSocket() {
        return socket;
    }
    public int getTdp() {
        return tdp;
    }
}
