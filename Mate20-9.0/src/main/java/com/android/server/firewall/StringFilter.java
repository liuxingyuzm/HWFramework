package com.android.server.firewall;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.PatternMatcher;
import com.android.server.am.HwBroadcastRadarUtil;
import com.android.server.net.watchlist.WatchlistLoggingHandler;
import java.io.IOException;
import java.util.regex.Pattern;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

abstract class StringFilter implements Filter {
    public static final FilterFactory ACTION = new ValueProvider(HwBroadcastRadarUtil.KEY_ACTION) {
        public String getValue(ComponentName resolvedComponent, Intent intent, String resolvedType) {
            return intent.getAction();
        }
    };
    private static final String ATTR_CONTAINS = "contains";
    private static final String ATTR_EQUALS = "equals";
    private static final String ATTR_IS_NULL = "isNull";
    private static final String ATTR_PATTERN = "pattern";
    private static final String ATTR_REGEX = "regex";
    private static final String ATTR_STARTS_WITH = "startsWith";
    public static final ValueProvider COMPONENT = new ValueProvider("component") {
        public String getValue(ComponentName resolvedComponent, Intent intent, String resolvedType) {
            if (resolvedComponent != null) {
                return resolvedComponent.flattenToString();
            }
            return null;
        }
    };
    public static final ValueProvider COMPONENT_NAME = new ValueProvider("component-name") {
        public String getValue(ComponentName resolvedComponent, Intent intent, String resolvedType) {
            if (resolvedComponent != null) {
                return resolvedComponent.getClassName();
            }
            return null;
        }
    };
    public static final ValueProvider COMPONENT_PACKAGE = new ValueProvider("component-package") {
        public String getValue(ComponentName resolvedComponent, Intent intent, String resolvedType) {
            if (resolvedComponent != null) {
                return resolvedComponent.getPackageName();
            }
            return null;
        }
    };
    public static final ValueProvider DATA = new ValueProvider("data") {
        public String getValue(ComponentName resolvedComponent, Intent intent, String resolvedType) {
            Uri data = intent.getData();
            if (data != null) {
                return data.toString();
            }
            return null;
        }
    };
    public static final ValueProvider HOST = new ValueProvider(WatchlistLoggingHandler.WatchlistEventKeys.HOST) {
        public String getValue(ComponentName resolvedComponent, Intent intent, String resolvedType) {
            Uri data = intent.getData();
            if (data != null) {
                return data.getHost();
            }
            return null;
        }
    };
    public static final ValueProvider MIME_TYPE = new ValueProvider("mime-type") {
        public String getValue(ComponentName resolvedComponent, Intent intent, String resolvedType) {
            return resolvedType;
        }
    };
    public static final ValueProvider PATH = new ValueProvider("path") {
        public String getValue(ComponentName resolvedComponent, Intent intent, String resolvedType) {
            Uri data = intent.getData();
            if (data != null) {
                return data.getPath();
            }
            return null;
        }
    };
    public static final ValueProvider SCHEME = new ValueProvider("scheme") {
        public String getValue(ComponentName resolvedComponent, Intent intent, String resolvedType) {
            Uri data = intent.getData();
            if (data != null) {
                return data.getScheme();
            }
            return null;
        }
    };
    public static final ValueProvider SSP = new ValueProvider("scheme-specific-part") {
        public String getValue(ComponentName resolvedComponent, Intent intent, String resolvedType) {
            Uri data = intent.getData();
            if (data != null) {
                return data.getSchemeSpecificPart();
            }
            return null;
        }
    };
    private final ValueProvider mValueProvider;

    private static class ContainsFilter extends StringFilter {
        private final String mFilterValue;

        public ContainsFilter(ValueProvider valueProvider, String attrValue) {
            super(valueProvider);
            this.mFilterValue = attrValue;
        }

        public boolean matchesValue(String value) {
            return value != null && value.contains(this.mFilterValue);
        }
    }

    private static class EqualsFilter extends StringFilter {
        private final String mFilterValue;

        public EqualsFilter(ValueProvider valueProvider, String attrValue) {
            super(valueProvider);
            this.mFilterValue = attrValue;
        }

        public boolean matchesValue(String value) {
            return value != null && value.equals(this.mFilterValue);
        }
    }

    private static class IsNullFilter extends StringFilter {
        private final boolean mIsNull;

        public IsNullFilter(ValueProvider valueProvider, String attrValue) {
            super(valueProvider);
            this.mIsNull = Boolean.parseBoolean(attrValue);
        }

        public IsNullFilter(ValueProvider valueProvider, boolean isNull) {
            super(valueProvider);
            this.mIsNull = isNull;
        }

        public boolean matchesValue(String value) {
            return (value == null) == this.mIsNull;
        }
    }

    private static class PatternStringFilter extends StringFilter {
        private final PatternMatcher mPattern;

        public PatternStringFilter(ValueProvider valueProvider, String attrValue) {
            super(valueProvider);
            this.mPattern = new PatternMatcher(attrValue, 2);
        }

        public boolean matchesValue(String value) {
            return value != null && this.mPattern.match(value);
        }
    }

    private static class RegexFilter extends StringFilter {
        private final Pattern mPattern;

        public RegexFilter(ValueProvider valueProvider, String attrValue) {
            super(valueProvider);
            this.mPattern = Pattern.compile(attrValue);
        }

        public boolean matchesValue(String value) {
            return value != null && this.mPattern.matcher(value).matches();
        }
    }

    private static class StartsWithFilter extends StringFilter {
        private final String mFilterValue;

        public StartsWithFilter(ValueProvider valueProvider, String attrValue) {
            super(valueProvider);
            this.mFilterValue = attrValue;
        }

        public boolean matchesValue(String value) {
            return value != null && value.startsWith(this.mFilterValue);
        }
    }

    private static abstract class ValueProvider extends FilterFactory {
        public abstract String getValue(ComponentName componentName, Intent intent, String str);

        protected ValueProvider(String tag) {
            super(tag);
        }

        public Filter newFilter(XmlPullParser parser) throws IOException, XmlPullParserException {
            return StringFilter.readFromXml(this, parser);
        }
    }

    /* access modifiers changed from: protected */
    public abstract boolean matchesValue(String str);

    private StringFilter(ValueProvider valueProvider) {
        this.mValueProvider = valueProvider;
    }

    public static StringFilter readFromXml(ValueProvider valueProvider, XmlPullParser parser) throws IOException, XmlPullParserException {
        StringFilter filter = null;
        for (int i = 0; i < parser.getAttributeCount(); i++) {
            StringFilter newFilter = getFilter(valueProvider, parser, i);
            if (newFilter != null) {
                if (filter == null) {
                    filter = newFilter;
                } else {
                    throw new XmlPullParserException("Multiple string filter attributes found");
                }
            }
        }
        if (filter == null) {
            return new IsNullFilter(valueProvider, false);
        }
        return filter;
    }

    private static StringFilter getFilter(ValueProvider valueProvider, XmlPullParser parser, int attributeIndex) {
        String attributeName = parser.getAttributeName(attributeIndex);
        char charAt = attributeName.charAt(0);
        if (charAt != 'c') {
            if (charAt != 'e') {
                if (charAt != 'i') {
                    if (charAt != 'p') {
                        switch (charAt) {
                            case 'r':
                                if (!attributeName.equals(ATTR_REGEX)) {
                                    return null;
                                }
                                return new RegexFilter(valueProvider, parser.getAttributeValue(attributeIndex));
                            case HdmiCecKeycode.CEC_KEYCODE_F3_GREEN /*115*/:
                                if (!attributeName.equals(ATTR_STARTS_WITH)) {
                                    return null;
                                }
                                return new StartsWithFilter(valueProvider, parser.getAttributeValue(attributeIndex));
                            default:
                                return null;
                        }
                    } else if (!attributeName.equals(ATTR_PATTERN)) {
                        return null;
                    } else {
                        return new PatternStringFilter(valueProvider, parser.getAttributeValue(attributeIndex));
                    }
                } else if (!attributeName.equals(ATTR_IS_NULL)) {
                    return null;
                } else {
                    return new IsNullFilter(valueProvider, parser.getAttributeValue(attributeIndex));
                }
            } else if (!attributeName.equals(ATTR_EQUALS)) {
                return null;
            } else {
                return new EqualsFilter(valueProvider, parser.getAttributeValue(attributeIndex));
            }
        } else if (!attributeName.equals(ATTR_CONTAINS)) {
            return null;
        } else {
            return new ContainsFilter(valueProvider, parser.getAttributeValue(attributeIndex));
        }
    }

    public boolean matches(IntentFirewall ifw, ComponentName resolvedComponent, Intent intent, int callerUid, int callerPid, String resolvedType, int receivingUid) {
        return matchesValue(this.mValueProvider.getValue(resolvedComponent, intent, resolvedType));
    }
}
