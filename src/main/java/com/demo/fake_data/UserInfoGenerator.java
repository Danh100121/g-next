package com.demo.fake_data;

import com.demo.entity.Gender;
import com.demo.entity.Status;

import java.io.*;
import java.time.LocalDate;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class UserInfoGenerator {
    private static final String ROOT_FILE_DATA = "src/main/java/com/demo/fake_data/data";
    private static final String FIRSTNAME_FILENAME = ROOT_FILE_DATA + "/first.name";
    private static final String MALE_FILENAME = ROOT_FILE_DATA + "/male.name";
    private static final String FEMALE_FILENAME = ROOT_FILE_DATA + "/female.name";
    private static final String EMAIL_FILENAME = ROOT_FILE_DATA + "/email.txt";

    private static String[] readLines(String filename) throws IOException {
        try (
                InputStream inputStream = new FileInputStream(filename);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))
        ) {
            return reader.lines().toArray(String[]::new);
        }
    }

    private static String[] readFirstNames() throws IOException {
        return readLines(FIRSTNAME_FILENAME);
    }

    private static String[] readMaleNames() throws IOException {
        return readLines(MALE_FILENAME);
    }

    private static String[] readFemaleNames() throws IOException {
        return readLines(FEMALE_FILENAME);
    }

    private static String[] readEmails() throws IOException {
        return readLines(EMAIL_FILENAME);
    }

    public static String generateFullName() throws IOException {
        String[] firstNames = readFirstNames();
        String[] maleNames = readMaleNames();
        String[] femaleNames = readFemaleNames();

        Random random = new Random();
        boolean isMale = random.nextBoolean();

        String firstName = firstNames[random.nextInt(firstNames.length)];
        String fullName;

        if (isMale) {
            String maleName = maleNames[random.nextInt(maleNames.length)];
            fullName = firstName + " " + maleName;
        } else {
            String femaleName = femaleNames[random.nextInt(femaleNames.length)];
            fullName = firstName + " " + femaleName;
        }

        return fullName;
    }

    public static String generateRandomEmail() throws IOException {
        String[] emails = readEmails();

        Random random = new Random();
        return emails[random.nextInt(emails.length)];
    }

    public static Gender generateRandomGender() {
        Gender[] genders = Gender.values();
        Random random = new Random();
        return genders[random.nextInt(genders.length)];
    }

    public static String generateRandomPhoneNumber() {
        Random random = new Random();
        StringBuilder phoneNumber = new StringBuilder("0");

        for (int i = 0; i < 9; i++) {
            phoneNumber.append(random.nextInt(10));
        }
        return phoneNumber.toString();
    }

    public static Status generateRandomStatus() {
        Status[] statuses = Status.values();
        Random random = new Random();
        return statuses[random.nextInt(statuses.length)];
    }

    public static LocalDate between(LocalDate startInclusive, LocalDate endExclusive) {
        long startEpochDay = startInclusive.toEpochDay();
        long endEpochDay = endExclusive.toEpochDay();
        long randomDay = ThreadLocalRandom
                .current()
                .nextLong(startEpochDay, endEpochDay);

        return LocalDate.ofEpochDay(randomDay);
    }

    public static String generateRandomAddress() {
        String[] address = {"Xuân Quan, Văn Giang, Hưng yên", "Mộ Lao, Hà Đông, Hà Nội", "Giao Yến, Giao Thủy, Nam Định", "Tân Triều, Thanh Trì, Hà Nội"};
        Random random = new Random();
        return address[random.nextInt(address.length)];
    }

    public static String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }
}