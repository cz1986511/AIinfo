<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <#include "/common/header.html"/>
    <meta name="description" content="">
    <meta name="author" content="">
    <title>信息管理</title>
</head>
<body>
    <div id="wrapper">
	    <#if userType == 1>
		  <#include "navigation1.html"/>
		<#else>
		  <#include "navigation.html"/>
        </#if>
		<!-- Page Content -->
        <div id="page-wrapper">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-lg-12">
                        <h1 class="page-header">欢迎${userName}</h1>
                    </div>
                    <!-- /.col-lg-12 -->
                </div>
            </div>
            <!-- /.container-fluid -->
        </div>
        <!-- /#page-wrapper -->
    </div>
    <!-- /#wrapper -->
    <#include "/common/footer.html" />
</body>
</html>