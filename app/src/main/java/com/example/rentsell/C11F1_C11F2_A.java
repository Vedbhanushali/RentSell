package com.example.rentsell;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static com.example.rentsell.C01.MyPREFERENCES;
import static com.example.rentsell.C01.USERID;
public class C11F1_C11F2_A extends RecyclerView.Adapter<C11F1_C11F2_A.C11_F1A_ViewHolder>  {

    ArrayList<C11F1_C11F2_M> list;
    String recyclerViewType;
    SharedPreferences sharedPreferences;
    Context context;

    public C11F1_C11F2_A(ArrayList<C11F1_C11F2_M> list, Context context, String recyclerViewType) {
        this.list = list;
        this.context=context;
        this.recyclerViewType=recyclerViewType;
    }

    @NonNull
    @Override
    public C11_F1A_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v ;
        if(recyclerViewType=="horizontal") {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.c112a_c113a_horizontal, parent, false);
        } else{
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.c112a_c113a_vertical, parent, false);
        }
        return  new C11_F1A_ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull C11F1_C11F2_A.C11_F1A_ViewHolder holder, int position) {

        C11F1_C11F2_M properties = list.get(position);
        holder.title.setText(properties.getTitle());
        holder.price.setText(properties.getPrice());
        holder.location.setText(properties.getLocation());
        if(properties.getImgUrl()==null){
            holder.imageView.setImageResource(R.drawable.home1);
        }else {
            Glide.with(context.getApplicationContext()).load(properties.getImgUrl()).into(holder.imageView);
        }
        try {
            if(properties.getProperty_for().contains("R")){
                properties.setProperty_for("Rent");
            }else {
                properties.setProperty_for("Sell");
            }
            holder.information.setText(properties.getType()+" for "+properties.getProperty_for());
        }catch (Exception e){

        }

        Log.e("info",properties.getTitle()+","+properties.getPrice()+","+properties.getLocation()+","+properties.getType()+" for "+properties.getProperty_for());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sharedPreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                if (sharedPreferences.contains(USERID)) {
                    Intent intent=new Intent(context.getApplicationContext(),C21.class);
                    intent.putExtra("pid",properties.getPid());
                    intent.putExtra("tid",properties.getTid());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    Toast.makeText(context, " opening", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "Please login first", Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context.getApplicationContext(),C01.class));
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setFilter(ArrayList<C11F1_C11F2_M> newsArrayList) {
        list.clear();
        list.addAll(newsArrayList);
        notifyDataSetChanged();
    }
//    @Override
//    public Filter getFilter() {
//            return filter;
//    }

    //    private Filter filter=new Filter() {
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            List<C11F1_C11F2_M> filteredList=new ArrayList<C11F1_C11F2_M>();
//            if(constraint==null || constraint.length()==0)
//            {
//                filteredList.addAll(list);
//            }else {
//                String filteredText=constraint.toString().toLowerCase().trim();
//                for (C11F1_C11F2_M c112_fm : list){
//                    if (c112_fm.getLocation().toLowerCase().contains(filteredText)){
//                        filteredList.add(c112_fm);
//                    }
//                }
//            }
//            FilterResults results = new FilterResults();
//            results.values = filteredList;
//            return results;
//        }
//
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//            list.clear();
//            list.addAll((List) results.values);
//            notifyDataSetChanged();
//        }
//    };
    public static class C11_F1A_ViewHolder extends RecyclerView.ViewHolder {
        TextView title,price,location,information;
        CardView cardView;
        ImageView imageView;
        public C11_F1A_ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.recyclerView_property);
            price= itemView.findViewById(R.id.recyclerView_amount);
            location=itemView.findViewById(R.id.recyclerView_location);
            cardView=itemView.findViewById(R.id.Card);
            imageView=itemView.findViewById(R.id.imag);
            try {
                information = itemView.findViewById(R.id.recyclerView_info);
            }catch (Exception e){

            }
        }
    }

}
