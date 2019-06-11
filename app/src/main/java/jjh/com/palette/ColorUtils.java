package jjh.com.palette;

import android.graphics.Color;

import java.util.Locale;

//색상 정보를 컨트롤 하는 클래스
public class ColorUtils {

    public static int[] toColorARGB(int color) { //색상을 argb 값으로 변경해 저장
        int[] argb = new int[4];
        argb[0] = Color.alpha(color);
        argb[1] = Color.red(color);
        argb[2] = Color.green(color);
        argb[3] = Color.blue(color);
        return argb;
    }

    //https://www.ginifab.com/feeds/pms/cmyk_to_rgb.php 인용
    public static void RGBToCMYK(int[] argb, int[] cmyk){ //argb 값을 cmyk 값으로 변환해 저장
        float _r = ((float) argb[1] / 255.0f);
        float _g = ((float) argb[2] / 255.0f);
        float _b = ((float) argb[3] / 255.0f);
        float key = 1.0f - max(_r, _g, _b);
        cmyk[0] = Math.round(((1.0f - _r - key) / (1.0f - key))*100.0f);
        cmyk[1] = Math.round(((1.0f - _g - key) / (1.0f - key))*100.0f);
        cmyk[2] = Math.round(((1.0f - _b - key) / (1.0f - key))*100.0f);
        cmyk[3] = Math.round(key*100.0f);
        for (int i = 0; i <cmyk.length; i++){
            if (cmyk[i] < 0)
                cmyk[i] = 0;
            if (cmyk[i] > 100)
                cmyk[i] = 100;
        }
    }

    public static void CMYKToRGB(int[] cmyk, int[] argb){ //cmyk 값을 rgb 값으로 변환해 저장
        float key = ((float)cmyk[3] / 100 );
        argb[1] = (int)(255 * ( 1 - ((float)cmyk[0] / 100) ) * ( 1 - key));
        argb[2] = (int)(255 * ( 1 - ((float)cmyk[1] / 100) ) * ( 1 - key));
        argb[3] = (int)(255 * ( 1 - ((float)cmyk[2] / 100) ) * ( 1 - key));
        for (int i = 1; i <argb.length; i++){
            if (argb[i] < 0)
                argb[i] = 0;
            if (argb[i] > 255)
                argb[i] = 255;
        }
    }


    private static float max(float a, float b, float c) //최대값 검사
    {
        if (a > b && a > c)
            return a;
        if (b > a && b > c)
            return b;
        if (c > a && c > b)
            return c;
        return a;
    }
}
