SELECT userId, name, birthyear, employeeType FROM users
WHERE userId = ${param(userId)}