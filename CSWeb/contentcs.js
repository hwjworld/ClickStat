
function getRandomNum(){
	var f = Math.floor(Math.random()*10);
	var s = 17-f;
	return f+''+s;
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
var cururl = location.href;
//address
url = "http://localhost:8080/cs/ClickStat.action?yjvg=" + getRandomNum() + "&id1=" + getUrlN(cururl) + "&id2=" + getUrlN(cururl) + "&op=a";
var js_obj = document.createElement("script");
js_obj.type = "text/javascript";
js_obj.setAttribute("src",url);
var head = document.getElementsByTagName("head").item(0);
head.appendChild(js_obj);

