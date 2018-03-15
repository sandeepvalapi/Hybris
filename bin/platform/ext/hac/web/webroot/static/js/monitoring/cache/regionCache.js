var tables = {};
var intervalId;

$(document).ready(function() {
	
    $("#accordion").accordion({
		heightStyle: "content",
      active: false,
      collapsible: true
    });
	
	$('#resetCache').click(function(e) {
        var token = $("meta[name='_csrf']").attr("content");
        var url = $('#resetCache').attr('data-url');
		$.ajax({
			url : url,
			type : 'POST',
			headers : {
				'Accept' : 'application/json',
                'X-CSRF-TOKEN' : token
			},
			success : function(data) {
				//not recommended on production mode, use the console.log() only if you are sure the console object is support by browser
				//console.log(data);
				updateRegions(data.regions);
			},
			error: hac.global.err
		});	
		
	});
	
	update(true);
	intervalId = setInterval('update(false)', 2000);
	
});

function update(init)
{
    var token = $("meta[name='_csrf']").attr("content");
    var url = $('#accordion').attr('data-updateDataUrl');
	$.ajax({
		url : url,
		type : 'GET',
		headers : {
			'Accept' : 'application/json',
            'X-CSRF-TOKEN' : token
		},
		success : function(data) {
			//console.log(data);
			
			if (init)
				initTables(data.regions);
			
			updateRegions(data.regions);
			
		},
		error: hac.global.err
	});			
}

function initTables(regions) {
	
	for (var pos in regions)
	{
		var regionName = regions[pos].name;
		tables[regionName] = $('#' + regions[pos].name + "_types").dataTable({
			
		});
	}
}

function updateRegions(regions)
{
	var pattern = /^([a-zA-Z0-9]+) \((.+)\)$/;
	for (var pos in regions)
	{
		var region = regions[pos];
		
		tables[region.name].fnClearTable();
		
		$('#' + region.name + '_maxEntries').html(region.maxEntries);
		$('#' + region.name + '_maxReachedSize').html(region.maxReachedSize);
		$('#' + region.name + '_factor').html(region.factor);
		
		try {
			$('#' + region.name + '_Hits').html(region.cacheStatistics.hits);
			$('#' + region.name + '_Misses').html(region.cacheStatistics.misses);
			$('#' + region.name + '_Invalidations').html(region.cacheStatistics.invalidations);
			$('#' + region.name + '_Evictions').html(region.cacheStatistics.evictions);
			$('#' + region.name + '_Fetches').html(region.cacheStatistics.fetches);
			$('#' + region.name + '_InstanceCount').html(region.cacheStatistics.instanceCount);
		}
		catch(err)
		{
			//
		}
		
		for (var typePos in region.typesStatistics)
		{
			var type = region.typesStatistics[typePos];
			var ratio = Math.round(100 * type.hits/(type.hits + type.misses));
			var instanceCount = type.misses - type.evictions - type.invalidations;
			
			var matches = pattern.exec(type.typeName);
			//console.log(matches);
			
			tables[region.name].fnAddData([(matches != null) ? getHoverSpan(matches[1], matches[2]) : type.type, type.hits, type.misses, ratio, type.invalidations, type.evictions, type.fetches, instanceCount]);
		}
	}
}

function getHoverSpan(name, fullName)
{
	return '<span title="' + fullName + '">' + name + '</span>';
}
