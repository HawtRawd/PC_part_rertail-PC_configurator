package components;

public class Case extends Product {
    private String formFactor;
    private int GPULengthMM;
    private String psuFormFactor;

    public Case(int id, String name, double price, int stock, String imageUrl, int manufacturerId, String manufacturerName, int categoryId, String categoryName, String formFactor, int GPULengthMM, String psuFormFactor) {
        super(id, name, price, stock, imageUrl, manufacturerId, manufacturerName, categoryId, categoryName);
        this.formFactor = formFactor;
        this.GPULengthMM = GPULengthMM;
        this.psuFormFactor = psuFormFactor;
    }

    public String getFormFactor() {
        return formFactor;
    }

    public int getGPULengthMM() {
        return GPULengthMM;
    }

    public String getPsuFormFactor() {
        return psuFormFactor;
    }
}