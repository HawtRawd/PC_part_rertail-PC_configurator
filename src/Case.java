public class Case extends Product{
    private FormFactor formFactor;
    private double gpuLength;
    private PSUFF psuff;

    public Case(int id, String name, String manufacturer, double price, int stock, ComponentType category, double gpuLength, FormFactor formFactor) {
        super(id, name, manufacturer, price, stock, category);
        this.gpuLength = gpuLength;
        this.formFactor = formFactor;
    }
    @Override
    public String getTechSpecsSummary() {
        return "Form factor: " + getFormFactor() + ", GPU length: " + getGpuLength() + ", PSU Form Factor: " + getPsuff();
    }

    public FormFactor getFormFactor() {
        return formFactor;
    }
    public double getGpuLength() {
        return gpuLength;
    }
    public PSUFF getPsuff() {
        return psuff;
    }
}
