package com.baeldung.drools;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import com.baeldung.drools.model.Fact;
import com.baeldung.drools.model.Result;

public class BackwardChaining {
    public static void main(String[] args) {
        Result result = new BackwardChaining().backwardChaining();
        System.out.println(result.getValue());
        result.getFacts().stream().forEach(System.out::println);
    }

    public Result backwardChaining() {
        Result result = new Result();
        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.getKieClasspathContainer();
        KieSession ksession = kContainer.newKieSession("ksession-backward-chaining");
        ksession.setGlobal("result", result);
        ksession.insert(new Fact("Asia", "Planet Earth"));
//        ksession.insert(new Fact("China", "Asia"));
        ksession.insert(new Fact("Great Wall of China", "China"));

        ksession.fireAllRules();

        return result;

    }

}