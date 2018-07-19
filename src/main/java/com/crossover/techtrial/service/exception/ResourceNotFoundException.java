package com.crossover.techtrial.service.exception;

/**
 * Class that represents when there is no resource found
 * @author Jonas Arioli
 *
 */
public class ResourceNotFoundException extends RuntimeException  {

	private static final long serialVersionUID = 1L;
	
	public ResourceNotFoundException(String exception) {
		super(exception);
	}

}
