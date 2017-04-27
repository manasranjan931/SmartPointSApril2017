package in.bizzmark.smartpoints_user.bo;

/**
 * Created by Chalam on 11/18/2016.
 */

public class AcknowledgementBO {

    String type;
    String billAmount;
    String storeName;
    String points;
    String deviceId;
    String disCountAmount;
    String time;
    String status;

    public AcknowledgementBO() {
    }

    public AcknowledgementBO(String status,String type,
                             String billAmount,
                             String storeName,
                             String points,
                             String deviceId,
                             String disCountAmount,
                        String time) {
        this.status = status;
        this.type = type;
        this.billAmount = billAmount;
        this.storeName = storeName;
        this.points = points;
        this.deviceId = deviceId;
        this.disCountAmount = disCountAmount;
        this.time = time;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(String billAmount) {
        this.billAmount = billAmount;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDisCountAmount() {
        return disCountAmount;
    }

    public void setDisCountAmount(String disCountAmount) {
        this.disCountAmount = disCountAmount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
