<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <#include "/common/header.html"/>
    <meta name="description" content="">
    <meta name="author" content="">
    <title>首页</title>
</head>
<body>
    <div id="wrapper">
            <div class="container-fluid">
				<div class="alert alert-info" role="alert">
					<h3 class="page-header">用户名:${userName}</h3>
					<h3 class="page-header">账号:${userTel}</h3>
					<h3 class="page-header">类型:${userType}</h3>
				</div>
            </div>
    </div>
    <!-- /#wrapper -->
    <#include "/common/footer.html" />
</body>
</html>