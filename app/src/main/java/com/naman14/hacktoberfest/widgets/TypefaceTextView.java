package com.naman14.hacktoberfest.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Typeface;
import androidx.collection.LruCache;
import androidx.appcompat.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.naman14.hacktoberfest.R;


/**
 * Created by naman on 30/06/15.
 */
public class TypefaceTextView extends AppCompatTextView {

    private static LruCache<String, Typeface> sTypefaceCache =
            new LruCache<String, Typeface>(12);

    public TypefaceTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.TypefaceTextView, 0, 0);

        try {
            String typefaceName = a.getString(
                    R.styleable.TypefaceTextView_typeface);

            if (!isInEditMode()) {
                Typeface typeface;
                if (!TextUtils.isEmpty(typefaceName)) {
                    typeface = sTypefaceCache.get(typefaceName);

                    if (typeface == null) {
                        typeface = Typeface.createFromAsset(context.getAssets(),
                                String.format("%s.ttf", typefaceName));

                        sTypefaceCache.put(typefaceName, typeface);
                    }

                } else {
                    typeface = sTypefaceCache.get("ArefRuqaa-Regular");

                    if (typeface == null) {
                        typeface = Typeface.createFromAsset(context.getAssets(),
                                String.format("%s.ttf", "ArefRuqaa-Regular"));

                        sTypefaceCache.put("ArefRuqaa-Regular", typeface);
                    }
                }

                setTypeface(typeface);

                setPaintFlags(getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
            }
        } finally {
            a.recycle();
        }
    }
}