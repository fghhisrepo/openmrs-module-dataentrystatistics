$('#startDate, #endDate').datepicker( {
        changeMonth: true,
        changeYear: true,
        dateFormat: 'dd-mm-yy',
        onChangeMonthYear: function(year, month, widget) {
            setTimeout(function() {
               $('.ui-datepicker-calendar').show();
            });
    	},
        onClose: function(dateText, inst) { 
            $(this).datepicker('setDate', new Date(inst.selectedYear, inst.selectedMonth, inst.selectedDay));
        }
		}).click(function(){
		    $('.ui-datepicker-calendar').show();
		});

$('#fromMonth, #toMonth').datepicker( {
	changeMonth: true,
	changeYear: true,
	showButtonPanel: true,
	dateFormat: 'dd-mm-yy',
	onClose: function(dateText, inst) { 
		$(this).datepicker('setDate', new Date(inst.selectedYear, inst.selectedMonth, 1));
	}
	});

$('#fromMonthTr').hide();
$('#toMonthTr').hide();	

$('#reportType').on('change', function() {

	var reportType = $('#reportType').val();

	if (reportType == 'MONTHLY_OBS') {	
		$('#startDateTr').hide();
		$('#endDateTr').hide();
		
		$('#fromMonthTr').show();
		$('#toMonthTr').show();	
		return;
	}
	
	$('#fromMonthTr').hide();
	$('#toMonthTr').hide();	
	$('#startDateTr').show();
	$('#endDateTr').show();
	
});
