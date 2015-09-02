package org.telegram.ui.Color;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import org.telegram.messenger.R;

/**
 * Created by xxfacusxx on 13/06/15.
 */
public class RgbSelectorView extends LinearLayout {

    private SeekBar seekRed;
    private SeekBar seekGreen;
    private SeekBar seekBlue;
    private SeekBar seekAlpha;
    private ImageView imgPreview;
    private OnColorChangedListener listener;

    public RgbSelectorView(Context context) {
        super(context);
        init();
    }

    public RgbSelectorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init()
    {
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rgbView = inflater.inflate(R.layout.color_rgbview, null);

        addView(rgbView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

        SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                setPreviewImage();
                onColorChanged();
            }
        };

        seekRed = (SeekBar)rgbView.findViewById(R.id.color_rgb_seekRed);
        seekRed.setOnSeekBarChangeListener(listener);
        seekGreen = (SeekBar)rgbView.findViewById(R.id.color_rgb_seekGreen);
        seekGreen.setOnSeekBarChangeListener(listener);
        seekBlue = (SeekBar)rgbView.findViewById(R.id.color_rgb_seekBlue);
        seekBlue.setOnSeekBarChangeListener(listener);
        seekAlpha = (SeekBar)rgbView.findViewById(R.id.color_rgb_seekAlpha);
        seekAlpha.setOnSeekBarChangeListener(listener);
        imgPreview = (ImageView)rgbView.findViewById(R.id.color_rgb_imgpreview);

        setColor(Color.BLACK);
    }

    private void setPreviewImage()
    {
        Bitmap preview = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        preview.setPixel(0, 0, getColor());

        imgPreview.setImageBitmap(preview);
    }

    public int getColor()
    {
        return Color.argb(seekAlpha.getProgress(), seekRed.getProgress(), seekGreen.getProgress(), seekBlue.getProgress());
    }

    public void setColor(int color)
    {
        seekAlpha.setProgress(Color.alpha(color));
        seekRed.setProgress(Color.red(color));
        seekGreen.setProgress(Color.green(color));
        seekBlue.setProgress(Color.blue(color));
        setPreviewImage();
    }

    private void onColorChanged()
    {
        if(listener != null)
            listener.colorChanged(getColor());
    }

    public void setOnColorChangedListener(OnColorChangedListener listener)
    {
        this.listener = listener;
    }

    public interface OnColorChangedListener
    {
        public void colorChanged(int color);
    }
}