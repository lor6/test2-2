package com.baeldung.examples.guice.modules;

import com.baeldung.examples.common.AccountService;
import com.baeldung.examples.common.AccountServiceImpl;
import com.baeldung.examples.common.BookService;
import com.baeldung.examples.common.BookServiceImpl;
import com.baeldung.examples.guice.Foo;
import com.baeldung.examples.guice.Person;
import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Provides;

public class GuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        try {
            bind(AccountService.class).to(AccountServiceImpl.class);
            bind(Foo.class).toProvider(new Provider<Foo>() {
                public Foo get() {
                    return null;
                }
            });

            bind(Person.class).toConstructor(Person.class.getConstructor());
            // bind(Person.class).toProvider(new Provider<Person>() {
            // public Person get() {
            // Person p = new Person();
            // return p;
            // }
            // });
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Provides
    public BookService bookServiceGenerator() {
        return new BookServiceImpl();
    }

}
