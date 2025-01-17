
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
			username: {
				/**Id a cui fare rifermento per visualizzare errore */
				container: '#infoUsername',
				validators: {
					notEmpty: {
						message: 'Il campo Username non può essere vuoto'
					},
					regexp: {
						/**\/^ inizio $\/ fine*/
						regexp: /^[a-zA-Z0-9!.-]{4,10}$/,
						message: 'Da 4 a 10 caratteri. (Lettere e/o numeri e/o .-!'
					},
					/**Richiama ajax del controller per vedere se l'utente è già presente */
					remote: {
						
						url: 'checkField',
						type: 'GET',
						data: function(validator) {
							return {
								field: 'username',
								value: validator.getFieldElements('username').val()
							};
						},
						message: 'Username già in uso',
						/**Per non farlo partire contemporanemanete al controllo del campo */
						delay: 200
						
					}
				}
			},
			 password: {
				/**Id a cui fare rifermento per visualizzare errore */
				container: '#infoPassword',
				validators: {
					notEmpty: {
						message: 'Il campo password non può essere vuoto'
					},
					regexp: {
						/**\/^ inizio $\/ fine*/
						regexp: /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^?!=])[a-zA-Z0-9@#$%^?!=]{7,15}$/,
						message: 'Da 7 a 15 caratteri. (Almeno una minuscola, maiuscola, numero e carattere speciale)'
					}
				}
			},
			email: {
				/**Id a cui fare rifermento per visualizzare errore */
				container: '#infoEmail',
				validators: {
					notEmpty: {
						message: 'Il campo email non può essere vuoto'
					},
					regexp: {
						/**\/^ inizio $\/ fine*/
						/**w = Qualsiasi valori caratteri numerici underscore  */
						regexp: /^[\w.%+-]+@[a-zA-Z0-9]+\.[a-zA-Z]{2,}$/,
						message: 'Inserire una mail valida'
					},
					remote: {
						
						url: 'checkField',
						type: 'GET',
						data: function(validator) {
							return {
								field: 'email',
								value: validator.getFieldElements('email').val(),
								azione: $('#azione').val()
							};
						},
						message: 'Email già in uso',
						delay: 200,
						enabled: function() {
							return $('azione').val() === 'registrazione';
						}
						
					}
				}
			},
		}
	});
});