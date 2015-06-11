package pl.uj.edu.student.tsp.guicedemo;

import com.google.inject.AbstractModule;

public class SimpleModule extends AbstractModule {

    @Override
    protected void configure() {
        //bind MessageService to Facebook Message implementation
        bind(MessageService.class).to(FacebookService.class);
    }

}