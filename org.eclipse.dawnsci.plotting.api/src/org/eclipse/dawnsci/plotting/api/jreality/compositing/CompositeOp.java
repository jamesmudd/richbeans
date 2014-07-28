/*
 * Copyright 2012 Diamond Light Source Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.eclipse.dawnsci.plotting.api.jreality.compositing;

/**
 * Different image compositing operator
 */
public enum CompositeOp {

	/**
	 * ADD Operator
	 */	
	ADD,
	/**
	 * SUBTRACT Operator
	 */	
	SUBTRACT,
	/**
	 * MULTIPLY Operator
	 */	
	MULTIPLY,
	/**
	 * DIVIDE Operator
	 */	
	DIVIDE,
	/**
	 * MAX Operator
	 */	
	MAX,
	/**
	 * MIN Operator
	 */	
	MIN
}
