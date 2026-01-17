package components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Build {
    private int id;
    private int user_id;
    private String buildName;
    private double totalPrice;

    private CPU cpu;
    private Motherboard mobo;
    private PSU psu;
    private Case pcCase;
    private Cooler cooler;

    private List<GPU> gpus = new ArrayList<>();
    private List<RAM> rams = new ArrayList<>();
    private List<Storage> storages = new ArrayList<>();
    private List<Accessory> accessories = new ArrayList<>();

    public Build() {}

    public Build(int id, int user_id, String buildName) {
        this.id = id;
        this.user_id = user_id;
        this.buildName = buildName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getBuildName() {
        return buildName;
    }

    public void setBuildName(String buildName) {
        this.buildName = buildName;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public CPU getCpu() {
        return cpu;
    }

    public void setCpu(CPU cpu) {
        this.cpu = cpu;
    }

    public Motherboard getMobo() {
        return mobo;
    }

    public void setMobo(Motherboard mobo) {
        this.mobo = mobo;
    }

    public PSU getPsu() {
        return psu;
    }

    public void setPsu(PSU psu) {
        this.psu = psu;
    }

    public Case getPcCase() {
        return pcCase;
    }

    public void setPcCase(Case pcCase) {
        this.pcCase = pcCase;
    }

    public Cooler getCooler() {
        return cooler;
    }

    public void setCooler(Cooler cooler) {
        this.cooler = cooler;
    }

    public List<GPU> getGpus() {
        return gpus;
    }

    public void setGpus(List<GPU> gpus) {
        this.gpus = gpus;
    }

    public List<RAM> getRams() {
        return rams;
    }

    public void setRams(List<RAM> rams) {
        this.rams = rams;
    }

    public List<Storage> getStorages() {
        return storages;
    }

    public void setStorages(List<Storage> storages) {
        this.storages = storages;
    }

    public List<Accessory> getAccessories() {
        return accessories;
    }

    public void setAccessories(List<Accessory> accessories) {
        this.accessories = accessories;
    }

    public double calculateTotal(){
        double total = 0;
        if(cpu != null) total += cpu.getPrice();
        if(mobo != null) total += mobo.getPrice();
        if(psu != null) total += psu.getPrice();
        if(cooler != null) total += cooler.getPrice();
        if(pcCase != null) total += pcCase.getPrice();
        for(GPU gpu : gpus) total += gpu.getPrice();
        for(Accessory accessory : accessories) total += accessory.getPrice();
        for(RAM ram : rams) total += ram.getPrice();
        for(Storage storage : storages) total += storage.getPrice();

        return total;
    }

    public List<Integer> getAllPartsIds(){
        List<Integer> ids = new ArrayList<>();
        if (cpu != null) ids.add(cpu.getId());
        if (mobo != null) ids.add(mobo.getId());
        if (psu != null) ids.add(psu.getId());
        if (pcCase != null) ids.add(pcCase.getId());
        if (cooler != null) ids.add(cooler.getId());
        for(GPU gpu : gpus) ids.add(gpu.getId());
        for (RAM r : rams) ids.add(r.getId());
        for (Storage s : storages) ids.add(s.getId());

        return ids;
    }


}
