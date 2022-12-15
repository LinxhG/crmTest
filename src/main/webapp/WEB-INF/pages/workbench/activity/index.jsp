<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()	+ request.getContextPath() + "/";
%>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

<%--jQuery--%>
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>

<%--BOOTSTRAP--%>
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>

<%--DateTimePicker--%>
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

<%--PAGINGTION plugin--%>
<link rel="stylesheet" type="text/css" href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css">
<script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
<script type="text/javascript" src="jquery/bs_pagination-master/localization/en.js"></script>
<script type="text/javascript">

	/*
	文件上传的表单三个条件
		1.表单组件标签必须用：
			<input type="file|text|password|radio|checkbox|hidden|button|submit|reset">
			<select>、<textarea>等
		2.请求方式只能用post
			get:参数通过请求头提交到后台，参数放在URL后边；
				只能向后台提交文本数据；
				由于放在URL后面，所以参数长度有限制；
				数据不安全，但是效率高
		   post:参数通过请求体提交到后台，参数放在请求体中；
				技能提交文件数据，又能提交二进制数据；
				理论上对参数长度没有限制；
				相对安全，效率相对低；
		3.表单的编码格式只能用：multipart/form-data
			根据http协议的规定，浏览器每次向后台提交参数，都会对参数进行统一编码；
			默认采用的编码格式是urlencoded，这种编码格式只能对文本数据进行编码；
			浏览器每次向后台提交参数，都会首先把所有的参数转换成字符串，然后对这些数据统一进行urlencoded编码；
			文件上传的表单编码格式只能用multipart/form-data；设置编码格式，阻止默认编码；
	 */

	$(function(){
		// 给“创建”按钮绑定单击事件
		$("#createActivityBtn").click(function (){
			// 初始化工作
			// 重置表单
			$("#createActivityForm").get(0).reset();
			// 弹出创建市场活动的模态窗口
			$("#createActivityModal").modal("show");
		});

		// 给“保存”按钮绑定单击事件
		$("#saveCreateActivityBtn").click(function (){
			// 收集模态窗口中的数据
			var owner = $("#create-marketActivityOwner").val();
			var name = $.trim($("#create-marketActivityName").val());
			var startDate = $("#create-startDate").val();
			var endDate = $("#create-endDate").val();
			var cost = $.trim($("#create-cost").val());
			var description = $("#create-description").val();
			// 表单验证
			if (owner == ""){
				alert("所有者不能为空！");
				return;
			}
			if (name == ""){
				alert("名称不能为空！");
				return;
			}
			if (startDate != "" && endDate != ""){
				// 使用字符串的大小代替日期的大小
				if (endDate < startDate) {
					alert("结束日期不能比开始日期小")
					return;
				}
			}
			/*
				正则表达式：
				1、语言，语法：定义字符串的匹配模式，可以用来判断指定的具体字符串是否符合匹配模式；
				2、语法通则：
					1)//：在js中定义一个正则表达式，var regExp=/..../
					2)^：匹配字符串的开头位置
					  $：匹配字符串的结尾
					3)[]：匹配指定字符集中的一位字符  var regExp=/^[abc]$/
													 var regExp=/^[a-z0-9]$/
					4){}：匹配次数，var regExp=/^[abc]{5}$/
							{m}：匹配m次
							{m,n}：匹配m次到n次
							{m,}：匹配m次或者更多次
					5)特殊符号：
						\d：匹配一位数字，相当于[0-9]
						\D：匹配一位非数字
						\w：匹配所有字符，包括字母、数字、下划线
						\W：匹配非字符，除了字母、数字、下划线之外的字符
						*：匹配0次或者多次，相当于{0,}
						+：匹配1次或者多次，相当于{1,}
						?：匹配0次或者1次，相当于{0,1}
			 */

			// 非负整数的正则表达式：^(([1-9]\d*)|0)$
			var regExp = /^(([1-9]\d*)|0)$/;
			if (!regExp.test(cost)){
				alert("成本只能是非负整数！")
				return;
			}
			// 发送请求
			$.ajax({
				url:'workbench/activity/saveCreateActivity.do',
				// 要和controller中实体类或变量的名称一致
				data:{
					owner:owner,
					name:name,
					startDate:startDate,
					endDate:endDate,
					cost:cost,
					description:description
				},
				type:'post',
				dataType:'json',
				success:function (data){
					if (data.code == "1"){
						// 关闭模态窗口
						$("#createActivityModal").modal("hide");
						// 刷新市场活动列，显示第一页数据，保持每页显示条数不变（保留）
						queryActivityByConditionForPage(1, $("#bs_pagination_page1").bs_pagination('getOption', 'rowsPerPage'));
					} else{
						// 提示信息
						alert(data.message)
						// 保持模态窗口，可以不写
						$("#createActivityModal").modal("show");
					}
				}
			});
		});

		// 容器加载完成之后，对容器调用工具函数
		// $("input[name='dateName']").datetimepicker({})
		$(".form-date").datetimepicker({
			language:'zh-CN',// 语言
			format:'yyyy-mm-dd',// 日期的格式
			minView:'month',// 可以选择的最小视图
			initialDate:new Date(),// 初始化显示的日期
			autoclose:true,// 设置选择日期或者时间之后，是否自动关闭日历
			todayBtn:true,// 设置是否显示“今天”按钮，默认false
			clearBtn:false// 设置是否显示“清空”按钮，默认是false
		});

		// 当市场活动主页面加载完成，查询所有数据的第一页以及所有数据的总条数,默认每页显示10条
		queryActivityByConditionForPage(1, 10);

		// 给"查询"按钮添加单击事件
		$("#queryActivityBtn").click(function () {
			// 查询所有符合条件数据的第一页以及所有符合条件数据的总条数;
			queryActivityByConditionForPage(1, $("#bs_pagination_page1").bs_pagination('getOption', 'rowsPerPage'));
		});

		// 给“全选”按钮加单击事件
		$("#selectAll").click(function (){
			// 如果“全选”按钮是选中状态，则列表中所有checkbox都选中
			// if(this.checked == true) {
			// 	$("#tBody input[type='checkbox']").prop("checked", true);
			// } else {
			// 	$("#tBody input[type='checkbox']").prop("checked", false);
			// }
			$("#tBody input[type='checkbox']").prop("checked", this.checked);
		});

		// 由于列表中的checkbox是动态生成的，所以在绑定单击事件的时候，存在未加载出checkbox的情况，下述方法只能给固有元素添加事件（添加事件时元素已生成）
		// 给列表中所有的checkbox加单击事件，实现全选和不选同步
		// $("#tBody input[type='checkbox']").click(function (){
		// 	// 如果列表中的所有checkbox都选中，则“全选”按钮也选中
		// 	if($("#tBody input[type='checkbox']").size() == $("#tBody input[type='checkbox']:checked").size()){
		// 		$("#selectAll").prop("checked", true);
		// 	} else {
		// 		// 如果至少有一个没有选中，则“全选”按钮也取消
		// 		$("#selectAll").prop("checked", false);
		// 	}
		// });

		// 以上原因，采用第二种绑定单击事件的方法：使用jQuery的on函数：父选择器.on("事件类型", 子选择器, function(){});
		$("#tBody").on("click", "input[type='checkbox']", function (){
			// 如果列表中的所有checkbox都选中，则“全选”按钮也选中
			if($("#tBody input[type='checkbox']").size() == $("#tBody input[type='checkbox']:checked").size()){
				$("#selectAll").prop("checked", true);
			} else {
				// 如果至少有一个没有选中，则“全选”按钮也取消
				$("#selectAll").prop("checked", false);
			}
		});

		// 给“删除”按钮添加单击事件
		$("#deleteActivityBtn").click(function (){
			var checkedIds = $("#tBody input[type='checkbox']:checked");
			if ( checkedIds.size() == 0) {
				alert("请选择需要删除的活动");
				return;
			}
			// 弹出确认删除
			if(window.confirm("确定删除？")){
				var ids = "";
				$.each(checkedIds, function (){
					ids += "id=" + this.value + "&";
				});
				ids = ids.substr(0, ids.length - 1);
				// 发送请求
				$.ajax({
					url:'workbench/activity/deleteActivityByIds.do',
					data:ids,
					type:'post',
					dataType:'json',
					success:function (data){
						if (data.code == "1"){
							// 刷新市场活动列表，显示第一页数据，保持每页显示条数不变
							queryActivityByConditionForPage(1, $("#bs_pagination_page1").bs_pagination('getOption', 'rowsPerPage'));
						} else{
							// 提示信息
							alert(data.message);
						}
					}
				});
			}
		});

		// 给“修改”按钮绑定单击事件
		$("#editActivityBtn").click(function (){
			// 初始化工作
			// 收集参数
			// 获取列表中被选中的checkbox
			var checkedIds = $("#tBody input[type=checkbox]:checked");
			if (checkedIds.size() == 0) {
				alert("请选择要修改的市场活动");
				return;
			}
			if (checkedIds.size() > 1) {
				alert("每次仅能修改一条市场活动");
				return;
			}
			// var id = checkedIds.get(0).value; //转换成dom对象获取也可以
			var id = checkedIds.val();
			// 发送请求
			$.ajax({
				url:'workbench/activity/queryActivityById.do',
				data:{
					id:id
				},
				type:'post',
				dataType:'json',
				success:function (data){
					// 把市场活动的信息显示在修改的模态窗口上
					$("#edit-id").val(data.id);
					$("#edit-marketActivityOwner").val(data.owner);
					$("#edit-marketActivityName").val(data.name);
					$("#edit-startTime").val(data.startDate);
					$("#edit-endTime").val(data.endDate);
					$("#edit-cost").val(data.cost);
					$("#edit-description").val(data.description);
					// 弹出修改市场活动的模态窗口
					$("#editActivityModal").modal("show");
				}
			});
		});

		// 给“更新”按钮添加单击事件
		$("#saveEditActivityBtn").click(function (){
			// 收集模态窗口中的数据
			var id = $("#edit-id").val();
			var owner = $("#edit-marketActivityOwner").val();
			var name = $.trim($("#edit-marketActivityName").val());
			var startDate = $("#edit-startDate").val();
			var endDate = $("#edit-endDate").val();
			var cost = $.trim($("#edit-cost").val());
			var description = $("#edit-description").val();
			// 表单验证
			if (owner == ""){
				alert("所有者不能为空！");
				return;
			}
			if (name == ""){
				alert("名称不能为空！");
				return;
			}
			if (startDate != "" && endDate != ""){
				// 使用字符串的大小代替日期的大小
				if (endDate < startDate) {
					alert("结束日期不能比开始日期小")
					return;
				}
			}
			// 非负整数的正则表达式：^(([1-9]\d*)|0)$
			var regExp = /^(([1-9]\d*)|0)$/;
			if (!regExp.test(cost)){
				alert("成本只能是非负整数！")
				return;
			}
			// 发送请求
			$.ajax({
				url:'workbench/activity/saveEditActivity.do',
				// 要和controller中实体类或变量的名称一致
				data:{
					id:id,
					owner:owner,
					name:name,
					startDate:startDate,
					endDate:endDate,
					cost:cost,
					description:description
				},
				type:'post',
				dataType:'json',
				success:function (data){ // 只能解析json字符串，返回文件解析不了
					if (data.code == "1"){
						// 关闭模态窗口
						$("#editActivityModal").modal("hide");
						// 刷新市场活动列，显示第一页数据，保持每页显示条数不变（保留）
						queryActivityByConditionForPage($("#bs_pagination_page1").bs_pagination('getOption', 'currentPage'), $("#bs_pagination_page1").bs_pagination('getOption', 'rowsPerPage'));
					} else{
						// 提示信息
						alert(data.message)
						// 保持模态窗口，可以不写
						$("#editActivityModal").modal("show");
					}
				}
			});
		});

		// 给“批量导出”添加单击事件
		$("#exportActivityAllBtn").click(function (){
			// 发送同步请求
			window.location.href="workbench/activity/exportAllActivities.do";
		});

		// 给“导入”按钮添加单击事件
		$("#importActivityBtn").click(function (){
			// 清空文件

			// 收集参数
			var activityFileName = $("#activityFile").val();
			// 验证是否是excel文件
			var suffix = activityFileName.substr(activityFileName.lastIndexOf(".") + 1).toLowerCase();
			if (suffix != "xls") {
				alert("只支持xls文件");
				return;
			}
			var activityFile = $("#activityFile").get(0).files[0];
			if (activityFile.size > 5*1024*1024) {
				alert("文件大小不能超过5MB");
				return;
			}

			// FormData是ajax提供的接口，可以模拟键值对向后台提交参数
			// FormData最大的优势是不但能提交文本数据，还能提交二进制数据
			var formData = new FormData();
			formData.append("activityFile", activityFile);
			$.ajax({
				url:'workbench/activity/importActivity.do',
				data:formData,
				processData:false,// 设置ajax向后台提交参数之前，是否吧参数统一转换成字符串：true-是，false-不是，默认true
				contentType:false,// 设置ajax向后台提交参数之前，是否吧所有的参数统一按urlencode编码：true-是
				type:'post',
				dataType:'json',
				success:function (data){
					if (data.code == "1"){
						// 提示成功导入记录条数
						alert("成功导入" + data.retData + "条记录");
						// 关闭模态窗口
						$("#importActivityModal").modal("hide");
						// 刷新市场活动列表，显示第一页数据，保持每页显示条数不变
						queryActivityByConditionForPage(1, $("#bs_pagination_page1").bs_pagination('getOption', 'rowsPerPage'));
					} else {
						// 提示信息
						alert(data.message);
						// 模态窗口不关闭
						$("#importActivityModal").modal("show");
					}
				}
			});
		});
	});

	function queryActivityByConditionForPage(pageNo, pageSize){
		// 1、当市场活动主页面加载完成，查询所有数据的第一页以及所有数据的总条数；
		// 收集参数query-name
		var name = $("#query-name").val();
		var owner = $("#query-owner").val();
		var startDate = $("#query-startDate").val();
		var endDate = $("#query-endDate").val();
		// var pageNo = 1;
		// var pageSize = 10;
		// 发送请求
		$.ajax({
			url:'workbench/activity/queryActivityByConditionForPage.do',
			data:{
				name:name,
				owner:owner,
				startDate:startDate,
				endDate:endDate,
				pageNo:pageNo,
				pageSize:pageSize
			},
			type:'post',
			dataType:'json',
			success:function (data){
				// 显示总条数
				// $("#totalRowsB").text(data.totalRows);
				// 显示列表，遍历activityList，拼接所有行数据
				var htmlStr = "";
				$.each(data.activityList, function (index, obj){
					htmlStr += "<tr class=\"active\">";
					htmlStr += "<td><input type=\"checkbox\" value = \"" + obj.id + "\"/></td>";
					htmlStr += "<td><a style=\"text-decoration: none; cursor: pointer;\" onclick=\"window.location.href = 'workbench/activity/detailActivity.do?id=" + obj.id + "'\">" + obj.name + "</a></td>";
					htmlStr += "<td>" + obj.owner + "</td>";
					htmlStr += "<td>" + obj.startDate + "</td>";
					htmlStr += "<td>" + obj.endDate + "</td>";
					htmlStr += "</tr>";
				});
				$("#tBody").html(htmlStr);

				// 取消全选按钮
				$("#selectAll").prop("checked", false);

				// 计算总页数
				var totalPages = 1;
				if (data.totalRows % pageSize == 0) {
					totalPages = data.totalRows / pageSize;
				} else {
					totalPages = parseInt(data.totalRows / pageSize) + 1;
				}

				// 因为里面的两个page参数，只有在success之后，才能保证获取到，所以写在success里
				// 对容器调用 bs_pagination 工具函数，显示翻页信息
				$("#bs_pagination_page1").bs_pagination({
					currentPage: pageNo,// 当前页号，相当于pageNo

					rowsPerPage: pageSize,// 每页显示条数，相当于pageSize
					totalRows: data.totalRows,// 总条数
					totalPages: totalPages,// 总页数，必填

					visiblePageLinks: 5,// 最多可以显示的卡片数

					showGoToPage: true,// 是否显示“跳转到”部分，默认true--显示
					showRowsPerPage: true,// 是否显示“每页显示条数”部分，默认true--显示
					showRowsInfo: true,// 是否显示记录的信息，默认true--显示

					// 用户每次切换页号，都自动触发该函数
					// 每次返回切换页号之后的pageNo和pageSize
					onChangePage: function (event, pageObj) { // returns page_num and rows_per_page after a link has clicked.
						// js代码
						queryActivityByConditionForPage(pageObj.currentPage, pageObj.rowsPerPage);
					}
				});
			}
		});
	}
</script>
</head>
<body>

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
				
					<form id="createActivityForm" class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-marketActivityOwner">
									<c:forEach items="${requestScope.users}" var="user">
										<option value="${user.id}">${user.name}</option>
									</c:forEach>
								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-marketActivityName">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startDate" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control form-date" id="create-startDate" readonly>
							</div>
							<label for="create-endDate" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control form-date" id="create-endDate" readonly>
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveCreateActivityBtn">保存</button>
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
								<select class="form-control" id="edit-marketActivityOwner">
									<c:forEach items="${requestScope.users}" var="user">
										<option value="${user.id}">${user.name}</option>
									</c:forEach>
								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-marketActivityName" value="发传单">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startDate" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-startDate" value="2020-10-10">
							</div>
							<label for="edit-endDate" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-endDate" value="2020-10-20">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost" value="5,000">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-description">市场活动Marketing，是指品牌主办或参与的展览会议与公关市场活动，包括自行主办的各类研讨会、客户交流会、演示会、新产品发布会、体验会、答谢会、年会和出席参加并布展或演讲的展览会、研讨会、行业交流会、颁奖典礼等</textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveEditActivityBtn">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 导入市场活动的模态窗口 -->
    <div class="modal fade" id="importActivityModal" role="dialog">
        <div class="modal-dialog" role="document" style="width: 85%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">导入市场活动</h4>
                </div>
                <div class="modal-body" style="height: 350px;">
                    <div style="position: relative;top: 20px; left: 50px;">
                        请选择要上传的文件：<small style="color: gray;">[仅支持.xls]</small>
                    </div>
                    <div style="position: relative;top: 40px; left: 50px;">
                        <input type="file" id="activityFile">
                    </div>
                    <div style="position: relative; width: 400px; height: 320px; left: 45% ; top: -40px;" >
                        <h3>重要提示</h3>
                        <ul>
                            <li>操作仅针对Excel，仅支持后缀名为XLS的文件。</li>
                            <li>给定文件的第一行将视为字段名。</li>
                            <li>请确认您的文件大小不超过5MB。</li>
                            <li>日期值以文本形式保存，必须符合yyyy-MM-dd格式。</li>
                            <li>日期时间以文本形式保存，必须符合yyyy-MM-dd HH:mm:ss的格式。</li>
                            <li>默认情况下，字符编码是UTF-8 (统一码)，请确保您导入的文件使用的是正确的字符编码方式。</li>
                            <li>建议您在导入真实数据之前用测试文件测试文件导入功能。</li>
                        </ul>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button id="importActivityBtn" type="button" class="btn btn-primary">导入</button>
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
				      <input class="form-control" type="text" id="query-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="query-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="query-startDate">
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="query-endDate">
				    </div>
				  </div>
				  
				  <button type="button" class="btn btn-default" id="queryActivityBtn">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="createActivityBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editActivityBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteActivityBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				<div class="btn-group" style="position: relative; top: 18%;">
                    <button type="button" class="btn btn-default" data-toggle="modal" data-target="#importActivityModal" ><span class="glyphicon glyphicon-import"></span> 上传列表数据（导入）</button>
                    <button id="exportActivityAllBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（批量导出）</button>
                    <button id="exportActivityXzBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（选择导出）</button>
                </div>
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="selectAll"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="tBody">
						<%--<tr class="active">--%>
						<%--	<td><input type="checkbox" /></td>--%>
						<%--	<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>--%>
                        <%--    <td>zhangsan</td>--%>
						<%--	<td>2020-10-10</td>--%>
						<%--	<td>2020-10-20</td>--%>
						<%--</tr>--%>
                        <%--<tr class="active">--%>
                        <%--    <td><input type="checkbox" /></td>--%>
                        <%--    <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>--%>
                        <%--    <td>zhangsan</td>--%>
                        <%--    <td>2020-10-10</td>--%>
                        <%--    <td>2020-10-20</td>--%>
                        <%--</tr>--%>
					</tbody>
				</table>
				<%--块元素上下会空一行，行元素不会；因此为了美观放在上一个块元素中--%>
				<div id="bs_pagination_page1"></div>
			</div>
			
			<%--<div style="height: 50px; position: relative;top: 30px;">--%>
			<%--	<div>--%>
			<%--		<button type="button" class="btn btn-default" style="cursor: default;">共<b id="totalRowsB">50</b>条记录</button>--%>
			<%--	</div>--%>
			<%--	<div class="btn-group" style="position: relative;top: -34px; left: 110px;">--%>
			<%--		<button type="button" class="btn btn-default" style="cursor: default;">显示</button>--%>
			<%--		<div class="btn-group">--%>
			<%--			<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">--%>
			<%--				10--%>
			<%--				<span class="caret"></span>--%>
			<%--			</button>--%>
			<%--			<ul class="dropdown-menu" role="menu">--%>
			<%--				<li><a href="#">20</a></li>--%>
			<%--				<li><a href="#">30</a></li>--%>
			<%--			</ul>--%>
			<%--		</div>--%>
			<%--		<button type="button" class="btn btn-default" style="cursor: default;">条/页</button>--%>
			<%--	</div>--%>
			<%--	<div style="position: relative;top: -88px; left: 285px;">--%>
			<%--		<nav>--%>
			<%--			<ul class="pagination">--%>
			<%--				<li class="disabled"><a href="#">首页</a></li>--%>
			<%--				<li class="disabled"><a href="#">上一页</a></li>--%>
			<%--				<li class="active"><a href="#">1</a></li>--%>
			<%--				<li><a href="#">2</a></li>--%>
			<%--				<li><a href="#">3</a></li>--%>
			<%--				<li><a href="#">4</a></li>--%>
			<%--				<li><a href="#">5</a></li>--%>
			<%--				<li><a href="#">下一页</a></li>--%>
			<%--				<li class="disabled"><a href="#">末页</a></li>--%>
			<%--			</ul>--%>
			<%--		</nav>--%>
			<%--	</div>--%>
			<%--</div>--%>
			
		</div>
		
	</div>
</body>
</html>