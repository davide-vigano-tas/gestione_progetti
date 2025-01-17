$(document).ready(function(){
	var commonConfig = {
		/**Per fare paginazione */
		"paging":true,
		/**Tipo di pagina, full_numbers attiva tutti i controlli per lagestione della paginazione */
		"pagingType": "full_numbers",
		/**Implemetato autmatica funzione di search */
		"searching":true,
		/**Label associate ai comandi */
		"info": true,
		/**Numero di vpci per pagina */
		"pageLength": 5,
		/**Seclta quante entry fare vedere, -1 tutti, il secondo array sono etichette*/
		"lengthMenu": [[5,10,25,50,-1], [5,10,25,50,"Tutti"]],
		"language": {
			/**quanti record sta visualizzando */
			"info": "Mostrati da _START_ a _END_ di _TOTAL_ voci",
			"emptyTable": "Nessun dato disponibile nella tabella",
			"zeroRecords": "Nessun risultato trovato",
			"lengthMenu": "Mostra _MENU_ voci",
			"search": "Cerca:",
			"infoEmpty": "Mostrati da 0 a 0 di 0 voci",
			"infoFiltered": "(Fitrati da _MAX_ voci totali)",
			"loadingRecords": "Caricamento...",
			"processing": "Elaborazione...",
			"paginate":{
				/**Voci della paginazione */
				"first": "Primo",
				"last": "Ultimo",
				"next": "Successivo",
				"previous": "Precedente"
			}
		},
		/**Evento di callback sul redesign della tabella */
		"drawCallback": function() {
			var api = this.api();
			var totalPages = api.page.info().pages;
			if(totalPages <= 1) {
				/**Classe del bottone */
				$('.paginate_button, .paginate_button').hide();
			} else {
				$('.paginate_button, .paginate_button').show();
				
			}
		}
		
		
	};
	
	
	var customTableConfig = {
		/**Per modificare def della colonna */
		"columnDefs": [
			{"orderable": false, "targets": -1}
		]
	};
	
	$('table').hide();
	
	$('table').each(function(){
		var tableId = $(this).attr('id');
		if(tableId === 'reportTable' || tableId === 'itemTable' || tableId === 'reportTableData' || tableId === 'articoliTableBody') {
			$(this).DataTable($.extend({}, commonConfig, customTableConfig));
		} else {
			$(this).DataTable(commonConfig);
		}
	});
	
	setTimeout(function(){
		$('table').fadeIn(300);
	}, 100);
});