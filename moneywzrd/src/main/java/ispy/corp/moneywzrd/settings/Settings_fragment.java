package ispy.corp.moneywzrd.settings;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import ispy.corp.moneywzrd.Login_main;
import ispy.corp.moneywzrd.R;

public class Settings_fragment extends Fragment {

    private SettingsFragmentViewModel mViewModel;
    View rootView;

    public static Settings_fragment newInstance() {
        return new Settings_fragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.settings_fragment, container, false);
        Button logout = (Button)rootView.findViewById(R.id.signOut);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getContext(), "Successfully logged out!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getContext(), Login_main.class));
            }
        });


        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SettingsFragmentViewModel.class);
        // TODO: Use the ViewModel
    }

}