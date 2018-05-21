package com.mamba.mambasdk.ui;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import com.mamba.mambasdk.R;
import com.mamba.mambasdk.util.ScreenDensityUtil;

import java.util.ArrayList;

/**
 * A subclass of {@link View}, used to show a column letter index on a
 * {@link ListView} which items sort by letter.
 * 
 * <p>
 * Invoking
 * {@link #setOnTouchedIndexLetterChangeListener(OnTouchedIndexLetterChangeListener)}
 * to listen to event that the touched index letter changed.
 * 
 * <p>
 * Invoke {@link #setIconCategory(int)} to set the category of icon
 * 
 * @attr ref {@link com.huawei.xs.widget.R.styleable#IndexLetters}
 * @attr ref {@link com.huawei.xs.widget.R.styleable#IndexLetters_columnGap}
 * @attr ref
 *       {@link com.huawei.xs.widget.R.styleable#IndexLetters_columnLetterColor}
 * @attr ref
 *       {@link com.huawei.xs.widget.R.styleable#IndexLetters_columnLetterSize}
 * @attr ref {@link com.huawei.xs.widget.R.styleable#IndexLetters_columnWidth}
 * @attr ref {@link com.huawei.xs.widget.R.styleable#IndexLetters_indexIcon}
 * @attr ref
 *       {@link com.huawei.xs.widget.R.styleable#IndexLetters_showDefaultLetters}
 * 
 */
public class ColumnIndexLetters extends View {

    /**
     * A String value indicates the icon index for searching.
     * 
     * @see #INDEX_ICON_FAVOURITES.
     */
    public final static String INDEX_ICON_SEARCH = "search";

    /**
     * A String value indicates the icon index for favorites.
     */
    public final static String INDEX_ICON_FAVOURITES = "favorites";

    /**
     * Indicates the letter index bar shows search icon only.
     */
    public final static int ICON_SEARCH = 0;

    /**
     * Indicates the letter index bar shows favorites icon only.
     */
    public final static int ICON_FAVOURITES = 1;

    /**
     * Indicates the letter index bar shows both search icon and favorites icon.
     */
    public final static int ICON_BOTH = 2;

    /**
     * Indicates the letter index bar does't show icon.
     */
    public final static int ICON_NON = 3;

    private final static int COLUMN_MAX_LETTERS_NUMBER = 28;
    private Context mContext;
    private Paint mPaint;
    private OnTouchedIndexLetterChangeListener mListener;
    private float mNextLocationY;
    private float columnWidth;
    private float columnGap;
    private float variationalGap;
    private float letterSize;
    private int letterColor;
    private int iconCategory = -1;
    private boolean showDefaultLetters;
    private IndexIcon[] icons;
    private String[] letters;
    private String[] lettersEN = { "#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P",
            "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
    private String[] lettersRU = { "#", "А", "Б", "В", "Г", "Д", "Е", "Ё", "Ж", "З", "И", "Й", "К", "Л", "М", "Н", "О",
            "П", "Р", "С", "Т", "У", "Ф", "Х", "Ц", "Ч", "Ш", "Щ", "Ъ", "Ы", "Ь", "Э", "Ю", "Я" };

    public ColumnIndexLetters(Context context) {
        super(context);
        mContext = context;
        showDefaultLetters = true;
        initDefaultAttrs();
        init();
    }

    public ColumnIndexLetters(Context context, IndexIcon[] icons, String[] letters) {
        this(context, null);
        this.icons = icons;
        this.letters = letters;
    }

    public ColumnIndexLetters(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initDefaultAttrs();
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.IndexLetters, 0, 0);
        try {
            columnWidth = a.getDimension(R.styleable.IndexLetters_columnWidth, columnWidth);
            columnGap = a.getDimension(R.styleable.IndexLetters_columnGap, columnGap);
            letterSize = a.getDimension(R.styleable.IndexLetters_columnLetterSize, letterSize);
            letterColor = a.getColor(R.styleable.IndexLetters_columnLetterColor, letterColor);
            showDefaultLetters = a.getBoolean(R.styleable.IndexLetters_showDefaultLetters, false);
            iconCategory = a.getInteger(R.styleable.IndexLetters_indexIcon, -1);
        } finally {
            a.recycle();
        }
        init();
    }

    /**
     * Register a callback to be invoked when the touched index letter changed
     * 
     * @param listener
     *            The callback will run.
     */
    public void setOnTouchedIndexLetterChangeListener(OnTouchedIndexLetterChangeListener listener) {
        mListener = listener;
    }

    /**
     * Interface definition for a callback be invoked when the touched index
     * letter changed
     * 
     */
    public interface OnTouchedIndexLetterChangeListener {
        /**
         * Will run when the touched index letter changed.
         * 
         * @param letter
         *            the current touched index letter.
         */
        void onIndexLetterChanged(String letter);
    }

    private void initDefaultAttrs() {
        Resources res = mContext.getResources();
        columnWidth = res.getDimension(R.dimen.column_min_width);
        if (ScreenDensityUtil.isNormalScreenDensityDpi(mContext)) {
            columnGap = res.getDimension(R.dimen.column_gap);
        } else {
            columnGap = res.getDimension(R.dimen.column_gap) * 2;
        }
        letterSize = res.getDimension(R.dimen.letter_size);
        letterColor = res.getColor(R.color.letter_color);
    }

    private void init() {
        if (showDefaultLetters) {
            useDefaultLetters();
        }
        mPaint = new Paint();
        mPaint.setColor(letterColor);
        mPaint.setTextSize(letterSize);
        mPaint.setAntiAlias(true);

        initIndexIcons();
    }

    private void initIndexIcons() {
        switch (iconCategory) {
            case ICON_SEARCH:
                icons = new IndexIcon[] { new IndexIcon(INDEX_ICON_SEARCH, R.drawable.common_alphabetical_index_search) };
                break;
            case ICON_FAVOURITES:
                icons = new IndexIcon[] { new IndexIcon(INDEX_ICON_FAVOURITES,
                        R.drawable.common_alphabetical_index_favorites) };
                break;
            case ICON_BOTH:
                icons = new IndexIcon[] {
                        new IndexIcon(INDEX_ICON_SEARCH, R.drawable.common_alphabetical_index_search),
                        new IndexIcon(INDEX_ICON_FAVOURITES, R.drawable.common_alphabetical_index_favorites) };
                break;
            case ICON_NON:
                icons = new IndexIcon[] {};
                break;
            default:
                break;
        }
    }

    public float getColumnWidth() {
        return columnWidth;
    }

    public void setColumnWidth(float columnWidth) {
        this.columnWidth = columnWidth;
    }

    public float getColumnGap() {
        return columnGap;
    }

    public void setColumnGap(float columnGap) {
        this.columnGap = columnGap;
    }

    public float getLetterSize() {
        return letterSize;
    }

    public void setLetterSize(float letterSize) {
        this.letterSize = letterSize;
    }

    public int getLetterColor() {
        return letterColor;
    }

    public void setLetterColor(int letterColor) {
        this.letterColor = letterColor;
    }

    public boolean isShowDefaultLetters() {
        return showDefaultLetters;
    }

    public void setShowDefaultLetters(boolean showDefaultLetters) {
        this.showDefaultLetters = showDefaultLetters;
    }

    public int getIconCategory() {
        return iconCategory;
    }

    /**
     * 
     * @param iconCategory
     *            {ICON_SEARCH, ICON_FAVOURITES, ICON_BOTH, ICON_NON}
     */
    public void setIconCategory(int iconCategory) {
        this.iconCategory = iconCategory;
        initIndexIcons();
        requestLayout();
    }

    public IndexIcon[] getIcons() {
        return icons;
    }

    public void setIcons(IndexIcon[] icons) {
        this.icons = icons;
        requestLayout();
    }

    public String[] getLetters() {
        return letters;
    }

    public void setLetters(String[] letters) {
        this.letters = letters;
        requestLayout();
    }

    public void setLetters(ArrayList<String> letters) {
        if (null == letters) {
            return;
        }
        String[] temp = new String[letters.size()];
        for (int i = 0; i < letters.size(); i++) {
            temp[i] = letters.get(i);
        }
        this.letters = temp;
        requestLayout();
    }

    public void useDefaultLetters() {
        if (getResources().getConfiguration().locale.getCountry().equalsIgnoreCase("RU")) {
            letters = lettersRU;
        } else {
            letters = lettersEN;
        }
    }

    public void setIconsAndLetters(IndexIcon[] icons, String[] letters) {
        this.icons = icons;
        this.letters = letters;
        requestLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawWidget(canvas);
    }

    private void drawWidget(Canvas canvas) {
        boolean haveIcon = (null != icons && icons.length > 0);
        boolean haveLetters = (null != letters && letters.length > 0);
        if (!haveLetters) {
            return;
        }
        mNextLocationY = haveIcon ? variationalGap : variationalGap * 2;
        for (int i = 0; i < (haveIcon ? icons.length : 0) + letters.length; i++) {
            if (haveIcon && i < icons.length) {
                drawIcon(canvas, i);
            } else {
                mNextLocationY += haveIcon && icons.length == i && variationalGap <= columnGap * 5 ? variationalGap : 0;
                int position = i - (haveIcon ? icons.length : 0);
                drawLetters(canvas, position);
            }
        }
    }

    private void drawIcon(Canvas canvas, int position) {
        Drawable icon = mContext.getResources().getDrawable(icons[position].getIconResId());
        int iconWidth = icon.getIntrinsicWidth();
        int iconHeight = icon.getIntrinsicHeight();
        int left = (int) columnWidth / 2 - iconWidth / 2;
        int top = (int) mNextLocationY;
        int right = left + iconWidth;
        int bottom = top + iconHeight;
        mNextLocationY += iconHeight + variationalGap;
        icon.setBounds(left, top, right, bottom);
        icon.draw(canvas);
    }

    private void drawLetters(Canvas canvas, int position) {
        Rect bounds = new Rect();
        mPaint.getTextBounds(letters[position], 0, 1, bounds);
        float textWidth = bounds.right - bounds.left;
        float textHeight = bounds.bottom - bounds.top;
        float xPos = columnWidth / 2f - textWidth / 2f;
        float yPos = mNextLocationY;
        mNextLocationY += textHeight + variationalGap;
        canvas.drawText(letters[position], xPos, yPos, mPaint);
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        return measureWidth();
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        return measureHeight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = measureWidth();
        int h = measureHeight();
        setMeasuredDimension(w, h);
    }

    private int measureWidth() {
        boolean haveIcon = (null != icons && icons.length > 0);
        boolean haveLetters = (null != letters && letters.length > 0);
        if (!haveIcon && !haveLetters) {
            return (int) columnWidth;
        }
        int iconWidth = haveIcon ? mContext.getResources().getDrawable(icons[0].getIconResId()).getIntrinsicWidth() * 3
                : 0;
        int lettersWidth = haveLetters ? (int) mPaint.measureText(letters[0]) * 3 : 0;
        return Math.max(iconWidth, lettersWidth);
    }

    private int measureHeight() {
        boolean haveIcon = (null != icons && icons.length > 0);
        boolean haveLetters = (null != letters && letters.length > 0);
        if (!haveIcon && !haveLetters) {
            return (int) columnWidth;
        }
        calculateBestGap(haveIcon, haveLetters);
        int height = 0;
        if (haveIcon) {
            for (IndexIcon icon : icons) {
                height += mContext.getResources().getDrawable(icon.getIconResId()).getIntrinsicHeight()
                        + variationalGap;
            }
        }
        if (haveLetters) {
            Rect bounds = new Rect();
            mPaint.getTextBounds(letters[0], 0, 1, bounds);
            float textHeight = bounds.bottom - bounds.top;
            height += textHeight * letters.length + variationalGap * (2 + letters.length);
        }
        return height;
    }

    private void calculateBestGap(boolean haveIcon, boolean haveLetters) {
        int contentLength = (haveIcon ? icons.length : 0) + (haveLetters ? letters.length : 0);
        variationalGap = (columnGap / (float) contentLength) * COLUMN_MAX_LETTERS_NUMBER;
        variationalGap = variationalGap > columnGap * 7 ? columnGap * 7 : variationalGap;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float yPos = event.getY();
                if (yPos > 0 && yPos < measureHeight()) {
                    String letter = getTouchedLetter(yPos);
                    if (null != mListener) {
                        mListener.onIndexLetterChanged(letter);
                    }
                }
                break;
            default:
                break;
        }
        return true;
    }

    private String getTouchedLetter(float yPos) {
        if (null == letters || null == icons) {
            return "";
        }
        boolean haveIcon = (null != icons && icons.length > 0);
        boolean haveLetters = (null != letters && letters.length > 0);
        if (!haveIcon && !haveLetters) {
            return null;
        }
        String letter = "";
        int letterSNum = letters.length + (haveIcon ? icons.length : 0);
        int position = (int) (yPos / (measureHeight() / (float) letterSNum));
        if (haveIcon && position < icons.length) {
            letter = icons[position].getIconName();
        } else {
            int relPosition = position - (haveIcon ? icons.length : 0);
            letter = letters[relPosition];
        }
        return letter;
    }

    class IndexIcon {
        private String iconName;
        private int iconResId;

        public IndexIcon(String iconName, int iconResId) {
            super();
            this.iconName = iconName;
            this.iconResId = iconResId;
        }

        public String getIconName() {
            return iconName;
        }

        public void setIconName(String iconName) {
            this.iconName = iconName;
        }

        public int getIconResId() {
            return iconResId;
        }

        public void setIconResId(int iconResId) {
            this.iconResId = iconResId;
        }

    }
}
