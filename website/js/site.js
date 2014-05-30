window.onload = load();

//document.getElementById("exec").onclick = alert("123");

function myFunction() {
	//load();
	alert("in myFunction");
	
	$.get("ajax/test.html", function(data) {
		//$(".result").html(data);
		alert("Load was performed.");
	});
}

function load() {
document.getElementById("ta_input").value = "SELECT l_partkey, SUM(l_extendedprice) as revenue \n\
FROM lineitem as outer \n\
WHERE l_quantity < \n\
              ( SELECT SUM(l_quantity) / 3 \n\
                FROM lineitem as inner \n\
                WHERE inner.l_partkey = outer.l_partkey ) \n\
GROUP BY l_partkey";
}