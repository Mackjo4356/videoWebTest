<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title></title>
</head>
<body onload="myfunction()">
<input type="hidden" value=${msg}  id="msg">
<%=request.getAttribute("msg")%>
<script>
function myfunction(){
    var msgElement = document.getElementById("msg");
    if(msgElement != null&&msgElement.value !=null){
        alert(msgElement.value);
        window.location.href="http://localhost:8080/customer/information.jsp";
    }
    else
    {
        console.log("msg为空");
    }

}
</script>
</body>
</html>