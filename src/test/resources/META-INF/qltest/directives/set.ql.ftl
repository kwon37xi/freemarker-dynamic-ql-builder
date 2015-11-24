UPDATE sometable
<@ql.set>
    <#if user.name??>name = ${param(user.name)},</#if>
    <#if user.birthyear gt 0>birthyear = ${param(user.birthyear)},</#if>
    <#if user.employeeType??>employeeType = ${para(user.employyType)}</#if>
</@ql.set>

WHERE
user.id = ${param(userId)}

