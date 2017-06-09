<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <#include "/common/header.html"/>
    <meta name="description" content="">
    <meta name="author" content="">
    <title>AIinfo - 首页</title>
</head>
<body>
    <div id="wrapper">
	    <#if userType == 1>
		  <#include "navigation1.html"/>
		<#else>
		  <#include "navigation.html"/>
        </#if>
            <div class="container-fluid">
				<div class="alert alert-info" role="alert">
					<h3 class="page-header">欢迎${userName}</h3>
					<span class="glyphicon glyphicon-heart"></span> ${wisdom}
				</div>
            </div>
    </div>
    <!-- /#wrapper -->
    <#include "/common/footer.html" />
</body>
</html>