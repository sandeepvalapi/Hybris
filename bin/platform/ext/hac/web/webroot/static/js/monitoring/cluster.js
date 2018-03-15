	var nodestable;
	
	$(document).ready(function() {
		nodestable = $('#clusternodes').dataTable({
			"bPaginate" : false,
			"bFilter" : false,
			"bInfo" : false
		});		
		
		
		$('#ping').click(function() {

			hac.global.notify("Nodes will be pinged. Table will update...");
            var token = $("meta[name='_csrf']").attr("content");

            var url = $('#ping').attr('data-url');
			$.ajax({
				url:url,
				type:'POST',
				headers:{
                    'Accept':'application/json',
                    'X-CSRF-TOKEN' : token
                },
				success: function(data) {
					update(data);
				},
				error: hac.global.err
			});	
			
		});	
		
		updateNodes();
		
	});
	
	function update(data)
	{
		updateNodeTable(data.nodes);
		updateConfiguration(data);
		updateBroadcastMethods(data.broadcastMethods);	
	}
	
	
	function updateNodes()
	{
		debug.log('updating...');
		var url = $('#clusternodes').attr('data-updateNodesUrl');
		$.ajax({
			url:url,
			type:'GET',
			headers:{'Accept':'application/json'},
			success: function(data) {
				update(data)
				
			},
			error: hac.global.err
		});	
	}
	
	function updateNodeTable(data)
	{
		nodestable.fnClearTable();
		for (pos in data)
		{
			var node = data[pos];
			nodestable.fnAddData([node.nodeIP, node.nodeID, node.dynamicNodeID, node.methodName]);
		}
	}
	
	function updateConfiguration(data)
	{
		$('#clusteringEnabled').html('' + data.clusterEnabled);
		$('#clusterIslandId').html(data.clusterIslandId);
		$('#clusterNodeId').html(data.clusterNodeId);
		$('#dynamicClusterNodeId').html(data.dynamicClusterNodeId);
	}	
	
	function updateBroadcastMethods(methods)
	{
		var wrapper = $('#methodWrapper'); 
		wrapper.html('');
		
		for (pos in methods)
		{
			var method = methods[pos];
			
			var head = '<h3>'+method.name+' Cluster Settings</h3>';
			wrapper.append(head);
			
			var settingsMap = method.settings;
			wrapper.append(dl(settingsMap));
			
			if (method.nodes) //only build nodes table in case there is data. only will be available for TCP clustering, not for UDP
			{
				var methodNodes = method.nodes;
				wrapper.append('<h4>Nodes</h4>');
				
				wrapper.append('<table id="'+method.name+'_table"><thead><tr><th>clusterNodeId</th><th>clusterNodeState</th><th>serverAddress</th><th>lastUp</th></tr></thead><tbody></tbody></table>')
				
				var tcpTable = $('#'+ method.name+'_table').dataTable({bInfo:false, bPaginate:false, bFilter: false, bSort: false});
				
				for (pos in methodNodes)
				{
					var nodeMap = methodNodes[pos];
					tcpTable.fnAddData([nodeMap.nodeID, nodeMap.nodeID, nodeMap.serverAddress, nodeMap.lastUp]);
				}
			}
			
		}
	}
	
	
	function dl(map)
	{
		var dl = '';
		dl += '<dl>';
		
		for (key in map)
		{
			dl += dlItem(key, map[key]);
		}
		
		dl+= '</dl>';
		
		return dl;		
	}
	
	function dlItem(key, value)
	{
		var item = '';
		item += '<dt>'+key+'</dt>';
		item +='<dd>'+value+'</dd>';
		return item;
	}