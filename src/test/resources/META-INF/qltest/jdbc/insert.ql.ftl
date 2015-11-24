INSERT INTO users (userId, name, birthyear, employeeType)
VALUES(${userId}, ${param(user.name)}, ${user.birthyear}, ${param(user.employeeType, 'enumToName')})