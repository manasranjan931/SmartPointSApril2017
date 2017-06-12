package in.bizzmark.smartpoints_user.utility;

import static in.bizzmark.smartpoints_user.NavigationActivity.device_Id;

/**
 * Created by User on 31-May-17.
 */

public class UrlUtility {

    public static final String LOGIN_URL = "http://35.154.104.54/smartpoints/api/login";

    public static final String REGISTER_URL = "http://35.154.104.54/smartpoints/api/register-customer";

    public static final String UPDATE_PROFILE_URL = "http://35.154.104.54/smartpoints/api/update-user";

    public static final String  POINTS_URL = "http://35.154.104.54/smartpoints/customer-api/get-total-points-for-all-stores?customerDeviceId="+device_Id;

   // public static final String ABOUT_STORE_URL = "http://35.154.104.54/smartpoints/customer-api/get-branch-details?branchId="+branch_Id;

    //public static final String EARN_TRANSACTION_URL = "http://35.154.104.54/smartpoints/customer-api/get-single-customer-all-branch-transactions?customerDeviceId="+device_Id+"&branchId="+branch_Id ;

    //public static final String REDEEM_TRANSACTION_URL = "http://35.154.104.54/smartpoints/customer-api/get-single-customer-all-branch-transactions?customerDeviceId="+device_Id+"&branchId="+branch_Id ;

    //public static final String REWARDS_URL = "http://35.154.104.54/smartpoints/customer-api/get-rewards-details?branchId="+branch_Id;
}
