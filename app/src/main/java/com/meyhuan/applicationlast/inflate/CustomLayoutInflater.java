package com.meyhuan.applicationlast.inflate;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * User  : guanhuan
 * Date  : 2017/5/3
 */

public class CustomLayoutInflater extends LayoutInflater{

        private static final String[] sClassPrefixList = {
                "android.widget.",
                "android.webkit."
        };
        public CustomLayoutInflater(Context context) {
            super(context);
        }

        protected CustomLayoutInflater(LayoutInflater original, Context newContext) {
            super(original, newContext);
        }

        @Override protected View onCreateView(String name, AttributeSet attrs) throws ClassNotFoundException {
            for (String prefix : sClassPrefixList) {
                try {
                    View view = createView(name, prefix, attrs);
                    if (view != null) {
                        return view;
                    }
                } catch (ClassNotFoundException e) {
                    // In this case we want to let the base class take a crack
                    // at it.
                }
            }
            return super.onCreateView(name, attrs);
        }
        public LayoutInflater cloneInContext(Context newContext) {
            return new CustomLayoutInflater(this, newContext);
        }

    @Override
    public View inflate(@LayoutRes int resource, @Nullable ViewGroup root, boolean attachToRoot) {
        ViewGroup viewGroup = root;
        traversalViewGroup(viewGroup);
        while (viewGroup != null){
            Log.e("Hooker", viewGroup.getClass().getName() + ", id = " + viewGroup.getId());
            viewGroup = (ViewGroup) viewGroup.getParent();
        }
        return super.inflate(resource, root, attachToRoot);
    }

    private void traversalViewGroup(ViewGroup rootView){
        if(rootView != null){
            int childCount = rootView.getChildCount();
            for(int i = 0; i < childCount; i++){
                View childView = rootView.getChildAt(i);
                if(childView.getTag() == null){
                    childView.setTag(getViewTag());
                }
                Log.e("Hooker", "childView name = " + childView.getClass().getName());
                if(childView instanceof ViewGroup){
                    traversalViewGroup((ViewGroup) childView);
                }
            }
        }
    }

    private static int VIEW_TAG = 0x10000000;
    private static int getViewTag(){
        return VIEW_TAG++;
    }
}
