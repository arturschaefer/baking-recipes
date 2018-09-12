package com.example.arturschaefer.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.arturschaefer.bakingapp.R;
import com.example.arturschaefer.bakingapp.RecipeListActivity;

public class WidgetIngredients extends AppWidgetProvider {
    public static final String WIDGET_EXTRA = "widget_extra";

    static void updateAppWidget(Context context, String jsonRecipeIngredients, AppWidgetManager appWidgetManager,
                                int appWidgetId, String recipeName) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_provider);
        Intent intent = new Intent(context, RecipeListActivity.class);
        intent.putExtra(WIDGET_EXTRA, recipeName);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);

        if(jsonRecipeIngredients.equals("")){
            jsonRecipeIngredients = "Empty Ingredients List";
        }

        views.setTextViewText(R.id.appwidget_text, jsonRecipeIngredients);
        views.setTextViewText(R.id.appwidget_title, recipeName);
        views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        WidgetService.startActionOpenRecipe(context);
    }

    public static void updateWidgetRecipe(Context context, String jsonRecipe, AppWidgetManager appWidgetManager,
                                          int[] appWidgetIds, String recipeName){
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, jsonRecipe, appWidgetManager, appWidgetId, recipeName);
        }
    }
}

