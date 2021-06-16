<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
	<base href="<%=basePath%>">
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>
<script type="text/javascript">

	$(function(){
		// 为搜索框的开始和结束日期添加日历样式
		$(".time").datetimepicker({
			minView: "month",
			language:  'zh-CN',
			format: 'yyyy-mm-dd',
			autoclose: true,
			todayBtn: true,
			pickerPosition: "bottom-left"
		});
		// 为创建按钮绑定事件，打开添加的模态窗口
		$("#addBtn").click(function () {


			$(".time").datetimepicker({
				minView: "month",
				language:  'zh-CN',
				format: 'yyyy-mm-dd',
				autoclose: true,
				todayBtn: true,
				pickerPosition: "bottom-left"
			});

			$.ajax({
				url:"workbench/activity/getUserList.do",
				type:"get",

				dataType:"json",
				success:function (data) {
					var html = "<option>请选择</option>";

					$.each(data,function (i,n) {
						html += "<option value='"+n.id+"'>"+n.name+"</option>"
					});
					$("#create-Owner").html(html);

					// 取得当前用户的id
					// 在js中使用el表达式，el表达式要套接在字符串中
					var id = "${user.id}";
					$("#create-Owner").val(id);

					// 操作模态窗口的方法：使用jQuery的modal方法
					$("#createActivityModal").modal("show");
				},
			})
		});
		// 为创建按钮绑定事件，添加信息
		$("#saveBtn").click(function () {
			$.ajax({
				url: "workbench/activity/save.do",
				data:{
					"owner":$.trim($("#create-Owner").val()),
					"name":$.trim($("#create-Name").val()),
					"startDate":$.trim($("#create-startDate").val()),
					"endDate":$.trim($("#create-endDate").val()),
					"cost":$.trim($("#create-cost").val()),
					"description":$.trim($("#create-description").val()),
				},
				type: "post",
				dataType: "json",
				success:function (data) {
					// date{success:true/false}

					if (data.success){
						// 添加成功后需要刷新市场活动信息列表（局部刷新）

						// 关闭窗口之前需要重置表单信息
						$("#activityAddForm")[0].reset();
						// 关闭添加操作的模态窗口
						$("#createActivityModal").modal("hide");

						// pageList(1,2)

						/*
						$("#activityPage").bs_pagination('getOption', 'currentPage'):
						操作完成后停留在当前页，
						$("#activityPage").bs_pagination('getOption', 'rowsPerPage'):
						操作后维持已经设置好的每页展现的记录数
						*/
						pageList(1
								,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
					}else {
						alert("添加市场活动信息失败");
					}
				}
			})
		});

		pageList(1,2);

		// 为查询按钮绑定事件，触发pageList方法
		$("#searchBtn").click(function () {

			// 在点击搜索之前将搜索框中的信息保存起来
			$("#hidden-name").val($.trim($("#search-name").val()));
			$("#hidden-owner").val($.trim($("#search-owner").val()));
			$("#hidden-startDate").val($.trim($("#search-startDate").val()));
			$("#hidden-endDate").val($.trim($("#search-endDate").val()));

			pageList(1,2);
		});

		// 为全选的复选框绑定事件，触发全选操作
		$("#qx").click(function () {
			$("input[name=xz ]").prop("checked",this.checked);
		});

		/*
		为每个复选框添加事件，如果每个复选框都被选择则自动勾选全选复选框
		动态拼接生成的元素不能够通过普通的方法来进行绑定事件
		需要使用jQuery on方法的形式来绑定事件
		语法：$(需要绑定元素的有效外层元素).on(绑定事件的方式，需要绑定事件的元素的jQuery对象，回调函数)
		 */
		$("#activityBody").on("click",$("input[name=xz]"),function () {
			$("#qx").prop("checked",$("input[name=xz]").length == $("input[name=xz]:checked").length)
		});

		// 为删除按钮绑定事件，执行市场活动删除操作
		$("#deleteBtn").click(function () {
			// 找到复选框选择的jQuery对象
			var $xz = $("input[name=xz]:checked");

			if ($xz.length == 0){
				alert("请选择需要删除的记录");
			}else {
				if (confirm("确定要删除所选中的记录吗？")){
					// 拼接参数
					var params = "";
					// 遍历$xz对象，取得每个dom元素的value值
					for(var i=0;i<$xz.length;i++){

						params += "id="+$xz[i].id;
						console.log(params);
						//如果不是最后一个元素，需要在后面追加一个&符
						if(i<$xz.length-1){

							params += "&";

						}

					}
					// alert(params);
					$.ajax({
						url:"workbench/activity/detele.do",
						data:params,
						type:"post",
						dataType:"json",
						success:function (data) {
							// data:{success:true/false}
							if (data.success){
								pageList(1
										,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
							}else {
								alert("删除市场活动失败");
							}
						},
					})
				}

			}
		});

		// 为修改按钮绑定事件，打开修改的的模态窗口
		$("#editBtn").click(function () {
			var $xz = $("input[name=xz]:checked");

			if ($xz.length == 0){
				alert("请选择需要修改的记录");
			}else if ($xz.length > 1){
				alert("只能选择一条记录去修改")
			}else {
				// 选择且只选择了一条
				var id = $xz[0].id;
				$.ajax({
					url:"workbench/activity/getUserListAndActivity.do",
					data:{
						"id":id
					},
					type:"get",
					dataType:"json",
					success:function (data) {
						// data:{"userList":[{用户1},{用户2}],"activity":{市场活动}}
						var html="<option></option>";
						$.each(data.userList,function (i,n) {
							html += "<option value='"+n.id+"'>"+n.name+"</option>";

						});
						$("#edit-owner").html(html);

						// 处理单条activity数据
						$("#edit-id").val(data.activity.id);
						$("#edit-name").val(data.activity.name);

						$("#edit-owner").val(data.activity.owner);
						$("#edit-startDate").val(data.activity.startDate);
						$("#edit-endDate").val(data.activity.endDate);
						$("#edit-cost").val(data.activity.cost);
						// console.log(data.activity.description);
						$("#edit-description").val(data.activity.description);

						// 在所有的值都填写好后来打开修改的模态窗口
						$("#editActivityModal").modal("show");
					}
				});

			}
		});
		// 为更新按钮绑定事件，更新后市场活动信息
		/*
		在开发中应该先做添加再做修改的操作，为了节省开发时间修改操作可以copy添加操作
		 */
		$("#updateBtn").click(function () {
			console.log($("#edit-owner").val());
			$.ajax({
				url: "workbench/activity/update.do",
				data:{
					"id":$.trim($("#edit-id").val()),
					"owner":$.trim($("#edit-owner").val()),
					"name":$.trim($("#edit-name").val()),
					"startDate":$.trim($("#edit-startDate").val()),
					"endDate":$.trim($("#edit-endDate").val()),
					"cost":$.trim($("#edit-cost").val()),
					"description":$.trim($("#edit-description").val()),
				},
				type: "post",
				dataType: "json",
				success:function (data) {
					// date{success:true/false}

					if (data.success){
						// 修改成功之后局部刷新市场活动信息列表
						pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
								,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

						// 关闭修改操作的模态窗口
						$("#editActivityModal").modal("hide");

					}else {
						alert("修改市场活动信息失败");
					}
				}
			})
		});
	});
	/*
	在页面加载完成之后的加载的方法来进行局部刷新展现列表信息
	对于所有关系型数据库来坐分页的操作，都需要pageNo和pageSize
	pageNo：页码   pageSize：每页展现的记录数
	调用pageList的情况：
	（1）点击左侧的市场活动超链接时；
	（2）添加，修改，删除后，需要刷新列表
	（3）点击查询按钮的时候
	（4）点击分页组件的时候
	 */
	function pageList(pageNo,pageSize){
		// 进行列表刷新时，将全选的复选框设为未选择
		$("#qx").prop("checked",false);

		// 在查询前将隐藏域中的信息取出来，重新赋予到搜索框中
		$("#search-name").val($.trim($("#hidden-name").val()));
		$("#search-owner").val($.trim($("#hidden-owner").val()));
		$("#search-startDate").val($.trim($("#hidden-startDate").val()));
		$("#search-endDate").val($.trim($("#hidden-endDate").val()));

		$.ajax({
			url:"workbench/activity/pageList.do",
			data: {
				"pageNo":pageNo,
				"pageSize":pageSize,
				"name":$.trim($("#search-name").val()),
				"owner":$.trim($("#search-owner").val()),
				"startDate":$.trim($("#search-startDate").val()),
				"endDate":$.trim($("#search-endDate").val()),
			},
			type:"get",
			dataType:"json",
			success:function (data) {
				/*
				data:[{市场活动信息1},{市场活动信息2}]
				查询的总条数：total
				data:[{"total":100},{"dataList":[{市场活动信息1},{市场活动信息2}]}]
				 */

				var html = "";
				$.each(data.dataList,function (i,n) {
					html += '<tr class="active">';
					html += '<td><input type="checkbox" name="xz" id="'+n.id+'"/></td>';
					html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.do?id='+n.id+'\';">'+n.name+'</a></td>';
					html += '<td>'+n.owner+'</td>';
					html += '<td>'+n.startDate+'</td>';
					html += '<td>'+n.endDate+'</td>';
					html += '</tr>';
				});
				$("#activityBody").html(html);

				// 计算总页数
				var totalPages = (data.total % pageSize) == 0 ? data.total / pageSize : parseInt(data.total / pageSize) + 1;

				// 数据处理完毕后，结合分页查询，对前端页面进行展示
				$("#activityPage").bs_pagination({
					currentPage: pageNo, // 页码
					rowsPerPage: pageSize, // 每页显示的记录条数
					maxRowsPerPage: 20, // 每页最多显示的记录条数
					totalPages: totalPages, // 总页数
					totalRows: data.total, // 总记录条数

					visiblePageLinks: 3, // 显示几个卡片

					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true,

					onChangePage : function(event, data){
						pageList(data.currentPage , data.rowsPerPage);
					}
				});
			}
		})
	}
</script>
</head>
<body>

	<input type="hidden" id="hidden-name">
	<input type="hidden" id="hidden-owner">
	<input type="hidden" id="hidden-startDate">
	<input type="hidden" id="hidden-endDate">

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form id="activityAddForm" class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-Owner">
								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-Name">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startDate" readonly>
							</div>
							<label for="create-endTime" class="col-sm-2 control-label " >结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endDate" readonly>
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
						<input type="hidden" id="edit-id">
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-owner">

								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-name" >
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-startDate" >
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-endDate" >
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost" >
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label" id="description">描述</label>
							<div class="col-sm-10" style="width: 81%;">
<%--
								关于文本域textarea：1.标签一定要以标签对的形式来呈现，正常情况标签对要紧挨着否则空格等会被认为是文本域内容
								2.textarea属于表单元素范畴，我们所有对表单域的取值和赋值操作应该统一使用改变value值
--%>
								<textarea class="form-control" rows="3" id="edit-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateBtn" >更新</button>
				</div>
			</div>
		</div>
	</div>
	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon ">开始日期</div>
					  <input class="form-control time" type="text" id="search-startDate" readonly />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control time" type="text" id="search-endtDate" readonly/>
				    </div>
				  </div>
				  
				  <button type="button" id="searchBtn" class="btn btn-default">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
<%--
					创建和修改按钮通过属性来触发模态窗口
					data-toggle="modal"：表示要打开一个模态窗口
					data-target="#createActivityModal"：表示要打开哪个模态窗口
					问题是不能对按钮的功能进行扩充只能进行打开模态窗口

--%>
				  <button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="qx"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activityBody">
<%--						<tr class="active">--%>
<%--							<td><input type="checkbox" /></td>--%>
<%--							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>--%>
<%--                            <td>zhangsan</td>--%>
<%--							<td>2020-10-10</td>--%>
<%--							<td>2020-10-20</td>--%>
<%--						</tr>--%>
<%--                        <tr class="active">--%>
<%--                            <td><input type="checkbox" /></td>--%>
<%--                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>--%>
<%--                            <td>zhangsan</td>--%>
<%--                            <td>2020-10-10</td>--%>
<%--                            <td>2020-10-20</td>--%>
<%--                        </tr>--%>
					</tbody>
				</table>
			</div>
			
			<div id="activityPage" style="height: 70px; position: relative;top: 30px;">

			</div>
			
		</div>
		
	</div>
</body>
</html>