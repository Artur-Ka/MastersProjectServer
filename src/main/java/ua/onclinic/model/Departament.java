package ua.onclinic.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import ua.onclinic.model.instance.EmployeeInstance;

public class Departament extends AbstractInstance
{
	private final CopyOnWriteArrayList<EmployeeInstance> _employees = new CopyOnWriteArrayList<>();
	
	public List<EmployeeInstance> getEmployees()
	{
		final List<EmployeeInstance> employees = new ArrayList<>(_employees.size());
		employees.addAll(_employees);
		return employees;
	}
	
	public EmployeeInstance getEmployee(int index)
	{
		return getEmployees().get(index);
	}
	
	public synchronized void setEmployees(List<EmployeeInstance> employees)
	{
		_employees.clear();
		_employees.addAll(employees);
	}
	
	public synchronized void addEmployees(List<EmployeeInstance> employees)
	{
		_employees.addAllAbsent(employees);
	}
	
	public synchronized void addEmployee(EmployeeInstance employee)
	{
		_employees.addIfAbsent(employee);
	}
	
	public synchronized void removeEmployee(EmployeeInstance employee)
	{
		_employees.remove(employee);
	}
}