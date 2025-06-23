# User Management System - Spring Boot + JWT + Soft Delete

## 🛠 Công nghệ sử dụng
- Java 17
- Spring Boot 3.x
- Spring Security
- Hibernate JPA
- MySQL
- Maven

## 📁 Cấu trúc dự án
src/main/java/org/example/user_management/
├── controller/              # AuthController
├── DTO/                    # DTOs: Login, Register, Response
├── entity/                 # Entity: User
├── repository/             # UserRepository
├── config/                 # SecurityConfig
├── service/
│   ├── interfaces/         # IUserService
│   └── implêmnt/               # UserService
├── exception/              # GlobalExceptionHandler (validation, login...)
└── UserManagementApplication.java  # Main entry point
## ⚙️ Cấu hình
### 📄 `application.properties`
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/user_db
spring.datasource.username=root
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.security.user.name=admin
spring.security.user.password=admin
spring.security.user.roles=USER
jwt.secret=my_super_secret_256bit_key_that_should_be_secure

▶️ Cài đặt và chạy
bash
Copy code
# 1. Clone project
$ git clone https://github.com/Pham-Vung/user-management-test-DTS.git

# 2. Mở bằng IntelliJ / VS Code

# 3. Cài đặt dependency
$ mvn clean install

# 4. Chạy ứng dụng
$ mvn spring-boot:run

Các API chính
✅ Đăng ký
POST /auth/register

🔐 Đăng nhập
POST /auth/login

Soft Delete với Hibernate
Dự án này sử dụng Soft Delete để không xóa bản ghi khỏi database, thay vào đó đánh dấu là đã xóa.

✅ Cấu hình trong entity User

@Entity
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET deleted = true WHERE id = ?")
@FilterDef(name = "deletedFilter", parameters = @ParamDef(name = "isDeleted", type = Boolean.class))
@Filter(name = "deletedFilter", condition = "deleted = :isDeleted")
public class User {
    ...
    private Boolean deleted = false;
}

 Ví dụ xoá mềm
Thay vì DELETE FROM users WHERE id = ?, Hibernate thực hiện:
UPDATE users SET deleted = true WHERE id = ?
