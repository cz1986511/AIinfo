<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <#include "/common/header.html"/>
    <title>books</title>
</head>
<body>
    <div id="wrapper">
		<#include "navigation.html"/>
        <div class="container-fluid">
		    <span class="glyphicon glyphicon-heart"></span> ${wisdom}
			<h2><span class="glyphicon glyphicon-heart-empty"></span> 图书池 <span class="glyphicon glyphicon-heart-empty"></span></h2>
			<#if books??>
			    <#list books as book>
					  <div class="alert alert-info" role="alert">
					    <span class="glyphicon glyphicon-book"></span> ${book.bookName}--<#if book.status == '02'>锁定<#elseif book.status == '03'>已借出<#else>未借出--<a href="book_borrow.html?bookId=${book.bookId}" class="alert-link">借阅</a></#if>
					  </div>
			    </#list>
			</#if>
	    </div>
	</div>
	<#include "/common/footer.html" />
</body>
</html>