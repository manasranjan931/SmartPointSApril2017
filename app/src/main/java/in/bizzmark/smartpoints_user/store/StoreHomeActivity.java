package in.bizzmark.smartpoints_user.store;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import in.bizzmark.smartpoints_user.R;

/**
 * Created by User on 01-May-17.
 */

public class StoreHomeActivity extends Activity implements BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    ImageView ivBackPress;
    BottomNavigationView bottomNavigationView;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_details);

        // findAllIds
        findViewByAllId();

        // For Store-Details
        viewStoreDetails();
    }

    private void findViewByAllId() {
        textView = (TextView) findViewById(R.id.tv);
        ivBackPress = (ImageView) findViewById(R.id.iv_back_arrow_store_details);
        ivBackPress.setOnClickListener(this);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setActivated(true);
        bottomNavigationView.setSelected(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        item.setChecked(true);
        if (item.getItemId() == R.id.store_details){
            viewStoreDetails();
            textView.setText("Store details");
        }else if (item.getItemId() == R.id.transactions){
            viewAllTransaction();
        }else if (item.getItemId() == R.id.offer_promotion){
            viewAllOffersPromotions();
        }

        return false;
    }

    // For all offers-promotions
    private void viewAllOffersPromotions() {
        textView.setText("Offers and promotions");

        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = new OfferPromoFrag();

        // update the main content by replacing fragments
        fragmentManager.beginTransaction()
                .replace(R.id.framelayout, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null)
                .commit();
    }

    // for all transactions
    private void viewAllTransaction() {
        textView.setText("Transactions");

        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = new TransactionFrag();

        // update the main content by replacing fragments
        fragmentManager.beginTransaction()
                .replace(R.id.framelayout, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null)
                .commit();
    }

    // for store details
    private void viewStoreDetails() {
        textView.setText("Store details");

        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = new AboutStoreFrag();

        // update the main content by replacing fragments
        fragmentManager.beginTransaction()
                .replace(R.id.framelayout, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onClick(View v) {
        if ( v == ivBackPress){
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
