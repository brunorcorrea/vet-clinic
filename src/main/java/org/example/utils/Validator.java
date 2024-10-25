package org.example.utils;

import java.util.Arrays;
import java.util.List;

public class Validator {

    private Validator() {
    }

    private static final List<String> INVALID_CPFS = Arrays.asList("00000000000", "11111111111", "22222222222", "33333333333", "44444444444", "55555555555", "66666666666", "77777777777", "88888888888", "99999999999");

    public static boolean isCPFValid(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            return false;
        }

        String cpfToValidate = filterCPFmask(cpf);

        if (cpfToValidate.length() != 11 || INVALID_CPFS.contains(cpfToValidate)) {
            return false;
        }

        int[] cpfArray = cpfToValidate.chars().map(Character::getNumericValue).toArray();

        int firstDigit = calculateDigit(cpfArray, 9);
        int secondDigit = calculateDigit(cpfArray, 10);

        return firstDigit == cpfArray[9] && secondDigit == cpfArray[10];
    }

    public static boolean isTelefoneValid(String telefone) {
        return telefone.matches("\\(\\d{2}\\)\\s\\d{4,5}-\\d{4}");
    }

    private static int calculateDigit(int[] cpfArray, int length) {
        int sum = 0;
        for (int i = 0; i < length; i++) {
            sum += cpfArray[i] * (length + 1 - i);
        }
        int digit = 11 - sum % 11;
        return digit >= 10 ? 0 : digit;
    }

    private static String filterCPFmask(String cpf) {
        return cpf.replace(".", "").replace("-", "");
    }
}
