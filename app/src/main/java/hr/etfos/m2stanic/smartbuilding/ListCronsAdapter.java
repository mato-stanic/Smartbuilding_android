package hr.etfos.m2stanic.smartbuilding;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by mato on 20.04.16..
 */
public class ListCronsAdapter extends BaseAdapter {

    private Context context;
    private static LayoutInflater inflater = null;
    List<ApartmentCronJob> result;

    public ListCronsAdapter(CronListActivity activity, List<ApartmentCronJob> crons) {
        // TODO Auto-generated constructor stub
        result=crons;
        context=activity;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tvRoom;
        TextView tvTime;
        TextView tvAction;
        TextView tvDays;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.list_crons, null);
        holder.tvRoom=(TextView) rowView.findViewById(R.id.tvRoomValues);
        holder.tvRoom.setText(result.get(position).getRoom());

        holder.tvTime=(TextView) rowView.findViewById(R.id.tvTimeValue);
        holder.tvTime.setText(result.get(position).getTime());

        holder.tvAction=(TextView) rowView.findViewById(R.id.tvActionValue);
        holder.tvAction.setText(result.get(position).getAction());

        holder.tvDays=(TextView) rowView.findViewById(R.id.tvDaysValues);
        List<String> days = result.get(position).getDays();
        String daysToDisplay = "";
        for(int i=0;i<days.size();i++){
            daysToDisplay=daysToDisplay + days.get(i);
            if(i!=days.size())
                daysToDisplay+=", ";
        }
        holder.tvDays.setText(daysToDisplay);

        rowView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ApartmentManager.deleteCron(context, result.get(position).getId());
                return true;
            }
        });

        return rowView;
    }

}
