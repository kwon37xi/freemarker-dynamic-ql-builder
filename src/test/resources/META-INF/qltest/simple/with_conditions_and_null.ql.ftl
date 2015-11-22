SELECT
name,
birthyear,
employeetype
FROM users
WHERE
1 = 1
<#if user.name??>
AND name=${param(user.name)}
</#if>

AND employeetype=${param(user.employeetype)}

<#if user.birthyear gt 0>
AND birthyear=${param(user.birthyear)}
</#if>
ORDER BY name
