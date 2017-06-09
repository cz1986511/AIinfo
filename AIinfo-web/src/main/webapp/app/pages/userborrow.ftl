<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <#include "/common/header.html"/>
    <title>我的 - 图书借阅记录</title>
</head>
<body>
    <div id="wrapper">
		<#if userType == 1>
		  <#include "navigation1.html"/>
		<#else>
		  <#include "navigation.html"/>
        </#if>
        <div class="container-fluid">
		    <div class="alert alert-warning" role="alert">
				<span class="glyphicon glyphicon-heart"></span> ${wisdom}
			</div>
			<h2><span class="glyphicon glyphicon-heart-empty"></span> 图书借阅记录 <span class="glyphicon glyphicon-heart-empty"></span></h2>
			<#if bookBorrows??>
			    <#list bookBorrows as bookBorrow>
					<div class="alert alert-info" role="alert">
					    <span class="glyphicon glyphicon-book"></span> ${bookBorrow.bookName}--${bookBorrow.userName}<#if bookBorrow.status == "01">--待审批<#elseif bookBorrow.status == "02">--已通过<#elseif bookBorrow.status == "03">--已还书<#elseif bookBorrow.status == "04">--已驳回</#if>
					</div>
			    </#list>
			</#if>
	    </div>
	</div>
	<#include "/common/footer.html" />
</body>
</html>