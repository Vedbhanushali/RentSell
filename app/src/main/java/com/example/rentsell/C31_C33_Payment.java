package com.example.rentsell;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;

public class C31_C33_Payment extends AppCompatActivity implements PaymentResultWithDataListener {
    //    EditText ;
    TextView paymentDetails,paymentAmount;
    Button paymentBtn;
    private String keyId = "2";
    String payAmount="",tid,pid,percentage="",propertyPrice="",todayDate,dueDate;
    Connection connect;             //holds the connection object to database
    String connectionResult="";     //holds the progress of inserting data
    String paymentStatus="";

    Calendar calendar ;
    Calendar nextCalender;
    java.sql.Date date_time;
    Date due_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c31_c33_payment);

//
//        Information information=new Information();
//        information.execute("");
        paymentDetails = (TextView) findViewById(R.id.payment_details_txt);
        paymentBtn = (Button) findViewById(R.id.make_payment_btn);
        paymentAmount = (TextView) findViewById(R.id.c31_c33_payment_amount);
        Checkout.preload(getApplicationContext());

        tid=getIntent().getStringExtra("tid");
        pid=getIntent().getStringExtra("pid");
        propertyPrice=getIntent().getStringExtra("price");
        Log.e("tid",tid);
//        paymentAmount.setText("200");



        getInformation(new MyCallback() {
            @Override
            public void onCallback(String price) {
//                paymentAmount.setText(price);
                Toast.makeText(getApplicationContext(), "price="+price, Toast.LENGTH_SHORT).show();
                payAmount=price;
                paymentAmount.setText(price);
            }
        });

        getKeyId(new MyCallback() {
            @Override
            public void onCallback(String price) {
                keyId=price;
            }
        });

        paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!paymentAmount.getText().toString().isEmpty()) {
                    try {
                        payAmount=paymentAmount.getText().toString();
                        startPayment(Double.parseDouble(payAmount) * 100);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Something is wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
//        if(paymentStatus.equals("success")){
//            insertData(payAmount);
//        }
    }


    public interface MyCallback{
        void onCallback(String price);
    }
    private void getInformation(MyCallback callback) {
//        final String[] amo = {""};
        String amountType=null;
//        try {
//
//            C00_connectionHelper connectionHelper = new C00_connectionHelper();
//            connect = connectionHelper.connectionClass();
//            if (connect != null) {
//                String query = "SELECT * FROM `o_parameter` where name='chargeAmountType' OR name='chargeAmountValue' OR name='razorPayKey'";
//                Statement st = connect.createStatement();
//                ResultSet rs = st.executeQuery(query);
//
//                while (rs.next()){
//
////                    if (rs.getString("name").contains("razorPayKey")) {
////                        keyId = rs.getString("value");
////                        callback.onCallback(keyId);
////                        Log.e("key", keyId);
////                        Toast.makeText(this, keyId, Toast.LENGTH_SHORT).show();
////                    }
//                    if(rs.getString("name").contains("chargeAmountType")){
//                        amountType=rs.getString("value");
//                        if(amountType.contains("P")){
//                            Toast.makeText(this, amountType, Toast.LENGTH_SHORT).show();
//                            Log.e("amounttype",amountType);
//                            getPercentage(new MyCallback() {
//                                @Override
//                                public void onCallback(String price) {
//                                    Log.e("getpere",price);
////                                    getKeyId(rs, new MyCallback() {
////                                        @Override
////                                        public void onCallback(String price) {
////                                            keyId=price;
////                                        }
////                                    });
//                                    callback.onCallback(price);
//                                }
//                            });
////                            String query2 = "SELECT * FROM `o_advertise_charges` where tid='"+tid+"'";
////                            Statement st2 = connect.createStatement();
////                            ResultSet rs2 = st2.executeQuery(query2);
////
//////                    Log.e("rs2",rs2.getCursorName());
//////                    Log.e("rs2",rs2.getString("percentage"));
////                            while (rs2.next()){
////                                percentage= rs2.getString("percentage");
////                                Log.e("rs2",percentage);
////                                Toast.makeText(this,percentage, Toast.LENGTH_SHORT).show();
////                                Double amo=(Double.parseDouble(propertyPrice)*Double.parseDouble(percentage))/100;
////
////                                payAmount= String.valueOf(amo);
////                            }
////                            calculateRupees(Double.parseDouble(percentage));
////                            connectionResult = "Successful fetched";
//
//                        }else {
//                            getAmount(rs, new MyCallback() {
//                                @Override
//                                public void onCallback(String price) {
//                                    Log.e("getamount",price);
////                                    getKeyId(rs, new MyCallback() {
////                                        @Override
////                                        public void onCallback(String price) {
////                                            keyId=price;
////                                        }
////                                    });
//                                    callback.onCallback(price);
//                                }
//                            });
//
////                            payAmount=getAmount(rs);
//                        }
//                    }
//
//
//                }
//                connectionResult = "Successful fetched";
////                Toast.makeText(this, connectionResult, Toast.LENGTH_SHORT).show();
//            } else {
//                connectionResult = "Check connection";
//            }
//            connect.close();
//        } catch (Exception ex) {
//            Log.e("Error in", ex.getMessage());
//            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
//            Log.e("excep",ex.getMessage());
//        }
    }

    private void getAmount(ResultSet rs,MyCallback callback) {
        String amo="";
        try {
            while (rs.next()) {
                if(rs.getString("name").contains("chargeAmountValue")){
                    amo=rs.getString("value");
//                    Toast.makeText(this,"chargeAmount "+ payAmount, Toast.LENGTH_SHORT).show();
                    callback.onCallback(amo);
                }
            }
        }catch (Exception e){
            Log.e("getAMo",e.getMessage());
//            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
//        return amo;
    }

    public void getKeyId(MyCallback callback){
//        try {
//
//            C00_connectionHelper connectionHelper = new C00_connectionHelper();
//            connect = connectionHelper.connectionClass();
//            if (connect != null) {
//                String query = "SELECT * FROM `o_parameter` where name='razorPayKey'";
//                Statement st = connect.createStatement();
//                ResultSet rs = st.executeQuery(query);
//
//                while (rs.next()){
//                    if (rs.getString("name").contains("razorPayKey")) {
//                        String keyId = rs.getString("value");
//                        callback.onCallback(keyId);
//                        Log.e("key", keyId);
//                        Toast.makeText(this, keyId, Toast.LENGTH_SHORT).show();
//                    }
//                }
//                connectionResult = "Successful fetched";
////                Toast.makeText(this, connectionResult, Toast.LENGTH_SHORT).show();
//            } else {
//                connectionResult = "Check connection";
//            }
//            connect.close();
//        } catch (Exception ex) {
//            Log.e("Error in", ex.getMessage());
//            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
//            Log.e("excep",ex.getMessage());
//        }
    }
    private void getPercentage(MyCallback callback) {

//        propertyList.add(new C11F1_C11F2_M("Apartment","10000","Ahmedabad"))'

//getting data of properties from database
//        try {
//
//            C00_connectionHelper connectionHelper = new C00_connectionHelper();
//            connect = connectionHelper.connectionClass();
//            if (connect != null) {
//                String query2 = "SELECT * FROM `o_advertise_charges` where tid='"+tid+"'";
//                Statement st2 = connect.createStatement();
//                ResultSet rs2 = st2.executeQuery(query2);
//
////                    Log.e("rs2",rs2.getCursorName());
////                    Log.e("rs2",rs2.getString("percentage"));
//                while (rs2.next()){
//                    percentage= rs2.getString("percentage");
//                    Log.e("rs2",percentage);
////                        Toast.makeText(this,percentage, Toast.LENGTH_SHORT).show();
//                    callback.onCallback(calculateRupees(Double.parseDouble(percentage)));
//                }
////                    paymentAmount.setText());
//                connectionResult = "Successful fetched";
//                Toast.makeText(this, connectionResult, Toast.LENGTH_SHORT).show();
//            } else {
//                connectionResult = "Check connection";
//            }
//            connect.close();
//        } catch (Exception ex) {
//            Log.e("Error in", ex.getMessage());
//            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
//            Log.e("excep2",ex.getMessage());
//        }

    }

    private String  calculateRupees(Double percentage) {
        Double amo=(Double.parseDouble(propertyPrice)*percentage)/100;
        Toast.makeText(this, String.valueOf(amo), Toast.LENGTH_SHORT).show();
        Log.e("amo",amo.toString());
        return String.valueOf(amo);

    }


    public void startPayment(Double amount) throws JSONException {
        Checkout checkout = new Checkout();

        checkout.setKeyID(keyId);
        /**
         * Instantiate Checkout
         */

        /**
         * Set your logo here
         */
        checkout.setImage(R.drawable.c31_c33_payment_logo);

        /**
         * Reference to current activity
         */
        final Activity activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            options.put("name", "Crown Software");
            options.put("description", "Reference No. #123456");
            options.put("image", "https://rzp.io/l/10hHHf99T");
//            options.put("order_id", orderid);//from response of step 3.
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", amount);//pass amount in currency subunits
            options.put("prefill.email", "roydodia@gmail.com");
            options.put("prefill.contact", "6355476308");
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            checkout.open(activity, options);

        } catch (Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }

    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID, PaymentData paymentData) {
        paymentAmount.setText("");
//        paymentDetails.setVisibility(View.VISIBLE);
//        paymentDetails.setText("payment id : " + razorpayPaymentID + "\nPaymetn Data : " + paymentData);
        paymentStatus="success";
        Snackbar.make(findViewById(R.id.c31_c33_payment), "payment id : " + razorpayPaymentID + "\nPaymetn Data : " + paymentData, Snackbar.LENGTH_SHORT).show();

        //insert data in databse
//        try {
//            C00_connectionHelper connectionHelper = new C00_connectionHelper();
//            connect = connectionHelper.connectionClass();
//            if (connect != null) {
//                String query;
//
//                query="INSERT INTO `c_advertise`(`pid`, `amount`, `due_time`, `date_time`) VALUES ('"+pid+"','"+payAmount+"',DATE_ADD(curdate(), INTERVAL 1 MONTH),curdate())";
//                Statement st = connect.createStatement();
//                st.executeUpdate(query);
//                Toast.makeText(getApplicationContext(), "inserted", Toast.LENGTH_SHORT).show();
//
//                connectionResult = "Successful inserted";
//                new Handler().postDelayed(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        startActivity(new Intent(getApplicationContext(),C31.class));
//                    }
//
//                }, 2000);
//
//            } else {
//                connectionResult = "Check connection";
//            }
//            connect.close();
//        } catch (Exception ex) {
//            Log.e("Error in", ex.getMessage());
//        }

    }

    @Override
    public void onPaymentError(int code, String response, PaymentData paymentData) {
        Snackbar.make(findViewById(R.id.c31_c33_payment), "Payment Failed due to " + response, Snackbar.LENGTH_SHORT).show();
        paymentStatus="fails";
        startActivity(new Intent(getApplicationContext(),C31.class));
//        paymentDetails.setVisibility(View.VISIBLE);
//        paymentDetails.setText("payment failed: " + response + "\nCode : " + code + "\nPaymetn Data : " + paymentData);

    }

//    private void insertData(String amount) {
////        calendar = Calendar.getInstance();
////        nextCalender=Calendar.getInstance();
////        nextCalender.add(Calendar.MONTH,1);
//
////        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
////        date_time= (Date) calendar.getTime();
////        due_time= (Date) nextCalender.getTime();
////        dueDate=simpleDateFormat.format(due_time);
////        todayDate=simpleDateFormat.format(date_time);
//        try {
//            C00_connectionHelper connectionHelper = new C00_connectionHelper();
//            connect = connectionHelper.connectionClass();
//            if (connect != null) {
//                String query;
//
//                query="INSERT INTO `c_advertise`(`pid`, `amount`, `due_time`, `date_time`) VALUES ('"+pid+"','"+amount+"',DATE_ADD(curdate(), INTERVAL 1 MONTH),curdate())";
//                Statement st = connect.createStatement();
//                st.executeUpdate(query);
//                Toast.makeText(this, "inserted", Toast.LENGTH_SHORT).show();
//
//                connectionResult = "Successful inserted";
//
//            } else {
//                connectionResult = "Check connection";
//            }
//            connect.close();
//        } catch (Exception ex) {
//            Log.e("Error in", ex.getMessage());
//        }
//    }
}