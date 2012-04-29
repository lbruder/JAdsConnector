// Copyright (c) 2011-2012, Leif Bruder <leifbruder@googlemail.com>
// 
// Permission to use, copy, modify, and/or distribute this software for any
// purpose with or without fee is hereby granted, provided that the above
// copyright notice and this permission notice appear in all copies.
// 
// THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
// WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
// ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
// WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
// ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
// OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.

package org.lb.plc;

import java.util.Hashtable;
import org.lb.plc.tpy.Variable;

public class NotificationHandleCache {
	private Hashtable<Variable, Long> variableToHandle;
	private Hashtable<Long, Variable> handleToVariable;

	public NotificationHandleCache() {
		this.variableToHandle = new Hashtable<Variable, Long>();
		this.handleToVariable = new Hashtable<Long, Variable>();
	}

	public void addNotification(Variable var, long notificationHandle) {
		variableToHandle.put(var, notificationHandle);
		handleToVariable.put(notificationHandle, var);
	}

	public long getHandleByVariable(Variable var) {
		return variableToHandle.get(var);
	}

	public void deleteNotification(Variable var) {
		long handle = variableToHandle.get(var);
		variableToHandle.remove(var);
		handleToVariable.remove(handle);
	}

	public Variable getVariableByHandle(Long handle) {
		return handleToVariable.get(handle);
	}
}
