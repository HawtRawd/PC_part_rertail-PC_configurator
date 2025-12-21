package components;

import components.Product;

public class CPU extends Product {
    private String socket;
    private double baseClock;
    private double boostClock;
    private int tdp;
    private int cores;

    public CPU(int id, String name, double price, int stock, String imageUrl,
               int manufacturerId, String manufacturerName,
               int categoryId, String categoryName,
             String socket, double baseClock, double boostClock, int tdp, int cores) {
        super(id, name, price, stock, imageUrl, manufacturerId, manufacturerName, categoryId, categoryName);

        this.socket = socket;
        this.baseClock = baseClock;
        this.boostClock = boostClock;
        this.tdp = tdp;
        this.cores = cores;
    }

    public String getSocket() {
        return socket;
    }

    public double getBaseClock() {
        return baseClock;
    }

    public double getBoostClock() {
        return boostClock;
    }

    public int getTdp() {
        return tdp;
    }

    public int getCores() {
        return cores;
    }
}