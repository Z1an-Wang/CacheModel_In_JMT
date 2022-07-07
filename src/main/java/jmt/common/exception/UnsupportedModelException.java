/**
 * Copyright (C) 2016, Laboratorio di Valutazione delle Prestazioni - Politecnico di Milano

 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 */

package jmt.common.exception;

/**
 * Exception thrown when the model is not supported by a solver.
 * Examples:
 * <br>
 * - a model has not enough processing capacity (saturation
 * may be due to the arrival rates of open classes).
 * <br>
 * - the population of one or more closed classes is equal to 0.
 */
public class UnsupportedModelException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnsupportedModelException() {
	}

	public UnsupportedModelException(Throwable cause) {
		super(cause);
	}

	public UnsupportedModelException(String message) {
		super(message);
	}

	public UnsupportedModelException(String message, Throwable cause) {
		super(message, cause);
	}
}
