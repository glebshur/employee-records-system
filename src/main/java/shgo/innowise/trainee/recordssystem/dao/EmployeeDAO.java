package shgo.innowise.trainee.recordssystem.dao;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import shgo.innowise.trainee.recordssystem.config.ConnectionProvider;
import shgo.innowise.trainee.recordssystem.entity.Employee;
import shgo.innowise.trainee.recordssystem.entity.Role;

/**
 * DAO for Employee entity.
 */
@Slf4j
public class EmployeeDAO {

    private static final String SELECT_ALL_QUERY = "SELECT * FROM employee";
    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM employee WHERE emp_id = ?";
    private static final String SELECT_ROLES_BY_ID_QUERY = "SELECT * FROM employee_role "
            + "WHERE emp_id = ?";
    private static final String INSERT_QUERY = "INSERT INTO employee(first_name, last_name, "
            + "middle_name, email, password, creation_date) VALUES(?,?,?,?,?,?)";
    private static final String INSERT_ROLES_QUERY = "INSERT INTO employee_role(emp_id, role_id) "
            + "VALUES(?,?)";
    private static final String UPDATE_QUERY = "UPDATE employee SET first_name = ?,"
            + "last_name = ?, middle_name = ? WHERE emp_id = ?";
    private static final String DELETE_ROLES_QUERY = "DELETE FROM employee_role WHERE emp_id = ?";
    private static final String DELETE_QUERY = "DELETE FROM employee WHERE emp_id = ?";

    private static final String ID = "emp_id";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String MIDDLE_NAME = "middle_name";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String CREATION_DATE = "creation_date";
    private static final String ROLE_ID = "role_id";

    private static EmployeeDAO instance;

    private EmployeeDAO() {
    }

    /**
     * Gives instance of EmployeeDAO object.
     *
     * @return instance of EmployeeDAO
     */
    public static EmployeeDAO getInstance() {
        if (instance == null) {
            instance = new EmployeeDAO();
        }
        return instance;
    }

    /**
     * Finds employee in database by id.
     *
     * @param id employee id
     * @return employee
     */
    public Optional<Employee> get(long id) {
        Optional<Employee> employeeOptional = Optional.empty();
        try (Connection connection = ConnectionProvider.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID_QUERY)) {
            statement.setLong(1, id);

            // select employee's data
            ResultSet employeeResultSet = statement.executeQuery();
            if (employeeResultSet.next()) {
                Employee employee = fillInEmployee(employeeResultSet);
                employeeOptional = Optional.of(employee);
            }

            // select employee's roles
            if (employeeOptional.isPresent()) {
                Employee employee = employeeOptional.get();
                employee.setRoles(getRoles(employee.getId(), connection));
            }

        } catch (SQLException e) {
            log.error(e.getSQLState());
            log.error(e.getMessage());
        }
        return employeeOptional;
    }

    /**
     * Finds all employees in database.
     *
     * @return employees list
     */
    public List<Employee> getAll() {
        List<Employee> employees = new ArrayList<>();

        try (Connection connection = ConnectionProvider.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_QUERY)) {

            // select employee's data
            ResultSet employeeResultSet = statement.executeQuery();
            while (employeeResultSet.next()) {
                Employee employee = fillInEmployee(employeeResultSet);
                employee.setRoles(getRoles(employee.getId(), connection));
                employees.add(employee);
            }


        } catch (SQLException e) {
            log.error(e.getSQLState());
            log.error(e.getMessage());
        }
        return employees;
    }

    /**
     * Inserts employee data in database.
     *
     * @param employee employee to create
     * @return created employee
     */
    public Employee create(Employee employee) {
        try (Connection connection = ConnectionProvider.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_QUERY,
                     Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, employee.getFirstName());
            statement.setString(2, employee.getLastName());
            statement.setString(3, employee.getMiddleName());
            statement.setString(4, employee.getEmail());
            statement.setString(5, employee.getPassword());
            statement.setString(6, employee.getCreationDate()
                    .format(DateTimeFormatter.BASIC_ISO_DATE));

            ResultSet key = statement.executeQuery();
            if (key.next()) {
                employee.setId(key.getLong(ID));
                insertRoles(employee, connection);
            }
        } catch (SQLException e) {
            log.error(e.getSQLState());
            log.error(e.getMessage());
        }

        return employee;
    }

    /**
     * Updates employee data in database.
     *
     * @param employee employee to update
     * @return updated employee
     */
    public Employee update(Employee employee) {
        try (Connection connection = ConnectionProvider.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {

            statement.setString(1, employee.getFirstName());
            statement.setString(2, employee.getLastName());
            statement.setString(3, employee.getMiddleName());
            statement.setLong(4, employee.getId());

            // delete old roles
            PreparedStatement deleteRolesStatement = connection.prepareStatement(DELETE_ROLES_QUERY);
            deleteRolesStatement.setLong(1, employee.getId());
            deleteRolesStatement.close();

            insertRoles(employee, connection);
        } catch (SQLException e) {
            log.error(e.getSQLState());
            log.error(e.getMessage());
        }

        return employee;
    }

    /**
     * Deletes employee from database.
     *
     * @param employee employee to delete
     */
    public void delete(Employee employee) {
        try (Connection connection = ConnectionProvider.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setLong(1, employee.getId());
            statement.execute();
        } catch (SQLException e) {
            log.error(e.getSQLState());
            log.error(e.getMessage());
        }
    }

    /**
     * Selects roles for employee from roles table.
     *
     * @param employeeId employee's id
     * @param connection current connection
     * @return set of roles
     */
    private Set<Role> getRoles(long employeeId, Connection connection) {
        Set<Role> roles = new HashSet<>();

        try (PreparedStatement roleSelect = connection.prepareStatement(SELECT_ROLES_BY_ID_QUERY)) {
            roleSelect.setLong(1, employeeId);

            ResultSet roleResultSet = roleSelect.executeQuery();
            while (roleResultSet.next()) {
                roles.add(Role.values()[roleResultSet.getInt(ROLE_ID)]);
            }
        } catch (SQLException e) {
            log.error(e.getSQLState());
            log.error(e.getMessage());
        }
        return roles;
    }

    /**
     * Fills employee object with data from result set.
     *
     * @param employeeResultSet result set with employee data
     * @return employee
     * @throws SQLException sql exception
     */
    private Employee fillInEmployee(ResultSet employeeResultSet) throws SQLException {
        Employee employee = new Employee();
        employee.setId(employeeResultSet.getLong(ID));
        employee.setFirstName(employeeResultSet.getString(FIRST_NAME));
        employee.setLastName(employeeResultSet.getString(LAST_NAME));
        employee.setMiddleName(employeeResultSet.getString(MIDDLE_NAME));
        employee.setEmail(employeeResultSet.getString(EMAIL));
        employee.setPassword(employeeResultSet.getString(PASSWORD));
        employee.setCreationDate(LocalDateTime.parse(employeeResultSet.getString(CREATION_DATE),
                DateTimeFormatter.BASIC_ISO_DATE));
        return employee;
    }

    /**
     * Inserts roles in database.
     *
     * @param employee   employee with roles to save
     * @param connection current connection
     * @throws SQLException sql exception
     */
    private void insertRoles(Employee employee, Connection connection) throws SQLException {
        for (Role role : employee.getRoles()) {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ROLES_QUERY);
            preparedStatement.setLong(1, employee.getId());
            preparedStatement.setLong(2, role.ordinal());
            preparedStatement.execute();
            preparedStatement.close();
        }
    }
}
