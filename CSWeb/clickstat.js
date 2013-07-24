//gtype=1,采用第一种方式,每个请求,存于procArray,统一替换
//gtype=2,采用第二种方式,统一请求ids,请求完替换


//请将localhost:8080替换成服务器ip地址及端口
var baseurl="http://localhost:8080/cs/ClickStat.action?yjvg=" + getRandomNum();


var gtype=2;
var procArray = new Array();//存放更新点击数的script 对象
var id1s = new Array();//存放ids页面加载完统一提交
var id2s = new Array();

//id1 artid  id2 recid
function clickop(id1, id2, op) {
	url = baseurl + "&id1=" + id1 + "&id2=" + id2 + "&op=" + op;
	var js_obj = sendReq(url);
	procArray[procArray.length]=js_obj;
	//document.head.appendChild(js_obj);
}

function sendReq(url){
	var js_obj = document.createElement("script");
	js_obj.type = "text/javascript";
	js_obj.setAttribute("src",url);
	return js_obj;
}

function getRandomNum(){
	var f = Math.floor(Math.random()*10);
	var s = 17-f;
	return f+''+s;
}

function getClickedNum(id){
	var dg = getIdObj(id);
	if(dg){
		var id2 = getId2(dg);
		if(gtype==1){
			return clickop(id,id2,"l");
		}else if(gtype==2){
			id1s[id1s.length]=id;
			id2s[id2s.length]=id2;
		}
	}
}

function addOne(id){
	var dg = getIdObj(id);
	if(dg){
		var id2 = getId2(dg);
		clickop(id,id2,"a");
		if(gtype==1){
			changeAll();
		}else if(gtype==2){
			gtype=1;
			changeAll();
			gtype=2;
		}
	}
}

function getIdObj(id){
	var dg = document.getElementById("a"+id);
	if(!dg){dg = document.getElementById("a "+id)};
	return dg;
}

function changeNum(id,num){
	var dg = document.getElementById(id);
	if(dg)
		dg.innerHTML=num;// + " ";
}

function changeAll(){
	if(gtype==1){
		for(i=0;i<procArray.length;i++){
			document.body.appendChild(procArray[i]);
		}
		procArray = new Array();
	}else if(gtype==2){
		document.body.appendChild(sendReq(baseurl+buildParams()));
		var id1s = new Array();
		var id2s = new Array();
	}
}

function buildParams(){
	var str = "&id1=";
	for(i in id1s){
		str=str+id1s[i]+";";
	}
	str+="&id2=";
	for(i in id2s){
		str=str+id2s[i]+";";
	}
	return str+="&op=l";
}

function getConUrlN(span){
	return getUrlN(span.getAttribute('url'));
}

function getArtUrlN(span){
	return getUrlN(span.getAttribute('url1'));
}

function getUrlN(url){
	try{
		var p = url.lastIndexOf(".");
		var slash = url.lastIndexOf("/")+1;
		return url.substring(slash,p);
	}catch(e){
		return 0;
	}
}

function getId2(span){
	var id2 = getConUrlN(span);
	if(id2==0)
		id2 = getArtUrlN(span);
	return id2;
		
}