package it.cnr.contab.progettiric00.bp;

import java.rmi.RemoteException;

import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;

public class ProgettoPianoEconomicoCRUDController extends it.cnr.jada.util.action.SimpleDetailCRUDController {

	public ProgettoPianoEconomicoCRUDController(String name, Class modelClass, String listPropertyName, it.cnr.jada.util.action.FormController parent) {
		super(name, modelClass, listPropertyName, parent);
	}

	public void validateForDelete(ActionContext context, OggettoBulk detail) throws ValidationException
	{
		if (!detail.isToBeCreated()) {
			try {
				Utility.createProgettoRicercaComponentSession().validaCancellazionePianoEconomicoAssociato(
					context.getUserContext(),
					(ProgettoBulk)getParentModel(),
					detail);
			} catch (ComponentException e) {
				throw new ValidationException(e.getMessage());
			} catch (RemoteException e) {
				throw new ValidationException(e.getMessage());
			}
		}
	}
}