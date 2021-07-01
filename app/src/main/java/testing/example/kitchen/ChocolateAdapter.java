package testing.example.kitchen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static testing.example.kitchen.R.layout.record;

import java.util.ArrayList;
import java.util.PriorityQueue;


public class ChocolateAdapter extends RecyclerView.Adapter<ChocolateAdapter.ChocolateViewHolder> {

    final private Context mContext;
    final private PriorityQueue<Chocolate> chocolateList;

    public ChocolateAdapter(Context mContext, PriorityQueue<Chocolate> chocolateList){
        this.mContext = mContext;
        this.chocolateList = chocolateList;
    }

    @NonNull
    @Override
    public ChocolateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        @SuppressLint("InflateParams") View view = inflater.inflate(record,null);
        return new ChocolateViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ChocolateViewHolder holder, int position) {
        ArrayList<Chocolate> removedItems = new ArrayList<>();

        while(!chocolateList.isEmpty()) {
            removedItems.add(chocolateList.remove());
            }
        holder.textViewName.setText(removedItems.get(position).getName());
        holder.textViewQuantity.setText(String.valueOf(removedItems.get(position).getQuantity()));

        while(!removedItems.isEmpty()){
            chocolateList.add(removedItems.remove(0));
        }
    }

    @Override
    public int getItemCount() {
        return chocolateList.size();
    }

    static class ChocolateViewHolder extends RecyclerView.ViewHolder{
        TextView textViewName, textViewQuantity;
        public ChocolateViewHolder(View itemView){
            super(itemView);

            textViewName = itemView.findViewById(R.id.record_name);
            textViewQuantity = itemView.findViewById(R.id.record_quantity);

        }
    }
}

