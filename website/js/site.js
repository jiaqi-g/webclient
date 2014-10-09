window.onload = load();
var myInterval = ""
var perCount = 0

function clearResults() {
	$(".plan").html("");
	$(".tab").html("");
	$(".time_close").html("");
	$(".time_abm").html("");
}

function fillTpch(sec) {
	var tpchId = $(".tpchMenu_" + sec).val();
	var percent = $(".percentMenu_" + sec).val();

	var rep = true;
	if (tpchId == 'nothing'||percent == 'nothing') {
		return;
	} 

	$.get("/corrected/".concat(tpchId, ".hive"), {},
	function(data) {
		if (rep) {
			data = data.replace(/SampleSize/g, percent);
			var scale = "";
			if (percent == "1") {
				scale = "100";
			} else if (percent == "2") {
				scale = "50";
			} else if (percent == "5") {
				scale = "20";
			} else if (percent == "10") {
				scale = "10";
			}
			data = data.replace(/\[SCALE\]/g, scale);
		}
		$(".ta_" + sec).val(data);
	});
}

function schema() {
	$('.tpch_img').show();
	/*
	var s = "================== TPC-H Schema ==================\n\n";
	s += "lineitem (tid int, L_ORDERKEY INT, L_PARTKEY INT, L_SUPPKEY INT, L_LINENUMBER INT, L_QUANTITY DOUBLE, L_EXTENDEDPRICE DOUBLE, L_DISCOUNT DOUBLE, L_TAX DOUBLE, L_RETURNFLAG STRING, L_LINESTATUS STRING, L_SHIPDATE STRING, L_COMMITDATE STRING, L_RECEIPTDATE STRING, L_SHIPINSTRUCT STRING, L_SHIPMODE STRING, L_COMMENT STRING)";
	s += "\n\n";
	s += "part (P_PARTKEY INT, P_NAME STRING, P_MFGR STRING, P_BRAND STRING, P_TYPE STRING, P_SIZE INT, P_CONTAINER STRING, P_RETAILPRICE DOUBLE, P_COMMENT STRING)";
	s += "\n\n";
	s += "supplier (S_SUPPKEY INT, S_NAME STRING, S_ADDRESS STRING, S_NATIONKEY INT, S_PHONE STRING, S_ACCTBAL DOUBLE, S_COMMENT STRING)";
	s += "\n\n";
	s += "partsupp (tid int, PS_PARTKEY INT, PS_SUPPKEY INT, PS_AVAILQTY INT, PS_SUPPLYCOST DOUBLE, PS_COMMENT STRING)";
	s += "\n\n";
	s += "nation (N_NATIONKEY INT, N_NAME STRING, N_REGIONKEY INT, N_COMMENT STRING)";
	s += "\n\n";
	s += "region (R_REGIONKEY INT, R_NAME STRING, R_COMMENT STRING)";
	s += "\n\n";
	s += "orders (O_ORDERKEY INT, O_CUSTKEY INT, O_ORDERSTATUS STRING, O_TOTALPRICE DOUBLE, O_ORDERDATE STRING, O_ORDERPRIORITY STRING, O_CLERK STRING, O_SHIPPRIORITY INT, O_COMMENT STRING)";
	s += "\n\n";
	s += "customer (tid int, C_CUSTKEY INT, C_NAME STRING, C_ADDRESS STRING, C_NATIONKEY INT, C_PHONE STRING, C_ACCTBAL DOUBLE, C_MKTSEGMENT STRING, C_COMMENT STRING)";
	s += "\n\n";*/
}

function hideAll() {
	$(".row-home").hide();
	$(".home-bar").removeClass("active");

	$(".row-veri").hide();
	$(".veri-bar").removeClass("active");
	
	$(".row-abm").hide();
	$(".abm-bar").removeClass("active");
	
	$(".row-close").hide();
	$(".close-bar").removeClass("active");
	
	$(".row-vanilla").hide();
	$(".vanilla-bar").removeClass("active");
}

function show(num) {
	//clear previous results
	clearResults();
	hideAll();

	if (num == 1) {
		$(".row-home").show();
		$(".home-bar").addClass("active");
	}
	else if (num == 2) {
		$(".row-veri").show();
		$(".veri-bar").addClass("active");
	}
	else if (num == 3) {
		$(".row-abm").show();
		$(".abm-bar").addClass("active");
	}
	else if (num == 4) {
		$(".row-close").show();
		$(".close-bar").addClass("active");
	}
	else if (num == 5) {
		$(".row-vanilla").show();
		$(".vanilla-bar").addClass("active");
	}

	$('html,body').scrollTop(0);
}

function hideBtn(num) {
	if (num == 1) {
		$(".abm_btn").hide();
	}
	else if (num == 2) {
		$(".close_btn").hide();
	}
	else if (num == 3) {
		$(".vanilla_btn").hide();
	}
}

function showAllBtns() {
	$(".abm_btn").show();
	$(".close_btn").show();
	$(".vanilla_btn").show();
}

function quantileCheckClick() {
 	document.getElementById("quantilePct").disabled = false;
 	document.getElementById("quantilePct2").disabled = false;
}

function confidenceCheckClick() {
 	document.getElementById("confFrom").disabled = false;
 	document.getElementById("confTo").disabled = false;
}


function confidenceCheckClick2() {
 	document.getElementById("confFrom2").disabled = false;
 	document.getElementById("confTo2").disabled = false;
}

function plan() {
	var sql = $(".ta_input").val();
	$(".ta_abm").val(sql);
	$(".ta_close").val(sql);
	$(".ta_vanilla").val(sql);

	//clear older plan
	$(".plan").html("");
	$(".plan").html("<text>Fetching Plan ... </text>");

	$.get("/plan", {query: sql},
	function(data) {
		//$(".plan").html(data);
		//alert(data);
		//var data = [{"name":"FS[2]","children":[{"name":"SEL[1]","parent":"FS[2]","children":[{"name":"TS[0]","parent":"SEL[1]","children":[]}]}]}];

		var words = data.split("|");
		var notice = "";
		/*
		$(".check_btn").prop("disabled",true);
		$(".abm_btn").prop("disabled",true);
		$(".close_btn").prop("disabled",true);
		$(".vanilla_btn").prop("disabled",true);*/
		var isAbmEligible = (parseInt(words[0]) == 1);
		var isCloseEligible = (parseInt(words[1]) == 1);
		var isBootstrapEligible = (parseInt(words[2]) == 1);

		showAllBtns();
		//clear text
		$(".plan").html("");
		
		if (isAbmEligible) {
			notice += "<p>Safe for abm</p>";
		} else {
			notice += "<p>Unsafe for abm</p>";
			hideBtn(1);
		}
		
		if (isCloseEligible) {
			notice += "<p>Safe for closed form</p>";
		} else {
			notice += "<p>Unsafe for closed form</p>";
			hideBtn(2);
		}
		
		if (isBootstrapEligible) {
			notice += "<p>Safe for vanilla bootstrap</p>";
		} else {
			notice += "<p>Unsafe for vanilla bootstrap</p>";
			hideBtn(3);
		}
		
		//$(".pad").html(notice);
		if (!isAbmEligible) {
			alert("Abm Unsafe Plan: " + words[3]);
		}
		if (!isCloseEligible) {
			alert("Close-Form Formula Unsafe Plan.");
		}
		var json_plan = words[4];
		if (json_plan.trim() != "none") {
			draw($.parseJSON(json_plan));
		}
	});
}

function stop_interval() {
	$.get("/stopInterval", {}, function(data) {});

    if(myInterval!=""){
        window.clearInterval(myInterval)
        intval=""
    }

    $(".vanilla_running").html("Stopped.").show();
}

function reloadGraph(dat) {
    if(myInterval!=""){
        window.clearInterval(myInterval)
        intval=""
    }

	var fileName = "../../../tmp.txt";
	perCount = 0;
	$.get(fileName, function(data) {
		vanillaPerformance(data);
        });

	myInterval = setInterval(function() {
		$.get(fileName, function(data) {
            vanillaPerformance(data);
        });
	},4000);
}

function vanillaPerformance(data) {    
	    // alert(data);
	    perCount ++;
	    var words = data.split(",")
        var vanillaLabel = "Bootstrap-" + words[0]
	    var bsTime = parseFloat(words[1])
	    var abmTime = parseFloat(words[2])
	    var title = "Running Performance-" + String(perCount);

            var data4 = google.visualization.arrayToDataTable([
                  ['Methods', 'Time(s)', {role:'style'}],
                  [vanillaLabel, bsTime, 'blue'],
                  ['ABM', abmTime, 'red']
                ]);
            var options4 = {
                        'title': title,
                        'vAxis': {'title': 'Execution Time (seconds)', 'titleTextStyle': {color: 'black'}},
			'backgroundColor':{'stroke': '#000000', 'strokeWidth' : 1},
			'legend': {'position':'none'},
			'bar': {'groupWidth': "35%"},
                };
            var chart4 = new google.visualization.ColumnChart(document.getElementById('vanilla_performance_div'));
            chart4.draw(data4, options4);

}

function execVanilla() {
	$(".vanilla_running").html("Loading ...").show();
	$("#vanilla_performance_div").html("");
	$("#vanilla_error_div").html("");

	var sql = $(".ta_vanilla").val();
	var tpchId = $(".tpchMenu_vanilla").val();
	var percent = $(".percentMenu_vanilla").val();

	if (percent == 'nothing' || tpchId == 'nothing' || tpchId == 'q20') {
		alert("Please choose a query from tpch benchmark!")
		return;
	}
	if (tpchId == 'nothing') {
		return;
	}
	if (tpchId == 'q20') {
		return;
	}

	$.get("/vanilla/".concat(tpchId.toUpperCase(), "_" , percent, ".txt"), {query: sql},
	function(data) {
		if (data.indexOf("Exception") != 0) {
			$(".vanilla_running").html("Running ...").show();
			var dat = getComplexData(data, ['Criteria', '100', '200', '500', '1000'], ['MEAN','STD','KS','CONF INTERVAL']);
			drawChart(dat, 3);
			reloadGraph(dat);
		}
		else {
			$(".vanilla_running").hide();
			alert(data);
		}
	});
}

function execClose() {
	//clear older time
	$(".time_close").html("");

	var sql = $(".ta_close").val();

	$(".myTab_close").html("Loading ... ");
	var varCheck = "false";
	if(document.getElementById('varRadio2').checked) {
		varCheck = "true"
	}
	var quantileCheck = "false";
	if(document.getElementById('quantileRadio2').checked) {
		quantileCheck = "true"
	}
	var confidenceCheck = "false";
	if(document.getElementById('confidenceRadio2').checked) {
		confidenceCheck = "true"
	}
	var quantileVal = document.getElementById('quantilePct2').value;
	var confidenceFrom =  document.getElementById('confFrom2').value;
	var confidenceTo = document.getElementById('confTo2').value;

	$.get("/search", {query: sql, doVar: varCheck, doQuantile: quantileCheck, doConfidence: confidenceCheck,
		quantileValue: quantileVal, confidenceFromValue: confidenceFrom, confidenceToValue: confidenceTo},
		function(data) {
			if (data.indexOf("Exception") != 0) {
				var jqObj = jQuery(data);
				var tabData = jqObj.find(".tableSecDiv").html();
				var closeTime = parseFloat(jqObj.find(".closeRes").html());
				$(".time_close").html("(Time: " + closeTime + "s)");
				$(".myTab_close").html(tabData);
				setMouse();
			}
			else {
				$(".myTab_close").html("");
				alert(data);
			}
		});
}

function execAbm() {
	//clear older time
	$(".time_abm").html("");

	var sql = $(".ta_abm").val();

	$(".myTab_abm").html("Loading ... ");
	var varCheck = "false";
	if(document.getElementById('varRadio').checked) {
		varCheck = "true"
	}
	var quantileCheck = "false";
	if(document.getElementById('quantileRadio').checked) {
		quantileCheck = "true"
	}
	var confidenceCheck = "false";
	if(document.getElementById('confidenceRadio').checked) {
		confidenceCheck = "true"
	}

	var quantileVal = document.getElementById('quantilePct').value;
	var confidenceFrom =  document.getElementById('confFrom').value;
	var confidenceTo = document.getElementById('confTo').value;

	$.get("/search", {query: sql, doVar: varCheck, doQuantile: quantileCheck, doConfidence: confidenceCheck,
		quantileValue: quantileVal, confidenceFromValue: confidenceFrom, confidenceToValue: confidenceTo},
		function(data) {
			if (data.indexOf("Exception") != 0) {
				var jqObj = jQuery(data);
				var tabData = jqObj.find(".tableSecDiv").html();
				var abmTime = parseFloat(jqObj.find(".abmRes").html());
				$(".time_abm").html("(Time: " + abmTime + "s)");
				$(".myTab_abm").html(tabData);
				setMouse();
			}
			else {
				$(".myTab_abm").html("");
				alert(data);
			}
		});
}

function compare() {
	var sec = "close";
	var sql = $(".ta_" + sec).val();

	$(".myTab_" + sec).html("Loading ... ");
	
	/*
	var varCheck = $(".varCheck").is(':checked');
	var existCheck = $(".existCheck").is(':checked');
	var quantileCheck = $(".quantileCheck").is(':checked');
	var confidenceCheck = $(".confidenceCheck").is(':checked');
	var quantileVal = $(".quantileBox").val();
	var confidenceFrom = $(".confFrom").val();
	var confidenceTo =$(".confTo").val();*/
		var varCheck = "false";
        if(document.getElementById('varRadio2').checked) {
                varCheck = "true"
        }
        var quantileCheck = "false";
        if(document.getElementById('quantileRadio2').checked) {
                quantileCheck = "true"
        }
        var confidenceCheck = "false";
        if(document.getElementById('confidenceRadio2').checked) {
                confidenceCheck = "true"
        }
        var quantileVal = document.getElementById('quantilePct2').value;
        var confidenceFrom = "5%"
        var confidenceTo = "95%"


	$.get("/compare", {query: sql, doVar: varCheck, doQuantile: quantileCheck, doConfidence: confidenceCheck,
		quantileValue: quantileVal, confidenceFromValue: confidenceFrom, confidenceToValue: confidenceTo},
		function(data) {
			if (data.indexOf("Exception") != 0) {
				var jqObj = jQuery(data);
				var tabData = jqObj.find(".tableSecDiv").html();

				var abmTime = parseFloat(jqObj.find(".abmRes").html());
				var closeTime = parseFloat(jqObj.find(".closeRes").html());
				var vanillaTime = parseFloat(jqObj.find(".vanillaRes").html());

				var timeDat = [['Methods', 'Time(s)'],['ABM',  abmTime],['Closed-Form', closeTime],['Query on Sample', vanillaTime]]
				//alert(timeDat);
				drawChart(timeDat, 2);
				$('#column_div').css({'top':500,'left':600, 'width':600, 'height': 300}).show();

				$(".myTab_" + sec).html(tabData);
				setMouse();
			}
			else {
				$(".myTab_" + sec).html("");
				alert(data);
			}
		});
}

function readTextFile(file)
{
    var rawFile = new XMLHttpRequest();
    var rawData = "empty";
    rawFile.open("GET", file, false);
    rawFile.onreadystatechange = function ()
    {
        if(rawFile.readyState == 4)
        {
            if(rawFile.status == 200 || rawFile.status == 0)
            {
                var rawData = rawFile.responseText;
                //alert(allText);
            }
	    rawData = "empty1";
        }
	else 
	{
	    rawData = "empty2";
	}
    }
    rawFile.send(null);
    return rawData;

}

function drawChart(dat, index) {
	if (index == 1) {
		//Distribution chart for each row
	    var data1 = google.visualization.arrayToDataTable(dat);
	    var options1 = {
	    		'title': 'Distribution',
	    		'curveType': 'function',
	    		'hAxis': {'format':'0.##E+0', 'showTextEvery':2},
	    		'vAxis': {'format':'0.##E+0', 'viewWindow':{'min':0}},
	        	'width': 450,
            	'height': 250,
				'legend': {'position':'none'}
	    	};
	    var chart1 = new google.visualization.LineChart(document.getElementById('chart_div'));
	    chart1.draw(data1, options1);
	}
	else if (index == 2) {
		//Closed-Form: compare chart
	    var data2 = google.visualization.arrayToDataTable(dat);
	    var options2 = {
	        	'title': 'Runtime Performance',
	        	'vAxis': {'minValue':0},
	        	'width': 450,
            	'height': 400
	        };
	    var chart2 = new google.visualization.ColumnChart(document.getElementById('column_div'));
	    chart2.draw(data2, options2);
	}
	else if (index == 3) {
		//Vanilla: relative error chart
	    var data3 = google.visualization.arrayToDataTable(dat);
	    var options3 = {
		        'title': 'Relative Error of Analytical Bootstrap over Vanilla Bootstrap',
		        'vAxis': {'title': 'Relative Error', 'titleTextStyle': {'color': 'black'}},
				'backgroundColor': {'stroke': '#000000', 'strokeWidth' : 1},
				'explorer': {'maxZoomOut':2}
	        };
	    var chart3 = new google.visualization.ColumnChart(document.getElementById('vanilla_error_div'));
	    chart3.draw(data3, options3);
	}
	else if (index == 4) {
		//Vanilla: performance chart
	    var data4 = google.visualization.arrayToDataTable([
	        	['Methods', 'Time(s)'],
	        	['Closed-Form', 8],
	        	['Vanilla', 20]
	        ]);
	    var options4 = {
	        	'title': 'Runtime Performance',
	        	'hAxis': {'title': '', 'titleTextStyle': {'color': 'red'}},
				'backgroundColor': {'stroke': '#000000', 'strokeWidth' : 1}
	        };
	    var chart4 = new google.visualization.ColumnChart(document.getElementById('vanilla_performance_div'));
	    chart4.draw(data4, options4);
	}
}

function getComplexData(rawString, titleArr, rowArr) {
	var rawArrs = rawString.split("\n");

	var arr = [];
	for (var i = 0; i < rawArrs.length; i++) {
		var rawArr = rawArrs[i].split(",");
		for (var j = 0; j < rawArr.length; j++) {
			var n = parseFloat(rawArr[j]);
			if (!isNaN(n)) {
				arr.push(n);
			}
		}
	}

	//alert(arr);

	var colCnt = titleArr.length - 1;
	
	var dat = [];
	dat.push(titleArr);
	for(var i = 0; i < arr.length / colCnt; i++) {
		var tmp = [];
		tmp.push(rowArr[i]);
		for(var j = 0; j < colCnt; j++) {
			tmp.push(arr[i*colCnt + j]);
		}
    	dat.push(tmp);
	}

	//alert(dat);
	return dat;
}

function getData(rawString, titleArr) {
	var arr = rawString.split(",");
	for (var i = 0; i < arr.length; i++) {
		arr[i] = parseFloat(arr[i]);
	}

	var colCnt = titleArr.length;
	
	var dat = [];
	dat.push(titleArr);
	for(var i = 0; i < arr.length / colCnt; i++) {
		var tmp = [];
		for(var j = 0; j < colCnt; j++) {
			tmp.push(arr[i*colCnt + j]);
		}
    	dat.push(tmp);
	}

	//alert(dat);
	return dat;
}

function setMouse() {
	var mouseX;
	var mouseY;
	$(document).mousemove(function(e) {
	   mouseX = e.pageX + 50 + "px"; 
	   mouseY = e.pageY - 50 + "px";
	});

	$(".tpchrow .draw").mouseover(function(){
	  //support multiple chart
	  //var argus = this.id.split(";");
	  //alert(argus.length);
	  //for (var i = 0; i < argus.length; i++) {

	  var dat = getData(this.id, ['X', 'Density']);
	  drawChart(dat, 1);
	  $('#chart_div').css({'top':mouseY,'left':mouseX}).show();
	});

	$('#chart_div').mouseover(function(){
	  $('#chart_div').show();
	});

	$(".tab").mouseout(function(){
	  $('#chart_div').hide();
	});

	$("#chart_div").click(function(){
	  $('#chart_div').hide();
	});

	$("#column_div").click(function(){
	  $('#column_div').hide();
	});

	$(".tpch_img").click(function(){
	  $('.tpch_img').hide();
	});
}

function load() {

//loading gif -- $(this).html('<img src=".gif" />');

//$('.tpch_img').hide();
show(1);
//$(".tab").html($(".example_tab").html());

//load percent menu
$("select[class*='percentMenu']").html($(".example_percent").html());

//load query menu
$(".tpchMenu_abm").html($(".example_abm").html());
$(".tpchMenu_close").html($(".example_close").html());
$(".tpchMenu_vanilla").html($(".example_vanilla").html());

//load query menu for plan check
$(".tpchMenu_input").html($(".test_tpch").html());

//--use google server side api
//$(".ta_input").val("select count(*) from region");

google.load("visualization", "1", {packages:["corechart"]});
google.setOnLoadCallback(drawChart);
//var dat = [['X', 'Density'],[-3,  0.004],[-2,  0.05],[-1,  0.24],[0,  0.40],[1,  0.24],[2,  0.05],[3,  0.004]];

setMouse();

//var treeData = [{"name":"FS[21]","val":"COL[1]: c1;COL[2]: c2;PREDICATE: x>3","children":[{"name":"SEL[20]","parent":"FS[21]","children":[{"name":"GBY[19]","parent":"SEL[20]","children":[{"name":"RS[18]","val":"nice and good","parent":"GBY[19]","children":[{"name":"GBY[17]","parent":"RS[18]","children":[{"name":"SEL[16]","parent":"GBY[17]","children":[{"name":"FIL[15]","parent":"SEL[16]","children":[{"name":"JOIN[14]","parent":"FIL[15]","children":[{"name":"RS[12]","parent":"JOIN[14]","children":[{"name":"SEL[11]","parent":"RS[12]","children":[{"name":"JOIN[10]","parent":"SEL[11]","children":[{"name":"RS[8]","parent":"JOIN[10]","children":[{"name":"TS[6]","parent":"RS[8]","children":[]}]},{"name":"RS[9]","parent":"JOIN[10]","children":[{"name":"TS[7]","parent":"RS[9]","children":[]}]}]}]}]},{"name":"RS[13]","parent":"JOIN[14]","children":[{"name":"SEL[5]","parent":"RS[13]","children":[{"name":"GBY[4]","parent":"SEL[5]","children":[{"name":"RS[3]","parent":"GBY[4]","children":[{"name":"GBY[2]","parent":"RS[3]","children":[{"name":"SEL[1]","parent":"GBY[2]","children":[{"name":"TS[0]","parent":"SEL[1]","children":[]}]}]}]}]}]}]}]}]}]}]}]}]}]}]}];
//var treeData = [{"val":"testVal","name":"FS[6]","children":[{"val":"testVal","name":"SEL[5]","parent":"FS[6]","children":[{"val":"testVal","name":"JOIN[4]","parent":"SEL[5]","children":[{"val":"testVal","name":"RS[2]","parent":"JOIN[4]","children":[{"val":"testVal","name":"TS[1]","parent":"RS[2]","children":[]}]},{"val":"testVal","name":"RS[3]","parent":"JOIN[4]","children":[{"val":"testVal","name":"TS[0]","parent":"RS[3]","children":[]}]}]}]}]}];
//var treeData = [{"val":"_col0;_col1;_col2;_col3;_col4;_col5;_col6;_col7;_col8;_col9;_col10;_col11;_col12;_col13;_col14;_col15;_col16;_col17;_col18;_col19;_col20;_col21;_col22;_col23;_col24;_col25;__col26;","name":"FS[6]","children":[{"val":"_col0 <- _col0;_col1 <- _col1;_col2 <- _col2;_col3 <- _col3;_col4 <- _col4;_col5 <- _col5;_col6 <- _col6;_col7 <- _col7;_col8 <- _col8;_col9 <- _col9;_col10 <- _col10;_col11 <- _col11;_col12 <- _col12;_col13 <- _col13;_col14 <- _col14;_col15 <- _col15;_col16 <- _col16;_col17 <- _col19;_col18 <- _col20;_col19 <- _col21;_col20 <- _col22;_col21 <- _col23;_col22 <- _col24;_col23 <- _col25;_col24 <- _col26;_col25 <- _col27;__col26 <- 1.0;","name":"SEL[5]","parent":"FS[6]","children":[{"val":"_col0;_col1;_col2;_col3;_col4;_col5;_col6;_col7;_col8;_col9;_col10;_col11;_col12;_col13;_col14;_col15;_col16;_col19;_col20;_col21;_col22;_col23;_col24;_col25;_col26;_col27;","name":"JOIN[4]","parent":"SEL[5]","children":[{"val":"VALUE._col0;VALUE._col1;VALUE._col2;VALUE._col3;VALUE._col4;VALUE._col5;VALUE._col6;VALUE._col7;VALUE._col8;VALUE._col9;VALUE._col10;VALUE._col11;VALUE._col12;VALUE._col13;VALUE._col14;VALUE._col15;VALUE._col16;","name":"RS[2]","parent":"JOIN[4]","children":[{"val":"tid;l_orderkey;l_partkey;l_suppkey;l_linenumber;l_quantity;l_extendedprice;l_discount;l_tax;l_returnflag;l_linestatus;l_shipdate;l_commitdate;l_receiptdate;l_shipinstruct;l_shipmode;l_comment;BLOCK__OFFSET__INSIDE__FILE;INPUT__FILE__NAME;TableName: lineitem isSampledTable: true;","name":"TS[0]","parent":"RS[2]","children":[]}]},{"val":"VALUE._col0;VALUE._col1;VALUE._col2;VALUE._col3;VALUE._col4;VALUE._col5;VALUE._col6;VALUE._col7;VALUE._col8;","name":"RS[3]","parent":"JOIN[4]","children":[{"val":"p_partkey;p_name;p_mfgr;p_brand;p_type;p_size;p_container;p_retailprice;p_comment;BLOCK__OFFSET__INSIDE__FILE;INPUT__FILE__NAME;TableName: part isSampledTable: false;","name":"TS[1]","parent":"RS[3]","children":[]}]}]}]}]}];
//var treeData = [{"val":"Schema;_col0;_col1;_col2;_col3;_col4;_col5;_col6;_col7;_col8;_col9;__col10;;","isAbmOp":"false","name":"FS[19]","children":[{"val":"Schema;_col0= _col0;_col1= _col1;_col2= quantile();_col3= quantile();_col4= quantile();_col5= quantile();_col6= quantile();_col7= quantile();_col8= quantile();_col9= quantile();__col10= exist_prob();;","isAbmOp":"true","name":"SEL[23]","parent":"FS[19]","children":[{"val":"Schema;_col0= _col0;_col1= _col1;_col2= Range(_col2);_col3= Range(_col3);_col4= Range(_col4);_col5= Range(_col5);_col6= Range(_col6);_col7= Range(_col7);_col8= Range(_col8);_col9= Range(_col9);__col10= Srv_Greater(__col10, 0.0, __col12);;","isAbmOp":"true","name":"SEL[22]","parent":"SEL[23]","children":[{"val":"Schema;_col0= _col0;_col1= _col1;_col2= _col2;_col3= _col3;_col4= _col4;_col5= _col5;_col6= _col6;_col7= _col7;_col8= _col8;_col9= _col9;__col10= __col10;__col11= __col11;__col12= GenRowId();;","isAbmOp":"true","name":"SEL[21]","parent":"SEL[22]","children":[{"val":"Schema;_col0= KEY._col0 (key);_col1= KEY._col1 (key);;_col2= srv_sum(VALUE._col0);_col3= srv_sum(VALUE._col1);_col4= srv_sum(VALUE._col2);_col5= srv_sum(VALUE._col3);_col6= srv_avg(VALUE._col4);_col7= srv_avg(VALUE._col5);_col8= srv_avg(VALUE._col6);_col9= srv_count(VALUE._col7);__col10= srv_count(VALUE.__col8);__col11= cond_merge(VALUE.__col9);;","isAbmOp":"false","name":"GBY[17]","parent":"SEL[21]","children":[{"val":"Schema;KEY._col0;KEY._col1;VALUE._col0;VALUE._col1;VALUE._col2;VALUE._col3;VALUE._col4;VALUE._col5;VALUE._col6;VALUE._col7;VALUE.__col8;VALUE.__col9;;","isAbmOp":"false","name":"RS[16]","parent":"GBY[17]","children":[{"val":"Schema;_col0= l_returnflag (key);_col1= l_linestatus (key);;_col2= srv_sum(l_quantity);_col3= srv_sum(l_extendedprice);_col4= srv_sum((l_extendedprice * (1 - l_discount)));_col5= srv_sum(((l_extendedprice * (1 - l_discount)) * (1 + l_tax)));_col6= srv_avg(l_quantity);_col7= srv_avg(l_extendedprice);_col8= srv_avg(l_discount);_col9= srv_count();__col10= srv_count();__col11= cond_merge();;","isAbmOp":"false","name":"GBY[15]","parent":"RS[16]","children":[{"val":"Schema;l_returnflag= l_returnflag;l_linestatus= l_linestatus;l_quantity= l_quantity;l_extendedprice= l_extendedprice;l_discount= l_discount;l_tax= l_tax;;","isAbmOp":"false","name":"SEL[14]","parent":"GBY[15]","children":[{"val":"Predicate: (l_shipdate <= '1998-09-01');","isAbmOp":"false","name":"FIL[20]","parent":"SEL[14]","children":[{"val":"Schema;tid;l_orderkey;l_partkey;l_suppkey;l_linenumber;l_quantity;l_extendedprice;l_discount;l_tax;l_returnflag;l_linestatus;l_shipdate;l_commitdate;l_receiptdate;l_shipinstruct;l_shipmode;l_comment;BLOCK__OFFSET__INSIDE__FILE;INPUT__FILE__NAME;;TableName: lineitem isSampledTable: true;","isAbmOp":"false","name":"TS[12]","parent":"FIL[20]","children":[]}]}]}]}]}]}]}]}]}]}];
//draw(treeData);

//alert(treeData[0].children.length);
//var r = getValFromJsonData(treeData[0],"RS[18]");
//alert(r);

//document.getElementById("ta_input").value = "SELECT l_partkey, SUM(l_extendedprice) as revenue \n\
//FROM lineitem as outer \n\
//WHERE l_quantity < \n\
//              ( SELECT SUM(l_quantity) / 3 \n\
//                FROM lineitem as inner \n\
//                WHERE inner.l_partkey = outer.l_partkey ) \n\
//GROUP BY l_partkey";
}

function getValFromJsonData(jsonObj, name) {

	if (jsonObj.name == name) {
		return jsonObj.val;
	}

	var children = jsonObj.children;
	if (typeof children != 'undefined') {
		for (var i = 0; i < children.length; i++) {
			var rs = getValFromJsonData(children[i], name);
			if (typeof rs != 'undefined') {
				return rs;
			}
		}
	}
	//return s;
}

function getAbmOpFromJsonData(jsonObj, name) {

	if (jsonObj.name == name) {
		return jsonObj.isAbmOp;
	}

	var children = jsonObj.children;
	if (typeof children != 'undefined') {
		for (var i = 0; i < children.length; i++) {
			var rs = getAbmOpFromJsonData(children[i], name);
			if (typeof rs != 'undefined') {
				return rs;
			}
		}
	}
	//return s;
}

//tree drawing
function draw(treeData) {
	var margin = {top: 100, right: 0, bottom: 0, left: -50},
	width = 800 - margin.right - margin.left,
	height = 800 - margin.top - margin.bottom;

	var i = 0;
	var tree = d3.layout.tree().size([600, 800]);
	//size([height, width]);

	// switch vertical / horizontal
	var diagonal = d3.svg.diagonal().projection(function(d) { return /*[0,0];*/ [d.x, d.y]; });

	//remove svg first
	d3.select("svg").remove();

	//find place for drawing
	var svg = d3.select(".plan").append("svg")
	 .attr("width", width*2 /* + margin.right + margin.left*/)
	 .attr("height", height*4 /* + margin.top + margin.bottom*/)
	 .append("g")
	 .attr("transform", "translate(" + 100 + "," + margin.top + ")");

	root = treeData[0];

	update(root);

	function update(source) {
	  // Compute the new tree layout.
	  var nodes = tree.nodes(root).reverse(), links = tree.links(nodes);

	  // Normalize for fixed-depth.
	  nodes.forEach(function(d) { d.y = d.depth * 80; });

	  // Declare the nodes
	  var node = svg.selectAll("g.node")
	   .data(nodes, function(d) { return d.id || (d.id = ++i); });

	  // Enter the nodes. -- switch vertical / horizontal
	  var nodeEnter = node.enter().append("g")
	   .attr("class", "node")
	   .attr("transform", function(d, i) {
            return "translate(" + d.x + "," + d.y + ")";})
	   .on("mouseover", function(d) {
	      var g = d3.select(this); // The node
	      // The class is used to remove the additional text later
	      var info = g.append('text')   
	         .classed('info', true);

	      var name = g.select('text').html();
	      var line = getValFromJsonData(treeData[0], name);
	      var isAbmOp = getAbmOpFromJsonData(treeData[0], name);

	      // Append schema & predicate besides node
	      if (typeof line != 'undefined') {
			  var words = line.split(';');
			  for (var i = 0; i < words.length; i++) {
			    var tspan = info.append('tspan').text(words[i]);
			    tspan.attr('x', 60);
			    if (i > 0) {
			    	tspan.attr('dy', '15');
			    }
			  }
		  }
  		})
	    .on("mouseout", function() {
	    	// Remove the info text on mouse out.
	    	d3.select(this).select('text.info').remove();
	    });

	  nodeEnter.append("circle")
	   .attr("r", 32)
	   .style("fill", function(d) {
	   			if (d.isAbmOp == "true") {
	   				return "#CCFFFF";
	   			}
	   			else {
	   				return "#fff";
	   			}
	   		})
	   .style("ss","red");

	  nodeEnter.append("text")
	   .attr("x", function(d) {
	   		return -28 + (10 - d.name.length)*2;
	   	})
	   .attr("dy", ".35em")
	   .attr("text-anchor", function(d) { return d.children || d._children ? "start" : "start"; })
	   .text(function(d) {	
	   		return d.name;
	   })
	   .style("fill-opacity", 1);

	  // Declare the links\A6
	  var link = svg.selectAll("path.link")
	   .data(links, function(d) { return d.target.id; });

	  // Enter the links.
	  link.enter().insert("path", "g")
	   .attr("class", "link")
	   .attr("d", diagonal);


		// new codes
		/*
        nodeEnter.on("mouseenter", function() {
            thisNode = d3.select(this)
            displayInfoBox(thisNode)
            thisNodeCol = thisNode.select(".node").style("ss")
            thisNode.selectAll(".node")
                .transition()
                .duration(1000)
                .style("fill", thisNodeCol)
        });

        nodeEnter.on("mouseleave", function(){
            destroyInfoBox()
            d3.select(this).selectAll(".node")
                .transition()
                .duration(250)
                .style("fill", null)
                .style("opacity", null)
        });*/
	}

	var normFac = 90
	var fontSize = 10
	var lineSpace = 2
	var boxHeight = 60
	var boxWidth = 130
	var infoBoxHeight = boxHeight*4.5
	var infoBoxWidth = boxWidth*4.5
	var width = 960
	var height = 1000
	
	   // Display up the info box (for mouse overs)
	function displayInfoBox(node) {
		var nodeName = node.attr("name")
        var infoX = infoBoxWidth/2*0.6 
        var infoY = infoBoxHeight/2*1.05
		var infoBox = svg.append("g")
		infoBox
            .attr("class", "popup")
            .attr("transform", function(d) {return "translate(" + infoX + "," + infoY + ")";})

		infoBox
            .append("text")
            .attr("y", function(d) {return  -infoBoxHeight/2 + fontSize + 2*lineSpace;})
            .attr("text-anchor", "middle")
            .text(nodeName)
            .attr("font-size", fontSize + 8 + "px")

      	infoBox.append("rect")
            .attr('id', 'performancebar')
            .attr("width", infoBoxWidth*0.99)
            .style("fill", "red")
            .style("stroke", "red")
            .attr("y", boxHeight/0)
            .attr("height", 0)
	
	
        var imgOffsetX = -infoBoxWidth/2 * 0.95
        var imgOffsetY = -infoBoxHeight/2 + fontSize+8 + 2*lineSpace
		infoBox
            .append("svg:image")
        	.attr("xlink:href", "sample_patches/"+nodeName+".png")
            .attr("width", infoBoxWidth*0.99)
            .attr("height", infoBoxHeight*0.99)
            .attr("transform", function(d) {return "translate(" + imgOffsetX + "," + imgOffsetY + ")";})
	}


    // Destroy the imfo box (when the mouseover ends)
    function destroyInfoBox() {
		svg.selectAll(".popup").remove()
	}

}

