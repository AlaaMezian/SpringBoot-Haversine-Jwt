package com.example.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

import com.example.constant.Constants;
import com.example.constant.ResponseCode;
import com.example.constant.ResponseStatus;
import com.example.entity.base.BaseEntity;
import com.example.exception.BadRequestException;
import com.example.exception.EmptyFieldException;
import com.example.response.base.BaseResponse;

public class CommonsValidator {

	public static void validateEmail(String email, String fieldName) {
		String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
		validateString(email, fieldName);

		if (!email.matches(regex)) {
			
			throw new BadRequestException("invalid email address");
		}
		if (isSpecialCharacter(email.charAt(0))) {
			throw new BadRequestException("email cant start with special charachter");
		}

		String[] split = email.split("@");
		
		if (split[1] != null && split[1].contains(".."))
			throw new BadRequestException("invalid email format please enter valid email such as test@gmail.com");
	}

	public static void validateEmail(String email) {
		validateEmail(email, "email");
	}

	public static boolean isNumeric(String s) {
		return s != null && s.matches("[-+]?\\d*\\.?\\d+");
	}


	private static boolean containsOnlyLetters(String s) {
		return s.chars().allMatch(value -> Character.isLetter(value) || Character.isWhitespace(value));
	}

	private static boolean containsOnlyNumber(String s) {
		return s.chars().allMatch(value -> Character.isDigit(value) || Character.isWhitespace(value));
	}

	public static void validateNumber(String name, int min, int max, String fieldName) {
		validateString(name, fieldName);
		validateLength(name, min, max, fieldName);
		if (!containsOnlyNumber(name))
			throw new BadRequestException("please enter a valid number" + fieldName);
	}


	public static void validateName(String name) {
		validateString(name, "name");
		if (!containsOnlyLetters(name))
			throw new BadRequestException("name must only contain letters");
	}

	public static boolean isProbablyArabic(String s) {
		String textWithoutSpace = s.trim().replaceAll(" ", ""); // to ignore whitepace
		for (int i = 0; i < textWithoutSpace.length();) {
			int c = textWithoutSpace.codePointAt(i);
			// 0*002D is for hyphen
			if (c >= 0x0600 && c <= 0x06FF || (c >= 0xFE70 && c <= 0xFEFF) || (c == 0x002D))
				i += Character.charCount(c);
			else
				return false;
		}
		return true;
	}

	public static void validateArabicText(String s, String fieldName) {
		validateString(s, fieldName);
		if (!isProbablyArabic(s))
			throw new BadRequestException(fieldName + " : " + "must be in arabic");
	}

	public static BaseResponse isValidUserName(String userName) {
		BaseResponse response = null;
		if (userName == null || userName.equals("")) {
			response = new BaseResponse(ResponseStatus.RESPONSE_STATUS_FAILED, ResponseCode.FAILED_RESPONSE_CODE,
					"user name is required");
		} else if (userName.length() > 20 || userName.length() < 6) {
			response = new BaseResponse(ResponseStatus.RESPONSE_STATUS_FAILED, ResponseCode.FAILED_RESPONSE_CODE,
					"userNameIsRequired");
		}
		return response;
	}

	// check whether it contain hyphen or not
	public static boolean validateHyphen(String s) {
		if (s.length() > 0) {
			if (s.contains("-") && !s.endsWith("-") && !s.startsWith("-")) {
				return true;
			}
		}
		return false;

	}

	public static void validatePassword(String password, String fieldName) {
		validateString(password, fieldName);

		if (isProbablyArabic(password))
			throw new BadRequestException("password should be in english");

		validatePasswordLength(password, fieldName);
	}

	public static void validatePasswordLength(String password, String fieldName) {
		validateLength(password, Constants.PASSWORD_MIN_CHAR_COUNT, Constants.PASSWORD_MAX_CHAR_COUNT, fieldName);
	}

	public static void validateString(String toValidate, String fieldName) throws EmptyFieldException {
		if (toValidate == null)
			throw new EmptyFieldException(fieldName);

		toValidate = toValidate.trim();
		if (toValidate.isEmpty()) {
			throw new EmptyFieldException(fieldName);
		}

		if (toValidate.length() > 255)
			throw new BadRequestException(fieldName + " exceeded the limit");
	}

	public static void validateLength(String toValidate, int minSize, int maxSize, String fieldName) {
		validateString(toValidate, fieldName);
		if (toValidate.length() < minSize || toValidate.length() > maxSize) {
			throw new BadRequestException(fieldName + " exceeded the limit");
		}
	}

	public static void validateLength(String toValidate, int maxSize, String fieldName) {
		validateLength(toValidate, 1, maxSize, fieldName);
	}

	public static void validateNumber(Double num, String fieldName) throws EmptyFieldException {
		if (num == null || num < 0 || num > 2000000000)
			throw new BadRequestException(fieldName + "exceed the limit ");
	}

	public static void validateNumber(Long num, String fieldName) throws EmptyFieldException {
		if (num == null || num < 0 || num > 2000000000)
			throw new BadRequestException(fieldName + "exceed the limit ");
	}

	public static void validateNumber(Integer num, String fieldName) throws EmptyFieldException {
		if (num == null || num < 0 || num > 2000000000)
			throw new BadRequestException(fieldName + "exceed the limit ");
	}

	public static void validateEntity(BaseEntity baseEntity, String fieldName) throws EntityNotFoundException {
		if (baseEntity.isActive() == Constants.ENTITY_STATUS_NOT_ACTIVE)
			throw new BadRequestException(fieldName +" is deleted");
	}

	private static boolean isSpecialCharacter(char c) {
		String specialCharacters = " !#$%&'()*+,-./:;<=>?@[]^_`{|}";
		String s = Character.toString(c);
		return specialCharacters.contains(s);
	}

	public static void validateBoolean(Boolean toCheck, String fieldName) {
		if (toCheck == null)
			throw new EmptyFieldException(fieldName);
	}

	public static void validateObject(Object value, String fieldName) {
		if (value == null)
			throw new EmptyFieldException(fieldName);
	}

	public static Date convertToDate(String dateToValidate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return sdf.parse(dateToValidate);
		} catch (ParseException e) {
			return null;
		}
	}

	public static boolean notEmpty(String text) {
		return text != null && !text.isEmpty();
	}

	public static boolean notEmpty(int[] text) {
		return text != null && text.length != 0;
	}

	public static boolean notEmpty(Boolean text) {
		return text != null;
	}

	public static boolean isEmptyArray(int[] text) {
		return text == null && text.length == 0;
	}

	public static boolean notEmpty(Double text) {
		return text != null && text != 0;
	}

	public static boolean notEmpty(Long text) {
		return text != null;
	}

	public static void validateEnglishString(String englishName, String fieldName) {
		validateString(englishName, fieldName);
		if (!isEnglish(englishName))
			throw new BadRequestException(fieldName + "only english letters allowed");
	}

	public static boolean isAlphaNumericWithSpecial(String name) {
		return name.matches("^[a-zA-Z0-9-.,_'\\s]*$");
	}

	private static boolean isAlphaNumeric(String name) {
		return name.matches("^[a-zA-Z0-9-]*$");
	}

	private static boolean isEnglish(String name) {
		return name.matches("[a-zA-Z-\\s]+");
	}

	public static Date convertStringToDate(String date) {
		String startDateString = date;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = null;
		try {
			startDate = df.parse(startDateString);
			String newDateString = df.format(startDate);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return startDate;
	}

	

	public static boolean validArabic(String s) {
		for (int i = 0; i < s.length();) {
			int c = s.codePointAt(i);
			if (c >= 0x0600 && c <= 0x06E0)
				return true;
			i += Character.charCount(c);
		}
		return false;
	}

}
