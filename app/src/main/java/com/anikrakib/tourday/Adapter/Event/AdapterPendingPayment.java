package com.anikrakib.tourday.Adapter.Event;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anikrakib.tourday.Models.Event.PendingPayment;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.Utils.ApiURL;
import com.anikrakib.tourday.WebService.RetrofitClient;
import com.google.android.material.snackbar.Snackbar;
import com.kishandonga.csbx.CustomSnackbar;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterPendingPayment extends RecyclerView.Adapter<AdapterPendingPayment.MyViewHolder>{

    Context context ;
    List<PendingPayment> mData;
    TextView eventTotalPendingTextView;
    TextView eventTotalGoingTextView;


    public AdapterPendingPayment(Context context, List<PendingPayment> mData, TextView eventTotalPendingTextView, TextView eventTotalGoingTextView) {
        this.context = context;
        this.mData = mData;
        this.eventTotalPendingTextView = eventTotalPendingTextView;
        this.eventTotalGoingTextView = eventTotalGoingTextView;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.pending_payment_user_list_item, parent, false);
        return new MyViewHolder(v);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder( MyViewHolder myViewHolder, int i) {
        myViewHolder.pendingUserTransactionId.setText("Tr-Id : "+mData.get(i).getTr()+"("+mData.get(i).getMethod()+")");
        //Toast.makeText(context,mData.size()+"",Toast.LENGTH_LONG).show();

        showUserData(mData.get(i).getUser(),myViewHolder.pendingUserName,myViewHolder.pendingEmail,myViewHolder.userImage);

        myViewHolder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentAction(mData.get(i).getEvent(),mData.get(i).getUser(),mData.get(i).getTr(),1,i);
            }
        });

        myViewHolder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentAction(mData.get(i).getEvent(),mData.get(i).getUser(),mData.get(i).getTr(),0,i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {


        TextView pendingUserName;
        TextView pendingEmail;
        TextView pendingUserTransactionId;
        CircleImageView userImage;
        Button accept,reject;


        public MyViewHolder(View itemView) {

            super(itemView);
            pendingEmail = itemView.findViewById(R.id.pendingUserEmail);
            pendingUserName = itemView.findViewById(R.id.pendingUserName);
            pendingUserTransactionId = itemView.findViewById(R.id.pendingUserTransactionId);
            userImage = itemView.findViewById(R.id.pendingUserProfilePic);
            accept = itemView.findViewById(R.id.yesButton);
            reject = itemView.findViewById(R.id.noButton);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //categoryItemClickListener.onCategoryClick(mData.get(getAdapterPosition()));



                }
            });

        }
    }

    public void showUserData(int userId, TextView pendingUserName, TextView pendingEmail, CircleImageView userImage){

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .getUserInfoByUserId(userId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    JSONObject jsonObject = null;
                    try {
                        assert response.body() != null;
                        jsonObject = new JSONObject(response.body().string());
                        JSONObject profile = jsonObject.getJSONObject("profile");

                        pendingUserName.setText(profile.getString("name"));
                        pendingEmail.setText(profile.getString("email"));
                        Picasso.get().load(ApiURL.IMAGE_BASE +profile.getString("picture")).fit().centerInside().into(userImage);
                        //pendingPayments.add(new PendingPayment(profile.getString("picture"),profile.getString("name"),profile.getString("email")));

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(context,"Token Not Correct",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(context,"Fail!",Toast.LENGTH_LONG).show();

            }
        });
    }

    public void paymentAction(int event, int user, String tr, int action, int position){
        SharedPreferences userPref = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String token = userPref.getString("token","");
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .eventPaymentAction(event,"Token "+token,user,tr,action);
        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    mData.remove(position);
                    notifyItemRemoved(position);
                    notifyDataSetChanged();
                    eventTotalPendingTextView.setText(mData.size()+"");
                    if(action == 1){
                        int a = Integer.parseInt(eventTotalGoingTextView.getText().toString());
                        eventTotalGoingTextView.setText((a+1)+"");
                        snackBar("Accepted",R.color.white);
                    }else{
                        snackBar("Rejected",R.color.dark_red);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(context,"Fail!",Toast.LENGTH_LONG).show();

            }
        });
    }

    public void snackBar(String text,int color){
        CustomSnackbar sb = new CustomSnackbar(context);
        sb.message(text);
        sb.padding(15);
        sb.textColorRes(color);
        sb.backgroundColorRes(R.color.colorPrimaryDark);
        sb.cornerRadius(15);
        sb.duration(Snackbar.LENGTH_LONG);
        sb.show();
    }
}
