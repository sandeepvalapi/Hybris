$(function () {

    $('#suspend').click(function () {

        if (confirm("Are you sure you want to suspend system?")) {
            var getTokenUrl = $('#suspend').attr('data-suspendTokenUrl');
            $.ajax({
                url: getTokenUrl,
                type: 'GET',
                headers: {
                    'Accept': 'application/json'
                },
                success: function (data) {
                    var form = $('<form action="' + $('#suspend').attr('data-suspendUrl') + '" method="post">' +
                        '<input type="hidden" name="suspendToken" value="' + data.suspendToken + '" />' +
                        '<input type="hidden" name="forShutdown" value="' + $('#forShutdown').is(':checked') + '" />' +
                        '</form>')
                    $('body').append(form)
                    form.submit()

                },
                error: hac.global.err
            });
        }
    });

    function renderOperations(dtos) {
        var output = $('<ul>');

        dtos.forEach(function (node) {
            output.append(renderNode(node))
        });

        return output;
    }

    function renderNode(node) {
        var output = $('<ul>');
        var li = $('<li ' + (node.suspendable === false ? 'class="cantSuspend"' : '') + ">");

        li.append(span("ID: "));
        li.append(node.threadId);
        li.append(delim());
        li.append(span("Name: "));
        li.append(node.threadName);

        if (node.category !== null) {
            li.append(delim());
            li.append(span("Category: "));
            li.append(node.category);
        }

        if (node.statusInfo !== null) {
            li.append(delim());
            li.append(span("Status Info: "));
            li.append(node.statusInfo, "infoName", node.statusInfo);
        }

        node.childThreads.forEach(function(child) {
            li.append(renderNode(child));
        });

        output.append(li);

        return output;
    }

    function span(content, cssClass, title) {
        var clazz = cssClass === null ? "infoName" : cssClass;
        var output = $('<span class="' + clazz + ' title="' + title + '">');
        output.append(content);

        return output;
    }

    function delim() {
        return ' :: '
    }

    function updateRunningOperationsView() {
        var url = $('#runningOperations').attr('data-updateRunningOperationsUrl')

        $.ajax({
            url: url,
            type: 'GET',
            headers: {
                'Accept': 'application/json'
            },
            success: function (data) {
                $('#runningOperations').html(renderOperations(data.runningOperations));
                $('#systemStatus').html(data.systemStatus);
            },
            error: hac.global.err
        });
    };

    setInterval(updateRunningOperationsView, 5000);

    updateRunningOperationsView();
})