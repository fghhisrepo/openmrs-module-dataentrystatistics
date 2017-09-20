$('#startDate, #endDate').datepicker(
		{
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

$('#fromMonth, #toMonth').datepicker(
		{
			changeMonth : true,
			changeYear : true,
			showButtonPanel : true,
			dateFormat : 'dd-mm-yy',
			onClose : function(dateText, inst) {
				$(this).datepicker('setDate',
						new Date(inst.selectedYear, inst.selectedMonth, 1));
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
	if (reportType == 'FORM_TYPES_OLD') {
		$('#locationTr').hide();
		$('#orderByTr').hide();
		return;
	}

	$('#fromMonthTr').hide();
	$('#toMonthTr').hide();
	$('#startDateTr').show();
	$('#endDateTr').show();

});

$('#orderBy').on('change', function() {
	var orderBy = $('#orderBy').val();
	if (orderBy == 'DISTRIC' || orderBy == "") {
		$('#locationTr').hide();
		return;
	}
	$('#locationTr').show();
});

// EXPORT TO CSV
function exportTableToCSV($table, filename) {

	var $headers = $table.find('tr:has(th)'), $rows = $table.find('tr:has(td)'), tmpColDelim = String
			.fromCharCode(11) // vertical tab character
	, tmpRowDelim = String.fromCharCode(0) // null character
	, colDelim = '","', rowDelim = '"\r\n"';
	var csv = '"';
	csv += formatRows($headers.map(grabRow));
	csv += rowDelim;
	csv += formatRows($rows.map(grabRow)) + '"';

	var csvData = 'data:application/csv;charset=utf-8,'
			+ encodeURIComponent(csv);

	$(this).attr({
		'download' : filename,
		'href' : csvData
	});

	function formatRows(rows) {
		return rows.get().join(tmpRowDelim).split(tmpRowDelim).join(rowDelim)
				.split(tmpColDelim).join(colDelim);
	}

	function grabRow(i, row) {

		var $row = $(row);
		var $cols = $row.find('td');
		if (!$cols.length)
			$cols = $row.find('th');
		return $cols.map(grabCol).get().join(tmpColDelim);
	}

	function grabCol(j, col) {
		var $col = $(col), $text = $col.text();
		return $text.replace('"', '""');
	}
}

$("#export").click(
		function(event) {

			var today = new Date();
			var formatedDate = today.getFullYear() + ''
					+ (today.getMonth() + 1) + '' + today.getDate();
			var fileName = $('#location').find(":selected").text().trim()
					.toUpperCase();
			var report = $('#reportSelected').text();

			fileName = 'DES_' + report + '_' + fileName + '_' + formatedDate;

			exportTableToCSV.apply(this, [ $('#dvData>table'),
					fileName + '.csv' ]);
		});
