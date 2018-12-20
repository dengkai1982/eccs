/**
 * 将字符串解析成日期
 * 使用实例
 *  parseDate('2016-08-11'); // Thu Aug 11 2016 00:00:00 GMT+0800
	parseDate('2016-08-11 13:28:43', 'yyyy-MM-dd HH:mm:ss') // Thu Aug 11 2016 13:28:43 GMT+0800
 * @param str 输入的日期字符串，如'2014-09-13'
 * @param fmt 字符串格式，默认'yyyy-MM-dd'，支持如下：y、M、d、H、m、s、S，不支持w和q
 * @returns 解析后的Date类型日期
 */
function parseDate(str, fmt)
{
    fmt = fmt || 'yyyy-MM-dd';
    var obj = {y: 0, M: 1, d: 0, H: 0, h: 0, m: 0, s: 0, S: 0};
    fmt.replace(/([^yMdHmsS]*?)(([yMdHmsS])\3*)([^yMdHmsS]*?)/g, function(m, $1, $2, $3, $4, idx, old)
    {
        str = str.replace(new RegExp($1+'(\\d{'+$2.length+'})'+$4), function(_m, _$1)
        {
            obj[$3] = parseInt(_$1);
            return '';
        });
        return '';
    });
    obj.M--; // 月份是从0开始的，所以要减去1
    var date = new Date(obj.y, obj.M, obj.d, obj.H, obj.m, obj.s);
    if(obj.S !== 0) date.setMilliseconds(obj.S); // 如果设置了毫秒
    return date;
}
/**
 * 将日期格式化成指定格式的字符串
 * 使用实例
 * formatDate(); // 2016-09-02 13:17:13
	formatDate(new Date(), 'yyyy-MM-dd'); // 2016-09-02
	// 2016-09-02 第3季度 星期五 13:19:15:792
	formatDate(new Date(), 'yyyy-MM-dd 第q季度 www HH:mm:ss:SSS');
	formatDate(1472793615764); // 2016-09-02 13:20:15
 * 
 * @param date 要格式化的日期，不传时默认当前时间，也可以是一个时间戳
 * @param fmt 目标字符串格式，支持的字符有：y,M,d,q,w,H,h,m,S，默认：yyyy-MM-dd HH:mm:ss
 * @returns 返回格式化后的日期字符串
 */
function formatDate(date, fmt)
{
    date = date == undefined ? new Date() : date;
    date = typeof date == 'number' ? new Date(date) : date;
    fmt = fmt || 'yyyy-MM-dd HH:mm:ss';
    var obj =
    {
        'y': date.getFullYear(), // 年份，注意必须用getFullYear
        'M': date.getMonth() + 1, // 月份，注意是从0-11
        'd': date.getDate(), // 日期
        'q': Math.floor((date.getMonth() + 3) / 3), // 季度
        'w': date.getDay(), // 星期，注意是0-6
        'H': date.getHours(), // 24小时制
        'h': date.getHours() % 12 == 0 ? 12 : date.getHours() % 12, // 12小时制
        'm': date.getMinutes(), // 分钟
        's': date.getSeconds(), // 秒
        'S': date.getMilliseconds() // 毫秒
    };
    var week = ['天', '一', '二', '三', '四', '五', '六'];
    for(var i in obj)
    {
        fmt = fmt.replace(new RegExp(i+'+', 'g'), function(m)
        {
            var val = obj[i] + '';
            if(i == 'w') return (m.length > 2 ? '星期' : '周') + week[val];
            for(var j = 0, len = val.length; j < m.length - len; j++) val = '0' + val;
            return m.length == 1 ? val : val.substring(val.length - m.length);
        });
    }
    return fmt;
}
/**
 * 格式化时间 
 * @param {Object} fmt
 */
Date.prototype.Format = function (fmt) { //author: meizz 
    var o = {
        "M+": this.getMonth() + 1, 
        "d+": this.getDate(), 
        "H+": this.getHours(),  
        "m+": this.getMinutes(),  
        "s+": this.getSeconds(), 
        "q+": Math.floor((this.getMonth() + 3) / 3), 
        "S": this.getMilliseconds()  
    };
    var year = this.getFullYear();
    var yearstr = year + '';
    yearstr = yearstr.length >= 4 ? yearstr : '0000'.substr(0, 4 - yearstr.length) + yearstr;
    
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (yearstr + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}
/**
 * 判断指定名称的函数是否存在
 * @param {String} funcName 调用函数名称
 */
function functionExist(funcName) {
  try {
    if (typeof(eval(funcName)) === "function") {
      return true;
    }
  } catch(e) {}
  return false;
}
/**
 * 去处字符两边空格
 */
String.prototype.trim=function(){
     return this.replace(/(^\s*)|(\s*$)/g,'');
}
/**
 * 判断字符串是否已指定的字符串结束
 * @param {String} str 字符串
 */
String.prototype.endWith=function(str){
	if(str==null||str==""||this.length==0||str.length>this.length){
		return false;
	}
	if(this.substring(this.length-str.length)==str){
	     return true;
	}else{
		return false;
	}
	return true;
}
/**
 * 判断字符串是否已指定的字符串开始
 * @param {String} str
 */
String.prototype.startWith=function(str){
	if(str==null||str==""||this.length==0||str.length>this.length){
		return false;
	}
	if(this.substr(0,str.length)==str){
		return true;
	}else{
		return false;
	}
	return true;
}
/**
 * 从数组中查找匹配的元素，如果找到返回在数组中的位置
 * @param {Object} val 要查找的元素
 */
Array.prototype.findMatchObject = function(val) {
	for (var i = 0; i < this.length; i++) {
		if (this[i] === val) return i;
	}
	return -1;
};
//兼容IE8-，为Array原型添加indexOf方法；
if (!Array.prototype.indexOf) {
    Array.prototype.indexOf = function (item) {
        var index = -1;
        for (var i = 0; i < this.length; i++) {
            if (this[i] === item) {
                index = i;
                break;
            }
        }
        return index;
    }
}
/**
 * 从数组中删除原始
 * @param {Object} val 要删除的元素
 */
Array.prototype.remove = function(val) {
	var index = this.indexOf(val);
	if (index > -1) {
		this.splice(index, 1);
	}
}
/**
 * 判断对象是否为undefined,null,NaN 
 */
function isNullObject(obj){
	if(typeof(obj)=="undefined"){
		return true;
	}
	if(!obj&&typeof(obj)!="undefined"){
		return true;
	}
	if(isNaN(obj)){
		return true;
	}
	return false;
}
//清除第一个字符
function clearFirstChar(str,char){
    if(str.startWith(char)){
        str=str.substr(1);
    }
    return str;
}
/**
 * 字典类型，包含key,value
 */
function Dictionary() {
	this.keys = new Array();
	this.data = new Object();
	/** 
	 * 放入一个键值对 
	 * @param {String} key 
	 * @param {Object} value 
	 */
	this.put = function(key, value) {
		if(this.data[key] == null) {
			this.keys.push(key);
			this.data[key] = value;
		} else {
			this.data[key] = value;//this.data[key];
		}
		return true;
	};

	/** 
	 * 获取某键对应的值 
	 * @param {String} key 
	 * @return {Object} value 
	 */
	this.get = function(key) {
		return this.data[key];
	};

	/** 
	 * 删除一个键值对 
	 * @param {String} key 
	 */
	this.remove = function(key) {
		for(var i = 0; i < this.keys.length; i++) {
			if(key === this.keys[i]) {
				this.keys.splice(i, 1);
				this.data[key] = null;
				return true;
			}
		}
		return false;
	};

	/** 
	 * 遍历Map,执行处理函数 
	 * 
	 * @param {Function} 回调函数 function(key,value,index){..} 
	 */
	this.each = function(fn) {
		if(typeof fn != 'function') {
			return;
		}
		var len = this.keys.length;
		for(var i = 0; i < len; i++) {
			var k = this.keys[i];
			fn(k, this.data[k], i);
		}
	};

	/** 
	 * 获取键值数组 
	 * @return entity[{key,value},{key,value}] 
	 */
	this.entrySet = function() {
		var len = this.keys.length;
		var entrys = new Array(len);
		for(var i = 0; i < len; i++) {
			entrys[i] = {
				key: this.keys[i],
				value: this.data[this.keys[i]]
			};
		}
		return entrys;
	};

	/** 
	 * 判断是否为空 
	 */
	this.isEmpty = function() {
		return this.keys.length == 0;
	};

	/** 
	 * 获取键值对数量 
	 */
	this.size = function() {
		return this.keys.length;
	};

	this.containsKey = function(key) {
		return this.keys.filter(function(v) {
			if(v === key) {
				return key;
			}
		}).length > 0;
	};
	/** 
	 * 重写toString 
	 */
	this.toString = function() {
		var s = "{";
		for(var i = 0; i < this.keys.length; i++) {
			var k = this.keys[i];
			s += k + "=" + this.data[k];
			if(this.keys.length > i + 1) {
				s += ','
			}
		}
		s += "}";
		return s;
	};
	/** 
	 * 解析字符串到Map 
	 * {a=A,b=B,c=B,} 
	 */
	this.parserStringToDictionary = function(str) {
		var count = 0;
		if(str && str.length > 0) {
			str = str.trim();
			var startIndex = str.indexOf("{"),
				endIndex = str.lastIndexOf("}");
			if(startIndex !== -1 && endIndex !== -1) {
				str = str.substring(startIndex + 1, endIndex);
				var arrs = str.split(",");
				for(var i = 0; i < arrs.length; i++) {
					var kv = arrs[i].trim();
					if(kv.length > 0 && kv.indexOf("=") !== -1) {
						var kv_arr = kv.split("=");
						if(kv_arr.length == 2) {
							if(this.put(kv_arr[0].trim(), kv_arr[1].trim())) {
								count++;
							} else {
								console.error('error: kv:' + kv);
							}

						}
					}
				}
			} else {
				console.log("data error:" + str);
			}
		} else {
			console.log('data is not empty');
		}
		return count;
	};
}
/**
 * 级联组件类型
 * @param {Object} id
 * @param {Object} name
 * @param {Object} parentId
 */
function CascadeComponent(id,name,parentId){
	this.id=id;
	this.name=name;
	this.parent=parentId;
	this.child=new Array();
	//添加子类别
	this.addChild=function(cascade){
		this.child.push(cascade);
	}
	this.getParentId=function(){
		return this.parent;
	}
	//获取名称数组
	this.getChildNameArray=function(){
		var a=new Array();
		var len = this.child.length;
		for(var i = 0; i < len; i++) {
			a[i]=this.child[i].name;
		}
		return a;
	}
	//查找指定的子类
	this.findByName=function(findName){
		if(this.name==findName){
			return this;
		}else if(this.hasChild()){
			for(var i=0;i<this.child.length;i++){
				var eachChild=this.child[i];
				var find = eachChild.findByName(findName);
				if(find){
					return find;
				}
			}
		}
	}
	//查找指定的子类
	this.findById=function(findId){
		if(this.id==findId){
			return this;
		}else if(this.hasChild()){
			for(var i=0;i<this.child.length;i++){
				var eachChild=this.child[i];
				var find = eachChild.findById(findId);
				if(find){
					return find;
				}
			}
		}
	}
	/** 
	 * 判断是否具备子节点  
	 */
	this.hasChild = function() {
		return this.child.length != 0;
	};
	/** 
	 * 获取子节点的长度
	 */
	this.childSize = function() {
		return this.child.length;
	};
	/**
	 * 获取所有子节点
	 */
	this.getChildIdArray=function(){
		var a=new Array();
		var len = this.child.length;
		for(var i = 0; i < len; i++) {
			a[i]=this.child[i].areaid;
		}
		return a;
	}
}
/**
 * 值校验
 * @param 验证类型 type
 * @param 验证值 value
 * 
 */
function checkValue(type, value){
  $valid = value.replace(/(^\s*)|(\s*$)/g, "");
	switch (type)
	{
      case "required":
          return /[^(^\s*)|(\s*$)]/.test($valid);
          break;
      case "chinese":
          return /^[\u0391-\uFFE5]+$/.test($valid);
          break;
      case "number":
          return /^([+-]?)\d*\.?\d+$/.test($valid);
          break;
      case "integer":
          return /^-?[1-9]\d*$/.test($valid);
          break;
      case "plusinteger":
          return /^[1-9]\d*$/.test($valid);
          break;
      case "unplusinteger":
          return /^-[1-9]\d*$/.test($valid);
          break;
      case "znumber":
          return /^[1-9]\d*|0$/.test($valid);
          break;
      case "fnumber":
          return /^-[1-9]\d*|0$/.test($valid);
          break;
      case "double":
          return /^[-\+]?\d+(\.\d+)?$/.test($valid);
          break;
      case "plusdouble":
          return /^[+]?\d+(\.\d+)?$/.test($valid);
          break;
      case "unplusdouble":
          return /^-[1-9]\d*\.\d*|-0\.\d*[1-9]\d*$/.test($valid);
          break;
      case "english":
          return /^[A-Za-z]+$/.test($valid);
          break;
      case "username":
          return /^[a-z]\w{3,}$/i.test($valid);
          break;
      case "mobile":
          return /^\s*(15\d{9}|13\d{9}|14\d{9}|17\d{9}|18\d{9}|16\d{9})\s*$/.test($valid);
          break;
      case "phone":
          return /^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/.test($valid);
          break;
      case "tel":
          return /^((\(\d{3}\))|(\d{3}\-))?13[0-9]\d{8}?$|15[89]\d{8}?$|170\d{8}?$|147\d{8}?$/.test($valid) || /^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/.test($valid);
          break;
      case "email":
          return /^[^@]+@[^@]+\.[^@]+$/.test($valid);
          break;
      case "url":
          return /^https:|http:\/\/[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\':+!]*([^<>\"\"])*$/.test($valid);
          break;
      case "ip":
          return /^[\d\.]{7,15}$/.test($valid);
          break;
      case "qq":
          return /^[1-9]\d{4,10}$/.test($valid);
          break;
      case "currency":
          return /^\d+(\.\d+)?$/.test($valid);
          break;
      case "zipcode":
          return /^[1-9]\d{5}$/.test($valid);
          break;
      case "chinesename":
          return /^[\u0391-\uFFE5]{2,15}$/.test($valid);
          break;
      case "englishname":
          return /^[A-Za-z]{1,161}$/.test($valid);
          break;
      case "age":
          return /^[1-99]?\d*$/.test($valid);
          break;
      case "date":
          return /^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/.test($valid);
          break;
      case "datetime":
          return /^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-)) (20|21|22|23|[0-1]?\d):[0-5]?\d:[0-5]?\d$/.test($valid);
          break;
      case "idcard":
          return /^(\d{6})(\d{4})(\d{2})(\d{2})(\d{3})([0-9]|X)$/.test($valid);
          break;
      case "bigenglish":
          return /^[A-Z]+$/.test($valid);
          break;
      case "smallenglish":
          return /^[a-z]+$/.test($valid);
          break;
      case "color":
          return /^#[0-9a-fA-F]{6}$/.test($valid);
          break;
      case "ascii":
          return /^[\x00-\xFF]+$/.test($valid);
          break;
      case "md5":
          return /^([a-fA-F0-9]{32})$/.test($valid);
          break;
      default:
      	return false;
  }
}