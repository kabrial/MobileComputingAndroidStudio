package ujm.com.mobilecomputingtp1;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.text.format.DateFormat.format;
import static android.text.format.DateFormat.is24HourFormat;

public class CallNotification extends Fragment {

    private AutoCompleteTextView textView;
    private static EditText date;
    private static EditText time;
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
        ListView reminders = view.findViewById(R.id.reminders);
        listsAdapter = new ListsAdapter(getContext(), listNotifications);
        reminders.setAdapter(listsAdapter);
        return view;
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
        textView = view.findViewById(R.id.toNumber);
        CallNotification.date = view.findViewById(R.id.date);
        CallNotification.time = view.findViewById(R.id.time);
    }

    private OnClickListener clearInputs() {
        return new OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.getText().clear();
                date.getText().clear();
                time.getText().clear();
            }
        };
    }

    private OnClickListener addNotification() {
        return new OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedName = textView.getText().toString();
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
