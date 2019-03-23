function valid(){
	
	var math = document.getElementById("math");
	var arabic = document.getElementById("arabic");
	var islamic = document.getElementById("islamic");
	if(math.value=="" || arabic.value=="" || islamic.value==""){
		alert("يجب رصد درجات جميع المواد");
		return false;
	}else if(isNaN(math.value) || isNaN(arabic.value) || isNaN(islamic.value)){
		alert("رصد الدرجات بلأرقام و ليس حروف");
		return false;
	}
	
		
}

function fvalidate(){
	
	var math = document.getElementById("math");
	var arabic = document.getElementById("arabic");
	var islamic = document.getElementById("islamic");
	var national = document.getElementById("national");
	var history = document.getElementById("history");
	var science =  document.getElementById("science");
	var computer = document.getElementById("computer");
	var english = document.getElementById("english");
	if(math.value == "" || arabic.value == "" || islamic.value == "" || national.value=="" || history.value=="" || science.value=="" || computer.value=="" || english.value==""){
		alert("يجب رصد درجات جميع المواد");
		return false;
	}else if(isNaN(math.value) || isNaN(arabic.value) || isNaN(islamic.value) || isNaN(national.value) || isNaN(history.value) || isNaN(science.value) || isNaN(computer.value) || isNaN(english.value)){
		alert("رصد الدرجات بلأرقام و ليس حروف");
		return false;
	}
}


function ftvalidate(){
	
	var math = document.getElementById("math");
	var arabic = document.getElementById("arabic");
	var islamic = document.getElementById("islamic");
	var national = document.getElementById("national");
	var history = document.getElementById("history");
	var science =  document.getElementById("science");
	
	
	if(math.value == "" || arabic.value == "" || islamic.value == "" || national.value=="" || history.value=="" || science.value==""){
		alert("يجب رصد درجات جميع المواد");
		return false;
	}else if(isNaN(math.value) || isNaN(arabic.value) || isNaN(islamic.value) || isNaN(national.value) || isNaN(history.value) || isNaN(science.value)){
		alert("رصد الدرجات بلأرقام و ليس حروف");
		return false;
	}
}


function searchv(){
	var name= document.getElementById("stid");
	
	if(name.value==""){
		alert("الرجاء كتابة رقم الطالب");
		return false;
	}else if(isNaN(name.value)){
		alert("هذا الحقل للبحث عن طريق أرقام الطلبة");
		return false;
	}
}




