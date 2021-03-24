package com.routecreation;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RoutelistAdapter extends RecyclerView.Adapter<RoutelistAdapter.Routelistviewholder> {
    List<RoutelistModel> list;
    Context context;
    View.OnClickListener listener;

    public RoutelistAdapter( Context context,List<RoutelistModel> list,View.OnClickListener listener) {
        this.list = list;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Routelistviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.routelist, parent, false);
        // View view = inflater.inflate(R.layout.activity_main2, parent, false);
        return new Routelistviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Routelistviewholder holder, int position) {
          holder.distance.setText(list.get(position).getDistance()+" KM");
          String[] timediv=list.get(position).getTime().split("\\.");
          holder.time.setText(timediv[0]+" hrs" +" "+timediv[1]+" min");
          holder.name.setText(list.get(position).getName());
          holder.linear.setTag(list.get(position));
          holder.linear.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Routelistviewholder extends RecyclerView.ViewHolder {

        TextView distance,time,name;
        LinearLayout linear;
        public Routelistviewholder(@NonNull View itemView) {
            super(itemView);

            distance = (TextView)itemView.findViewById(R.id.distance);
            time = (TextView)itemView.findViewById(R.id.time);
            name = (TextView)itemView.findViewById(R.id.name);
            linear = (LinearLayout) itemView.findViewById(R.id.linear);
        }
    }
    public void removeItem(int position) {

        SQLiteDatabase db;
        db=context.openOrCreateDatabase("RouteDb", Context.MODE_PRIVATE, null);
        Cursor c=db.rawQuery("SELECT * FROM Route WHERE Id ='"+ list.get(position).getId()+"'", null);
        if(c.moveToFirst())
        {
            db.execSQL("DELETE FROM Route WHERE Id ='"+ list.get(position).getId()+"'");
            // msg(this, "Record Deleted");
        }
        else
        {
            //  msg(this, "Invalid Employee Name ");
            Toast.makeText(context, "Please Try Again", Toast.LENGTH_SHORT).show();

        }
        //db.execSQL("DELETE FROM Route WHERE Id ='"+ list.get(position).getId()+"'");
            // msg(this, "Record Deleted");


        list.remove(position);
        notifyItemRemoved(position);
    }
}

