<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <#include "/common/header.html"/>
    <title>机器列表页</title>
</head>
<body>
    <div id="wrapper">
    <#include "navigation1.html"/>
      <div id="page-wrapper">
        <div class="container-fluid">
          <div class="row">
            <div class="col-lg-12">
			  <h2>IP:${ip}</h2>
			  <h2>名称:${name}</h2>
			</div>
			<div class="col-lg-12">
			  <p>开关列表</p>
			</div>
			  <form name="switch" action="scheduleAction.html" method="get">
			  <table class="table table-striped">
			  <thead>
			    <tr>
			      <th width=200>BeanName</th>
			      <th width=200>开关名</th>
			      <th width=400>描述</th>
			      <th width=60>类型</th>
			      <th width=100>当前值</th>
			      <th width=100>操作</th>
			    </tr>
			    </thead>
			    <tbody id="resText">
			    </tbody>
			  </table>
			  </form>
			<div class="col-lg-12">
			  <p>时间程序列表</p>
			</div>
			  <form name="schedule" action="scheduleAction.html" method="get">
			  <table class="table">
			  <thead>
			    <tr>
			      <th width=200>BeanName</th>
			      <th width=100>开关名</th>
			      <th width=100>组</th>
			      <th width=200>描述</th>
			      <th width=100>是否启动</th>
			      <th width=100>规则</th>
			      <th width=400>操作</th>
			    </tr>
			    </thead>
			    <tbody id="resText1">
			    </tbody>
			  </table>
			  </form>
			</div>
		</div>
	  </div>
	</div>
	<script type="text/javascript">
	    $(document).on("click",".btn-update",function(){
	       var bothoers=$(this).parent().parent();
	       var beanName=bothoers.find(".td-beanName").text();
	       var fieldName=bothoers.find(".td-fieldName").text();
	       var currValue=bothoers.find(".input-currValue").val();
	       $.ajax({
	           type: "GET",
               url: "http://${ip}:${port}/${name}/aiinfo.do",
               data: {type:'SWITCH', action:'set', beanName:beanName, fieldName:fieldName, newValue:currValue},
               dataType: "json",
               success: function(data){
                 if(data[0].SetSuccess){
                     alert("修改成功！");
                 } else {
                     alert("修改失败！");
                 }
               }
	       });
	    });
	    
	    $(document).on("click",".btn-update1",function(){
	       var bothoers=$(this).parent().parent();
	       var jbeanName=bothoers.find(".td-jbeanName").text();
	       var jobName=bothoers.find(".td-jobName").text();
	       var jobGroup=bothoers.find(".td-jobGroup").text();
	       $.ajax({
	           type: "GET",
               url: "http://${ip}:${port}/${name}/aiinfo.do",
               data: {type:'QUARTZ', action:'invoke', fname:jbeanName, jobName:jobName, jobGroup:jobGroup},
               dataType: "json",
               success: function(data){
                 if(data == true){
                     alert("执行成功！");
                 } else {
                     alert("执行失败！");
                 }
               }
	       });
	    });
	    
	    $(document).on("click",".btn-update2",function(){
	       var bothoers=$(this).parent().parent();
	       var jbeanName=bothoers.find(".td-jbeanName").text();
	       var jobName=bothoers.find(".td-jobName").text();
	       var jobGroup=bothoers.find(".td-jobGroup").text();
	       $.ajax({
	           type: "GET",
               url: "http://${ip}:${port}/${name}/aiinfo.do",
               data: {type:'QUARTZ', action:'pause', fname:jbeanName, jobName:jobName, jobGroup:jobGroup},
               dataType: "json",
               success: function(data){
                 if(data == true){
                     alert("执行成功！");
                 } else {
                     alert("执行失败！");
                 }
               }
	       });
	    });
	    
	    $(document).on("click",".btn-update3",function(){
	       var bothoers=$(this).parent().parent();
	       var jbeanName=bothoers.find(".td-jbeanName").text();
	       var jobName=bothoers.find(".td-jobName").text();
	       var jobGroup=bothoers.find(".td-jobGroup").text();
	       $.ajax({
	           type: "GET",
               url: "http://${ip}:${port}/${name}/aiinfo.do",
               data: {type:'QUARTZ', action:'resume', fname:jbeanName, jobName:jobName, jobGroup:jobGroup},
               dataType: "json",
               success: function(data){
                 if(data == true){
                     alert("执行成功！");
                 } else {
                     alert("执行失败！");
                 }
               }
	       });
	    });
	    
	    $(document).on("click",".btn-update4",function(){
	       var bothoers=$(this).parent().parent();
	       var jbeanName=bothoers.find(".td-jbeanName").text();
	       var jobName=bothoers.find(".td-jobName").text();
	       var jobGroup=bothoers.find(".td-jobGroup").text();
	       var cronExp=bothoers.find(".input-cronExp").val();
	       $.ajax({
	           type: "GET",
               url: "http://${ip}:${port}/${name}/aiinfo.do",
               data: {type:'QUARTZ', action:'setNewCronExp', fname:jbeanName, jobName:jobName, jobGroup:jobGroup, newCronExp:cronExp},
               dataType: "json",
               success: function(data){
                 if(data == true){
                     alert("修改成功！");
                 } else {
                     alert("修改失败！");
                 }
               }
	       });
	    });
	    
        $(document).ready(function (){
        $.ajax({
             type: "GET",
             url: "http://${ip}:${port}/${name}/aiinfo.do",
             data: {type:'QUARTZ', action:'list'},
             dataType: "json",
             success: function(data){
                         $('#resText1').empty();   //清空resText1里面的所有内容
                         var html = ''; 
                         $.each(data, function(index, comment){
                               html +='<tr><td class="td-jbeanName">' + comment.factory + '</td>'
                                      +'<td class="td-jobName">' + comment.job.jobName + '</td>'
                                      +'<td class="td-jobGroup">' + comment.job.jobGroup + '</td>'
                                      +'<td>' + comment.job.desc + '</td>'
                                      +'<td>' + comment.isStarted + '</td>'
                                      +'<td class="td-cronExp" ><input class="input-cronExp" type="text" value="' + comment.job.cronExp + '"/></td>'
                                      +'<td><button class="btn-update1" type = "button">立即执行</button><button class="btn-update2" type = "button">暂停任务</button><button class="btn-update3" type = "button">回复任务</button><button class="btn-update4" type = "button">修改规则</button></td></tr>';
                         });
                         $('#resText1').html(html);
                      }
             });
        $.ajax({
             type: "GET",
             url: "http://${ip}:${port}/${name}/aiinfo.do",
             data: {type:'SWITCH', action:'list'},
             dataType: "json",
             success: function(data){
                         $('#resText').empty();   //清空resText里面的所有内容
                         var html = ''; 
                         $.each(data, function(index, comment){
                               html += '<tr><td class="td-beanName">' + comment.beanName + '</td>'
                                      +'<td class="td-fieldName">' + comment.fieldName + '</td>'
                                      +'<td>' + comment.sdesc + '</td>'
                                      +'<td>' + comment.stype + '</td>'
                                      +'<td class="td-currValue" ><input class="input-currValue" type="text" value="' + comment.currValue + '"/></td>'
                                      +'<td><button class="btn-update" type = "button">修改</button></td></tr>';
                         });
                         $('#resText').html(html);
                      }
             });
        });
	</script>
	<#include "/common/footer.html" />
</body>
</html>