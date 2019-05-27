package jjh.com.palette;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;
import com.skydoves.colorpickerview.sliders.BrightnessSlideBar;

import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class NewThemeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View newTheme = inflater.inflate(R.layout.fragment_newtheme,container,false);



        final ColorPickerView colorPickerView = newTheme.findViewById(R.id.colorPickerView);

        //final AlphaSlideBar alphaSlideBar = findViewById(R.id.alphaSlideBar);
        BrightnessSlideBar brightnessSlideBar = newTheme.findViewById(R.id.brightnessSlide);


        colorPickerView.setColorListener(new ColorEnvelopeListener() {
            @Override
            public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {

                Log.d("dd",envelope.getHexCode()+"/"+Arrays.toString(envelope.getArgb()));
            }
        });

        //colorPickerView.setPureColor();
        Log.d("ddd",colorPickerView.getColor()+"/"+colorPickerView.getColorEnvelope());
        //colorPickerView.attachAlphaSlider(alphaSlideBar);
        colorPickerView.attachBrightnessSlider(brightnessSlideBar);
        return newTheme;
    }
}
