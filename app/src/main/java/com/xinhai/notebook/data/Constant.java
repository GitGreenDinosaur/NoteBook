package com.xinhai.notebook.data;

public class Constant {

    public static final int DB_VERSION = 1;

    //保存私密空间的密码
    public static final String KEY_PRIVATE_PASS = "key_private_pass";

    //保存私密空间的密码
    public static final String KEY_NOTE_INFO = "key_note_info";

    //保存私密空间的密码
    public static final String FLAG_DIALOG_CALLBACK = "flag_dialog_callback";

    //保存指纹识别权限
    public static final String KEY_BIOMETRIC_PASS = "key_biometric_pass";

    //保存列表滑动是否开启
    public static final String KEY_SETTING_SLIDE = "key_setting_slide";

//    0表示无任何 1表示置顶 2表示废纸篓 3表示私密
    public static final String EDIT_STATUS = "edit_status";

    public static final int EDIT_STATUS_ADD = -1;

    public static final int NOTE_STATUS_NORMAL = 0;

    public static final int NOTE_STATUS_TOP = 1;

    public static final int NOTE_STATUS_WASTER = 2;

    public static final int NOTE_STATUS_PRIVATE = 3;

    public static final String DB_NAME = "notebook.db";

    public static final String TABLE_NAME_NOTE = "note";

    public static final String TABLE_NAME_USER = "user";

    public static final String TABLE_NAME_SYNC = "sync";

    public static final String TABLE_NAME_WASTER = "waster";

    public static final String TABLE_NAME_PRIVATE = "private";

    public static final String TABLE_NAME_TOP = "top";

}
