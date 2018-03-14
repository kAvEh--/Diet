package ir.eynakgroup.caloriemeter.shop;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Eynak_PC1 on 3/30/2017.
 */

public class LocalPurchase {

    String SKU;
    int price;
    boolean isPremium;
    boolean purchased;
    ArrayList<Product> products;
    String imagePath;

    public LocalPurchase(String SKU, int price, boolean isPremium, ArrayList<Product> products, String imagePath) {
        this.SKU = SKU;
        this.price = price;
        this.isPremium = isPremium;
        this.products = new ArrayList<>();
        this.products.addAll(products);
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isPurchased() {
        return purchased;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }

    public String getSKU() {
        return SKU;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isPremium() {
        return isPremium;
    }

    public void setPremium(boolean premium) {
        isPremium = premium;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products.addAll(products);
    }

    public String getProductsAsString() {
        JSONArray productArray = new JSONArray();
        for (Product p : products)
            productArray.put(p.ordinal());
        return productArray.toString();
    }

    @Override
    public String toString() {
        return SKU + ", " + imagePath + ", " + products;
    }
}
