<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>AIinfo - 历史记录</title>
    <#include "/common/header.html"/>
</head>
<body>
    <div id="wrapper">
    	<#if userType == 1>
		<#include "navigation1.html"/>
		<#else>
		<#include "navigation.html"/>
		</#if>
      <div id="page-wrapper">
        <div class="container-fluid">
	    <div class="row-fluid">
		<div class="span12">
		    <form class="form-inline" id="searchList" method="post">
			<div class="form-group col-lg-8 col-md-6 col-sm-12">
			    <div>
				<span>年：</span>
				<select class="form-control" id="statisticsYear" name="statisticsYear">
					<option value="2018" "<#if statisticsYear == 2018> selected='true' </#if>" >2018</option>
					<option value="2019" "<#if statisticsYear == 2019> selected='true' </#if>" >2019</option>
					<option value="2020" "<#if statisticsYear == 2020> selected='true' </#if>" >2020</option>
					<option value="2021" "<#if statisticsYear == 2021> selected='true' </#if>" >2021</option>
					<option value="2022" "<#if statisticsYear == 2022> selected='true' </#if>" >2022</option>
				</select>
				<span>月：</span>
				<select class="form-control" id="statisticsMon" name="statisticsMon">
				    <option value="0" "<#if statisticsMon == 0> selected='true' </#if>" >全部</option>
				    <#list 1..13 as i>
					  <#if i<13 >
					    <option value="i" "<#if statisticsMon == i> selected='true' </#if>" >${i}月</option>
					  </#if>
					</#list>
				</select>
				<span>日：</span>
				<select class="form-control" id="statisticsDay" name="statisticsDay">
				    <option value="0" "<#if statisticsDay == 0> selected='true' </#if>" >全部</option>
				    <#list 1..31 as i>
					  <#if i<31 >
					    <option value="i" "<#if statisticsDay == i> selected='true' </#if>" >${i}日</option>
					  </#if>
					</#list>
				</select>
				<span>类型：</span>
				<select class="form-control" id="datatype" name="datatype">
				    <option value="09" "<#if datatype == 09> selected='true' </#if>" >全部</option>
					<option value="01" "<#if datatype == 01> selected='true' </#if>" >奶粉</option>
					<option value="02" "<#if datatype == 02> selected='true' </#if>" >母乳</option>
				</select>
				<button id="btnSearch" type="submit" class="btn">查询</button>
				</div>
			</div>
			<#if type == 1>
			<table class="table">
				<thead>
					<tr>
						<th>
							统计时间
						</th>
						<th>
							数量
						</th>
						<th>
							类型
						</th>
					</tr>
				</thead>
				<tbody>
				  <#if statisticsBases??>
					<#list statisticsBases as statisticsBase>
					<tr class="info">
						<td>
					        ${statisticsBase.department}
						</td>
						<td>
							${statisticsBase.employeeNum}
						</td>
					</tr>
					</#list>
				  </#if>
				</tbody>
			</table>
			<#else>
			</#if>
			</form>
		    </div>
			<div id="echarts" style="width: 1600px;height:400px;"></div>
	      </div>
        </div>
	  </div>
	</div>
	<#include "/common/footer.html" />
	<script type="text/javascript">
	    function getData(myChart){
			var statisticsYear = $("#statisticsYear").val();
			var statisticsMon = $("#statisticsMon").val();
			var statisticsDay = $("#statisticsDay").val();
			var datatype = $("#datatype").val();
			$.ajax({
				type: "POST",
				url: "statisticsrecord.json",
				data: {statisticsYear:statisticsYear, statisticsMon:statisticsMon, statisticsDay:statisticsDay, datatype:datatype},
				dataType: "json",
				success: function(data){
					if(data.status){
					  myChart.setOption({
					    title: {
						    text: "图表显示"
						},
						xAxis: {
							data: data.departments
						},
						series: [{
							// 根据名字对应到相应的系列
							name: '人数',
							data: data.nums
						}]
					  });
					} else {
					  alert("数据获取失败！");
					}
				}
		    });
		}
		
	    var myChart = echarts.init(document.getElementById('echarts'));
		var option = {
				title: {
					text: ''
				},
				tooltip: {},
				legend: {
					data:['']
				},
				xAxis: {
					data: []
				},
				yAxis: {},
				series: [{
					name: '',
					type: 'bar',
					data: []
				}]
			};
		myChart.setOption(option);
		getData(myChart);
		
	</script>
</body>
</html>