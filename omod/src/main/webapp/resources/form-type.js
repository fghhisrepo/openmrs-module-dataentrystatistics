$('#startDate, #endDate').datepicker({
	changeMonth : true,
	changeYear : true,
	dateFormat : 'dd-mm-yy',
	onChangeMonthYear : function(year, month, widget) {
		setTimeout(function() {
			$('.ui-datepicker-calendar').show();
		});
	},
	onClose : function(dateText, inst) {
		$(this).datepicker(
				'setDate',
				new Date(inst.selectedYear, inst.selectedMonth,
						inst.selectedDay));
	}
}).click(function() {
	$('.ui-datepicker-calendar').show();
});

$('#fromMonth, #toMonth').datepicker({
	changeMonth : true,
	changeYear : true,
	showButtonPanel : true,
	dateFormat : 'dd-mm-yy',
	onClose : function(dateText, inst) {
		$(this).datepicker('setDate',
				new Date(inst.selectedYear, inst.selectedMonth, 1));
	}
});

// Call this initially in case it is a response then the view is maintained as it was before.
reportTypeFieldsShowing();

$('#reportType').on('change', function() {
	reportTypeFieldsShowing()
});

$('#orderBy').on('change', function() {
	var orderBy = $('#orderBy').val();
	if (orderBy == 'DISTRIC' || orderBy == "") {
		$('#locationTr').hide();
	}
	if (orderBy == 'HEALTHY_FACILITIES') {
		$('#locationTr').show();
	}

});

function showingOrderByField() {
	var orderByValue = $('#orderBy').val();
    $('#orderByTr').show();

    if (orderByValue == 'HEALTHY_FACILITIES') {
        $('#locationTr').show();
    }
}

function reportTypeFieldsShowing() {
    var reportType = $('#reportType').val();

    if (reportType == 'MONTHLY_OBS') {
        $('#startDateTr').hide();
        $('#endDateTr').hide();

        $('#fromMonthTr').show();
        $('#toMonthTr').show();
        $('#hideTr').hide();
        showingOrderByField();
        return;
    }

    if (reportType == 'FORM_TYPES_OLD') {
        $('#startDateTr').show();
        $('#endDateTr').show();
        $('#fromMonthTr').hide();
        $('#toMonthTr').hide();
        $('#hideTr').show();
        $('#locationTr').hide();
        $('#orderByTr').hide();
        return;
    }

    $('#fromMonthTr').hide();
    $('#toMonthTr').hide();
    $('#startDateTr').show();
    $('#endDateTr').show();
    $('#hideTr').hide();
    showingOrderByField();

}