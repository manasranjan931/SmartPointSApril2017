package in.bizzmark.smartpoints_user.store;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.bizzmark.smartpoints_user.R;

/**
 * Created by User on 02-May-17.
 */

public class AboutStoreFrag extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_store,container,false);
        return view;
    }
}
