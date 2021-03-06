package android.service.autofill;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.autofill.AutofillId;
import android.view.autofill.Helper;
import android.widget.RemoteViews;
import com.android.internal.util.Preconditions;
import java.util.ArrayList;
import java.util.regex.Pattern;

public final class ImageTransformation extends InternalTransformation implements Transformation, Parcelable {
    public static final Parcelable.Creator<ImageTransformation> CREATOR = new Parcelable.Creator<ImageTransformation>() {
        /* class android.service.autofill.ImageTransformation.AnonymousClass1 */

        @Override // android.os.Parcelable.Creator
        public ImageTransformation createFromParcel(Parcel parcel) {
            Builder builder;
            AutofillId id = (AutofillId) parcel.readParcelable(null);
            Pattern[] regexs = (Pattern[]) parcel.readSerializable();
            int[] resIds = parcel.createIntArray();
            CharSequence[] contentDescriptions = parcel.readCharSequenceArray();
            CharSequence contentDescription = contentDescriptions[0];
            if (contentDescription != null) {
                builder = new Builder(id, regexs[0], resIds[0], contentDescription);
            } else {
                builder = new Builder(id, regexs[0], resIds[0]);
            }
            int size = regexs.length;
            for (int i = 1; i < size; i++) {
                if (contentDescriptions[i] != null) {
                    builder.addOption(regexs[i], resIds[i], contentDescriptions[i]);
                } else {
                    builder.addOption(regexs[i], resIds[i]);
                }
            }
            return builder.build();
        }

        @Override // android.os.Parcelable.Creator
        public ImageTransformation[] newArray(int size) {
            return new ImageTransformation[size];
        }
    };
    private static final String TAG = "ImageTransformation";
    private final AutofillId mId;
    private final ArrayList<Option> mOptions;

    private ImageTransformation(Builder builder) {
        this.mId = builder.mId;
        this.mOptions = builder.mOptions;
    }

    @Override // android.service.autofill.InternalTransformation
    public void apply(ValueFinder finder, RemoteViews parentTemplate, int childViewId) throws Exception {
        String value = finder.findByAutofillId(this.mId);
        if (value == null) {
            Log.w(TAG, "No view for id " + this.mId);
            return;
        }
        int size = this.mOptions.size();
        if (Helper.sDebug) {
            Log.d(TAG, size + " multiple options on id " + childViewId + " to compare against");
        }
        for (int i = 0; i < size; i++) {
            Option option = this.mOptions.get(i);
            try {
                if (option.pattern.matcher(value).matches()) {
                    Log.d(TAG, "Found match at " + i + ": " + option);
                    parentTemplate.setImageViewResource(childViewId, option.resId);
                    if (option.contentDescription != null) {
                        parentTemplate.setContentDescription(childViewId, option.contentDescription);
                        return;
                    }
                    return;
                }
            } catch (Exception e) {
                Log.w(TAG, "Error matching regex #" + i + "(" + option.pattern + ") on id " + option.resId + ": " + e.getClass());
                throw e;
            }
        }
        if (Helper.sDebug) {
            Log.d(TAG, "No match for " + value);
        }
    }

    public static class Builder {
        private boolean mDestroyed;
        private final AutofillId mId;
        private final ArrayList<Option> mOptions = new ArrayList<>();

        @Deprecated
        public Builder(AutofillId id, Pattern regex, int resId) {
            this.mId = (AutofillId) Preconditions.checkNotNull(id);
            addOption(regex, resId);
        }

        public Builder(AutofillId id, Pattern regex, int resId, CharSequence contentDescription) {
            this.mId = (AutofillId) Preconditions.checkNotNull(id);
            addOption(regex, resId, contentDescription);
        }

        @Deprecated
        public Builder addOption(Pattern regex, int resId) {
            addOptionInternal(regex, resId, null);
            return this;
        }

        public Builder addOption(Pattern regex, int resId, CharSequence contentDescription) {
            addOptionInternal(regex, resId, (CharSequence) Preconditions.checkNotNull(contentDescription));
            return this;
        }

        private void addOptionInternal(Pattern regex, int resId, CharSequence contentDescription) {
            throwIfDestroyed();
            Preconditions.checkNotNull(regex);
            Preconditions.checkArgument(resId != 0);
            this.mOptions.add(new Option(regex, resId, contentDescription));
        }

        public ImageTransformation build() {
            throwIfDestroyed();
            this.mDestroyed = true;
            return new ImageTransformation(this);
        }

        private void throwIfDestroyed() {
            Preconditions.checkState(!this.mDestroyed, "Already called build()");
        }
    }

    public String toString() {
        if (!Helper.sDebug) {
            return super.toString();
        }
        return "ImageTransformation: [id=" + this.mId + ", options=" + this.mOptions + "]";
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX DEBUG: Multi-variable search result rejected for r8v0, resolved type: android.os.Parcel */
    /* JADX DEBUG: Multi-variable search result rejected for r3v0, resolved type: java.lang.String[] */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v0, types: [java.util.regex.Pattern[], java.io.Serializable] */
    /* JADX WARNING: Unknown variable types count: 1 */
    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeParcelable(this.mId, flags);
        int size = this.mOptions.size();
        ?? r1 = new Pattern[size];
        int[] resIds = new int[size];
        CharSequence[] contentDescriptions = new String[size];
        for (int i = 0; i < size; i++) {
            Option option = this.mOptions.get(i);
            r1[i] = option.pattern;
            resIds[i] = option.resId;
            contentDescriptions[i] = option.contentDescription;
        }
        parcel.writeSerializable(r1);
        parcel.writeIntArray(resIds);
        parcel.writeCharSequenceArray(contentDescriptions);
    }

    /* access modifiers changed from: private */
    public static final class Option {
        public final CharSequence contentDescription;
        public final Pattern pattern;
        public final int resId;

        Option(Pattern pattern2, int resId2, CharSequence contentDescription2) {
            this.pattern = pattern2;
            this.resId = resId2;
            this.contentDescription = TextUtils.trimNoCopySpans(contentDescription2);
        }
    }
}
