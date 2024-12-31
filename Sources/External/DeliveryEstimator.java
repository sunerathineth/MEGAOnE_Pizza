package External;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.json.JSONArray;

public class DeliveryEstimator {
    private static final String openCageAPI = "cf28143f3cbd45ccb37c39b0ee203432";
    private static final String apiKey = "5b3ce3597851110001cf6248c8e8e61d3cdd4603adf6d4aadc428743";
    private static final String url = "https://api.openrouteservice.org/v2/directions/driving-car/json";

    
    public static String getUserCountry() {
        try {
            URL url = new URL("http://ipinfo.io/json");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Structure of the result
            // System.out.println("Raw response from ipinfo.io: " + response.toString());

            JSONObject jsonResponse = new JSONObject(response.toString());
            return jsonResponse.getString("country");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    
    public static List<String> getCoordinatesFromPlace(String placeName, String userCountry) throws Exception {
        String encodedPlaceName = URLEncoder.encode(placeName, StandardCharsets.UTF_8.toString());
        String urlString = "https://api.opencagedata.com/geocode/v1/json?q=" + encodedPlaceName + "&key=" + openCageAPI;
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        JSONObject jsonResponse = new JSONObject(response.toString());
        List<String> placeCoordinates = new ArrayList<>();

        if (jsonResponse.has("results") && jsonResponse.getJSONArray("results").length() > 0) {
            JSONArray results = jsonResponse.getJSONArray("results");
            
            for (int i = 0; i < Math.min(5, results.length()); i++) {
                JSONObject result = results.getJSONObject(i);
                String formattedPlace = result.getString("formatted");
                JSONObject components = result.getJSONObject("components");
                String country = components.optString("country_code").toUpperCase();

                if (country.equals(userCountry)) {
                    JSONObject geometry = result.getJSONObject("geometry");
                    double lat = geometry.getDouble("lat");
                    double lng = geometry.getDouble("lng");
                    placeCoordinates.add(formattedPlace + ": " + lat + "," + lng);
                }
            }
        } else {
            System.out.println("Could not find coordinates for: " + placeName);
        }

        return placeCoordinates;
    }



    public static double calculateDeliveryTime(String startCoordinates, String endCoordinates) {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
            TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
            };
            
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

            JSONObject payload = new JSONObject();
            payload.put("coordinates", new double[][] {
                parseCoordinates(startCoordinates),
                parseCoordinates(endCoordinates)
            });

            JSONObject jsonResponse = fetchRouteData(payload);

            if (jsonResponse.has("routes") && jsonResponse.getJSONArray("routes").length() > 0) {
                JSONObject route = jsonResponse.getJSONArray("routes").getJSONObject(0);

                if (route.has("summary")) {
                    JSONObject summary = route.getJSONObject("summary");

                    double distanceInMeters = summary.getDouble("distance");

                    double averageSpeed = 40.0; // avg speed - km/h
                    double distanceInKilometers = distanceInMeters / 1000.0;
                    double timeInHours = distanceInKilometers / averageSpeed;
                    double timeInMinutes = timeInHours * 60;

                    return timeInMinutes;
                } else {
                    System.out.println("Error: 'summary' field is missing in the route.");
                    return 0;
                }
            } else {
                System.out.println("Error: No routes found in the API response.");
                return 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private static JSONObject fetchRouteData(JSONObject payload) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", apiKey);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            os.write(payload.toString().getBytes());
            os.flush();
        }

        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("Failed API call with status: " + responseCode);
        }

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            return new JSONObject(response.toString());
        }
    }

    private static double[] parseCoordinates(String coordinates) {
        try {
            String[] parts = coordinates.split(",");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid coordinate format. Expected 'latitude,longitude'.");
            }
            return new double[] { Double.parseDouble(parts[1]), Double.parseDouble(parts[0]) };
        } catch (Exception e) {
            System.err.println("Invalid coordinates format: " + e.getMessage());
            throw e;
        }
    }

}