package com.baeldung.springbootmvc.nosuchbeandefinitionexception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BeanA {
	
	@Autowired
	BeanB dependency;

}