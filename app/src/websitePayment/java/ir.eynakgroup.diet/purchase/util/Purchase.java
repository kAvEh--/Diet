package ir.eynakgroup.diet.purchase.util;

/**
 * Represents an in-app billing purchase.
 */
public class Purchase {
    String expirationDate;
    String mSku;

    public Purchase(String expirationDate, String mSku) {
        this.expirationDate = expirationDate;
        this.mSku = mSku;
    }

    public String getSku() {
        return mSku;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public String getToken() {
        return null;
    }
}
