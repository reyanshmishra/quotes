package com.motivational.quotes.Networking;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.motivational.quotes.R;
import com.motivational.quotes.Utils.Common;
import com.motivational.quotes.Utils.Constants;

import java.lang.reflect.Method;
import java.util.List;

import static com.motivational.quotes.Activities.EditQuoteActivity.getTagOrComment;

public class QuoteAccessibilityService extends AccessibilityService {
    private static final String TAG = QuoteAccessibilityService.class.getSimpleName();
    boolean found = false;
    List<AccessibilityNodeInfo> list;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (((Common) Common.getContext()).isPosted()) {
            return;
        }
        if (AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED == event.getEventType()) {
            AccessibilityNodeInfo nodeInfo = event.getSource();
            if (nodeInfo == null) return;

            save(nodeInfo);

            startPosting(nodeInfo);

            goAhead(nodeInfo);
            pasteCaption(nodeInfo);

        } else if (AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED == event.getEventType()) {
            AccessibilityNodeInfo nodeInfo = event.getSource();
            if (nodeInfo == null) return;
            nextStep(nodeInfo);
        }
    }

    private void pasteCaption(AccessibilityNodeInfo nodeInfo) {
        int s = nodeInfo.getChildCount();
        for (int i = 0; i < s; i++) {
            AccessibilityNodeInfo childNodeView = nodeInfo.getChild(i);
            if (childNodeView != null) {
                if (childNodeView.getClassName().equals("android.widget.ScrollView")) {
                    int ss = childNodeView.getChildCount();
                    for (int j = 0; j < ss; j++) {
                        AccessibilityNodeInfo cn = childNodeView.getChild(j);
                        if (cn != null) {
                            if (cn.getText() != null && cn.getText().equals("Write a caption…")) {
                                Bundle arguments = new Bundle();
                                arguments.putString(AccessibilityNodeInfoCompat.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, getCaption());
                                cn.performAction(AccessibilityNodeInfoCompat.ACTION_SET_TEXT, arguments);
                                phewPost(nodeInfo);
                            }
                        }
                    }
                }
            }
        }
    }


    private void goAhead(AccessibilityNodeInfo nodeInfo) {
        int s = nodeInfo.getChildCount();
        for (int i = 0; i < s; i++) {
            AccessibilityNodeInfo childNodeView = nodeInfo.getChild(i);
            if (childNodeView != null) {
                if (childNodeView.getText() != null && childNodeView.getText().equals("New Post")) {
                    found = true;
                    continue;
                }
                if (found) {
                    childNodeView.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    found = false;
                }
            }
        }
    }


    private void nextStep(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo.getText() != null && nodeInfo.getText().equals("Next")) {
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
    }


    private void phewPost(AccessibilityNodeInfo nodeInfo) {
        list = nodeInfo.findAccessibilityNodeInfosByText("Share");
        for (AccessibilityNodeInfo node : list) {
            if (node.getText() != null && node.getText().equals("Share")) {
                node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                ((Common) Common.getContext()).setPosted(true);
            }
        }
    }


    private String getCaption() {
        return "Follow @motivational_mantra\n" + getTagOrComment(Constants.MESSAGES) + "\n.\n.\n.\n" + getTagOrComment(Constants.TAGS) + " " + getTagOrComment(Constants.TAGS);
    }


    private void save(AccessibilityNodeInfo nodeInfo) {
        list = nodeInfo.findAccessibilityNodeInfosByViewId("com.motivational.quotes:id/button_save");
        for (AccessibilityNodeInfo node : list) {
            node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
    }


    private void startPosting(AccessibilityNodeInfo nodeInfo) {
        list = nodeInfo.findAccessibilityNodeInfosByText("Instagram");
        for (AccessibilityNodeInfo node : list) {
            AccessibilityNodeInfo d = node.getParent();
            d.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
    }


    @Override
    public void onServiceConnected() {
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED | AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED;
        info.notificationTimeout = 100;
        info.feedbackType = AccessibilityEvent.TYPES_ALL_MASK;
        setServiceInfo(info);
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
