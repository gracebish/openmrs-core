/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.openmrs.ConceptNameTag;
import org.openmrs.api.context.Context;
import org.openmrs.test.jupiter.BaseContextSensitiveTest;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

/**
 * Tests methods on the {@link ConceptNameTagValidator} class.
 *
 * @since 1.10
 */
public class ConceptNameTagValidatorTest extends BaseContextSensitiveTest {
	
	@Test
	public void validate_shouldFailValidationIfConceptNameTagIsNull() {
		Errors errors = new BindException(new ConceptNameTag(), "cnt");
		assertThrows(IllegalArgumentException.class, () -> new ConceptNameTagValidator().validate(null, errors));
	}
	
	/**
	 * @see ConceptNameTagValidator#validate(Object,Errors)
	 */
	@Test
	public void validate_shouldFailValidationIfTagIsNullOrEmptyOrWhitespace() {
		ConceptNameTag cnt = new ConceptNameTag();
		
		Errors errors = new BindException(cnt, "cnt");
		new ConceptNameTagValidator().validate(cnt, errors);
		assertTrue(errors.hasFieldErrors("tag"));
		
		cnt.setTag("");
		errors = new BindException(cnt, "cnt");
		new ConceptNameTagValidator().validate(cnt, errors);
		assertTrue(errors.hasFieldErrors("tag"));
		
		cnt.setTag(" ");
		errors = new BindException(cnt, "cnt");
		new ConceptNameTagValidator().validate(cnt, errors);
		assertTrue(errors.hasFieldErrors("tag"));
	}
	
	/**
	 * @see ConceptNameTagValidator#validate(Object,Errors)
	 */
	@Test
	public void validate_shouldPassValidationIfAllRequiredFieldsHaveProperValues() {
		ConceptNameTag cnt = new ConceptNameTag();
		
		cnt.setTag("tag");
		
		Errors errors = new BindException(cnt, "cnt");
		new ConceptNameTagValidator().validate(cnt, errors);
		assertFalse(errors.hasErrors());
	}
	
	/**
	 * @see ConceptNameTagValidator#validate(Object,Errors)
	 */
	@Test
	public void validate_shouldFailIfTheConceptNameTagIsADuplicate() {
		String objectName = "duplicate concept name tag";
		
		ConceptNameTag existing = Context.getConceptService().getConceptNameTag(1);
		
		ConceptNameTag cnt = new ConceptNameTag();
		cnt.setTag(existing.getTag());
		
		Errors errors = new BindException(cnt, objectName);
		new ConceptNameTagValidator().validate(cnt, errors);
		assertTrue(errors.hasErrors());
		assertTrue(errors.hasFieldErrors("tag"));
	}
	
	/**
	 * @see ConceptNameTagValidator#validate(Object,Errors)
	 */
	@Test
	public void validate_shouldPassValidationIfFieldLengthsAreCorrect() {
		ConceptNameTag cnt = new ConceptNameTag();
		
		cnt.setTag("tag");
		cnt.setVoidReason("VoidReason");
		
		Errors errors = new BindException(cnt, "cnt");
		new ConceptNameTagValidator().validate(cnt, errors);
		assertFalse(errors.hasErrors());
	}
	
	/**
	 * @see ConceptNameTagValidator#validate(Object,Errors)
	 */
	@Test
	public void validate_shouldFailValidationIfFieldLengthsAreNotCorrect() {
		ConceptNameTag cnt = new ConceptNameTag();
		
		cnt
		        .setTag("too long text too long text too long text too long text too long text too long text too long text too long text too long text too long text too long text too long text too long text too long text too long text too long text too long text too long text too long text too long text");
		cnt
		        .setVoidReason("too long text too long text too long text too long text too long text too long text too long text too long text too long text too long text too long text too long text too long text too long text too long text too long text too long text too long text too long text too long text");
		
		Errors errors = new BindException(cnt, "cnt");
		new ConceptNameTagValidator().validate(cnt, errors);
		assertTrue(errors.hasFieldErrors("tag"));
		assertTrue(errors.hasFieldErrors("voidReason"));
	}
	
	@Test
	public void validate_shouldNotFailIfTheConceptNameTagIsTheSame() {
		String objectName = "duplicate concept name tag";

		ConceptNameTag cnt = Context.getConceptService().getConceptNameTag(1);

		Errors errors = new BindException(cnt, objectName);
		new ConceptNameTagValidator().validate(cnt, errors);
		assertFalse(errors.hasErrors());
		assertFalse(errors.hasFieldErrors("tag"));
	}
}
