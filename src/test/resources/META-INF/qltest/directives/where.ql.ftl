SELECT *
FROM somewhere
<@ql.where>
    <#if user.name?has_content>
    name = ${param(user.name)}
    </#if>
    <#if user.birthyear gt 0>
    AND birthyear = ${param(user.birthyear)}
    </#if>
    <#if user.employeeType??>
    AND employeeType = ${param(user.employeeType, 'enumToName')}
    </#if>
    <#list userIds!>
    AND userId IN (<#items as userId>${param(userId)}<#sep>,</#sep></#items>)
    </#list>
</@ql.where>

ORDER BY userId
LIMIT 10
