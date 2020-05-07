<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<html>
<head>
<title>Hello World!</title>
</head>
<body>

<!-- HTMLとしてのコメントです。 -->
<h1>Hello World!</h1>

<%-- JSPとしてのコメントです。 --%>
<%
// Javaとしての行コメントです。
out.println(new java.util.Date());

/* Javaとしてのブロックコメントです。
　 これはコメントです。
// これもコメントです。
   これもまたコメントです。 */

%>

</body>
</html><!-- 末端のコメントです。
-->