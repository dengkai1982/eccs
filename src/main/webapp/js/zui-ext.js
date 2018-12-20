/**
 * 显示toast 
 * @param {String} msg 消息
 */
function toast(msg) {
    var myMessager = new $.zui.Messager(msg, {
        placement: "center",
        close: false,
        time: 3000,
        type: "danger"
    }).show();
}
$.fn.clearFormAndHidden = function() {
    this.find("input").not(".notClear").not(":button, :submit, :reset").val("").removeAttr('checked').removeAttr('selected');
    this.find("select").val("");
    this.find("textarea").val("").html("");
    $('select.chosen-select').trigger('chosen:updated');
}
//清除表单内容
$.fn.clearForm = function() {
	this.find("input").not(":button, :submit, :reset, :hidden").val("").removeAttr('checked').removeAttr('selected');
	this.find("select").val("");
	this.find("textarea").val("").html("");
	$('select.chosen-select').trigger('chosen:updated');
}
//表单内容到json
$.fn.formToJson = function(otherString) {
	var serializeObj = {},
		array = this.serializeArray();
	$(array).each(function() {
		if(serializeObj[this.name]) {
			serializeObj[this.name] += ';' + this.value;
		} else {
			serializeObj[this.name] = this.value;
		}
	});

	if(otherString != undefined) {
		var otherArray = otherString.split(';');
		$(otherArray).each(function() {
			var otherSplitArray = this.split(':');
			serializeObj[otherSplitArray[0]] = otherSplitArray[1];
		});
	}
	return serializeObj;
}
//表单验证
//验证失败fn(el,hint) el:对象,hint:提示
$.fn.formValidate=function(fn)
{
	var tag="validate";
	var flag=true;
	this.find("input["+tag+"]").each(function(i,d){
		var $el=$(d);
		var $validates=$el.attr(tag).split(",");
		var $checkValue = $el.val();
		$.each($validates, function(idx, obj) {
			if(!flag){
				return false;
			}
			var $validate=obj.split(":");
			var $checkType=$validate[0];
			var hint=$validate[1];
			if($checkType=="min"){
				if($checkValue.length<parseInt(hint)){
					fn($el,"长度必须大于"+hint);	
					flag=false;
				}
			}else if($checkType=="max"){
				if($checkValue.length>parseInt(hint)){
					fn($el,"长度必须小于"+hint);	
					flag=false;
				}
			}else if($checkType=="length"){
				if($checkValue.length!=parseInt(hint)){
					fn($el,"长度不足"+hint);	
					flag=false;
				}
			}else{
				if(!checkValue($checkType,$checkValue)){
					fn($el,hint);
					flag=false;
				}
			}
		});
	});
	this.find("select["+tag+"]").each(function(i,d){
		var $el=$(d);
		var $validates=$el.attr(tag).split(",");
		var $checkValue = $el.val();
		$.each($validates, function(idx, obj) {
            if(!flag){
                return false;
            }
			var $validate=obj.split(":");
			var $checkType=$validate[0];
			var hint=$validate[1];
			if(!checkValue($checkType,$checkValue)){
				fn($el,hint);
				flag=false;
			}
		})
	});
	return flag;
};
/*调用模板*/
var templateDictionary=new Dictionary();
function getTemplateHtml(templateId,jsonData,Dom){
	var key="script#"+templateId;
	var compiledTemplate=templateDictionary.get(key);
	if(!compiledTemplate){
		var templateEl = Dom(key).html();
		compiledTemplate = Template7.compile(templateEl);
		templateDictionary.put(key,compiledTemplate);
	}
	return compiledTemplate(jsonData).trim();
}
//显示遮罩,需要引入jQuery.blockUI.js
function showMask(msg){
	$.blockUI({
		message:"<div style='padding:10px;text-align:center;width:120px'>" +
		"<i class='icon icon-spin icon-spinner-indicator' style='font-size:32px;color:#fff'></i>" +
		"<div style='color:#fff'>"+msg+"</div></div>",
		css:{
			border:'none',
			textAlign:'center',
			padding:'20px 140px 20px 140px',
			background:'none'
		}
	})
}
//关闭遮罩
function closeMask(){
	$(".blockUI").fadeOut("slow");
	$.unblockUI();
}
/**
 * ajax请求，返回json
 * @param {Object} url 请求地址
 * @param {Object} method 请求方法
 * @param {Object} data 数据
 * @param {Object} loadingMsg 
 * @param {Object} timeout 等待
 * @param {Object} sfn 成功后回调 success(data,textStatus,xhr)
 * @param {Object} efn 失败回调 error(xhr,textStatus)
 */
function jsonAjax(url,method,data,loadingMsg,timeout,sfn,efn){
	showMask(loadingMsg);
	$.ajax({
	    url:url,
	    type:method, //GET
	    async:true,    //或false,是否异步
	    data:data,
	    timeout:timeout,
	    dataType:"JSON",
	    success:function(data,textStatus,xhr){
	    		closeMask();	
			sfn(data,textStatus,xhr);	
	    },
	    error:function(xhr,textStatus){
	    		closeMask();	
	    		efn(xhr,textStatus);
	    }
	})
}
/**
 * post方式的ajax请求，返回json
 * @param {Object} url 请求地址
 * @param {Object} data 数据
 * @param {Object} sfn 成功后回调方法
 */
function postJson(url,data,sfn){
	jsonAjax(url,"post",data,"正在加载,请稍后",5000,sfn,function(xhr,textStatus){
		
	});
}
function getJson(url,data,loadingMsg,timeout,sfn,efn){
	jsonAjax(url,"get",data,loadingMsg,timeout,sfn,efn);
}