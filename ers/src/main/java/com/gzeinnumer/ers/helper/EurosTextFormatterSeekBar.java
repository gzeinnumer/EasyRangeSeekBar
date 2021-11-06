package com.gzeinnumer.ers.helper;


import com.gzeinnumer.ers.callback.TextFormatterSeekBar;

public class EurosTextFormatterSeekBar implements TextFormatterSeekBar {

    @Override
    public String format(float value) {
        return String.format("%d", (int) value);
    }
}
