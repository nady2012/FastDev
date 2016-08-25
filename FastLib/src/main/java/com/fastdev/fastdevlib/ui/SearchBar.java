package com.fastdev.fastdevlib.ui;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.fastdev.fastdevlib.R;
import com.fastdev.fastdevlib.util.KeyCodeActionManager;

/**
 * A subclass of {@link RelativeLayout}, used to show a custom search bar.
 * 
 * <p>
 * Invoking {@link #setOnActionChangedListener(OnActionChagedListener)} to
 * listen to the event that search action changed.
 * 
 * <p>
 * Invoking {@link #setOnEditorActionListener(OnEditorActionListener)} to listen
 * to the event that the Search Key on the system soft keyboard has been
 * clicked.
 * 
 * 
 */
public class SearchBar extends RelativeLayout {

    private EditText mEdtSearch;
    private TextView mCancelButton;
    private ImageButton mClearButton;
    private OnActionChagedListener mActionListener;
    private OnEditorActionListener mEditorActionListener;
    private InputMethodManager inputMethodMgr;
    private Context mContext;
    private KeyCodeActionManager mKeyCodeActionManager;

    public SearchBar(Context context) {
        this(context, null);
    }

    public SearchBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initViews();
        mKeyCodeActionManager = KeyCodeActionManager.getInstance();
    }

    /**
     * Register a callback to be invoked when the search action changed, such as
     * started search, search content changing or canceled search.
     * 
     * @param listener
     *            The callback will run.
     */
    public void setOnActionChangedListener(OnActionChagedListener listener) {
        mActionListener = listener;
    }

    /**
     * Interface definition for a callback to be invoked when when the search
     * action changed.
     * 
     */
    public interface OnActionChagedListener {
        /**
         * Will run when search starts
         */
        void searchStart();

        /**
         * Will run when search content changes
         * 
         * @param content
         */
        void searchContentChanged(String content);

        /**
         * Will run when search cancels.
         */
        void searchCancel();
    }

    /**
     * Register a callback to be invoked when the search key on the system soft
     * keyboard is pressed.
     * 
     * @param onEditorActionListener
     *            The callback will run
     */
    public void setOnEditorActionListener(OnEditorActionListener onEditorActionListener) {
        mEditorActionListener = onEditorActionListener;
    }

    public void startSearch() {
        mKeyCodeActionManager.setKeyCodeAction(1, mKeyCodeAction);
        startSearch(true);
    }

    public void cancelSearch() {
        mKeyCodeActionManager.removeKeyCodeAction(1);
        this.requestFocus();
        hideSoftKeypad();
        mEdtSearch.setText("");
        if (null != mActionListener) {
            mActionListener.searchCancel();
        }
    }

    /**
     * Sets the text to be displayed when the text of the TextView is empty.
     * Null means to use the normal empty text. The hint does not currently
     * participate in determining the size of the view.
     * 
     * @param hint
     */
    public void setSearchHint(String hint) {
        if (null == mEdtSearch) {
            return;
        }
        mEdtSearch.setHint(hint);
    }

    private void initViews() {
        this.setFocusable(true);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.xsp_view_search_bar, this);

        mEdtSearch = (EditText) findViewById(R.id.search_edit);
        mClearButton = (ImageButton) findViewById(R.id.search_clear);
        mCancelButton = (TextView) findViewById(R.id.search_cancel);
        initListener();

        inputMethodMgr = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    private void initListener() {
        mEdtSearch.addTextChangedListener(editTextWatcher);

        mClearButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mEdtSearch.setText("");
            }
        });

        mCancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelSearch();
            }
        });

        mEdtSearch.setOnEditorActionListener(onEditorActionListener);
    }

    private TextWatcher editTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            mClearButton.setVisibility(((s == null) || (s.length() < 1)) ? View.GONE : View.VISIBLE);
            if (null != mActionListener) {
                mActionListener.searchContentChanged(s.toString());
            }
        }
    };

    private KeyCodeActionManager.OnKeyCodeActionListener mKeyCodeAction = new KeyCodeActionManager.OnKeyCodeActionListener() {

        @Override
        public boolean onKeyCodeAction(int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && SearchBar.this.getVisibility() == View.VISIBLE) {
                cancelSearch();
                return true;
            }
            return false;
        }
    };

    private OnEditorActionListener onEditorActionListener = new OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (!isShown()) {
                return true;
            }

            hideSoftKeypad();
            if (null == mEditorActionListener) {
                return true;
            }

            return mEditorActionListener.onEditorAction(v, actionId, event);
        }
    };

    private void startSearch(boolean hasFocus) {
        if (hasFocus && null != mActionListener) {
            mActionListener.searchStart();
        }
        mCancelButton.setVisibility(hasFocus ? View.VISIBLE : View.GONE);
        if (hasFocus) {
            showSoftKeypad();
        }
    }

    private void showSoftKeypad() {
        if (mEdtSearch != null) {
            mEdtSearch.requestFocus();
        }
        if (inputMethodMgr != null && mEdtSearch != null) {
            inputMethodMgr.showSoftInput(mEdtSearch, InputMethodManager.RESULT_UNCHANGED_SHOWN);
        }
    }

    private void hideSoftKeypad() {
        if (inputMethodMgr != null && inputMethodMgr.isActive() && mEdtSearch != null) {
            inputMethodMgr.hideSoftInputFromWindow(mEdtSearch.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
