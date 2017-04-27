package in.bizzmark.smartpoints_user.bo;

/**
 * Created by User on 25-Apr-17.
 */

public class EarnPointsBO {
    public String storename;
    public String points;

    public EarnPointsBO() {
    }

    public EarnPointsBO(String storename, String points) {
        this.storename = storename;
        this.points = points;
    }

    public String getStorename() {
        return storename;
    }

    public void setStorename(String storename) {
        this.storename = storename;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }
}
