
/**Quando lapagina viebe caricata. Validazione in meniera asincrona. feedbackIcon, cambiano in base al caso
 * 
 */
$(document).ready(function(){
	$('#utenteForm').bootstrapValidator({
		feedbackIcons: {
			valid: 'glyphicon glyphicon-ok',
			invalid: 'glyphicon glyphicon-remove',
			validating: 'glyphicon glyphicon-refresh'
		},
		/*Come validare campi*/
		fields: {
			nome: {
				/**Id a cui fare rifermento per visualizzare errore */
				container: '#infoNome',
				validators: {
					notEmpty: {
						message: 'Il campo nome non può essere vuoto'
					},
					regexp: {
						/**\/^ inizio $\/ fine*/
						regexp: /^[a-zA-Z ,.'-]{2,30}$/,
						message: 'Da 2 a 30 caratteri. (Solo lettere)'
					}
				}
			},
			cognome: {
				/**Id a cui fare rifermento per visualizzare errore */
				container: '#infoCognome',
				validators: {
					notEmpty: {
						message: 'Il campo cognome non può essere vuoto'
					},
					regexp: {
						/**\/^ inizio $\/ fine*/
						regexp: /^[a-zA-Z ,.'-]{2,30}$/,
						message: 'Da 2 a 30 caratteri. (Solo lettere)'
					}
				}
			},
			
			indirizzo: {
				/**Id a cui fare rifermento per visualizzare errore */
				container: '#infoIndirizzo',
				validators: {
					notEmpty: {
						message: 'Il campo Indirizzo non può essere vuoto'
					},
					regexp: {
						/**\/^ inizio $\/ fine*/
						regexp: /^[a-zA-Z ,.'-]{6,45}[0-9]{1,4}$/,
						message: 'Inserire Via | Piazza | Viale e numero civico'
					}
				}
			},
			cap: {
				/**Id a cui fare rifermento per visualizzare errore */
				container: '#infoCap',
				validators: {
					notEmpty: {
						message: 'Il campo cap non può essere vuoto'
					},
					regexp: {
						/**\/^ inizio $\/ fine*/
						regexp: /^[0-9]{5}$/,
						message: 'Inserire 5 cifre'
					}
				}
			},
			nascita: {
				/**Id a cui fare rifermento per visualizzare errore */
				container: '#infoNascita',
				validators: {
					notEmpty: {
						message: 'Il campo nascita non può essere vuoto'
					},
					date: {
						format: 'DD/MM/YYYY',
						message: 'Inserire data di nascita, Formato: DD/MM/YYYY'
					}
				}
			},
			password: {
				container: '#infoPassword',
				enabled: false
			}
		}
	});
});