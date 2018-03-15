
//Chart Object, containing all relevant information.
function Chart(chartContainer, legendContainer, roundNrs, unit, tickCount, selectable){
	
	this.roundNrs = roundNrs;
	this.timeformat = timeformat;
	this.tickCount = tickCount;
	this.unit = unit;
	this.chartId = chartContainer.attr("chartId");
	this.chart = null;
	this.chartContainer = chartContainer;
	this.legendContainer = legendContainer;
	this.selectable = selectable;
	
	//plots the chart into the given div.
	this.plot = $.proxy(function(){
		this.chart = $.plot(this.chartContainer,[], createChartOptions(this.legendContainer, this.roundNrs, this.unit, this.tickCount, this.timeformat, this.selectable,  false));
	},this);
	
	//makes chart selectable, if it should be.
	if(selectable){
		this.chartContainer.bind("plotselected", $.proxy(function(event, ranges){
			this.chart.clearSelection(true);
			
			$("#overlay").css("visibility", "visible");
			$("#zoomedChart").css("visibility", "visible");
			
			$("#overlay").fadeIn();
			$("#zoomedChart").show();
			fetchSelection(ranges.xaxis.from, ranges.xaxis.to, this);
		},this));
	}
	
	//Build up new Data and plot them.
	this.update = $.proxy(function(data){
		var plotData = [];
		
		for(var line in data.lines){
			var lineData = {};
			lineData.label = data.lines[line].label;
			lineData.data = data.lines[line].data;
			lineData.color = data.lines[line].color;
			if(lineData.data.length > 0){
				plotData.push(lineData);
			}
		}
		if(plotData.length > 0){
			this.chart.setData(plotData);
			this.chart.setupGrid();
			this.chart.draw();
		}
		else{
			this.deactivate();
		}
	},this);
	
	//deactivates chart, e.g. when no data available.
	this.deactivate = $.proxy(function(){
		this.disabled = true;
		this.chartContainer.empty();
		this.legendContainer.empty();
		this.chart = null;
		this.chartContainer.html(
				"<div class='ui-widget' style='position:absolute;top:35%;left:25%'>" +
					"<div class='ui-state-highlight-charts ui-corner-all' style='padding:0.7em'>" + 
						"<span class='ui-icon ui-icon-info' style='margin-top:5px;float:left;margin-right:.3em'></span>" +
						"<div style='float:right;margin-top:3px'>No Data available.</div>" + 
						"<div style='clear:both'></div>" +
					"</div>" +
				"</div>");
	},this);
}

//Create Chart-options for Line-Charts on hybris hAC.
function createChartOptions(legendContainer, round, unit, tickCount, timeformat, selectable, big){
	return {
		xaxis: {
			mode: "time",
	        ticks: function(axis){ //Function for generating correct number of ticks statically.
	        	var date = new Date();
	        	var dif = axis.max - axis.min;
	            var res = new Array(tickCount);
	            res = new Array(tickCount);
	            for(var i = 0; i <= tickCount;i++){
	            	if(i == tickCount - 1){
	            		res.push(axis.min + dif * i * (1/(tickCount-1)) - (dif / 40));
	            	}
	            	else{
	            		res.push(axis.min + dif * i * (1/(tickCount-1)));
	            	}
	            }
	            return res;
	        },
	        timeformat: timeformat,
	        tickLength: 0
	    },
	    yaxis: {
	    	labelWidth: 20,
	    	ticks: 2,
	    	minTickSize: 2,
	    	min: 0
	    },
	    series: {
	    	lines: { 
	    		show: true, 
	    		lineWidth: 2,
	    		fill: 0.15
	    	},
	        points: { show: false },
	        shadowSize: 0
	    },
	    legend: {
	    	container: legendContainer,
	    	position: "nw",
	    	noColumns: ((big) ? 5 : 2),
	    	backgroundColor: null,
	    	backgroundOpacity: 0,
	    	labelBoxBorderColor: null,
	        labelFormatter: function(label, series) {
	        	return "<span style='font-size:11px;font-family:Arial'><b>" + label + "</b></span>";
	        }
	     },
	     grid: {
	    	 show: true,
	    	 clickable: false,
	    	 hoverable: true,
	    	 borderWidth: 0
	     },
	     selection: {
	    	 mode: ((selectable) ? "x" : null), 
	    	 color: "#2055CC"
	     },
	     tooltip: true,
	     tooltipOpts: {
	    	 content: "<label style='font-size:12px;font-family:Arial'><strong>%x</strong></label></br><label style='font-size:13px;font-family:Arial'>%s:<strong> %y."+round+" "+unit+"</strong></label>",
	    	 shifts: {
	    		 x: 10,
	    		 y: 20
	    	 },
	    	 defaultTheme: true,
	    	 timezone: null,
	    	 timezoneoffset: 0
	     }
	}
}