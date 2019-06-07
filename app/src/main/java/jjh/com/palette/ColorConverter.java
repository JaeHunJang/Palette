package jjh.com.palette;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.util.Log;

import java.math.BigInteger;
import java.util.Locale;

//https://webnautes.tistory.com/1130
//16진수 변환방법 인용
public class ColorConverter{
    int[] rgb;
    int hex = 9;
    ColorConverter() {
        rgb = new int[3];
        hex = 9;
    }

    public int[] getRgb() {
        return rgb;
    }

    public int getHex() {

        return hex;
    }

    @TargetApi(27)
    public int setRgb(int a,int r, int g, int b) {
        String temp = "";
        rgb[0] = r; rgb[1] =g; rgb[2] = b;
        String[] color = {Integer.toHexString(r),Integer.toHexString(g),Integer.toHexString(b)};
        for (int i = 0; i < rgb.length; i++) {
            color[i] = color[i].length() == 1 ? ("0" + color[i]) : color[i];
            temp += color[i];
        }
        //hex = Color.toArgb(Color.parseColor(temp));// hexStringToByte(temp);
        //hex = hexStringToByte(temp);
        Log.d("hexS",Color.rgb(r,g,b)+"");
        //Log.d("hexS",hex+"");
        return Color.argb(a,r,g,b);
    }
    public static byte hexStringToByte(String s) {
        int len = s.length();
        byte data = 0;
        for (int i = 0; i < len; i += 6) {
            data = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
    public static String byteToHexString(byte[] bytes){
        StringBuilder sb = new StringBuilder();
        for(byte b : bytes){
            sb.append(String.format("%06X", b&0xff));
        }
        return sb.toString();
    }

    public static String getHexCode(int color) {
        int a = Color.alpha(color);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        return String.format(Locale.getDefault(), "%02X%02X%02X%02X", a, r, g, b);
    }

    public static int[] getColorARGB(int color) {
        int[] argb = new int[4];
        argb[0] = Color.alpha(color);
        argb[1] = Color.red(color);
        argb[2] = Color.green(color);
        argb[3] = Color.blue(color);
        return argb;
    }
}
