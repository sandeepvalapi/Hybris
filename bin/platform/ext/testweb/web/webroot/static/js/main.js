$(function () {
    var collapseImgUrl = $('#mainContainer').attr('data-collapseIconUrl');
    var expandImgUrl = $('#mainContainer').attr('data-expandIconUrl')

    // Data table for all test suites
    var allDataTable = $('#allTestSuites').dataTable({
        "bStateSave": true,
        "iDisplayLength": 50
    })

    var filteredDataTable = $('#filteredTestSuites').dataTable({
        "bStateSave": true,
        "iDisplayLength": 50
    })

    $('#replacedSwitch').prop("checked",true);
    $('#unfiltered').hide();
    $('#filtered').show();

    $('#replacedSwitch').click(function () {
        if($('#replacedSwitch').is(":checked"))
        {
            $('#unfiltered').hide();
            $('#filtered').show();
            $('#filteredTestSuites_filter').children('input:eq(0)').val($('#allTestSuites_filter').children('input:eq(0)').val());
            $('#filteredTestSuites_length').children('select:eq(0)').val($('#allTestSuites_length').children('select:eq(0)').val());
            $('#filteredTestSuites_length').children('select:eq(0)').change();
            filteredDataTable.fnFilter($('#allTestSuites_filter').children('input:eq(0)').val());
        }
        else
        {
            $('#unfiltered').show();
            $('#filtered').hide();
            $('#allTestSuites_filter').children('input:eq(0)').val($('#filteredTestSuites_filter').children('input:eq(0)').val());
            $('#allTestSuites_length').children('select:eq(0)').val($('#filteredTestSuites_length').children('select:eq(0)').val());
            $('#allTestSuites_length').children('select:eq(0)').change();
            allDataTable.fnFilter($('#filteredTestSuites_filter').children('input:eq(0)').val());
        }
    });

    // select/deselect all extensions
    $('#selectAllExtensions').click(function () {
        $(".extensions").trigger('click');
    });

    // select/deselect all testcases;
    $('#selectAllSuites').click(function () {
        $(".testSuite").trigger('click');
    });

    // Expand/Collapse test cases from single test suite
    var loadTestCasesUrl = $('#run-by-testsuite').attr('data-testCasesUrl');
    $('body').on("click", '.test-cases-toggle', function () {
        var toggleImg = this;
        var testSuite = $(toggleImg).attr('data-testSuite');
        var contentContainerId = $(toggleImg).attr('data-contentContainerId');
        var id = $(toggleImg).attr('id');
        if ($(toggleImg).attr('src') == expandImgUrl) {
            $.ajax({
                url: loadTestCasesUrl,
                type: 'POST',
                data: {'testSuite': testSuite},
                headers: {'Accept': 'application/json'},
                success: function (data) {
                    var testCaseCheckboxes = "<br />";
                    var filteredTestCaseCheckboxes = "<br />";
                    $.each(data, function () {
                        var simpleTestCaseName = this.split('#')[1].split('(')[0];
                        testCaseCheckboxes += "<input class=\"testCaseCheckbox\" id=\"" + this + "\" name=\"testCases\" type=\"checkbox\" value=\"" + this + "\" />" +
                            "<label class=\"testCaseName\" for=\"" + this + "\">" + simpleTestCaseName + "</label><br />";
                        filteredTestCaseCheckboxes += "<input class=\"testCaseCheckbox\" id=\"filtered-" + this + "\" name=\"testCases\" type=\"checkbox\" value=\"" + this + "\" />" +
                            "<label class=\"testCaseName\" for=\"" + this + "\">" + simpleTestCaseName + "</label><br />";
                    })

                    $('#' + contentContainerId).html(
                        testCaseCheckboxes
                    );
                    $('#filtered' + contentContainerId).html(
                        filteredTestCaseCheckboxes
                    );
                    $(toggleImg).attr('src', collapseImgUrl);

                    if(id.substring(0,9)=='filtered-')
                    {
                        var element = document.getElementById(id.substring(9));
                        if (element) {
                            element.src = collapseImgUrl;
                        }
                    }
                    else {
                        var element = document.getElementById('filtered-'+id);
                        if (element) {
                            element.src=collapseImgUrl;
                        }
                    }
                }
            })
        } else {
            $('#' + contentContainerId).html("");
            $('#filtered' + contentContainerId).html("");

            $(toggleImg).attr('src', expandImgUrl);
            var id = $(toggleImg).attr('id');
            if(id.substring(0,9)=='filtered-')
            {
                var element = document.getElementById(id.substring(9));
                if (element) {
                    element.src = expandImgUrl;
                }
            }
            else
            {
                var element = document.getElementById('filtered-'+id);
                if (element) {
                    element.src=expandImgUrl;
                }
            }
        }
    })

    // unchecks any checked testCase if whole test suite is checked
    $('.testSuite').click(function () {
        var contentContainerId = $(this).attr('data-contentContainerId');
        $('span#' + contentContainerId + " input:checked").each(function () {
            $(this).prop('checked', false);
        })
        $('span#filtered' + contentContainerId + " input:checked").each(function () {
            $(this).prop('checked', false);
        })
    })

    $('body').on("click", '.testCaseCheckbox', function ()
    {
        var id = $(this).attr('id');
        if(id.substring(0,9)=='filtered-')
        {
            var element = document.getElementById(id.substring(9));
            if (element) {
                element.checked=$(this).is(':checked');
            }
        }
        else
        {
            var element = document.getElementById('filtered-'+id);
            if (element) {
                element.checked = $(this).is(':checked');
            }
        }
    });

    $('body').on("click", '.testSuite', function ()
    {
        var id = $(this).attr('id');
        if(id.substring(0,9)=='filtered-')
        {
            var element = document.getElementById(id.substring(9));
            if (element) {
                element.checked = $(this).is(':checked');
            }
        }
        else
        {
            var element = document.getElementById('filtered-'+id);
            if (element) {
                element.checked = $(this).is(':checked');
            }
        }
    });

    // toggles test cases result
    $('.testCasesContainer').hide();
    $('.toggleTestCases').click(function () {
        var testCasesContainerClass = $(this).attr('data-testCasesClass');
        $('.' + testCasesContainerClass).toggle();
        if ($(this).attr('src') == expandImgUrl) {
            $(this).attr('src', collapseImgUrl);
        } else {
            $(this).attr('src', expandImgUrl);
        }
    })

    // shows errors/infos dislog
    $('.testCaseIcon_false').click(function () {
        var stackTrace = $(this).parent().attr("data-stackTrace");

        $('#errorsContainer').html("");
        $('#errorsContainer').append(renderTestDetails(stackTrace));
        $('#errorsContainer').dialog({'title': 'Test case details', 'draggable': true, 'modal': true, 'width': 'auto'});
        $('#errorsContainer').dialog('open');
        $('#errorsContainer').dialog('moveToTop');

    }).attr('title', 'Show result');

    var renderTestDetails = function (stackTrace) {
        var container = $('<div />');
        container.attr('class', 'testCaseErrorStackTrace');

        var pre = $('<pre />');
        pre.text(stackTrace).html();

        container.append(pre);
        return container;
    }

    // overall coloring for results
    var overallResult = $('#overallResult').attr('data-result');
    if (overallResult == 'false') {
        $('#test-results-table>thead>tr').children("th").css("background-color", "#A80000");
    } else {
        $('#test-results-table>thead>tr').children("th").css("background-color", "#009933");
    }
})