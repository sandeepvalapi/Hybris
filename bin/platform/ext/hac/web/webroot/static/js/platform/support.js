var buttonTextCreate = "Create";
var buttonTextSend = "Send via eMail"

$(document).ready(function () {
    var token = $("meta[name='_csrf']").attr("content");

    $('#buttonCreateZIP').click(
        function (e) {
            var files = [];
            $('#buttonCreateZIP').html(
                    buttonTextCreate + ' '
                    + hac.global.getSpinnerImg());
            $('#buttonDownloadZIP').fadeOut();
            $('#buttonSendZIP').fadeOut();

            $('#supportFiles input:checked').each(
                function (element) {
                    files[files.length] = this.id;
                });

            var url = $('#buttonCreateZIP')
                .attr('data-url');
            $.ajax({
                url: url,
                type: 'POST',
                data: 'files='
                    + encodeURIComponent(files
                        .join('|')),
                headers: {
                    'Accept': 'application/json',
                    'X-CSRF-TOKEN': token
                },
                success: function (data) {
                    debug.log(data);
                    $('#buttonCreateZIP').html(
                        buttonTextCreate);
                    $('#buttonDownloadZIP').html(
                            'Download (' + data.size
                            + 'kb)...');
                    $('#buttonDownloadZIP').fadeIn();
                    $('#buttonSendZIP').fadeIn();

                },
                error: hac.global.err
            });

        });

    $('#checkSelectAll').click(function (e) {
        var check = this.checked;
        $('#supportFiles input').each(function (element) {
            this.checked = check;
        });

    });

    $('#buttonDownloadZIP').click(function (e) {
        $('#downloadForm').submit();
    });

    $('#buttonSendZIP').click(function (e) {
        var token = $("meta[name='_csrf']").attr("content");
        $('#buttonSendZIP')
            .html(
                buttonTextSend
                + ' '
                + hac.global
                .getSpinnerImg());

        var url = $('#buttonSendZIP').attr('data-url');
        $.ajax({
            url: url,
            type: 'POST',
            headers: {
                'Accept': 'application/json',
                'X-CSRF-TOKEN' : token
            },
            success: function (data) {
                debug.log(data);
                $('#buttonSendZIP')
                    .html(
                    buttonTextSend);

                if (data.success)
                    hac.global
                        .notify("Support ZIP has been sent.");
                else
                    hac.global
                        .error("Trouble sending ZIP, please downlaod and send to support@hybris.de!");
            },
            error: hac.global.err
        });

    });

    loadZipData();

});

function loadZipData() {
    var url = $('#supportFiles').attr('data-url');
    var token = $("meta[name='_csrf']").attr("content");

    $.ajax({
        url: url,
        type: 'GET',
        headers: {
            'Accept': 'application/json',
            'X-CSRF-TOKEN' : token
        },
        success: function (data) {
            debug.log(data);

            for (pos in data) {
                var file = data[pos];
                $('#supportFiles').append(
                        '<input type="checkbox" id="' + file.absolute
                        + '"> <label for="' + file.absolute + '">'
                        + file.name + ' (' + file.size + 'kb - '
                        + file.parent + ')</label><br/>');
            }

        },
        error: hac.global.err
    });
}
