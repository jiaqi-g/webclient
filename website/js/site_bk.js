window.onload = load();

function fillTpch(sec) {
	var tpchId = $(".tpchMenu_" + sec).val();
	var percent = $(".percentMenu_" + sec).val();
	//alert(percent);

	//TODO: tpchId == nothing
	$.get("/corrected/".concat(tpchId, ".hive"), {},
	function(data) {
		if (percent == 'nothing') {
			percent = 1;
		}
		data = data.replace(/lineitem/g, "lineitem_" + percent)
				   .replace(/customer/g, "customer_" + percent)
				   .replace(/partsupp/g, "partsupp_" + percent)
		$(".ta_" + sec).val(data);
	});
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
}

function quantileCheckClick() {
	var isChecked = $(".quantileCheck").is(':checked');
	if (isChecked) {
		$(".quantileBox").prop('disabled', false);
	} else {
		$(".quantileBox").prop('disabled', true);
	}
}

function confidenceCheckClick() {
	var isChecked = $(".confidenceCheck").is(':checked');
	if (isChecked) {
		$(".confFrom").prop('disabled', false);
		$(".confTo").prop('disabled', false);
	} else {
		$(".confFrom").prop('disabled', true);
		$(".confTo").prop('disabled', true);
	}
}

function plan() {

	var sql = $(".ta_input").val();
	$(".ta_abm").val(sql);
	$(".ta_close").val(sql);
	$(".ta_vanilla").val(sql);

	$.get("/plan", {query: sql},
	function(data) {
		//$(".plan").html(data);
		//alert(data);
		//var data = [{"name":"FS[2]","children":[{"name":"SEL[1]","parent":"FS[2]","children":[{"name":"TS[0]","parent":"SEL[1]","children":[]}]}]}];
		draw($.parseJSON(data));
		//alert(data);
	});
}

function exec(sec) {
	var sql = $(".ta_" + sec).val();

	$(".myTab_" + sec).html("Loading ... ");
	
	var varCheck = $(".varCheck").is(':checked');
	var existCheck = $(".existCheck").is(':checked');
	var quantileCheck = $(".quantileCheck").is(':checked');
	var confidenceCheck = $(".confidenceCheck").is(':checked');
	var quantileVal = $(".quantileBox").val();
	var confidenceFrom = $(".confFrom").val();
	var confidenceTo =$(".confTo").val();

	$.get("/search", {query: sql, doVar: varCheck, doExist: existCheck, doQuantile: quantileCheck, doConfidence: confidenceCheck,
		quantileValue: quantileVal, confidenceFromValue: confidenceFrom, confidenceToValue: confidenceTo},
	function(data) {
		var jqObj = jQuery(data);
		//alert(data);
		var tabData = jqObj.find(".tableSecDiv").html();
		//alert(tabData);
		$(".myTab_" + sec).html(tabData);
	});
}

function compare() {
	var sec = "close";
	var sql = $(".ta_" + sec).val();

	$(".myTab_" + sec).html("Loading ... ");
	
	var varCheck = $(".varCheck").is(':checked');
	var existCheck = $(".existCheck").is(':checked');
	var quantileCheck = $(".quantileCheck").is(':checked');
	var confidenceCheck = $(".confidenceCheck").is(':checked');
	var quantileVal = $(".quantileBox").val();
	var confidenceFrom = $(".confFrom").val();
	var confidenceTo =$(".confTo").val();

	//TODO: need to query another page (not search)
	$.get("/search", {query: sql, doVar: varCheck, doExist: existCheck, doQuantile: quantileCheck, doConfidence: confidenceCheck,
		quantileValue: quantileVal, confidenceFromValue: confidenceFrom, confidenceToValue: confidenceTo},
	function(data) {
		var jqObj = jQuery(data);
		var tabData = jqObj.find(".tableSecDiv").html();
		var timeData = jqObj.find(".timeSecDiv").html();
		//alert(timeData);

		$('#column_div').css({'top':180,'left':300, 'width':900, 'height': 500}).show();
		$(".myTab_" + sec).html(tabData);
	});
}

function drawChart(dat, index) {
	if (index == 1) {
	    var data1 = google.visualization.arrayToDataTable(dat);
	    var options1 = {
	    		title: 'Distribution',
	    		curveType: 'function', 
	        	width: 450,
            	height: 250,
	    		legend: {position: 'bottom'}
	    	};
	    var chart1 = new google.visualization.LineChart(document.getElementById('chart_div'));

	    chart1.draw(data1, options1);
	}
	else {
	    var data2 = google.visualization.arrayToDataTable([
	          ['Methods', 'Time(s)'],
	          ['ABM',  10],
	          ['Closed-Form', 8],
	          ['Vanilla', 20]
	        ]);
	    var options2 = {
	        	'title': 'Runtime Performance',
	        	'hAxis': {'title': '', 'titleTextStyle': {'color': 'red'}},
	        	'width': 900,
            	'height': 500
	        };
	    var chart2 = new google.visualization.ColumnChart(document.getElementById('column_div'));
	    chart2.draw(data2, options2);
	}
}

function setMouse() {
	var mouseX;
	var mouseY;
	$(document).mousemove(function(e) {
	   mouseX = e.pageX + "px"; 
	   mouseY = e.pageY + "px";
	});

	$(".tpchrow").mouseover(function(){
	  var arr = this.title.split(",");

	  arr[0] = parseFloat(arr[0]);
	  arr[1] = parseFloat(arr[1]);
	  arr[2] = parseFloat(arr[2]);
	  arr[3] = parseFloat(arr[3]);
	  arr[4] = parseFloat(arr[4]);
	  arr[5] = parseFloat(arr[5]);
	  arr[6] = parseFloat(arr[6]);
	  arr[7] = parseFloat(arr[7]);
	  arr[8] = parseFloat(arr[8]);
	  arr[9] = parseFloat(arr[9]);
	  arr[10] = parseFloat(arr[10]);
	  arr[11] = parseFloat(arr[11]);
	  arr[12] = parseFloat(arr[12]);
	  arr[13] = parseFloat(arr[13]);
	  

	  var dat = [
	  ['X', 'Density'],
	  [arr[0], arr[1]],
	  [arr[2], arr[3]],
	  [arr[4], arr[5]],
	  [arr[6], arr[7]],
	  [arr[8], arr[9]],
	  [arr[10], arr[11]],
	  [arr[12], arr[13]]
	  ];
	  //var dat = [['X', 'Density'],['-3',  0.004],['-2',  0.05],['-1',  0.24],['0',  0.40],['1',  0.24],['2',  0.05],['3',  0.004]];
	  //var dat = [['X', 'Density'],[-3,  0.004],[-2,  0.05],[-1,  0.24],[0,  0.40],[1,  0.24],[2,  0.05],[3,  0.004]];
	  
	  //alert(dat);
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
}

function load() {

show(1);

//--use google server side api
$(".ta_input").val("select count(*) from region");
google.load("visualization", "1", {packages:["corechart"]});
google.setOnLoadCallback(drawChart);
//var dat = [['X', 'Density'],[-3,  0.004],[-2,  0.05],[-1,  0.24],[0,  0.40],[1,  0.24],[2,  0.05],[3,  0.004]];

setMouse();
//var treeData = [{"name":"FS[21]","children":[{"name":"SEL[20]","parent":"FS[21]","children":[{"name":"GBY[19]","parent":"SEL[20]","children":[{"name":"RS[18]","parent":"GBY[19]","children":[{"name":"GBY[17]","parent":"RS[18]","children":[{"name":"SEL[16]","parent":"GBY[17]","children":[{"name":"FIL[15]","parent":"SEL[16]","children":[{"name":"JOIN[14]","parent":"FIL[15]","children":[{"name":"RS[12]","parent":"JOIN[14]","children":[{"name":"SEL[11]","parent":"RS[12]","children":[{"name":"JOIN[10]","parent":"SEL[11]","children":[{"name":"RS[8]","parent":"JOIN[10]","children":[{"name":"TS[6]","parent":"RS[8]","children":[]}]},{"name":"RS[9]","parent":"JOIN[10]","children":[{"name":"TS[7]","parent":"RS[9]","children":[]}]}]}]}]},{"name":"RS[13]","parent":"JOIN[14]","children":[{"name":"SEL[5]","parent":"RS[13]","children":[{"name":"GBY[4]","parent":"SEL[5]","children":[{"name":"RS[3]","parent":"GBY[4]","children":[{"name":"GBY[2]","parent":"RS[3]","children":[{"name":"SEL[1]","parent":"GBY[2]","children":[{"name":"TS[0]","parent":"SEL[1]","children":[]}]}]}]}]}]}]}]}]}]}]}]}]}]}]}];
//draw(treeData);

//document.getElementById("ta_input").value = "SELECT l_partkey, SUM(l_extendedprice) as revenue \n\
//FROM lineitem as outer \n\
//WHERE l_quantity < \n\
//              ( SELECT SUM(l_quantity) / 3 \n\
//                FROM lineitem as inner \n\
//                WHERE inner.l_partkey = outer.l_partkey ) \n\
//GROUP BY l_partkey";
}

//tree drawing
function draw(treeData) {

	//alert(treeData);
	
	var margin = {top: 100, right: 100, bottom: 0, left: 0},
	width = 1000 - margin.right - margin.left,
	height = 1200 - margin.top - margin.bottom;

	var i = 0;

	var tree = d3.layout.tree().size([height, width]);

	// switch vertical / horizontal
	var diagonal = d3.svg.diagonal().projection(function(d) { return [d.x, d.y]; });

	//remove first
	d3.select("svg").remove();

	//find place for drawing
	var svg = d3.select(".plan").append("svg")
	 .attr("width", width + margin.right + margin.left)
	 .attr("height", height + margin.top + margin.bottom)
	 .append("g")
	 .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

	root = treeData[0];

	update(root);

	function update(source) {
	  // Compute the new tree layout.
	  var nodes = tree.nodes(root).reverse(), links = tree.links(nodes);

	  // Normalize for fixed-depth.
	  nodes.forEach(function(d) { d.y = d.depth * 120; });

	  // Declare the nodes
	  var node = svg.selectAll("g.node")
	   .data(nodes, function(d) { return d.id || (d.id = ++i); });

	  // Enter the nodes. -- switch vertical / horizontal
	  var nodeEnter = node.enter().append("g")
	   .attr("class", "node")
	   .attr("transform", function(d) { return "translate(" + d.x + "," + d.y + ")"; });

	  nodeEnter.append("circle")
	   .attr("r", 28)
	   .style("fill", "#fff")
	   .style("ss","red");

	  nodeEnter.append("text")
	   // .attr("x", function(d) { return d.children || d._children ? -13 : 13; })
	   .attr("x", function(d) { return d.children || d._children ? 15 : -15; })
	   .attr("dy", ".35em")
	   .attr("text-anchor", function(d) { return d.children || d._children ? "end" : "start"; })
	   .text(function(d) { return d.name; })
	   .style("fill-opacity", 1);

	  // Declare the links¦
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
        }); */

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
		svg.selectAll(".popup")
			.remove()
	}


}
