package com.android.internal.app;

import com.android.internal.app.LocaleStore;
import java.util.ArrayList;

public interface IHwSuggestedLocaleAdapterInner {
    int getItemViewTypeEx(int i);

    String getLocaleInfoScript(LocaleStore.LocaleInfo localeInfo);

    int getSuggestionCount();

    ArrayList<LocaleStore.LocaleInfo> getmLocaleOptions();

    ArrayList<LocaleStore.LocaleInfo> getmOriginalLocaleOptions();

    boolean isCountryMode();

    boolean isShowHeaders();

    boolean isSuggestedLocale(LocaleStore.LocaleInfo localeInfo);

    void setCountryMode(boolean z);

    void setSuggestionCount(int i);

    void setmLocaleOptions(ArrayList<LocaleStore.LocaleInfo> arrayList);

    void setmOriginalLocaleOptions(ArrayList<LocaleStore.LocaleInfo> arrayList);
}
