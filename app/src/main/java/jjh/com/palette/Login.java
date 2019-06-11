package jjh.com.palette;

//싱글톤 패턴 구현 방법
//https://dreamlog.tistory.com/495
public final class Login {

    private static Login login = new Login();
    private String id; //로그인된 아이디
    private boolean loginState; //현재 로그인 상태

    private Login() {
        id = null;
        loginState = false;
    }

    public String getId() {
        return id;
    }
    public void setId(String id){
        this.id = id;
    }

    public boolean getLoginState(){
        return loginState;
    }

    public  void setLoginState(boolean loginState){
        this.loginState = loginState;
    }

    public static Login getInstance(){
        return login;
    }
}
