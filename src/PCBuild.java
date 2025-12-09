import java.util.ArrayList;
import java.util.List;

public class PCBuild {
    private List<Product> parts = new ArrayList<>();
    private CPU selectedCPU;
    private Motherboard selectedMotherboard;
    private PSU selectedPSU;
    private Case selectedCase;

    private List<Storage> selectedStorages = new ArrayList<>();
    private List<GPU> selectedGPUs = new ArrayList<>();
    private List<RAM> selectedRAMs = new ArrayList<>();

    public void addRam(RAM newRam){
        if(selectedMotherboard != null && selectedRAMs.size() >= selectedMotherboard.getRamSlot){}
    }

    public void addPart(Product product) {
        parts.add(product);

        if(product instanceof CPU) {
            this.selectedCPU = (CPU) product;
        }
        if(product instanceof Motherboard) {
            this.selectedMotherboard = (Motherboard) product;
        }
        if(product instanceof GPU) {
            this.se
        }
    }
}
