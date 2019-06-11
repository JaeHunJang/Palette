package jjh.com.palette;
import java.util.regex.Pattern;

//문자열 패턴을 확인하는 클래스
public class StringChecker {

    Pattern p;

    StringChecker(){
        p = Pattern.compile("^[a-zA-Z0-9]+$"); //영문, 숫자 패턴
    }

    boolean strPatternCheck(CharSequence str){
        if (str.toString().equals(""))
            return false;
        return p.matcher(str).matches();
    }
}
