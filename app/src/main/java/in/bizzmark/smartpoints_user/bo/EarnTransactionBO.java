package in.bizzmark.smartpoints_user.bo;

/**
 * Created by User on 18-May-17.
 */

public class EarnTransactionBO {

    public String storeName;
    public String bill_amount;
    public String points;
    public String type;
    public String date_time;

    public EarnTransactionBO() {
        // require empty-constructor
    }

    public EarnTransactionBO(String storeName, String bill_amount,
                             String points, String type, String date_time) {
        this.storeName = storeName;
        this.bill_amount = bill_amount;
        this.points = points;
        this.type = type;
        this.date_time = date_time;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getBill_amount() {
        return bill_amount;
    }

    public void setBill_amount(String bill_amount) {
        this.bill_amount = bill_amount;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

}
