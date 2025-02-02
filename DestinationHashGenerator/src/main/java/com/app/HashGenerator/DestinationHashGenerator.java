package com.app.HashGenerator;
import org.json.JSONObject;
import org.json.JSONArray;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class DestinationHashGenerator {

    public static void main(String[] args) {
        // Ensure two arguments are provided
        if (args.length != 2) {
            System.out.println("Usage: java -jar DestinationHashGenerator.jar <PRN Number> <JSON File Path>");
            return;
        }

        // Extract PRN number and JSON file path from arguments
        String prnNumber = args[0].toLowerCase().replaceAll("\\s", "");
        String jsonFilePath = args[1];

        try {
            // Read the JSON file content as a String
            String jsonContent = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
            
            // Parse the JSON content into a JSONObject
            JSONObject jsonObject = new JSONObject(jsonContent);
            
            // Find the first instance of "destination" key
            String destinationValue = findDestination(jsonObject);
            if (destinationValue == null) {
                System.out.println("Key 'destination' not found in the JSON file.");
                return; // Exit if the key is not found
            }

            // Generate a random alphanumeric string of 8 characters
            String randomString = generateRandomString(8);
            
            // Concatenate PRN, destination value, and the random string
            String combinedString = prnNumber + destinationValue + randomString;

            // Generate MD5 hash of the combined string
            String hash = generateMD5Hash(combinedString);
            
            // Print the result in the format <hash>;<randomString>
            System.out.println(hash + ";" + randomString);
        } catch (Exception e) {
            e.printStackTrace(); // Print any exception that occurs
        }
    }

    // Method to traverse the JSON object and find the first occurrence of the "destination" key
    private static String findDestination(JSONObject jsonObject) {
        for (String key : jsonObject.keySet()) {
            Object value = jsonObject.get(key);

            if (key.equals("destination")) {
                return value.toString(); // Return the value if the key matches "destination"
            } else if (value instanceof JSONObject) {
                // Recursively search in nested JSONObject
                String result = findDestination((JSONObject) value);
                if (result != null) {
                    return result; // Stop searching once the key is found
                }
            } else if (value instanceof JSONArray) {
                // Iterate through the array if the value is a JSONArray
                for (Object item : (JSONArray) value) {
                    if (item instanceof JSONObject) {
                        String result = findDestination((JSONObject) item);
                        if (result != null) {
                            return result; // Stop searching once the key is found
                        }
                    }
                }
            }
        }
        return null; // Return null if the key is not found
    }

    // Method to generate a random alphanumeric string of a specified length
    private static String generateRandomString(int length) {
        SecureRandom random = new SecureRandom(); // Secure random number generator
        byte[] bytes = new byte[length]; // Byte array to hold random bytes
        random.nextBytes(bytes); // Generate random bytes
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes).substring(0, length);
    }

    // Method to generate an MD5 hash from a string
    private static String generateMD5Hash(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5"); // Initialize MD5 digest
        byte[] hashBytes = md.digest(input.getBytes()); // Generate the hash bytes
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b)); // Convert each byte to a hexadecimal string
        }
        return sb.toString(); // Return the complete hash as a string
    }
}
