<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <#include "/common/header.html"/>
    <title>AIinfo - 荣誉榜</title>
</head>
<body>
    <div id="wrapper">
	    <#if userType == 1>
			<#include "navigation1.html"/>
		<#else>
		    <#include "navigation.html"/>
		</#if>
		<div class="container-fluid" id="booksList">
		    <div class="alert alert-warning" role="alert">
				<span class="glyphicon glyphicon-heart"></span> ${wisdom}
			</div>
			<h2><span class="glyphicon glyphicon-heart-empty"></span> 捐书榜 <span class="glyphicon glyphicon-heart-empty"></span></h2>
			<#if userBooks??>
			    <#list userBooks as userBook>
					  <div class="alert alert-info" role="alert">
					    <span class="glyphicon glyphicon-book"></span> ${userBook.bookSource}--<#list 1..userBook.bookNum as s><span class="glyphicon glyphicon-thumbs-up"></span> </#list>
					  </div>
			    </#list>
			</#if>
	    </div>
	</div>
	<#include "/common/footer.html" />
</body>
</html>