package pl.uj.edu.student.tsp.guicedemo;

import com.google.inject.Guice;
import com.google.inject.Injector;

import javax.inject.Inject;

public class ClientApplication {

    @Inject
    private FacebookService concreteService;

    @Inject
    private MessageService serviceInterface;

    public boolean sendMessage(String msg, String rec) {
        concreteService.sendMessage(msg, rec);
        serviceInterface.sendMessage(msg, rec);

        return true;
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new SimpleModule());
        ClientApplication app = injector.getInstance(ClientApplication.class);

        app.sendMessage("Hi Pankaj", "pankaj@abc.com");
    }
}
 