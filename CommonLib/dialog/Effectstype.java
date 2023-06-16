package com.lqpdc.commonlib.dialog;

import com.lqpdc.commonlib.dialog.effect.BaseEffects;
import com.lqpdc.commonlib.dialog.effect.FadeIn;
import com.lqpdc.commonlib.dialog.effect.Fall;
import com.lqpdc.commonlib.dialog.effect.FlipH;
import com.lqpdc.commonlib.dialog.effect.FlipV;
import com.lqpdc.commonlib.dialog.effect.NewsPaper;
import com.lqpdc.commonlib.dialog.effect.RotateBottom;
import com.lqpdc.commonlib.dialog.effect.RotateLeft;
import com.lqpdc.commonlib.dialog.effect.Shake;
import com.lqpdc.commonlib.dialog.effect.SideFall;
import com.lqpdc.commonlib.dialog.effect.SlideBottom;
import com.lqpdc.commonlib.dialog.effect.SlideLeft;
import com.lqpdc.commonlib.dialog.effect.SlideRight;
import com.lqpdc.commonlib.dialog.effect.SlideTop;
import com.lqpdc.commonlib.dialog.effect.Slit;


/**
 * Created by lee on 2014/7/30.
 */
public enum  Effectstype {

    Fadein(FadeIn.class),
    Slideleft(SlideLeft.class),
    Slidetop(SlideTop.class),
    SlideBottom(SlideBottom.class),
    Slideright(SlideRight.class),
    Fall(Fall.class),
    Newspager(NewsPaper.class),
    Fliph(FlipH.class),
    Flipv(FlipV.class),
    RotateBottom(RotateBottom.class),
    RotateLeft(RotateLeft.class),
    Slit(Slit.class),
    Shake(Shake.class),
    Sidefill(SideFall.class);
    
    private Class effectsClazz;

    private Effectstype(Class mclass) {
        effectsClazz = mclass;
    }

    public BaseEffects getAnimator() {
        try {
            return (BaseEffects) effectsClazz.newInstance();
        } catch (Exception e) {
            throw new Error("Can not init animatorClazz instance");
        }
    }
}
