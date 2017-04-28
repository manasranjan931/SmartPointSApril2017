package in.bizzmark.smartpoints_user.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.ArrayList;

import in.bizzmark.smartpoints_user.R;
import in.bizzmark.smartpoints_user.bo.EarnPointsBO;

/**
 * Created by User on 25-Apr-17.
 */

public class PointsAdapter extends RecyclerView.Adapter<PointsAdapter.ViewHolder>{

    ArrayList<EarnPointsBO> pointsBOList;
    Context context;

    AdapterView.OnItemClickListener mItemClickListener;

    public PointsAdapter(ArrayList<EarnPointsBO> pointsBOList,Context context) {
        this.pointsBOList = pointsBOList;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return pointsBOList.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        EarnPointsBO earnPointsBO = pointsBOList.get(position);
        holder.storename.setText(earnPointsBO.getStorename());
        holder.points.setText(earnPointsBO.getPoints());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.points_row,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView storename;
        public TextView points;

        public ViewHolder(View itemView) {
            super(itemView);
            storename = (TextView) itemView.findViewById(R.id.tvStoreName);
            points = (TextView) itemView.findViewById(R.id.tvPoints);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
