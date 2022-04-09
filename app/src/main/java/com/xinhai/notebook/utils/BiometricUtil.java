package com.xinhai.notebook.utils;

import android.content.Context;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

/**
 * 生物识别工具累
 */
public class BiometricUtil {

    /**
     *返回值见上文的“是否可用的状态码”
     */
    public int isFingerprintAvailable(Context context){
        BiometricManager manager = BiometricManager.from(context);
        return manager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK);
    }

    /**
     * 开始验证
     *
     * @param activity
     * @param callBack 验证结果回调
     */
    public static void authenticate(FragmentActivity activity, BiometricPrompt.AuthenticationCallback callBack) {
        BiometricPrompt.PromptInfo promptInfo = createUi();
        BiometricPrompt prompt = new BiometricPrompt(activity, ContextCompat.getMainExecutor(activity), callBack);
        prompt.authenticate(promptInfo);
    }

    private static BiometricPrompt.PromptInfo createUi() {
        return new BiometricPrompt.PromptInfo.Builder()
                .setTitle("验证指纹")
//                .setSubtitle("请触摸传感器")
                .setNegativeButtonText("使用密码")
                .build();
    }

}
