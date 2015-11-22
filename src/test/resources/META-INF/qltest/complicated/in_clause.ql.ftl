SELECT *
FROM SOMEWHERE
WHERE
<#list userIds!>
user_id in (<#items as userId>${param(userId)}<#sep>,</#sep></#items>)
<#else>
user_id IS NOT NULL
</#list>