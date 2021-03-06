package com.android.uiautomator.core;

import android.app.UiAutomation.AccessibilityEventFilter;
import android.graphics.Point;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;
import android.view.InputEvent;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;
import android.view.MotionEvent.PointerProperties;
import android.view.accessibility.AccessibilityEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

class InteractionController {
    private static final boolean DEBUG = false;
    private static final String LOG_TAG = null;
    private static final int MOTION_EVENT_INJECTION_DELAY_MILLIS = 5;
    private static final long REGULAR_CLICK_LENGTH = 100;
    private long mDownTime;
    private final KeyCharacterMap mKeyCharacterMap;
    private final UiAutomatorBridge mUiAutomatorBridge;

    /* renamed from: com.android.uiautomator.core.InteractionController.1 */
    class AnonymousClass1 implements Runnable {
        final /* synthetic */ int val$keyCode;
        final /* synthetic */ int val$metaState;

        AnonymousClass1(int val$keyCode, int val$metaState) {
            this.val$keyCode = val$keyCode;
            this.val$metaState = val$metaState;
        }

        public void run() {
            long eventTime = SystemClock.uptimeMillis();
            if (InteractionController.this.injectEventSync(new KeyEvent(eventTime, eventTime, 0, this.val$keyCode, 0, this.val$metaState, -1, 0, 0, 257))) {
                InteractionController.this.injectEventSync(new KeyEvent(eventTime, eventTime, 1, this.val$keyCode, 0, this.val$metaState, -1, 0, 0, 257));
            }
        }
    }

    /* renamed from: com.android.uiautomator.core.InteractionController.2 */
    class AnonymousClass2 implements Runnable {
        final /* synthetic */ int val$x;
        final /* synthetic */ int val$y;

        AnonymousClass2(int val$x, int val$y) {
            this.val$x = val$x;
            this.val$y = val$y;
        }

        public void run() {
            if (InteractionController.this.touchDown(this.val$x, this.val$y)) {
                SystemClock.sleep(InteractionController.REGULAR_CLICK_LENGTH);
                InteractionController.this.touchUp(this.val$x, this.val$y);
            }
        }
    }

    /* renamed from: com.android.uiautomator.core.InteractionController.3 */
    class AnonymousClass3 implements Runnable {
        final /* synthetic */ int val$downX;
        final /* synthetic */ int val$downY;
        final /* synthetic */ int val$steps;
        final /* synthetic */ int val$upX;
        final /* synthetic */ int val$upY;

        AnonymousClass3(int val$downX, int val$downY, int val$upX, int val$upY, int val$steps) {
            this.val$downX = val$downX;
            this.val$downY = val$downY;
            this.val$upX = val$upX;
            this.val$upY = val$upY;
            this.val$steps = val$steps;
        }

        public void run() {
            InteractionController.this.swipe(this.val$downX, this.val$downY, this.val$upX, this.val$upY, this.val$steps);
        }
    }

    class EventCollectingPredicate implements AccessibilityEventFilter {
        List<AccessibilityEvent> mEventsList;
        int mMask;

        EventCollectingPredicate(int mask, List<AccessibilityEvent> events) {
            this.mMask = mask;
            this.mEventsList = events;
        }

        public boolean accept(AccessibilityEvent t) {
            if ((t.getEventType() & this.mMask) != 0) {
                this.mEventsList.add(AccessibilityEvent.obtain(t));
            }
            return InteractionController.DEBUG;
        }
    }

    class WaitForAllEventPredicate implements AccessibilityEventFilter {
        int mMask;

        public boolean accept(android.view.accessibility.AccessibilityEvent r1) {
            /* JADX: method processing error */
/*
            Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: com.android.uiautomator.core.InteractionController.WaitForAllEventPredicate.accept(android.view.accessibility.AccessibilityEvent):boolean
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:113)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:263)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: not-int
	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:568)
	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:56)
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:99)
	... 6 more
*/
            /*
            // Can't load method instructions.
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.uiautomator.core.InteractionController.WaitForAllEventPredicate.accept(android.view.accessibility.AccessibilityEvent):boolean");
        }

        WaitForAllEventPredicate(int mask) {
            this.mMask = mask;
        }
    }

    class WaitForAnyEventPredicate implements AccessibilityEventFilter {
        int mMask;

        WaitForAnyEventPredicate(int mask) {
            this.mMask = mask;
        }

        public boolean accept(AccessibilityEvent t) {
            if ((t.getEventType() & this.mMask) != 0) {
                return true;
            }
            return InteractionController.DEBUG;
        }
    }

    static {
        /* JADX: method processing error */
/*
        Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: com.android.uiautomator.core.InteractionController.<clinit>():void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:113)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
Caused by: jadx.core.utils.exceptions.DecodeException:  in method: com.android.uiautomator.core.InteractionController.<clinit>():void
	at jadx.core.dex.instructions.InsnDecoder.decodeInsns(InsnDecoder.java:46)
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:98)
	... 5 more
Caused by: java.lang.IllegalArgumentException: bogus opcode: 00e9
	at com.android.dx.io.OpcodeInfo.get(OpcodeInfo.java:1197)
	at com.android.dx.io.OpcodeInfo.getFormat(OpcodeInfo.java:1212)
	at com.android.dx.io.instructions.DecodedInstruction.decode(DecodedInstruction.java:72)
	at jadx.core.dex.instructions.InsnDecoder.decodeInsns(InsnDecoder.java:43)
	... 6 more
*/
        /*
        // Can't load method instructions.
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.uiautomator.core.InteractionController.<clinit>():void");
    }

    public InteractionController(UiAutomatorBridge bridge) {
        this.mKeyCharacterMap = KeyCharacterMap.load(-1);
        this.mUiAutomatorBridge = bridge;
    }

    private AccessibilityEvent runAndWaitForEvents(Runnable command, AccessibilityEventFilter filter, long timeout) {
        try {
            return this.mUiAutomatorBridge.executeCommandAndWaitForAccessibilityEvent(command, filter, timeout);
        } catch (TimeoutException e) {
            Log.w(LOG_TAG, "runAndwaitForEvent timedout waiting for events");
            return null;
        } catch (Exception e2) {
            Log.e(LOG_TAG, "exception from executeCommandAndWaitForAccessibilityEvent", e2);
            return null;
        }
    }

    public boolean sendKeyAndWaitForEvent(int keyCode, int metaState, int eventType, long timeout) {
        return runAndWaitForEvents(new AnonymousClass1(keyCode, metaState), new WaitForAnyEventPredicate(eventType), timeout) != null ? true : DEBUG;
    }

    public boolean clickNoSync(int x, int y) {
        Log.d(LOG_TAG, "clickNoSync (" + x + ", " + y + ")");
        if (touchDown(x, y)) {
            SystemClock.sleep(REGULAR_CLICK_LENGTH);
            if (touchUp(x, y)) {
                return true;
            }
        }
        return DEBUG;
    }

    public boolean clickAndSync(int x, int y, long timeout) {
        Log.d(LOG_TAG, String.format("clickAndSync(%d, %d)", new Object[]{Integer.valueOf(x), Integer.valueOf(y)}));
        if (runAndWaitForEvents(clickRunnable(x, y), new WaitForAnyEventPredicate(2052), timeout) != null) {
            return true;
        }
        return DEBUG;
    }

    public boolean clickAndWaitForNewWindow(int x, int y, long timeout) {
        Log.d(LOG_TAG, String.format("clickAndWaitForNewWindow(%d, %d)", new Object[]{Integer.valueOf(x), Integer.valueOf(y)}));
        if (runAndWaitForEvents(clickRunnable(x, y), new WaitForAllEventPredicate(2080), timeout) != null) {
            return true;
        }
        return DEBUG;
    }

    private Runnable clickRunnable(int x, int y) {
        return new AnonymousClass2(x, y);
    }

    public boolean longTapNoSync(int x, int y) {
        if (DEBUG) {
            Log.d(LOG_TAG, "longTapNoSync (" + x + ", " + y + ")");
        }
        if (touchDown(x, y)) {
            SystemClock.sleep(this.mUiAutomatorBridge.getSystemLongPressTime());
            if (touchUp(x, y)) {
                return true;
            }
        }
        return DEBUG;
    }

    private boolean touchDown(int x, int y) {
        if (DEBUG) {
            Log.d(LOG_TAG, "touchDown (" + x + ", " + y + ")");
        }
        this.mDownTime = SystemClock.uptimeMillis();
        MotionEvent event = MotionEvent.obtain(this.mDownTime, this.mDownTime, 0, (float) x, (float) y, 1);
        event.setSource(4098);
        return injectEventSync(event);
    }

    private boolean touchUp(int x, int y) {
        if (DEBUG) {
            Log.d(LOG_TAG, "touchUp (" + x + ", " + y + ")");
        }
        MotionEvent event = MotionEvent.obtain(this.mDownTime, SystemClock.uptimeMillis(), 1, (float) x, (float) y, 1);
        event.setSource(4098);
        this.mDownTime = 0;
        return injectEventSync(event);
    }

    private boolean touchMove(int x, int y) {
        if (DEBUG) {
            Log.d(LOG_TAG, "touchMove (" + x + ", " + y + ")");
        }
        MotionEvent event = MotionEvent.obtain(this.mDownTime, SystemClock.uptimeMillis(), 2, (float) x, (float) y, 1);
        event.setSource(4098);
        return injectEventSync(event);
    }

    public boolean scrollSwipe(int downX, int downY, int upX, int upY, int steps) {
        Log.d(LOG_TAG, "scrollSwipe (" + downX + ", " + downY + ", " + upX + ", " + upY + ", " + steps + ")");
        Runnable command = new AnonymousClass3(downX, downY, upX, upY, steps);
        ArrayList<AccessibilityEvent> events = new ArrayList();
        runAndWaitForEvents(command, new EventCollectingPredicate(4096, events), Configurator.getInstance().getScrollAcknowledgmentTimeout());
        AccessibilityEvent event = getLastMatchingEvent(events, 4096);
        if (event == null) {
            recycleAccessibilityEvents(events);
            return DEBUG;
        }
        boolean z;
        boolean foundEnd = DEBUG;
        if (event.getFromIndex() != -1 && event.getToIndex() != -1 && event.getItemCount() != -1) {
            foundEnd = event.getFromIndex() != 0 ? event.getItemCount() + -1 == event.getToIndex() ? true : DEBUG : true;
            Log.d(LOG_TAG, "scrollSwipe reached scroll end: " + foundEnd);
        } else if (!(event.getScrollX() == -1 || event.getScrollY() == -1)) {
            if (downX == upX) {
                foundEnd = event.getScrollY() != 0 ? event.getScrollY() == event.getMaxScrollY() ? true : DEBUG : true;
                Log.d(LOG_TAG, "Vertical scrollSwipe reached scroll end: " + foundEnd);
            } else if (downY == upY) {
                foundEnd = event.getScrollX() != 0 ? event.getScrollX() == event.getMaxScrollX() ? true : DEBUG : true;
                Log.d(LOG_TAG, "Horizontal scrollSwipe reached scroll end: " + foundEnd);
            }
        }
        recycleAccessibilityEvents(events);
        if (foundEnd) {
            z = DEBUG;
        } else {
            z = true;
        }
        return z;
    }

    private AccessibilityEvent getLastMatchingEvent(List<AccessibilityEvent> events, int type) {
        for (int x = events.size(); x > 0; x--) {
            AccessibilityEvent event = (AccessibilityEvent) events.get(x - 1);
            if (event.getEventType() == type) {
                return event;
            }
        }
        return null;
    }

    private void recycleAccessibilityEvents(List<AccessibilityEvent> events) {
        for (AccessibilityEvent event : events) {
            event.recycle();
        }
        events.clear();
    }

    public boolean swipe(int downX, int downY, int upX, int upY, int steps) {
        return swipe(downX, downY, upX, upY, steps, DEBUG);
    }

    public boolean swipe(int downX, int downY, int upX, int upY, int steps, boolean drag) {
        int swipeSteps = steps;
        if (steps == 0) {
            swipeSteps = 1;
        }
        double xStep = ((double) (upX - downX)) / ((double) swipeSteps);
        double yStep = ((double) (upY - downY)) / ((double) swipeSteps);
        boolean ret = touchDown(downX, downY);
        if (drag) {
            SystemClock.sleep(this.mUiAutomatorBridge.getSystemLongPressTime());
        }
        for (int i = 1; i < swipeSteps; i++) {
            ret &= touchMove(((int) (((double) i) * xStep)) + downX, ((int) (((double) i) * yStep)) + downY);
            if (!ret) {
                break;
            }
            SystemClock.sleep(5);
        }
        if (drag) {
            SystemClock.sleep(REGULAR_CLICK_LENGTH);
        }
        return ret & touchUp(upX, upY);
    }

    public boolean swipe(Point[] segments, int segmentSteps) {
        int swipeSteps = segmentSteps;
        if (segmentSteps == 0) {
            segmentSteps = 1;
        }
        if (segments.length == 0) {
            return DEBUG;
        }
        boolean ret = touchDown(segments[0].x, segments[0].y);
        for (int seg = 0; seg < segments.length; seg++) {
            if (seg + 1 < segments.length) {
                double xStep = ((double) (segments[seg + 1].x - segments[seg].x)) / ((double) segmentSteps);
                double yStep = ((double) (segments[seg + 1].y - segments[seg].y)) / ((double) segmentSteps);
                for (int i = 1; i < swipeSteps; i++) {
                    ret &= touchMove(segments[seg].x + ((int) (((double) i) * xStep)), segments[seg].y + ((int) (((double) i) * yStep)));
                    if (!ret) {
                        break;
                    }
                    SystemClock.sleep(5);
                }
            }
        }
        return ret & touchUp(segments[segments.length - 1].x, segments[segments.length - 1].y);
    }

    public boolean sendText(String text) {
        if (DEBUG) {
            Log.d(LOG_TAG, "sendText (" + text + ")");
        }
        KeyEvent[] events = this.mKeyCharacterMap.getEvents(text.toCharArray());
        if (events != null) {
            long keyDelay = Configurator.getInstance().getKeyInjectionDelay();
            for (KeyEvent event2 : events) {
                if (!injectEventSync(KeyEvent.changeTimeRepeat(event2, SystemClock.uptimeMillis(), 0))) {
                    return DEBUG;
                }
                SystemClock.sleep(keyDelay);
            }
        }
        return true;
    }

    public boolean sendKey(int keyCode, int metaState) {
        if (DEBUG) {
            Log.d(LOG_TAG, "sendKey (" + keyCode + ", " + metaState + ")");
        }
        long eventTime = SystemClock.uptimeMillis();
        if (injectEventSync(new KeyEvent(eventTime, eventTime, 0, keyCode, 0, metaState, -1, 0, 0, 257)) && injectEventSync(new KeyEvent(eventTime, eventTime, 1, keyCode, 0, metaState, -1, 0, 0, 257))) {
            return true;
        }
        return DEBUG;
    }

    public void setRotationRight() {
        this.mUiAutomatorBridge.setRotation(3);
    }

    public void setRotationLeft() {
        this.mUiAutomatorBridge.setRotation(1);
    }

    public void setRotationNatural() {
        this.mUiAutomatorBridge.setRotation(0);
    }

    public void freezeRotation() {
        this.mUiAutomatorBridge.setRotation(-1);
    }

    public void unfreezeRotation() {
        this.mUiAutomatorBridge.setRotation(-2);
    }

    public boolean wakeDevice() throws RemoteException {
        if (isScreenOn()) {
            return DEBUG;
        }
        sendKey(26, 0);
        return true;
    }

    public boolean sleepDevice() throws RemoteException {
        if (!isScreenOn()) {
            return DEBUG;
        }
        sendKey(26, 0);
        return true;
    }

    public boolean isScreenOn() throws RemoteException {
        return this.mUiAutomatorBridge.isScreenOn();
    }

    private boolean injectEventSync(InputEvent event) {
        return this.mUiAutomatorBridge.injectInputEvent(event, true);
    }

    private int getPointerAction(int motionEnvent, int index) {
        return (index << 8) + motionEnvent;
    }

    public boolean performMultiPointerGesture(PointerCoords[]... touches) {
        if (touches.length < 2) {
            throw new IllegalArgumentException("Must provide coordinates for at least 2 pointers");
        }
        int x;
        int maxSteps = 0;
        for (x = 0; x < touches.length; x++) {
            if (maxSteps < touches[x].length) {
                maxSteps = touches[x].length;
            }
        }
        PointerProperties[] properties = new PointerProperties[touches.length];
        PointerCoords[] pointerCoords = new PointerCoords[touches.length];
        for (x = 0; x < touches.length; x++) {
            PointerProperties prop = new PointerProperties();
            prop.id = x;
            prop.toolType = 1;
            properties[x] = prop;
            pointerCoords[x] = touches[x][0];
        }
        long downTime = SystemClock.uptimeMillis();
        boolean ret = injectEventSync(MotionEvent.obtain(downTime, SystemClock.uptimeMillis(), 0, 1, properties, pointerCoords, 0, 0, 1.0f, 1.0f, 0, 0, 4098, 0));
        for (x = 1; x < touches.length; x++) {
            ret &= injectEventSync(MotionEvent.obtain(downTime, SystemClock.uptimeMillis(), getPointerAction(MOTION_EVENT_INJECTION_DELAY_MILLIS, x), x + 1, properties, pointerCoords, 0, 0, 1.0f, 1.0f, 0, 0, 4098, 0));
        }
        for (int i = 1; i < maxSteps - 1; i++) {
            for (x = 0; x < touches.length; x++) {
                if (touches[x].length > i) {
                    pointerCoords[x] = touches[x][i];
                } else {
                    pointerCoords[x] = touches[x][touches[x].length - 1];
                }
            }
            ret &= injectEventSync(MotionEvent.obtain(downTime, SystemClock.uptimeMillis(), 2, touches.length, properties, pointerCoords, 0, 0, 1.0f, 1.0f, 0, 0, 4098, 0));
            SystemClock.sleep(5);
        }
        for (x = 0; x < touches.length; x++) {
            pointerCoords[x] = touches[x][touches[x].length - 1];
        }
        for (x = 1; x < touches.length; x++) {
            ret &= injectEventSync(MotionEvent.obtain(downTime, SystemClock.uptimeMillis(), getPointerAction(6, x), x + 1, properties, pointerCoords, 0, 0, 1.0f, 1.0f, 0, 0, 4098, 0));
        }
        Log.i(LOG_TAG, "x " + pointerCoords[0].x);
        return ret & injectEventSync(MotionEvent.obtain(downTime, SystemClock.uptimeMillis(), 1, 1, properties, pointerCoords, 0, 0, 1.0f, 1.0f, 0, 0, 4098, 0));
    }

    public boolean toggleRecentApps() {
        return this.mUiAutomatorBridge.performGlobalAction(3);
    }

    public boolean openNotification() {
        return this.mUiAutomatorBridge.performGlobalAction(4);
    }

    public boolean openQuickSettings() {
        return this.mUiAutomatorBridge.performGlobalAction(MOTION_EVENT_INJECTION_DELAY_MILLIS);
    }
}
