<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <#include "/common/header.html"/>
    <title>bookborrow</title>
</head>
<body>
    <div id="wrapper">
		<#include "navigation1.html"/>
        <div class="container-fluid">
		    <div class="alert alert-warning" role="alert">
				<span class="glyphicon glyphicon-heart"></span> ${wisdom}
			</div>
			<h2><span class="glyphicon glyphicon-heart-empty"></span> 图书借阅记录 <span class="glyphicon glyphicon-heart-empty"></span></h2>
			<#if bookBorrows??>
			    <#list bookBorrows as bookBorrow>
					<div class="alert alert-info" role="alert">
					    <span class="glyphicon glyphicon-book"></span> ${bookBorrow.bookName}--${bookBorrow.userName}<#if bookBorrow.status == "01"><button type="button" class="btn btn-link" id="btn-bookBorrow" value="${book.bookId}"><strong>同意</strong></button><button type="button" class="btn btn-link" id="btn-bookBorrow" value="${book.bookId}"><strong>驳回</strong></button><#elseif bookborrow.status == "02"><button type="button" class="btn btn-link" id="btn-bookBorrow" value="${book.bookId}"><strong>还书</strong></button></#if>
					</div>
			    </#list>
			</#if>
	    </div>
	</div>
	<script type="text/javascript">
	  $('#btn-unread').click(function(){
	       var unread = document.getElementById("me-unread").value;
	       $.ajax({
	           type: "POST",
               url: "modify_book.action",
               data: {unread:unread},
               dataType: "json",
               success: function(data){
                 if(data.status){
                     location.reload(true);
                     alert("修改成功！");
                 } else {
                     location.reload(true);
                     alert(data.msg);
                 }
               }
	       });
	    });
		
		$('#btn-read').click(function(){
	       var read = document.getElementById("me-read").value;
	       $.ajax({
	           type: "POST",
               url: "modify_book.action",
               data: {read:read},
               dataType: "json",
               success: function(data){
                 if(data.status){
                     location.reload(true);
                     alert("修改成功！");
                 } else {
                     location.reload(true);
                     alert(data.msg);
                 }
               }
	       });
	    });
	</script>
	<#include "/common/footer.html" />
</body>
</html>