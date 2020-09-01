package com.anikrakib.tourday.Fragment;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anikrakib.tourday.Activity.ExploreActivity;
import com.anikrakib.tourday.Activity.LocationActivity;
import com.anikrakib.tourday.Activity.SignInActivity;
import com.anikrakib.tourday.R;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;


public class EditProfile extends Fragment {

    TextView userEmailTextView,userLocationTextView,userNameTextView;
    EditText userEmailEditText;
    ImageButton saveEmailImageButton;
    LinearLayout userEmailLayout;
    Intent intent;

    public EditProfile() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        userEmailLayout = view.findViewById(R.id.editEmailLayout);
        userEmailTextView = view.findViewById(R.id.editEmailTextView);
        userLocationTextView = view.findViewById(R.id.editLocationTextView);
        userEmailEditText = view.findViewById(R.id.editEmailEditText);
        saveEmailImageButton = view.findViewById(R.id.clickOkImageButton);
        userNameTextView = view.findViewById(R.id.editUsernameTextView);

        userEmailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        userEmailTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                userEmailLayout.setVisibility(View.VISIBLE);
                userEmailEditText.setText(userEmailTextView.getText());
                userEmailTextView.setVisibility(View.GONE);
                return true;
            }
        });
        userLocationTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                intent = new Intent(getActivity(),LocationActivity.class);
                intent.putExtra("recentLocation",userLocationTextView.getText());
                getActivity().startActivity(intent);
                return true;
            }
        });
        userEmailTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DynamicToast.makeWarning(getContext(), "Press Long Click To Edit Email").show();
            }
        });
        userLocationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DynamicToast.makeWarning(getContext(), "Press Long Click To Edit Location").show();
            }
        });
        userNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DynamicToast.makeWarning(getContext(), "Username Can't be Changed").show();
            }
        });

        return view;

    }

    private void checkInputs() {
        if (!TextUtils.isEmpty(userEmailEditText.getText())) {
            saveEmailImageButton.setEnabled(true);
            saveEmailImageButton.setColorFilter(ContextCompat.getColor(getContext(),R.color.black));
        } else {
            saveEmailImageButton.setEnabled(false);
            saveEmailImageButton.setColorFilter(ContextCompat.getColor(getContext(),R.color.color_secondary_text));
        }
    }
}