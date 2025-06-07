CREATE TABLE students (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          email VARCHAR(100) UNIQUE NOT NULL
);


CREATE TABLE courses (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         title VARCHAR(100) NOT NULL,
                         description TEXT,
                         created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE enrollments (
                             student_id INT,
                             course_id INT,
                             enrollment_date DATE,
                             grade CHAR(2),
                             PRIMARY KEY (student_id, course_id),
                             FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
                             FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
);
