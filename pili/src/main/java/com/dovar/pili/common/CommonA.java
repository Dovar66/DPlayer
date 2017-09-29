package com.dovar.pili.common;

import android.view.View;

/**
 * Created by heweizong on 2017/9/29.
 */

public class CommonA {
    public static CommonA.classB a(int var0, int var1, int var2, int var3, int var4) {
        int var5 = View.getDefaultSize(var3, var1);
        int var6 = View.getDefaultSize(var4, var2);
        if(var0 == 0) {
            return new CommonA.classB(var3, var4);
        } else if(var3 > 0 && var4 > 0) {
            int var7 = View.MeasureSpec.getMode(var1);
            int var8 = View.MeasureSpec.getSize(var1);
            int var9 = View.MeasureSpec.getMode(var2);
            int var10 = View.MeasureSpec.getSize(var2);
            if(var7 == 1073741824 && var9 == 1073741824) {
                float var11 = (float)var8 / (float)var10;
                float var12;
                switch(var0) {
                    case 1:
                    case 2:
                    default:
                        var12 = (float)var3 / (float)var4;
                        break;
                    case 3:
                        var12 = 1.7777778F;
                        break;
                    case 4:
                        var12 = 1.3333334F;
                }

                switch(var0) {
                    case 1:
                    case 3:
                    case 4:
                        if(var12 > var11) {
                            var5 = var8;
                            var6 = (int)((float)var8 / var12);
                        } else {
                            var6 = var10;
                            var5 = (int)((float)var10 * var12);
                        }
                        break;
                    case 2:
                        if(var12 > var11) {
                            var6 = var10;
                            var5 = (int)((float)var10 * var12);
                        } else {
                            var5 = var8;
                            var6 = (int)((float)var8 / var12);
                        }
                }
            } else if(var7 == 1073741824) {
                var5 = var8;
                var6 = var8 * var4 / var3;
                if(var9 == -2147483648 && var6 > var10) {
                    var6 = var10;
                }
            } else if(var9 == 1073741824) {
                var6 = var10;
                var5 = var10 * var3 / var4;
                if(var7 == -2147483648 && var5 > var8) {
                    var5 = var8;
                }
            } else {
                var5 = var3;
                var6 = var4;
                if(var9 == -2147483648 && var4 > var10) {
                    var6 = var10;
                    var5 = var10 * var3 / var4;
                }

                if(var7 == -2147483648 && var5 > var8) {
                    var5 = var8;
                    var6 = var8 * var4 / var3;
                }
            }

            return new CommonA.classB(var5, var6);
        } else {
            return new CommonA.classB(var5, var6);
        }
    }

    public static class classB {
        public final int a;
        public final int b;

        public classB(int var1, int var2) {
            this.a = var1;
            this.b = var2;
        }
    }
}
