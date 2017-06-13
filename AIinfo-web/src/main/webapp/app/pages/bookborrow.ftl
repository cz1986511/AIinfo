<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <#include "/common/header.html"/>
    <title>AIinfo - 图书借阅记录管理</title>
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
					    <span class="glyphicon glyphicon-book"></span> ${bookBorrow.bookName}--${bookBorrow.userName}<#if bookBorrow.status == "01"><button type="button" class="btn btn-link" value="${bookBorrow.id}"><strong>同意</strong></button><button type="button" class="btn btn-link" value="${bookBorrow.id}"><strong>驳回</strong></button><#elseif bookBorrow.status == "02"><button type="button" class="btn btn-link" value="${bookBorrow.id}"><strong>还书</strong></button><#elseif bookBorrow.status == "03">--已还书<#elseif bookBorrow.status == "04">--已驳回</#if>
					</div>
			    </#list>
			</#if>
	    </div>
	</div>
	<script type="text/javascript">
	  $("button").click(function(){
	       var action = $(this).text();
		   var id = $(this).val();
		   var falge = false;
		   if(action == "同意"){
		       var status = "02";
			   falge = true;
		   }
		   if(action == "驳回"){
		       var status = "04";
			   falge = true;
		   }
		   if(action == "还书"){
		       var status = "03";
			   falge = true;
		   }
		   if (falge) {
		     $.ajax({
	           type: "POST",
               url: "modifyborrow.action",
               data: {id:id, status:status},
               dataType: "json",
               success: function(data){
                 if(data.status){
                     location.reload(true);
                     alert("修改成功:" + data.msg);
                 } else {
                     location.reload(true);
                     alert("修改失败:" + data.msg);
                 }
               }
	         });
		   }
	    });
	</script>
	<#include "/common/footer.html" />
</body>
</html>