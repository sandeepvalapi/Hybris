ACC.sanitizer = {
		
		matcher: /<\/?([a-zA-Z0-9]+)*(.*?)\/?>/igm,

		whitelist: [
			'pre',
			'address',
			'em',
			'hr'
		],

		sanitize: function(html, useWhitelist=true) {
			html = String(html) || '';

		    var matches = ACC.sanitizer.match(html);

		    matches.forEach(function(tag){
		        if (!useWhitelist || ACC.sanitizer.whitelist.indexOf(tag.name) == -1) {
		          html = html.replace(tag.full, '');
		        }
		    });

		    return html;
	},

	match: function(html) {
	    html = String(html) || '';

	    var matches = [],
	        match;

	    while ((match = ACC.sanitizer.matcher.exec(html)) != null) {
	      var attrr = match[2].split(' '),
	          attrs = [];

	      // extract attributes from the tag
	      attrr.shift();
	      attrr.forEach(function(attr){
	        attr = attr.split('=');
	        var attr_name = attr[0],
	            attr_val = attr.length > 1 ? attr.slice(1).join('=') : null;
	        // remove quotes from attributes
	        if (attr_val && attr_val.charAt(0).match(/'|"/)) attr_val = attr_val.slice(1);
	        if (attr_val && attr_val.charAt(attr_val.length-1).match(/'|"/)) attr_val = attr_val.slice(0, -1);
	        attr = {
	          name: attr_name,
	          value: attr_val
	        };
	        if (!attr.value) delete attr.value;
	        if (attr.name) attrs.push(attr);
	      });

	      var tag = {
	        full: match[0],
	        name: match[1],
	        attr: attrs
	      };

	      matches.push(tag);
	    }

	    return matches;
	  }
};