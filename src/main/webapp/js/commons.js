var SUCCESS = "success";
var SESSION_INVALIDE = "_session_invalide";
$(document).ready(function () {
    initTheme();
    initApplication();
    if(functionExist("pageReady")){
        pageReady($(this));
    }
});
function doPrivilege(){
    var authorMap=new Dictionary();
    $(".subAuthors").each(function(i,d){
        var $this=$(d);
        authorMap.put($this.attr("id"),$this.attr("visit")=="true"?true:false);
    });
    $(".privilege").each(function(i,d){
        var $this=$(d);
        var access=$this.attr("access");
        if(!authorMap.get(access)){
            $this.addClass("hidden");
        }
    });
}
function initApplication() {
    // 选择时间和日期
    $(".form-date").datetimepicker({
        language: "zh-CN",
        weekStart: 1,
        todayBtn: 1,
        autoclose: 1,
        todayHighlight: 1,
        startView: 2,
        minView: 2,
        forceParse: 0,
        format: "yyyy-mm-dd"
    });
    $(".clearDateTime").click(function () {
        $("input[name='" + $(this).attr("ref") + "']").val("");
    })
    $("#showSearch").click(function () {
        var sc = $(".search_container");
        if (sc.hasClass("hidden")) {
            sc.removeClass("hidden");
        } else {
            sc.addClass("hidden");
        }
    });
}

function initTheme() {
    var theme = $.zui.store.get("theme");
    if (theme == undefined) {
        selectTheme("indigo");
        theme = "indigo";
    } else {
        selectTheme(theme);
    }
    setThemeActive(theme);
}

function setThemeActive(theme) {
    $(".theme-option").each(function () {
        var dv = $(this).children("a").attr("data-value");
        if (dv == theme) {
            $(this).addClass("active");
        } else {
            $(this).removeClass("active");
        }
    });
}

function selectTheme(theme) {
    $("#zui_theme").attr("href", contextPath + "/zui/css/zui-theme-" + theme + ".css");
    $("#frame_zheme").attr("href", contextPath + "/css/theme-" + theme + ".css");
    $.zui.store.set("theme", theme);
    setThemeActive(theme);
}

//漂浮消息
function showMessage(text, timeout) {
    var msg = new $.zui.Messager(text, {
        close: false,
        type: 'danger',
        placement: 'center',
        time: timeout,
    }).show();
}

function showBlock(msg) {
    $.blockUI({
        message: "<div style='padding:10px;text-align:center;width:120px'>" +
        "<i class='icon icon-spin icon-spinner-indicator' style='font-size:32px;color:#fff'></i>" +
        "<div style='color:#fff'>" + msg + "</div></div>",
        css: {
            border: 'none',
            textAlign: 'center',
            padding: '20px 140px 20px 140px',
            background: 'none'
        }
    })
}

function hideBlock() {
    $(".blockUI").fadeOut("slow");
    $.unblockUI();
}

/**
 * textStatus
 * @param url 请求地址
 * @param data 请求数据
 * @param loadingMsg 加载时显示数据
 * @param handler 成功后回调
 * @returns
 */
function postJSON(url, data, loadingMsg, handler) {
    postJSON(url, data, loadingMsg, handler, 10000);
}

/**
 * post请求,返回json数据
 * 统一Ajax后的json返回格式
 * 增加额外提交参数 ajax=ajax来表示是ajax请求，服务端需返回msg=_session_invalide来表示session失效
 {
     code:'success|fail',
     msg:'fail_msg',
     body:'other_info'
 }
 * @param url 请求地址
 * @param data 请求数据
 * @param loadingMsg 加载时显示数据
 * @param handler 成功后回调
 * @param timeout 超时时间
 * @returns
 */
function postJSON(url, data, loadingMsg, handler, timeout) {
    showBlock(loadingMsg);
    data["ajax"] = "ajax";
    $.ajax({
        url: url,
        type: "POST", //GET
        async: true,    //或false,是否异步
        data: data,
        timeout: timeout,
        dataType: "JSON",
        success: function (result, textStatus, xhr) {
            hideBlock();
            if (result.code == "fail" && result.msg == SESSION_INVALIDE) {
                window.location.href = "privilege.jsp"
            } else if (typeof handler == 'function') {
                handler(result);
            }
        },
        error: function (xhr, textStatus) {
            hideBlock();
            console.log(xhr);
            showMessage("系统异常,请稍后再试");
        }
    })
}

function confirmOper(msg, fn) {
    bootbox.confirm({
        title: "警告",
        message: msg,
        buttons: {
            cancel: {
                label: '<i class="fa fa-times"></i> 取消'
            },
            confirm: {
                label: '<i class="fa fa-check"></i> 确定'
            }
        },
        callback: function (result) {
            if (result) {
                fn();
            }
        }
    })
}
/**
 * 完成操作后确认
 */
function newOrUpdateFinish(title,message,isEdit){
    bootbox.confirm({
        title:title,
        message: message,
        size: 'small',
        buttons: {
            confirm: {
                label: '确认',
                className: 'btn-success'
            },
            cancel: {
                label: '取消',
                className: 'btn-danger'
            }
        },
        callback: function (result) {
            if(result){
                window.location.href=backUrl;
            }else{
                if(!isEdit){
                    window.location.reload();
                }
            }
        }
    });
}

function getDataGridHeight(){
    var bodyHeight=$("body").height()
    var dataGridTop=$("#remoteDataGrid").offset().top
    var height=  bodyHeight-dataGridTop-120;
    console.log(height);
    return height;
}
function getDataGridLastWidth(){
    if($("#remoteDataGrid").width()<1280){
        return 400;
    }else{
        return 800;
    }
}