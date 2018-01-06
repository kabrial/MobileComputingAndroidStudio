package ujm.com.mobilecomputingtp1;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.text.format.DateFormat.format;
import static android.text.format.DateFormat.is24HourFormat;

public class CallNotification extends Fragment {

    private AutoCompleteTextView contact;
    private static EditText date;
    private static EditText time;
    private final List<String> listNotifications = new ArrayList<>();
    private ListsAdapter listsAdapter;
    private ArrayList<Map<String, String>> contacts;
    private Cursor people;

    public static CallNotification newInstance() {
        return new CallNotification();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_call_notification, container, false);
        initializeComponents(view);
        contacts = new ArrayList<>();
        populateContacts();
        ListView reminders = view.findViewById(R.id.reminders);
        listsAdapter = new ListsAdapter(getContext(), listNotifications);
        reminders.setAdapter(listsAdapter);

        SimpleAdapter adapter = new SimpleAdapter(getContext(), contacts, R.layout.contact_info,
                new String[]{"Name", "Phone"}, new int[]{R.id.contact_name, R.id.contact_number});

        contact.setAdapter(adapter);
        contact.setThreshold(2);
        contact.setOnItemClickListener(retrieveContact());

        return view;
    }

    private AdapterView.OnItemClickListener retrieveContact() {
        return new AdapterView.OnItemClickListener() {
            @Override
            @SuppressWarnings("unchecked")
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, String> map = (Map<String, String>) parent.getItemAtPosition(position);
                String name = map.get("Name");
                contact.setText(name);
            }
        };
    }


    private void initializeComponents(View view) {
        final Button send = view.findViewById(R.id.Send);
        final Button cancel = view.findViewById(R.id.Cancel);
        final Button date = view.findViewById(R.id.btn_date);
        final Button time = view.findViewById(R.id.btn_time);
        send.setOnClickListener(addNotification());
        cancel.setOnClickListener(clearInputs());
        date.setOnClickListener(changeDate());
        time.setOnClickListener(changeTime());
        contact = view.findViewById(R.id.contact);
        CallNotification.date = view.findViewById(R.id.date);
        CallNotification.time = view.findViewById(R.id.time);
    }

    public void populateContacts() {
        contacts.clear();
        people = getContext().getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (people.moveToNext()) {
            String contactName = people.getString(people
                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String phoneNumber = people.getString(people
                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            Map<String, String> contactRetrieved = new HashMap<>();
            contactRetrieved.put("Name", contactName);
            contactRetrieved.put("Phone", phoneNumber);
            contacts.add(contactRetrieved);
        }
        getActivity().startManagingCursor(people);
    }

    @Override
    public void onStop() {
        people.close();
        super.onStop();
    }

    private OnClickListener clearInputs() {
        return new OnClickListener() {
            @Override
            public void onClick(View view) {
                contact.getText().clear();
                date.getText().clear();
                time.getText().clear();
            }
        };
    }

    private OnClickListener addNotification() {
        return new OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedName = contact.getText().toString();
                String selectedDate = date.getText().toString();
                String selectedTime = time.getText().toString();
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

    private OnClickListener changeDate() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment fragment = new DatePickerFragment();
                fragment.show(getFragmentManager(), "datePicker");
            }
        };
    }

    private OnClickListener changeTime() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment fragment = new TimePickerFragment();
                fragment.show(getFragmentManager(), "timePicker");
            }
        };
    }

    public static class DatePickerFragment extends DialogFragment implements OnDateSetListener {

        public static Date selected = new Date();

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            selected = calendar.getTime();
            date.setText(format("dd/MM/yyyy", selected));
        }
    }

    public static class TimePickerFragment extends DialogFragment implements OnTimeSetListener {

        public static Date selected = new Date();

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    is24HourFormat(getActivity()));
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            selected = calendar.getTime();
            time.setText(format("HH:mm", selected));
        }
    }
}
