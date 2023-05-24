package sample;
public class UserInventoryItem {
    private String category;
    private String productID;
    private String productName;
    private double purchaseValue;
    private double sellingPrice;
    private String lastUpdated;
    private int inventory;

    public UserInventoryItem(String category, String productID, String productName, double purchaseValue,
                             double sellingPrice, String lastUpdated, int inventory) {
        this.category = category;
        this.productID = productID;
        this.productName = productName;
        this.purchaseValue = purchaseValue;
        this.sellingPrice = sellingPrice;
        this.lastUpdated = lastUpdated;
        this.inventory = inventory;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPurchaseValue() {
        return purchaseValue;
    }

    public void setPurchaseValue(double purchaseValue) {
        this.purchaseValue = purchaseValue;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }
}
