package com.example.arturschaefer.bakingapp.widget;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.arturschaefer.bakingapp.model.Ingredient;
import com.example.arturschaefer.bakingapp.model.Recipe;
import com.google.gson.Gson;

import java.util.List;

public class WidgetService extends IntentService {
    public static final String ACTION_OPEN_RECIPE = "com.example.arturschaefer.bakingapp";
    public static final String SHARED_PREF = "shared_pref_baking";
    public static final String JSON_RESULT = "json_result";
    public static final String LOG_TAG = WidgetService.class.getSimpleName();

    public static final String CHANNEL_ID = "channel_01";
    int id = 1;

    public WidgetService(String name) {
        super(name);
    }

    public WidgetService() {
        super(WidgetService.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            if (Build.VERSION.SDK_INT >= 26) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                        WidgetService.class.getSimpleName(),
                        NotificationManager.IMPORTANCE_DEFAULT);

                ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).createNotificationChannel(channel);

                Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setContentTitle("")
                        .setContentText("").build();

                startForeground(id, notification);
            }
        } catch (NullPointerException ex){
            Log.e(LOG_TAG, ex.toString());
        } catch (Exception e){
            Log.e(LOG_TAG, "Error in the create WidgetService");
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_OPEN_RECIPE.equals(action)) {
                handleActionOpenRecipe();
            }
        }
    }

    private void handleActionOpenRecipe() {
        SharedPreferences sharedpreferences =
                getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        String jsonRecipe = sharedpreferences.getString(JSON_RESULT, "");

        try {
            StringBuilder stringBuilder = new StringBuilder();

            Gson gson = new Gson();
            Recipe recipe = gson.fromJson(jsonRecipe, Recipe.class);
            String recipeName = recipe.getmName();

            List<Ingredient> ingredientList = recipe.getmIngredientList();
            String quantity;
            String measure;
            String ingredientName;
            String line;

            for (Ingredient ingredient : ingredientList) {
                quantity = String.valueOf(ingredient.getmQuantity());
                measure = ingredient.getmMeasure();
                ingredientName = ingredient.getmIngredient();
                line = quantity + " " + measure + " " + ingredientName + "\n";
                stringBuilder.append(line);
            }

            String ingredientsString = stringBuilder.toString();
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, WidgetIngredients.class));
            WidgetIngredients.updateWidgetRecipe(this, ingredientsString, appWidgetManager, appWidgetIds, recipeName);
        } catch (Exception ex){
            Log.e(LOG_TAG, "Json: " + jsonRecipe);
        }
    }

    public static void startActionOpenRecipe(Context context) {
        Intent intent = new Intent(context, WidgetService.class);
        intent.setAction(ACTION_OPEN_RECIPE);
        context.startService(intent);
    }
}
