<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>信息管理</title>
    <#include "/common/header.html"/>
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