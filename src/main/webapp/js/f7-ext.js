/*f7扩展,需要在js-ext.js和framework7.min.js之后引入*/
var templateDictionary=new Dictionary();
/**
 * 通过模板ID加装模板
 * 模板ID类型：<script id="radio_list_panel" type="text/template7">...</script>
 * 返回模板数据
 * */
function getTemplateHtml(templateId,jsonData,Dom7){
	var key="script#"+templateId;
	var compiledTemplate=templateDictionary.get(key);
	if(!compiledTemplate){
		var templateEl = Dom7(key).html();
		compiledTemplate = Template7.compile(templateEl);
		templateDictionary.put(key,compiledTemplate);
	}
	return compiledTemplate(jsonData).trim();
}
/**通过url加装模板*/
function getTemplateHtmlByUrl(url,jsonData,Dom7){
	var compiledTemplate=templateDictionary.get(url);
	if(!compiledTemplate){
		var html;
		app.request({
			url:url,
			async:false,
			cache:false,
			success:function(data){
				html=data;
			}
		});
		compiledTemplate = Template7.compile(html);
		templateDictionary.put(url,compiledTemplate);
	}
	return compiledTemplate(jsonData).trim();
}
/**存储键值对*/
function putStorage(key,value){
	if(window.plus){
		plus.storage.setItem(key, value);
	}else if(window.localStorage){
		localStorage.setItem(key,value);
	}
}
/**存储键值对*/
function getStorage(key){
	if(window.plus){
		return plus.storage.getItem(key);
	}else if(window.localStorage){
		return localStorage.getItem(key);
	}
}
/**移除存储值*/
function removeStorage(key){
	if(window.plus){
		plus.storage.removeItem(key);
	}else if(window.localStorage){
		localStorage.removeItem(key);
	}
}