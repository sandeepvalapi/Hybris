
var baseUrl;
var sectionFilter;
var priorityFilter;

function updateSectionFilter()
{
	sectionFilter = "";
	
	for (i = 0; i < document.getElementsByName("section").length; i++)
	{
		if(document.getElementsByName("section")[i].checked == true)
		{
			if(sectionFilter != "")
				sectionFilter += "&";

			sectionFilter += "section" + i + "=" + document.getElementsByName("section")[i].value;
		}
	}
}

function updatePriorityFilter()
{
	priorityFilter = "";
	
	for (i = 0; i < document.getElementsByName("priority").length; i++)
	{
		if(document.getElementsByName("priority")[i].checked == true)
		{
			if(priorityFilter != "")
				priorityFilter += "&";
			priorityFilter += document.getElementsByName("priority")[i].value + "=true";
		}
	}
}


function getLinkUrl()
{
	return baseUrl + "&" + sectionFilter + "&" + priorityFilter;
}


function applyFilters()
{
	updateSectionFilter();
	updatePriorityFilter();
	window.location.href=getLinkUrl();
}