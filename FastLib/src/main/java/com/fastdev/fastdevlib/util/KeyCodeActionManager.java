package com.fastdev.fastdevlib.util;

import android.util.SparseArray;
import android.view.KeyEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A singleton class to manage the key code action.
 * 
 * <p>
 * Invoking {@link #setKeyCodeAction(Integer, OnKeyCodeActionListener)} to set a
 * key code action with its level. INvoking
 * {@link #executeKeyCodeAction(int, KeyEvent)} to execute the key code action
 * which has the highest level.
 * 
 * <p>
 * Currently this class has been used to manage the Back Key click event on Home
 * page(HomePageActivity).
 * 
 */
public class KeyCodeActionManager {
    private SparseArray<OnKeyCodeActionListener> actionMap;
    private ArrayList<Integer> actionLevel;
    private static KeyCodeActionManager instance;

    /**
     * Get the instance of KeyCodeActionManager.
     * 
     * @return the instance of KeyCodeActionManager.
     */
    public static KeyCodeActionManager getInstance() {
        if (null == instance) {
            instance = new KeyCodeActionManager();
        }
        return instance;
    }

    private KeyCodeActionManager() {
        actionMap = new SparseArray<OnKeyCodeActionListener>();
        actionLevel = new ArrayList<Integer>();
    }

    /**
     * Interface definition for a callback to be invoked when invokes
     * {@link KeyCodeActionManager#executeKeyCodeAction(int, KeyEvent)}
     * 
     */
    public interface OnKeyCodeActionListener {
        boolean onKeyCodeAction(int keyCode, KeyEvent event);
    }

    /**
     * Register a key code action.
     * 
     * @param level
     *            The level of the key code action.
     * @param actionListener
     *            The callback method to be invoked when calls
     *            {@link #executeKeyCodeAction(int, KeyEvent)}.
     */
    public void setKeyCodeAction(int level, OnKeyCodeActionListener actionListener) {
        actionMap.put(level, actionListener);
        if (!actionLevel.contains(level)) {
            actionLevel.add(level);
        }
    }

    /**
     * Unregister a key code action.
     * 
     * @param levelKey
     *            the level of the key code action
     */
    public void removeKeyCodeAction(Integer levelKey) {
        actionMap.remove(levelKey);
        actionLevel.remove(levelKey);
    }

    /**
     * Execute a key code action. The action with the highest level will be
     * executed.
     * 
     * @param keyCode
     *            The value in event.getKeyCode().
     * @param event
     *            Description of the key event.
     * @return true, if the key code action execute successfully. false,
     *         otherwise.
     */
    public boolean executeKeyCodeAction(int keyCode, KeyEvent event) {
        if (null == actionLevel || actionLevel.isEmpty() || null == actionMap || actionMap.size() <= 0) {
            return false;
        }

        sortActionLevelList(actionLevel);

        return actionMap.get(actionLevel.get(0)).onKeyCodeAction(keyCode, event);
    }

    private static void sortActionLevelList(List<Integer> levels) {
        Collections.sort(levels, new Comparator<Integer>() {
            @Override
            public int compare(Integer object1, Integer object2) {
                return (object2).compareTo(object1);
            }
        });
    }

}
