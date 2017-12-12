package ujm.com.mobilecomputingtp1;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class Authors extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_authors, container, false);
    }

    public static Authors newInstance() {
        Authors fragment = new Authors();
        return fragment;
    }

}
