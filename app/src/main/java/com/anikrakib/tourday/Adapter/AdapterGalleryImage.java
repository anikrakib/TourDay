package com.anikrakib.tourday.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.anikrakib.tourday.Models.PostItem;
import com.anikrakib.tourday.R;
import com.anikrakib.tourday.WebService.RetrofitClient;
import com.makeramen.roundedimageview.RoundedImageView;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;
import com.squareup.picasso.Picasso;
import com.tylersuehr.socialtextview.SocialTextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class AdapterGalleryImage extends RecyclerView.Adapter<AdapterGalleryImage.ExampleViewHolder> {
    private Context mContext;
    private ArrayList<PostItem> mPostItemList;
    Dialog myDialog;
    String postId;

    public AdapterGalleryImage(Context context, ArrayList<PostItem> exampleList) {
        mContext = context;
        mPostItemList = exampleList;
    }
    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.gallery_image_item, parent, false);
        return new ExampleViewHolder(v);
    }
    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {

        final PostItem currentItem = mPostItemList.get(position);
        String imageUrl = currentItem.getImageUrl();
        String post = currentItem.getPost();
        String date = currentItem.getDate();
        String location = currentItem.getLocation();
        int likeCount = currentItem.getLikeCount();
        postId = currentItem.getmId();

        Picasso.get().load("https://www.tourday.team/"+imageUrl).into(holder.galleryImage);

    }
    @Override
    public int getItemCount() {
        return mPostItemList.size();
    }
    public class ExampleViewHolder extends RecyclerView.ViewHolder{

        RoundedImageView galleryImage;

        public ExampleViewHolder(View itemView) {
            super(itemView);

            galleryImage = itemView.findViewById(R.id.galleryImageItem);
        }
    }

}