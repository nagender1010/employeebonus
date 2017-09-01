package imcs.core.serviceImpl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import imcs.core.model.Credentials;
import imcs.core.model.Department;
import imcs.core.model.Employee;
import imcs.core.service.EmployeeService;
import trng.imcs.core.dao.EmployeeDAO;
import trng.imcs.core.dao.impl.EmployeeDAOImpl;
import trng.imcs.util.EmpUtil;

public class EmployeeServiceImpl implements EmployeeService {
	final Logger logger = Logger.getLogger(EmployeeServiceImpl.class);
	EmployeeDAO dao = new EmployeeDAOImpl();

	public List<Employee> loadFromFile() {
		logger.info("Loading file from source");
		List<Employee> empList = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader("..\\EmpLibrary\\sourceFiles\\employeeInfo.txt"))) {
			String line;
			String[] d = null;
			int num = 0;
			while ((line = br.readLine()) != null) {
				d = line.split(",");
				String jDate = d[3];
				Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(jDate);
				String bDate = d[4];
				Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(bDate);
				Employee e = new Employee(Integer.parseInt(d[0]), d[1], Integer.parseInt(d[2]), date2, date1,
						Float.parseFloat(d[5]), Integer.parseInt(d[6]), Integer.parseInt(d[7]), d[8]);
				empList.add(e);
			}
		} catch (FileNotFoundException e1) {
			logger.error(e1.getMessage());
		} catch (IOException e1) {
			logger.error(e1.getMessage());
		} catch (ParseException e1) {
			logger.error(e1.getMessage());
		}
		return empList;
	}

	public List<Employee> getAllEmployeeDetails(int deptNo) throws SQLException {
		if (deptNo % 2 == 0) {
			return dao.getAll(deptNo, "dateOfBirth");
		}
		return dao.getAll(deptNo, "dateOfJoining");

	}

	@Override
	public void addEmployee(Employee e) throws SQLException {
		dao.addEmployee(e);
	}

	@Override
	public Employee getEmployee(int empId) throws SQLException {
		return dao.getEmployee(empId);
	}

	@Override
	public boolean updateEmployee(Employee e) throws SQLException {
		if (dao.updateEmployee(e)) {
			return true;
		}

		return false;
	}

	@Override
	public List<Employee> getAll(int deptId) throws SQLException {
		return dao.getAll(deptId);
	}

	@Override
	public boolean deleteEmployee(int empId) throws SQLException {
		return dao.deleteEmployee(empId);
	}

	@Override
	public Department getDepartment(int deptId) {
		Department department1 = null;
		List<Department> deptList = EmpUtil.getDepartmentList();
		for (Department department : deptList) {
			if (department.getId() == deptId) {
				return department;
			}
		}

		return department1;
	}

	@Override
	public boolean validateUser(String username, String password) {
		
		Credentials cred = new Credentials(username, password);
		if (new EmpUtil().getCredentials().contains(cred)) {
			return true;
		}
		return false;
	}

}
