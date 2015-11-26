# Freemarker Dynamic QL(SQL/JPQL/HQL/...) Builder
[![kr.pe.kwonnam.jspMaven Central](https://maven-badges.herokuapp.com/maven-central/kr.pe.kwonnam.freemarkerdynamicqlbuilder/freemarker-dynamic-ql-builder/badge.svg)](https://maven-badges.herokuapp.com/maven-central/kr.pe.kwonnam.freemarkerdynamicqlbuilder/freemarker-dynamic-ql-builder)

Do you need to build native SQL dynamically in Java application? But are you worried about how to bind query parameters? Look at the following example.

When you have this file `users/select.ql.ftl`

```sql
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
```

Run this template with the following java code

```java
FreemarkerDynamicQlBuilder dynamicQlBuilder = ....;

User user = new User();
user.setName(""); // empty on purpose
user.setBirthyear(2015);
user.setEmployeeType(EmployeeType.FULLTIME);

Map<String,Object> dataModel = new HashMap<String,Object>();
dataModel.put("user", user);
dataModel.put("userIds", new int[]{100, 200, 300});

DynamicQuery dynamicQuery = = dynamicQlBuilder.buildQuery("users/select", dataModel);
// dynamicQuery 에 생성된 QL과 파리머터 목록이 들어 있다.
```

Then you will get `DynamicQuery` object with the following properties
```
dynamicQuery.getQueryString() 
==> String
"SELECT *
FROM somewhere
WHERE birthyear = ?
    AND employeeType = ?
    AND userId IN (?,?,?)
ORDER BY userId
LIMIT 10"

dynamicQuery.getParameters()
==> List<Object> : [2015, FULLTIME, 100, 200, 300] 

dynamicQuery.getQueryParameterArray()
==> Object[] : [2015, FULLTIME, 100, 200, 300] 
```
## Getting Started

* [Freemarker Dynamic QL Builder 시작하기 - 한국어](https://github.com/kwon37xi/freemarker-dynamic-ql-builder/wiki/GettingStarted_KO)
* [Getting Started Freemarker Dynamic QL Builder - English](https://github.com/kwon37xi/freemarker-dynamic-ql-builder/wiki/GettingStarted_EN)

## Requirements
  * Java 6 or later
  * This library depends on only [freemarker](http://freemarker.org) 2.3.23+ and [slf4j](http://www.slf4j.org/).