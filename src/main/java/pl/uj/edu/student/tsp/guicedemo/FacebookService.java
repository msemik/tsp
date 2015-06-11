package pl.uj.edu.student.tsp.guicedemo;

/**
 * Created by michal on 11.06.15.
 */
public class FacebookService implements MessageService {
    @Override
    public boolean sendMessage(String msg, String rec) {

        System.out.println("Facebook message..");
        return false;
    }
}
