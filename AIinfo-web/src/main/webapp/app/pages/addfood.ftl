<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <#include "/common/header.html"/>
    <title>AIinfo - 记录</title>
</head>
<body>
    <div id="wrapper">
	    <#if userType == 1>
			<#include "navigation1.html"/>
		<#else>
		    <#include "navigation.html"/>
		</#if>
        <div class="container-fluid" id="addFood" style="display:none">
			<h2><span class="glyphicon glyphicon-heart-empty"></span> 新增记录 <span class="glyphicon glyphicon-heart-empty"></span></h2>
			<div class="input-group">
				<span class="input-group-addon" id="sizing-addon1">日期</span>
				<input type="date" id="date" class="form-control" placeholder="日期" aria-describedby="sizing-addon2">
			</div>
			<div class="input-group">
				<span class="input-group-addon" id="sizing-addon1">时间</span>
				<input type="time" id="time" class="form-control" placeholder="时间" aria-describedby="sizing-addon2">
			</div>
			<div class="input-group">
				<span class="input-group-addon" id="sizing-addon2">类型</span>
				<select class="form-control" id="type" name="type"><option value="01">奶粉</option><option value="02">母乳</option></select>
			</div>
			<div class="input-group">
				<span class="input-group-addon" id="sizing-addon3">数量</span>
				<input type="text" id="number" class="form-control" placeholder="Type" aria-describedby="sizing-addon2">
			</div>
			<div class="input-group">
				<span class="input-group-addon" id="sizing-addon4">单位</span>
				<select class="form-control" id="unit" name="unit"><option value="01">毫升</option><option value="02">升</option></select>
			</div>
			<input class="btn btn-success" type="button" id="btn-save" value="保存" />
			<input class="btn btn-default" type="button" id="btn-cancel" value="取消" />
	    </div>
		<div class="container-fluid" id="booksList">
		    <div class="alert alert-warning" role="alert">
				<span class="glyphicon glyphicon-heart"></span> ${wisdom}
			</div>
			<h2><span class="glyphicon glyphicon-heart-empty"></span> 当日记录 <span class="glyphicon glyphicon-heart-empty"></span></h2>
			<#if userType == 1>
				<input class="btn btn-info" type="button" id="btn-addFood" value="新增记录" />
			</#if>
			<span>
			  今日总量:${count}
			</span>
			<#if records??>
			    <#list records as record>
					  <div class="alert alert-info" role="alert">
					    <span class="glyphicon glyphicon-book"></span> ${(record.recordTime?string('yyyy-MM-dd HH:mm'))!}--<#if record.type == "01"><font color="green">奶粉</font><#elseif record.type == "02"><font color="green">母乳</font></#if>--${record.number}--<#if record.unit == "01"><font color="green">毫升</font><#elseif record.unit == "02"><font color="green">升</font></#if><button type="button" class="btn btn-link" id="btn-delRecord" value="${record.id}"><strong>删除</strong></button>
					  </div>
			    </#list>
			</#if>
	    </div>
	</div>
	<script type="text/javascript">
	    $("#btn-addFood").click(function(){
          $("#addFood").css("display","")
        })
		$("#btn-cancel").click(function(){
          $("#addFood").css("display","none")
        })
		$("#btn-save").click(function(){
		  var date = $('#date').val();
		  var time = $('#time').val();
		  var recordTime = date + " " + time;
		  var number = $('#number').val();
		  var type = $('#type').val();
		  var unit = $('#unit').val();
		  if (!recordTime) {
		    alert("记录时间不能为空");
		    return false;
		  }
		  if (!number) {
		    alert("数量不能为空");
		    return false;
		  }
		  $.ajax({
	           type: "POST",
               url: "addfood.action",
               data: {recordTime:recordTime, number:number, type:type, unit:unit},
               dataType: "json",
               success: function(data){
			     var data = JSON.parse(data);
                 if(data.status == true){
                     location.reload(true);
                     alert("成功:" + data.msg);
                 } else {
                     location.reload(true);
                     alert("失败:" + data.msg);
                 }
               }
	       });
        })
		
		$("#btn-delRecord").click(function(){
		  var id = $('#btn-delRecord').val();
		      $.ajax({
	           type: "POST",
               url: "delfoodrecord.action",
               data: {id:id},
               dataType: "json",
               success: function(data){
			     var data = JSON.parse(data);
                 if(data.status == true){
                     location.reload(true);
                     alert("成功:" + data.msg);
                 } else {
                     location.reload(true);
                     alert("失败:" + data.msg);
                 }
               }
	          });
        })
	</script>
	<#include "/common/footer.html" />
</body>
</html>