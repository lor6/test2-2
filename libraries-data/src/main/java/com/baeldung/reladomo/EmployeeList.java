package com.baeldung.reladomo;
import com.gs.fw.finder.Operation;
import java.util.*;
public class EmployeeList extends EmployeeListAbstract
{
	public EmployeeList()
	{
		super();
	}

	public EmployeeList(int initialSize)
	{
		super(initialSize);
	}

	public EmployeeList(Collection c)
	{
		super(c);
	}

	public EmployeeList(Operation operation)
	{
		super(operation);
	}
}
