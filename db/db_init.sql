DROP TABLE IF EXISTS employee_role;
DROP TABLE IF EXISTS employee;

CREATE TABLE employee
(
	emp_id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	first_name VARCHAR(255) NOT NULL,
	last_name VARCHAR(255),
	middle_name VARCHAR(255),
	email VARCHAR(255) UNIQUE NOT NULL,
	"password" VARCHAR(3000) NOT NULL,
	creation_date TIMESTAMP NOT NULL
);

CREATE TABLE employee_role
(
	emp_id BIGINT NOT NULL,
	role_id BIGINT NOT NULL,
	CONSTRAINT employee_role_emp_id_fkey FOREIGN KEY(emp_id)
	REFERENCES employee (emp_id) ON DELETE CASCADE
);

INSERT INTO employee(first_name, last_name, middle_name, email, creation_date,"password")
VALUES 	('Gleb', 'Shurov', 'Olegovich', 'gleb@gmail.com', '2023-03-19T09:33:00', '$2a$12$jHSB7.qQW.O.f4f3K8yJ5efVfA1uPhTZcIEJSmDw7Mrni4PchDDG.'),
		('Admin', 'Adminov', 'Adminovich', 'admin@admin.com', '2023-03-18T19:20:00', '$2a$12$XnjRpAogsTzS2DjWR8pFf.PI1nkoq7XrkZV4WSw/Yo36pxmfwkZIy'),
		('Ivan', 'Ivanov', 'Petrovich', 'ivan@gmail.com', '2023-03-19T09:33:00', '$2a$12$Sbu.WWE2acxdme37PXMI5u066xSdz5vbhD5pxDxwjr6qCxYOmO1B6');
		
INSERT INTO employee_role (emp_id, role_id)
VALUES 	(1, 0),
		(2, 0),
		(2, 1),
		(3, 0);