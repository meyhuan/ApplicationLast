package com.meyhuan.applicationlast.inflate;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.math.BigInteger;
import java.security.MessageDigest;


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
        View viewGroup =  super.inflate(resource, root, attachToRoot);
        View rootView = viewGroup;
        View tempView = viewGroup;
        while (tempView != null){
            rootView = viewGroup;
            tempView = (ViewGroup) tempView.getParent();
        }
        traversalViewGroup(rootView);
        return viewGroup;
    }

    private void traversalViewGroup(View rootView){
        if(rootView != null && rootView instanceof ViewGroup){
            if(rootView.getTag() == null){
                rootView.setTag(getViewTag());
            }
            ViewGroup viewGroup = (ViewGroup) rootView;
            int childCount = viewGroup.getChildCount();
            for(int i = 0; i < childCount; i++){
                View childView = viewGroup.getChildAt(i);
                if(childView.getTag() == null){
                    childView.setTag(combineTag(getViewTag(), viewGroup.getTag().toString()));
                }
                Log.e("Hooker", "childView name = " + childView.getClass().getName() + "id = " + childView.getTag().toString() );
                if(childView instanceof ViewGroup){
                    traversalViewGroup(childView);
                }
            }
        }
    }

  private String combineTag(String tag1, String tag2){
        return getMD5(getMD5(tag1) + getMD5(tag2));
    }

    private static int VIEW_TAG = 0x10000000;
    private static String getViewTag(){
        return String.valueOf(VIEW_TAG++);
    }
    /**
     * 对字符串md5加密
     *
     * @param str
     * @return
     */
    public static String getMD5(String str) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {

        }
        return "null";
    }

}
