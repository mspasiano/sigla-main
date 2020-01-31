/*
 * Copyright (C) 2020  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.contab.ordmag.magazzino.bp;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.util.action.SimpleCRUDBP;

public class CRUDMagazzinoBP extends SimpleCRUDBP {
	private static final long serialVersionUID = 1L;

	public CRUDMagazzinoBP() {
		super();
	}

	public CRUDMagazzinoBP(String function) {
		super(function);
	}

	@Override
	protected void init(Config config, ActionContext actioncontext) throws BusinessProcessException {
		setTab("tab","tabMagazzino");
		super.init(config, actioncontext);
	}
}