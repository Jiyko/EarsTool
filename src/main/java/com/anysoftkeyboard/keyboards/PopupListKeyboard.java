package com.anysoftkeyboard.keyboards;

import android.content.Context;
import android.support.annotation.NonNull;

import com.anysoftkeyboard.addons.AddOn;
import com.sevencupsoftea.ears.R;
//import com.menny.android.anysoftkeyboard.R;

public class PopupListKeyboard extends AnyPopupKeyboard {
	private final int mAdditionalWidth;

	public PopupListKeyboard(@NonNull AddOn keyboardAddOn, Context askContext, KeyboardDimens keyboardDimens, String[] keysNames, String[] keyValues, String name) {
		super(keyboardAddOn, askContext, askContext, R.xml.quick_text_list_popup, keyboardDimens, name);
		int rowWidth = 0;
		Key baseKey = getKeys().get(0);
		Row row = baseKey.row;
		//now adding the popups
		final float y = baseKey.y;
		final float keyHorizontalGap = row.defaultHorizontalGap;
		baseKey.codes = new int[]{0};
		baseKey.label = keysNames[0];
		baseKey.text = keyValues[0];
		float x = baseKey.width;
		Key aKey = null;
		for (int entryIndex = 1; entryIndex < keysNames.length; entryIndex++) {
			x += (keyHorizontalGap / 2);

			aKey = new AnyKey(row, keyboardDimens);
			aKey.codes = new int[]{0};
			aKey.label = keysNames[entryIndex];
			aKey.text = keyValues[entryIndex];
			aKey.x = (int) x;
			aKey.width -= keyHorizontalGap;//the gap is on both sides
			aKey.y = (int) y;
			final int xOffset = (int) (aKey.width + keyHorizontalGap + (keyHorizontalGap / 2));
			x += xOffset;
			rowWidth += xOffset;
			getKeys().add(aKey);
		}
		//adding edge flag to the last key
		baseKey.edgeFlags = EDGE_LEFT;
		//this holds the last key
		if (aKey != null)
			aKey.edgeFlags = EDGE_RIGHT;
		else
			baseKey.edgeFlags |= EDGE_RIGHT;//adding another flag, since the baseKey is the only one in the row

		mAdditionalWidth = rowWidth;
	}

	@Override
	public int getMinWidth() {
		return super.getMinWidth() + mAdditionalWidth;
	}
}
