package com.loschimbitas.icm_taller3_loschimbitas.util;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class JSONMANAGER {

    private static final String TAG = JSONMANAGER.class.getSimpleName();

    // Método para leer un archivo JSON desde el directorio "assets" de la aplicación
    public static String readJSONFromAsset(Context context, String fileName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(fileName);

            // Lee el archivo JSON como cadena
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            Log.e(TAG, "Error al leer el archivo JSON", e);
        }
        return json;
    }

    // Método para procesar un objeto JSON
    public static void processJSON(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            // Aquí puedes realizar las operaciones necesarias con el objeto JSON
            // Por ejemplo, obtener valores específicos utilizando jsonObject.getString("clave")
        } catch (JSONException e) {
            Log.e(TAG, "Error al procesar el JSON", e);
        }
    }
}
