package ujm.com.mobilecomputingtp1;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CallNotification extends Fragment {

    private AutoCompleteTextView textView = null;

    private EditText datesTextView = null;
    private EditText timesTextView = null;

    private final List<String> listNotifications = new ArrayList<>();
    private ListsAdapter listsAdapter;

    public static CallNotification newInstance() {
        return new CallNotification();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_call_notification, container, false);
        initializeComponents(view);

        //Create adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>
                (getContext(), android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());
        textView.setThreshold(1);

        //Set adapter to AutoCompleteTextView
        textView.setAdapter(adapter);
//        textView.setOnItemSelectedListener(this);
//        textView.setOnItemClickListener(this);

        ListView mListView = view.findViewById(R.id.listView);
        listsAdapter = new ListsAdapter(getContext(), listNotifications);
        mListView.setAdapter(listsAdapter);
        return view;
    }

    private void initializeComponents(View view) {
        final Button Send = view.findViewById(R.id.Send);
        final Button Cancel = view.findViewById(R.id.Cancel);
        Send.setOnClickListener(addNotification());
        Cancel.setOnClickListener(clearInputs());
        textView = view.findViewById(R.id.toNumber);
        datesTextView = view.findViewById(R.id.date);
        timesTextView = view.findViewById(R.id.time);
    }

    private OnClickListener clearInputs() {
        return new OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.getText().clear();
                datesTextView.getText().clear();
                timesTextView.getText().clear();
            }
        };
    }

    private OnClickListener addNotification() {
        return new OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedName = textView.getText().toString();
                String selectedDate = datesTextView.getText().toString();
                String selectedTime = timesTextView.getText().toString();
                if (selectedName.length() == 0) {
                    Toast.makeText(getContext(), "Please fill phone number",
                            Toast.LENGTH_LONG).show();
                } else if (selectedDate.length() == 0) {
                    Toast.makeText(getContext(), "Please fill date",
                            Toast.LENGTH_LONG).show();
                } else if (selectedTime.length() == 0) {
                    Toast.makeText(getContext(), "Please fill times",
                            Toast.LENGTH_LONG).show();
                } else {
                    listNotifications.add("Name or Number: " + selectedName + "\nDate: " + selectedDate + " Time: " + selectedTime);
                    listsAdapter.notifyDataSetChanged();
                }
            }
        };
    }
}
