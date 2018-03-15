if (!hac) var hac = {};

hac.searchsuggest = {};

hac.searchsuggest["features"] = [
 		/*platform*/
 		{
			label : "Tenants",
			desc : "Tenants switching and configuration.",
			link : "tenants" 			
 		},
 		{
			label : "PK Analyzer",
			desc : "Use the PK analyzer to extract type code information.",
			link : "platform/pkanalyzer" 			 			
 		},
 		{
			label : "Classpath Analyzer",
			desc : "Classpath analyze tool.",
			link : "platform/jars" 			 			
 		},
 		{
			label : "Initialization",
			desc : "Initialize the new hybris Multichannel Suite.",
			link : "platform/init"
		},
		{
			label : "Update",
			desc : "Update the running hybris Multichannel Suite.",
			link : "platform/update"
		}, 		
 		
 		{
			label : "Configuration",
			desc : "Display a list of all currently set properties and their values.",
			link : "platform/config"
		},                                 
		{
			label : "System",
			desc : "Information about the running Java Virtual Machine, System environment and properties.",
			link : "platform/system"
		},                                 
		{
			label : "Log4j",
			desc : "List of all configured logges and tool for changing logger levels in runtime.",
			link : "platform/log4j"
		},                                 
		{
			label : "Extensions",
			desc : "List all installed extensions of the hybris Multichannel Suite.",
			link : "platform/extensions"
		}, 
		{
			label : "License",
			desc : "Display information on the currently used license.",
			link : "platform/license"
		},
		{
			label : "Support",
			desc : "An easy-to-use functionality for providing information about the running system to the hybris Support.",
			link : "platform/support"
		},		
		
		/* monitoring */
		
		{
			label : "Cache",
			desc : "Show status of the hybris Platform cache.",
			link : "monitoring/cache"
		},
		{
			label : "Cluster",
			desc : "List of available cluster nodes.",
			link : "monitoring/cluster"
		},	
		{
			label : "Database",
			desc : "List of active database connections.",
			link : "monitoring/database"
		},	
		{
			label : "Cron Jobs",
			desc : "List of all running cron jobs with option to abort.",
			link : "monitoring/cronjobs"
		},	
		{
			label : "JMX MBeans",
			desc : "List of available JMX MBeans.",
			link : "monitoring/jmx"
		},		
		{
			label : "Memory",
			desc : "Show available java virtual machine VM memory.",
			link : "monitoring/memory"
		},		
		{
			label : "Threaddump",
			desc : "List of all running threads.",
			link : "monitoring/threaddump"
		},	
		{
			label : "Performance",
			desc : "Run performance tests, Linpack, SQL, SQL Max and overall test",
			link : "monitoring/performance"
		},		

		/* maintenance */
		{
			label : "Cleanup",
			desc : "Cleanup the type system, lucene search indexes or media files.",
			link : "maintain/cleanup"
		},		
		{
			label : "Encryption Keys",
			desc : "Generate Advanced Encryption Standard (AES) keys or migreate existing keys",
			link : "maintain/keys"
		},
		
		{
			label : "Deployment",
			desc : "Look up types with or without deployment tables as well as existing tables without types.",
			link : "maintain/deployments"
		},		
		
		/* console */
		{
			label : "BeanShell Console",
			desc : "Execute code directly via the hybris BeanShell console.",
			link : "console/beanshell"
		},
		{
			label : "Groovy Console",
			desc : "Execute code directly via the hybris Groovy console.",
			link : "console/groovy"
		},		
		{
			label : "FlexibleSearch",
			desc : "Test FlexibleSearch queries and plain SQL statements.",
			link : "console/flexsearch"
		},
		{
			label : "Impex Import",
			desc : "Impex Import data page based on the ImpEx functionality.",
			link : "console/impex/import"
		},
		{
			label : "Impex Export",
			desc : "Impex Export data page based on the ImpEx functionality.",
			link : "console/impex/export"
		},
		{
			label : "LDAP",
			desc : "Query your configured LDAP server, import LDIF file and check configuration.",
			link : "console/ldap"
		}	

];


hac.searchsuggest.searchFeatures = function(query) {
	debug.log("Searching for: " + query);
	var matches = [];
	var regex = new RegExp('\\b'+query+'\\w+\\b', "gi")

	for (pos in hac.searchsuggest.features) {
		var feature = hac.searchsuggest.features[pos];
		if (feature.label.match(regex) || feature.desc.match(regex))
			matches[matches.length] = feature;
	}
	
	if (matches.length > 9)
		return matches.slice(0,9);
	else return matches;
	
};


$(document).ready(function() {
	$( "#searchsuggest" ).autocomplete({
		minLength: 0,
		/*source: hac.searchsuggest["features"],*/
		source: function(req, callback) {
			callback(hac.searchsuggest.searchFeatures(req.term));
			
			
		},
		focus: function( event, ui ) {
			$( "#searchsuggest" ).val( ui.item.label );
			return false;
		},
		select: function( event, ui ) {
			debug.log('selected ' + ui.item.link);
			$( "#searchsuggest" ).val( ui.item.label );
			location.href = hac.homeLink + ui.item.link;
			return false;
		}
	})
	.data( "ui-autocomplete" )._renderItem = function( ul, item ) {
		return $( "<li></li>" )
			.data( "ui-autocomplete-item", item )
			.append( "<a><strong>" + item.label + "</strong><br>" + item.desc + "</a>" )
			.appendTo( ul );
	};
});