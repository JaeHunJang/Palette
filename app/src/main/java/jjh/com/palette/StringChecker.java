package jjh.com.palette;
import java.util.regex.Pattern;

public class StringChecker {

    Pattern p;

    StringChecker(){
        p = Pattern.compile("^[a-zA-Z0-9]+$"); //영문, 숫자 패턴
    }

    StringChecker(String patterns){
        p = Pattern.compile(patterns);
    }

    boolean strPatternCheck(CharSequence str){
        if (str.toString().equals(""))
            return false;
        return p.matcher(str).matches();
    }
}
