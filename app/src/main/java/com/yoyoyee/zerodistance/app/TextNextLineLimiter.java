package com.yoyoyee.zerodistance.app;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by futur on 2016/4/9.
 * <p/>
 * 用法範例
 * editTextName.addTextChangedListener(new TextNextLineLimiter());
 *
 *
 * 用來限制自述使用的
 */
public class TextNextLineLimiter implements TextWatcher {

    private int maxLength; // 儲存最大的字串長度
    private int currentEnd = 0; // 儲存目前字串改變的結束位置，例如：abcdefg變成abcd1234efg，變化的結束位置就在索引8

    public TextNextLineLimiter() {

    }


    @Override
    public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {

    }

    @Override
    public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
        currentEnd = start + count; // 取得變化的結束位置
    }

    @Override
    public void afterTextChanged(final Editable s) {
        if(limitNextLine(s) ) { // 若變化後的長度超過最大長度
            // 刪除最後變化的字元
            currentEnd--;
            s.delete(currentEnd, currentEnd + 1);
        }
    }

    /**
     * 限制不能夠
     */
    protected Boolean limitNextLine(final CharSequence c) {
        String text=String.valueOf(c);
        return text.contains("\n");
    }
}