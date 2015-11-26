SELECT
a, b, c, d
FROM
somewhere
<@ql.where>
    <#if name??>
    name = ${param(name)}
    </#if>
    <#if createdAt??>
    AND createdAt >= ${param(createdAt)};
    </#if>
    <#list userIds!>
    AND userId in (
        <#items as userId>
        ${param(userId)}<#sep>,</#sep>
        </#items>
    )
    </#list>
</@ql.where>

ORDER BY a DESC
