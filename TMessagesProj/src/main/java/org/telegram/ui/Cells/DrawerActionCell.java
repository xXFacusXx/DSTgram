/*
 * This is the source code of Telegram for Android v. 1.7.x.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Nikolai Kudashov, 2013-2014.
 */

package org.telegram.ui.Cells;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.telegram.android.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.ui.Components.LayoutHelper;

public class DrawerActionCell extends FrameLayout {

    private TextView textView;
    private static Drawable drawable;

    public DrawerActionCell(Context context) {
        super(context);

        textView = new TextView(context);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        textView.setLines(1);
        textView.setMaxLines(1);
        textView.setSingleLine(true);
        textView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        textView.setCompoundDrawablePadding(AndroidUtilities.dp(34));
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("Tematizacion", Activity.MODE_PRIVATE);
        textView.setTextColor(preferences.getInt("TextoIconosColorDrawer", 0xff424242));
        addView(textView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT, Gravity.LEFT | Gravity.TOP, 14, 0, 16, 0));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(48), MeasureSpec.EXACTLY));
    }

    public void setTextAndIcon(String text, int resId) {
        textView.setText(text);
        drawable = getResources().getDrawable(resId);
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("Tematizacion", Activity.MODE_PRIVATE);
        drawable.setColorFilter(preferences.getInt("IconosColorDrawer", 0xff0d5e80), PorterDuff.Mode.MULTIPLY);
        textView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        textView.setTextColor(preferences.getInt("TextoIconosColorDrawer", 0xff424242));
    }
}
