package ujm.com.mobilecomputingtp1;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class CallNotification extends Fragment implements OnItemClickListener, OnItemSelectedListener {

    private AutoCompleteTextView textView = null;

    // Store contacts values in these arraylist
    public static ArrayList<String> phoneValueArr = new ArrayList<>();
    public static ArrayList<String> nameValueArr = new ArrayList<>();

    String toNumberValue = "";


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
        textView.setOnItemSelectedListener(this);
        textView.setOnItemClickListener(this);

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

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
                               long arg3) {
        // TODO Auto-generated method stub
        //Log.d("AutocompleteContacts", "onItemSelected() position " + position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

        InputMethodManager imm = (InputMethodManager) this.getActivity().getSystemService(
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getActivity().getCurrentFocus().getWindowToken(), 0);

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub

        // Get Array index value for selected name
        int i = nameValueArr.indexOf("" + arg0.getItemAtPosition(arg2));

        // If name exist in name ArrayList
        if (i >= 0) {

            // Get Phone Number
            toNumberValue = phoneValueArr.get(i);

            InputMethodManager imm = (InputMethodManager) this.getActivity().getSystemService(
                    INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(this.getActivity().getCurrentFocus().getWindowToken(), 0);

            // Show Alert
            Toast.makeText(this.getActivity().getBaseContext(),
                    "Position:" + arg2 + " Name:" + arg0.getItemAtPosition(arg2) + " Number:" + toNumberValue,
                    Toast.LENGTH_LONG).show();

            Log.d("AutocompleteContacts",
                    "Position:" + arg2 + " Name:" + arg0.getItemAtPosition(arg2) + " Number:" + toNumberValue);

        }

    }
}
