package testSettings;

import org.apache.commons.lang3.RandomStringUtils;

public class CourierLoginCredentials {

    String login;
    String password;

    public CourierLoginCredentials() {
    }

    public CourierLoginCredentials(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}