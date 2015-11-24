UPDATE sometable
<@ql.trim prefix="SET " suffixOverrides=[","]>
    <#if user.name??>name = ${param(user.name)},</#if>
    <#if user.birthyear gt 0>birthyear = ${param(user.birthyear)},</#if>
    <#if user.employeeType??>employeeType = ${para(user.employyType)}</#if>
</@ql.trim>

WHERE
user.id = ${param(userId)}

