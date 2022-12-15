<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()	+ request.getContextPath() + "/";
%>
<html>
<head>
	<base href="<%=basePath%>">
	<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>

<script type="text/javascript">

	//默认情况下取消和保存按钮是隐藏的
	var cancelAndSaveBtnDefault = true;
	
	$(function(){
		$("#remark").focus(function(){
			if(cancelAndSaveBtnDefault){
				//设置remarkDiv的高度为130px
				$("#remarkDiv").css("height","130px");
				//显示
				$("#cancelAndSaveBtn").show("2000");
				cancelAndSaveBtnDefault = false;
			}
		});
		
		$("#cancelBtn").click(function(){
			//显示
			$("#cancelAndSaveBtn").hide();
			//设置remarkDiv的高度为130px
			$("#remarkDiv").css("height","90px");
			cancelAndSaveBtnDefault = true;
		});

		// 这种传统方式，只能给固有事件添加单击事件
		// $(".remarkDiv").mouseover(function(){
		// 	$(this).children("div").children("div").show();
		// });
		$("#remarkDivList").on("mouseover", ".remarkDiv", function (){
			$(this).children("div").children("div").show();
		});
		
		// $(".remarkDiv").mouseout(function(){
		// 	$(this).children("div").children("div").hide();
		// });
		$("#remarkDivList").on("mouseout", ".remarkDiv", function (){
			$(this).children("div").children("div").hide();
		});

		// $(".myHref").mouseover(function(){
		// 	$(this).children("span").css("color","red");
		// });
		$("#remarkDivList").on("mouseover", ".myHref", function (){
			$(this).children("span").css("color","red");
		});

		// $(".myHref").mouseout(function(){
		// 	$(this).children("span").css("color","#E6E6E6");
		// });
		$("#remarkDivList").on("mouseout", ".myHref", function (){
			$(this).children("span").css("color","#E6E6E6");
		});

		<%--
		jsp的运行原理
		xxx.jsp：
			1）tomcat中运行：把xxx.jsp翻译成一个servlet并运行，结果是一个html网页，把html网页输出到浏览器
			2）html网页在浏览器上运行：先从上到下加载html网页到浏览器，在加载过程中运行前端代码，当页面都加载完成，再执行入口函数
		--%>

		// 给“保存”按钮添加单击事件
		$("#saveCreateActivityRemarkBtn").click(function (){
			// 收集参数
			var noteContent = $.trim($("#remark").val());
			var activityId = '${activity.id}';
			if (noteContent == ""){
				alert("备注内容不能为空");
				return;
			}
			// 发送请求
			$.ajax({
				url:'workbench/activity/saveCreateActivityRemark.do',
				data:{
					noteContent:noteContent,
					activityId:activityId
				},
				type:'post',
				dataType:'json',
				success:function (data){
					if (data.code == "1"){
						// 清空输入框
						$("#remark").val("");
						// 刷新备注列表
						var htmlStr= "";
						htmlStr += "<div id=\"div_" + data.retData.id + "\" class=\"remarkDiv\" style=\"height: 60px;\">";
						htmlStr += "<img title=\"${sessionScope.sessionUser.name}\" src=\"image/user-thumbnail.png\" style=\"width: 30px; height:30px;\">";
						htmlStr += "<div style=\"position: relative; top: -40px; left: 40px;\">";
						htmlStr += "<h5>" + data.retData.noteContent + "</h5>";
						htmlStr += "<font color=\"gray\">市场活动</font> <font color=\"gray\">-</font> <b>${activity.name}</b> <small style=\"color: gray;\"> " + data.retData.createTime + " 由 ${sessionScope.sessionUser.name} '创建'</small>";
						htmlStr += "<div style=\"position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;\">";
						htmlStr += "<a class=\"myHref\" name=\"editA\" remarkId=\"" + data.retData.id + "\" href=\"javascript:void(0);\"><span class=\"glyphicon glyphicon-edit\" style=\"font-size: 20px; color: #E6E6E6;\"></span></a>";
						htmlStr += "&nbsp;&nbsp;&nbsp;&nbsp;";
						htmlStr += "<a class=\"myHref\" name=\"deleteA\" remarkId=\"" + data.retData.id + "\" href=\"javascript:void(0);\"><span class=\"glyphicon glyphicon-remove\" style=\"font-size: 20px; color: #E6E6E6;\"></span></a>";
						htmlStr += "</div>";
						htmlStr += "</div>";
						htmlStr += "</div>";
						$("#remarkDiv").before(htmlStr);
					} else {
						alert(data.message);
					}
				}
			});
		});

		<%--
		给元素扩展属性：html页面是可扩展的标记语言，可以给指定的标签任意扩展属性，只要属性名符合命名规范即可
		两个目的：
			1）如果标签保存数据：
				如果是表单组件标签，优先使用value属性，只有value不方便使用时，使用自定义属性；
				如果不是表单组件标签，不推荐使用value，推荐使用自定义属性；
			2）定位标签：
				优先考虑id属性，其次考虑name属性，只有id和name属性都不方便使用，才考虑使用自定义属性；
		--%>

		// 给所有的删除图标添加单击事件
		$("#remarkDivList").on("click", "a[name='deleteA']", function (){
			// 收集参数
			var id = $(this).attr("remarkId");
			$.ajax({
				url:'workbench/activity/deleteActivityRemarkById.do',
				data: {
					id:id
				},
				type:'post',
				dataType:'json',
				success:function (data){
					if (data.code == "1") {
						// 刷新备注列表
						$("#div_" + id).remove();
					} else {
						alert(data.message);
					}
				}
			});
		});

		// 给所有的修改图标添加单击事件
		$("#remarkDivList").on("click", "a[name='editA']", function (){
			// 收集参数
			var id = $(this).attr("remarkId");
			var noteContent = $("#div_" + id + " h5").text();
			// 把备注的id和noteContent写到修改备注的模态窗口中
			$("#edit-id").val(id);
			$("#edit-noteContent").val(noteContent);
			// 弹出修改备注的模态窗口
			$("#editRemarkModal").modal("show");
		});

		// 给更新按钮添加单击事件
		$("#updateRemarkBtn").click(function (){
			// 收集参数
			var id = $("#edit-id").val();
			var noteContent = $.trim($("#edit-noteContent").val());
			// 表单验证
			if (noteContent == ""){
				alert("备注内容不能为空");
				return;
			}
			$.ajax({
				url:'workbench/activity/saveEditActivityRemark.do',
				data: {
					id:id,
					noteContent:noteContent
				},
				type:'post',
				dataType:'json',
				success:function (data){
					if (data.code == "1") {
						// 关闭模态窗口
						$("#editRemarkModal").modal("hide");
						// 刷新备注列表
						$("#div_" + data.retData.id + " h5").text(data.retData.noteContent);
						$("#div_" + data.retData.id + " small").text(" " + data.retData.editTime  + " 由 ${sessionScope.sessionUser.name} 修改");
					} else {
						alert(data.message);
						// 模态窗口不关闭
						$("#editRemarkModal").modal("show");
					}
				}
			});
		});
	});
	
</script>

</head>
<body>
	
	<!-- 修改市场活动备注的模态窗口 -->
	<div class="modal fade" id="editRemarkModal" role="dialog">
		<%-- 备注的id --%>
		<input type="hidden" id="remarkId">
        <div class="modal-dialog" role="document" style="width: 40%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">修改备注</h4>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal" role="form">
						<input type="hidden" id="edit-id">
                        <div class="form-group">
                            <label for="edit-noteContent" class="col-sm-2 control-label">内容</label>
                            <div class="col-sm-10" style="width: 81%;">
                                <textarea class="form-control" rows="3" id="edit-noteContent"></textarea>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary" id="updateRemarkBtn">更新</button>
                </div>
            </div>
        </div>
    </div>

    

	<!-- 返回按钮 -->
	<div style="position: relative; top: 35px; left: 10px;">
		<a href="javascript:void(0);" onclick="window.history.back();"><span class="glyphicon glyphicon-arrow-left" style="font-size: 20px; color: #DDDDDD"></span></a>
	</div>
	
	<!-- 大标题 -->
	<div style="position: relative; left: 40px; top: -30px;">
		<div class="page-header">
			<h3>市场活动-${activity.name} <small>${activity.startDate} ~ ${activity.endDate}</small></h3>
		</div>
		
	</div>
	
	<br/>
	<br/>
	<br/>

	<!-- 详细信息 -->
	<div style="position: relative; top: -70px;">
		<div style="position: relative; left: 40px; height: 30px;">
			<div style="width: 300px; color: gray;">所有者</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${activity.owner}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">名称</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${activity.name}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>

		<div style="position: relative; left: 40px; height: 30px; top: 10px;">
			<div style="width: 300px; color: gray;">开始日期</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${activity.startDate}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">结束日期</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${activity.endDate}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 20px;">
			<div style="width: 300px; color: gray;">成本</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${activity.cost}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 30px;">
			<div style="width: 300px; color: gray;">创建者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${activity.createBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">${activity.createTime}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 40px;">
			<div style="width: 300px; color: gray;">修改者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${activity.editBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">${activity.editTime}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 50px;">
			<div style="width: 300px; color: gray;">描述</div>
			<div style="width: 630px;position: relative; left: 200px; top: -20px;">
				<b>
					${activity.description}
				</b>
			</div>
			<div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
	</div>
	
	<!-- 备注 -->
	<div style="position: relative; top: 30px; left: 40px;" id="remarkDivList">
		<div class="page-header">
			<h4>备注</h4>
		</div>

		<%--
		两种遍历方式:
			jQuery的foreach函数：遍历js遍历，不是作用域里的数据用它；
			jstl：主要是遍历作用域里的数据；与el表达式一起用，jstl内获取数据一般用el表达式
		--%>

		<%--遍历remarks显示所有的备注--%>
		<c:forEach items="${remarks}" var="remark">
			<div id="div_${remark.id}" class="remarkDiv" style="height: 60px;">
				<img title="${remark.createBy}" src="image/user-thumbnail.png" style="width: 30px; height:30px;">

				<div style="position: relative; top: -40px; left: 40px;" >
					<h5>${remark.noteContent}</h5>
					<font color="gray">市场活动</font> <font color="gray">-</font> <b>${activity.name}</b> <small style="color: gray;"> ${remark.editFlag == '1' ? remark.editTime : remark.createTime} 由 ${remark.editFlag == '1' ? remark.editBy : remark.createBy} ${remark.editFlag == '1' ? '修改' : '创建'}</small>
					<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">

						<%--
							使用标签保存数据，以便在需要的时候能获取到这些数据
							给标签添加属性：
								1、如果是表单组件标签，优先使用value属性，只有value不方便使用时，使用自定义属性；
								2、如果不是表单组件标签，不推荐使用value属性，推荐使用自定义属性。
							获取属性值时：
								1、如果获取表单组件标签的value属性值：dom对象.value
																  jQuery.val()
								2、如果自定义的属性，不管是什么标签，只能用：jquery对象.attr("属性名");
						--%>

						<a class="myHref" name="editA" remarkId="${remark.id}" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
						&nbsp;&nbsp;&nbsp;&nbsp;
						<a class="myHref" name="deleteA" remarkId="${remark.id}" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
					</div>
				</div>
			</div>
		</c:forEach>
		<!-- 备注1 -->
		
		<!-- 备注2 -->
		<%--<div class="remarkDiv" style="height: 60px;">--%>
		<%--	<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">--%>
		<%--	<div style="position: relative; top: -40px; left: 40px;" >--%>
		<%--		<h5>呵呵！</h5>--%>
		<%--		<font color="gray">市场活动</font> <font color="gray">-</font> <b>发传单</b> <small style="color: gray;"> 2017-01-22 10:20:10 由zhangsan</small>--%>
		<%--		<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">--%>
		<%--			<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>--%>
		<%--			&nbsp;&nbsp;&nbsp;&nbsp;--%>
		<%--			<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>--%>
		<%--		</div>--%>
		<%--	</div>--%>
		<%--</div>--%>
		
		<div id="remarkDiv" style="background-color: #E6E6E6; width: 870px; height: 90px;">
			<form role="form" style="position: relative;top: 10px; left: 10px;">
				<textarea id="remark" class="form-control" style="width: 850px; resize : none;" rows="2"  placeholder="添加备注..."></textarea>
				<p id="cancelAndSaveBtn" style="position: relative;left: 737px; top: 10px; display: none;">
					<button id="cancelBtn" type="button" class="btn btn-default">取消</button>
					<button type="button" class="btn btn-primary" id="saveCreateActivityRemarkBtn">保存</button>
				</p>
			</form>
		</div>
	</div>
	<div style="height: 200px;"></div>
</body>
</html>