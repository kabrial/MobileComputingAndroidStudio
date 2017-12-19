package ujm.com.mobilecomputingtp1;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ListsAdapter extends BaseAdapter {

    private Context context;
    private List<String> data;
    private static LayoutInflater inflater = null;

    public ListsAdapter(Context context, List<String> data) {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null)
            view = inflater.inflate(R.layout.row, null);
        TextView text = view.findViewById(R.id.text);
        text.setText(data.get(position));
        Switch switchButton = view.findViewById(R.id.switchId);
        // Put true in switch button.
        switchButton.setChecked(false);
        switchButton.setOnCheckedChangeListener(switchChange());
        return view;
    }

    private OnCheckedChangeListener switchChange() {
        return new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                String text = b ? "Notifications On" : "Notifications Off";
                if (b) {
                    Toast.makeText(context, text, Toast.LENGTH_LONG).show();
                    createNewCallNotification("Kevin");
                } else {
                    Toast.makeText(context, text, Toast.LENGTH_LONG).show();
                }
            }
        };
    }


    private void createNewCallNotification(String contactName) {
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
// The id of the channel.
        String id = "my_channel_01";
// The user-visible name of the channel.
        CharSequence name = context.getString(R.string.channel_name);
// The user-visible description of the channel.
        String description = context.getString(R.string.channel_description);
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(id, name, importance);
            // Configure the notification channel.
            mChannel.setDescription(description);
            mChannel.enableLights(true);
// Sets the notification light color for notifications posted to this
// channel, if the device supports this feature.
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(mChannel);
        }


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, id)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(contactName);
        mNotificationManager.notify(Double.valueOf(Math.random() * 10).intValue(), builder.build());
    }

}
