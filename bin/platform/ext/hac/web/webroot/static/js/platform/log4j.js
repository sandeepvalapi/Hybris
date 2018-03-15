$(function() {
	var table;
	table = $('#loggers').dataTable({
        "iDisplayLength" : 50,
        "aoColumns": [
            { "sClass": "truncate" },
            { "sClass": "truncate" },
            { }
        ]
    });

	function validateLoggerName(string) {
		var regex = new RegExp("^\\w+(\\.\\w+)*$"); // java class pattern, ex.: 'this.is.my.Class'
		return regex.test(string);
	}
	
	$("body").on('click', '#configureLogLevelButton',function(e) {	
		var loggerName = $("#loggerToConfigure").val();
		var levelName = $("#logLevelToSet").val();

        if (!validateLoggerName(loggerName))
        {
            hac.global.error("Please enter a valid logger!");
            return;
        }
		
		configureLogger(loggerName, levelName);
	});
	
	$("body").on('change', '.loggerLevels',function() {
		var loggerName = $(this).attr('data-loggerName');
		var levelName = $(this).val();
	
		configureLogger(loggerName, levelName);
	});
	
	function configureLogger(loggerName, levelName) {
		var url = $('#loggers').attr('data-changeLoggerLevelUrl');
		debug.log("loggerName -> " + loggerName);
		debug.log("levelName -> " + levelName);
        var token = $("meta[name='_csrf']").attr("content");

        $.ajax({
			url : url,
			type : 'POST',
			data: 'loggerName=' + loggerName + "&levelName=" + levelName,
			headers : {
				'Accept' : 'application/json',
                'X-CSRF-TOKEN' : token
			},
			success : function(data) {
				if (!!data.loggers) {
                    hac.global.notify("Log level for logger " + data.loggerName + " changed to " + data.levelName);

                    table.fnClearTable();

                    $.each(data.loggers, function (index, logger) {
						var select = $('<select>');
                        select.addClass("loggerLevels");
                        select.attr("data-loggerName", logger.name);
                        $(data.levels).each(function(index, level) {
                        	if(level.standardLevel === logger.effectiveLevel.standardLevel)
							{
                                select.append($('<option selected="selected">').val(level.standardLevel).text(level.standardLevel));
							} else {
                                select.append($('<option>').val(level.standardLevel).text(level.standardLevel));
                            }
                        });


						var name = "<div title=" + logger.name + ">" + logger.name + "</div>";
						var parentName = "<div title=" + logger.parentName + ">" + logger.parentName + "</div>";

                        table.fnAddData([name,
                            parentName, select[0].outerHTML]);
                    });
                } else if(data.validationError) {
                    hac.global.error("Please enter a valid logger!");
				} else {
                    hac.global.notify("Failed to change level for logger " + data.loggerName);
				}
			},
			error : hac.global.err
		});
	}
});
