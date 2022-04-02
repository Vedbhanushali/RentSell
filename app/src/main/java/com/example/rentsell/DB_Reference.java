package com.example.rentsell;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Dictionary;

public class DB_Reference {
    DatabaseReference dbreference= FirebaseDatabase.getInstance().getReference("RentSell");

    public Dictionary<String,String> getPropertyData(String pid){
        Dictionary<String,String> property = null;
        DatabaseReference tempRefernce=dbreference.child("c_property").child(pid);
        Query query=tempRefernce;
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return property;
    }

    //get type from type id
    public interface typeCallback{
        void onCallback(String type);
    }
    public void getType(String tid,typeCallback callback){
        final String[] type = new String[1];
        type[0]="default";
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("RentSell").child("o_property_type");

        Query query1=reference.orderByChild("tid").equalTo(tid);
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    for(DataSnapshot dsp:ds.getChildren()){
                        if(dsp.getKey().contains("type")){
                            type[0] = String.valueOf(dsp.getValue());
                        }
                    }
                }
                callback.onCallback(type[0]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
