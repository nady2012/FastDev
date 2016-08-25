package com.fastdev.fastdevlib.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.fastdev.fastdevlib.R;

import java.util.Arrays;

/**
 * A subclass of {@link XSWLetterWidget}. Used to show a letter and icon index
 * for contacts list.
 * 
 * <p>
 * Invoke {@link #setIconCategory(int)} to set the category of icon, by default
 * will be {@link #SEARCH_ICON_ONLY}.
 * 
 */
public class XSWLetterWidgetWithPicture extends XSWLetterWidget {
    /**
     * Indicates the letter index bar shows search icon only.
     */
    public static final int SEARCH_ICON_ONLY = 0;

    /**
     * Indicates the letter index bar shows star icon only.
     */
    public static final int STAR_ICON_ONLY = 1;

    /**
     * Indicates the letter index bar shows both search icon and star icon.
     */
    public static final int BOTH_ICON = 2;

    private int mIconCategory = SEARCH_ICON_ONLY;

    /**
     * Indicates the search icon.
     */
    public static final String ICON_SEARCH = "com.huawei.xs.widget.countrycode.XSWLetterWidgetWithPicture.ICON_SEARCH";

    /**
     * Indicates the star icon.
     */
    public static final String ICON_STAR = "com.huawei.xs.widget.countrycode.XSWLetterWidgetWithPicture.ICON_STAR";

    private String[] lettersEnglish = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P",
            "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
    private String[] lettersRussia = { "袗", "袘", "袙", "袚", "袛", "袝", "衼", "袞", "袟", "袠", "袡", "袣", "袥", "袦", "袧", "袨",
            "袩", "袪", "小", "孝", "校", "肖", "啸", "笑", "效", "楔", "些", "歇", "蝎", "鞋", "协", "挟", "携" };

    private Context mContext;
    private int mIconNumber;

    public XSWLetterWidgetWithPicture(Context context) {
        super(context);
        initRes(context);
    }

    public XSWLetterWidgetWithPicture(Context context, AttributeSet attrs) {
        super(context, attrs);
        initRes(context);
    }

    public XSWLetterWidgetWithPicture(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initRes(context);
    }

    private void initLetters() {
        if (getResources().getConfiguration().locale.getCountry().equalsIgnoreCase(COUNTRY_RUSSIA)) {
            letters = lettersRussia;
        } else {
            letters = lettersEnglish;
        }
    }

    private void initRes(Context context) {
        mContext = context;
        initLetters();
    }

    /**
     * Set the category of icon, by default will be {@link #SEARCH_ICON_ONLY}.
     * 
     * @param category
     *            icon category:SEARCH_ICON_ONLY or STAR_ICON_ONLY or BOTH_ICON
     */
    public void setIconCategory(int category) {
        mIconCategory = category;
    }

    private int getIconNumber(int category) {
        int number;
        switch (category) {
            case SEARCH_ICON_ONLY:
            case STAR_ICON_ONLY:
                number = 1;
                break;
            case BOTH_ICON:
                number = 2;
                break;
            default:
                number = 0;
                break;
        }
        return number;
    }

    @Override
    protected void drawWidget(Canvas canvas, int width, int height) {
        mIconNumber = getIconNumber(mIconCategory);
        int elementLength = letters.length + mIconNumber;
        int singleHeight = height / elementLength;
        for (int i = 0; i < elementLength; i++) {
            paintNormalLetter.setColor(getResources().getColor(R.color.gray));
            paintNormalLetter.setTextSize(getResources().getDimension(R.dimen.font_size1));
            paintNormalLetter.setAntiAlias(true);

            int letterPosition = i;
            if (i >= mIconNumber && mIconNumber > 0) {
                letterPosition = i - mIconNumber;
            }
            float xPos = width / 2 - paintNormalLetter.measureText(letters[letterPosition]) / 2;
            float yPos = singleHeight * i + singleHeight;
            if (i == choose) {
                float y = singleHeight * i;
                paintChoosedLetter.setColor(getResources().getColor(R.color.rcs_content_color));

                RectF rect = new RectF(xPos / 4, y + singleHeight / 4, xPos
                        + paintNormalLetter.measureText(letters[letterPosition]) + 3 * xPos / 4, y + 5 * singleHeight
                        / 4);
                canvas.drawRoundRect(rect, 5, 5, paintChoosedLetter);
                paintNormalLetter.setColor(Color.WHITE);
                paintNormalLetter.setFakeBoldText(true);
            }
            if (i < mIconNumber) {
                drawIcon(canvas, i, width, singleHeight);
            } else {
                canvas.drawText(letters[letterPosition], xPos, yPos, paintNormalLetter);
            }
            paintNormalLetter.reset();
        }
    }

    private void drawIcon(Canvas canvas, int position, int width, int singleHeight) {
        Drawable icon = null;
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.common_search);
        icon = mContext.getResources().getDrawable(R.drawable.common_search);
        bmp = Bitmap.createScaledBitmap(bmp, 50, 50, true);

        int iconWidth = icon.getIntrinsicWidth();
        int iconHeight = icon.getIntrinsicHeight();
        int left = width / 2 - iconWidth / 2;
        int top = (int) (singleHeight * (position + 0.5));
        icon.setBounds(left, top, left + iconWidth, top + iconHeight);
        icon.draw(canvas);
    }

    @Override
    public void moveToLetter(String s) {
        if (Arrays.asList(letters).contains(s)) {
            for (int i = 0; i < letters.length; i++) {
                if (letters[i].equals(s)) {
                    refresh(i + mIconNumber);
                    break;
                }
            }
        } else if (ICON_SEARCH.equals(s)) {
            refresh(0);
        } else if (ICON_STAR.equals(s)) {
            refresh(1);
        }
    }

    @Override
    protected int getChoosePosition(float yPos) {
        return (int) (yPos / getHeight() * (letters.length + mIconNumber));
    }

    @Override
    protected void doLetterChangedAction(int oldChoose, int newChoose, OnTouchingLetterChangedListener listener) {
        setBackgroundResource(Color.TRANSPARENT);
        if (oldChoose != newChoose && listener != null && newChoose < (letters.length + mIconNumber)) {
            String letter = "";
            if (newChoose >= mIconNumber) {
                letter = letters[newChoose - mIconNumber];
            } else {
                if (BOTH_ICON != mIconCategory) {
                    letter = mIconCategory == SEARCH_ICON_ONLY ? ICON_SEARCH : ICON_STAR;
                } else {
                    letter = newChoose == 0 ? ICON_SEARCH : ICON_STAR;
                }
            }
            listener.onTouchingLetterChanged(letter);
            choose = newChoose;
            invalidate();
        }
    }
}
