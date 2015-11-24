SELECT userId, name, birthyear, employeeType
FROM users
WHERE
name LIKE ${param(name)}
ORDER BY name ASC