
function clickop(id, op) {
	url = "http://59.42.253.182:8093/cs/ClickStat.action?yjvg=" + getRandomNum() + "&id=" + id + "&op=" + op;
	var js_obj = document.createElement("script");
	js_obj.type = "text/javascript";
	js_obj.setAttribute("src",url);
	document.body.appendChild(js_obj);
}

function getRandomNum(){
	var f = Math.floor(Math.random()*10);
	var s = 17-f;
	return f+''+s;
}

function getClickedNum(id){
	return clickop(id,"l");
}

function addOne(id){
	return clickop(id,"a");
}

function changeNum(id,num){
	document.getElementById(id).innerHTML=num + " ";
}