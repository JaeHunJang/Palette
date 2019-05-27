package jjh.com.palette;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class UserInfoFragment extends Fragment {
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView id, birth;
        TextInputLayout til_pw, til_nick, til_hint;
        TextInputEditText pw, nick, hint;

        id = view.findViewById(R.id.ui_tv_id);
        birth = view.findViewById(R.id.ui_tv_birth);
        pw = view.findViewById(R.id.ui_tv_pw);
        nick = view.findViewById(R.id.ui_tv_nick);
        hint = view.findViewById(R.id.ui_tv_hint);
        til_pw = view.findViewById(R.id.ui_til_pw);
        til_nick = view.findViewById(R.id.ui_til_nick);
        til_hint = view.findViewById(R.id.ui_til_hint);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View userInfo = inflater.inflate(R.layout.fragment_userinfo,container,false);

        return userInfo;
    }

}
