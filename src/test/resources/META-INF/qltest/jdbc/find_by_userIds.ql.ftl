SELECT userId, name, birthyear, employeeType
FROM users
<@ql.where>
    <#list userIds!>
    userId in (
        <#items as userId>
        ${param(userId)}<#sep>,</#sep>
        </#items>
    )
    </#list>
</@ql.where>

ORDER BY userId ASC